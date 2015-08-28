package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;

/**
 * Created by kirichenko on 17.05.2015.
 */
public class LoadProtocols extends CommonMainLoading implements ICallbackForLoadingMainInformation, ICallBackForUnderDownLoad {

    private ICommonLoadComplete iLoadedComleteCallBack;

    private LoginAccount oLoginAccount;
    private DataBaseHelper sqlHelper;
    private Context mContext;

    private ArrayList<String> alAllForms;

    private Integer iLockCallBack;
    private String sAddress;


    public LoadProtocols(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.mContext = mContext;
        this.sAddress = sAddress;
        alAllForms = new ArrayList<>();
        iLockCallBack = new Integer(0);
    }

    /**
     * Загрузка списка заполненых протоколов
     *
     * @param sVisitId               id визита
     * @param mContext               Контекст
     * @param iLoadedComleteCallBack Колбэк куда мы сообщим об успешности
     */
    public void startLoadFullFieldProtocolsByVisit(String sVisitId, Context mContext, ICommonLoadComplete iLoadedComleteCallBack) {

        this.mContext = mContext;
        this.iLoadedComleteCallBack = iLoadedComleteCallBack;

        ControllClassLoading controllClassLoading = new ControllClassLoading();
        controllClassLoading.setEnumType_data(TYPE_DATA.FULL_FIELD_PROTOCOLS);
        controllClassLoading.setsValueVisitId(sVisitId);

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/" + sVisitId + "/protocollist";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, controllClassLoading);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    public void startLoadStructureFullFieldProtocolsByVisit(String sValueIdProtocols) {

        ControllClassLoading controllClassLoading = new ControllClassLoading();
        controllClassLoading.setEnumType_data(TYPE_DATA.STRUCTURE_FULL_FIELD_PROTOCOLS);
        controllClassLoading.setsValueVisitId(sValueIdProtocols);

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/protocol/" + sValueIdProtocols;

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, controllClassLoading);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    public int startLoadEnabledProtocols(ILoginEnableAccess iLoginEnableAccess) {

        ControllClassLoading controllClassLoading = new ControllClassLoading();
        controllClassLoading.setEnumType_data(TYPE_DATA.ENABLE_PROTOCOLS);
        controllClassLoading.setiLoginEnableAccess(iLoginEnableAccess);

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/useprotocollist";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, controllClassLoading);

        if (oLoginAccount != null) {
            loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        }

        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

        return 1;

    }

    public void startLoadStructureEnabledProtocols(String sValueIdProtocols, ICommonLoadComplete iLoadedComleteCallBack) {

        this.iLoadedComleteCallBack = iLoadedComleteCallBack;

        ControllClassLoading controllClassLoading = new ControllClassLoading();
        controllClassLoading.setEnumType_data(TYPE_DATA.STRUCTURE_ENABLE_PROTOCOLS);
        controllClassLoading.setsValueIdProtocols(sValueIdProtocols);

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/protocolstruct/" + sValueIdProtocols;

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, controllClassLoading);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object oControllClassLoading) {

        if (oControllClassLoading != null) {
            if (oControllClassLoading instanceof ControllClassLoading) {
                if (((ControllClassLoading) oControllClassLoading).getEnumType_data() == TYPE_DATA.FULL_FIELD_PROTOCOLS) {
                    saveFullFieldProtocolsByVisit(dataAboutLoadedObjects, (ControllClassLoading) oControllClassLoading);
                } else if (((ControllClassLoading) oControllClassLoading).getEnumType_data() == TYPE_DATA.STRUCTURE_FULL_FIELD_PROTOCOLS) {
                    saveStructureFullFieldProtocolsByVisit(dataAboutLoadedObjects, (ControllClassLoading) oControllClassLoading);
                } else if (((ControllClassLoading) oControllClassLoading).getEnumType_data() == TYPE_DATA.ENABLE_PROTOCOLS) {
                    saveEnableFieldProtocolsByVisit(dataAboutLoadedObjects, (ControllClassLoading) oControllClassLoading);
                } else if (((ControllClassLoading) oControllClassLoading).getEnumType_data() == TYPE_DATA.STRUCTURE_ENABLE_PROTOCOLS) {
                    saveStructureEnableFieldProtocolsByVisit(dataAboutLoadedObjects, (ControllClassLoading) oControllClassLoading);
                }
            }
        }
    }

    private void saveFullFieldProtocolsByVisit(ArrayList<JSONObject> dataAboutLoadedObjects, ControllClassLoading oControllClassLoading) {

        String sVisitId = null, sFormResultId = null, sFormid = null, sText = null,
                sDate = null;

        synchronized (iLockCallBack) {
            iLockCallBack += dataAboutLoadedObjects.size();
        }

        FullFieldProtocols.removeAllInfornationAboutTables(sqlHelper);

        if (dataAboutLoadedObjects.size() == 0) {
            if (iLoadedComleteCallBack != null) {
                iLoadedComleteCallBack.loadCompleted();
            }
            return;
        }

        if (dataAboutLoadedObjects != null) {


            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {

                    sVisitId = String.valueOf(oControllClassLoading.getsValueVisitId());
                    sFormResultId = String.valueOf(oItemGuides.get(FullFieldProtocols.FORMRESULTID));
                    sFormid = String.valueOf(oItemGuides.get(FullFieldProtocols.FORMID));
                    sText = String.valueOf(oItemGuides.get(FullFieldProtocols.TEXT));
                    sDate = String.valueOf(oItemGuides.get(FullFieldProtocols.DATE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(FullFieldProtocols.VISITID, sVisitId);
                values.put(FullFieldProtocols.FORMRESULTID, sFormResultId);
                values.put(FullFieldProtocols.FORMID, sFormid);
                values.put(FullFieldProtocols.TEXT, sText);
                values.put(FullFieldProtocols.DATE, sDate);

                sqlHelper.getWritableDatabase().insertWithOnConflict(FullFieldProtocols.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                startLoadStructureFullFieldProtocolsByVisit(sFormResultId);

            }
        }
    }

    private void saveStructureFullFieldProtocolsByVisit(ArrayList<JSONObject> dataAboutLoadedObjects, ControllClassLoading oControllClassLoading) {

        String sFormItemId = null, sTyp = null, sValueType = null,
                sDataType = null, sIsMulti = null, sIsListStc = null, sColor = null,
                sText = null, sStatus = null, sMandatory = null,
                sIsBlockinPut = null, sDiagType = null, sDefaultValueId = null,
                sDefaultValue = null, sForResultValue = null, sCText = null, sProtocolId = null, sFilterId = null;

        ArrayList<JSONObject> listOfJSONObject = new ArrayList<>();

        int iCountToLoadSubReport = 0;

        if (oControllClassLoading != null) {
            sProtocolId = String.valueOf(oControllClassLoading.getsValueVisitId());
        }

        StructureFullFieldProtocols.removeAllInfornationAboutTables(sqlHelper, sProtocolId);

        if (dataAboutLoadedObjects != null) {

            LoadGuidesForSpinnerData oLoadGuidesForSpinnerData;

            if (dataAboutLoadedObjects.size() == 0) {
                underLoadComplete(sProtocolId);
                return;
            } else {


                for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {
                    try {

                        JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                        listOfJSONObject.add(oItemGuides);

                        sValueType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.VALUETYPE));
                        if (sValueType != null) {
                            if (sValueType.equals("2") || sValueType.equals("3")) {
                                iCountToLoadSubReport++;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                oLoadGuidesForSpinnerData = new LoadGuidesForSpinnerData(oLoginAccount, sqlHelper, sAddress, iCountToLoadSubReport, mContext);
            }

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = listOfJSONObject.get(iCount);
                try {

                    if (oControllClassLoading != null) {
                        sProtocolId = String.valueOf(oControllClassLoading.getsValueVisitId());
                    }

                    sFormItemId = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.FORMITEMID));
                    sTyp = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.TYP));

                    sValueType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.VALUETYPE));
                    sDataType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DATATYPE));
                    sIsMulti = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.ISMULTI));
                    sIsListStc = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.ISLISTCHKR));
                    sColor = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.COLOR));
                    sText = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.TEXT));
                    sStatus = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.STATUS));
                    sMandatory = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.MANDATORY));

                    sIsBlockinPut = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.ISBLOCKINPUT));
                    sDiagType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DIAGTYPE));
                    sDefaultValueId = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DEFAULTVALUEID));
                    sDefaultValue = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DEFAULTVALUE));
                    sForResultValue = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.FORMRESULTVALUEID));
                    sCText = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.CTEXT));
                    sFilterId = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.FILTERID));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(StructureFullFieldProtocols.PROTOCOLID, sProtocolId);
                values.put(StructureFullFieldProtocols.FORMITEMID, sFormItemId);
                values.put(StructureFullFieldProtocols.TYP, sTyp);
                values.put(StructureFullFieldProtocols.EXTRAFIELD, "-");
                values.put(StructureFullFieldProtocols.VALUETYPE, sValueType);
                values.put(StructureFullFieldProtocols.DATATYPE, sDataType);
                values.put(StructureFullFieldProtocols.ISMULTI, sIsMulti);
                values.put(StructureFullFieldProtocols.ISLISTCHKR, sIsListStc);
                values.put(StructureFullFieldProtocols.COLOR, sColor);
                values.put(StructureFullFieldProtocols.TEXT, sText);
                values.put(StructureFullFieldProtocols.STATUS, sStatus);
                values.put(StructureFullFieldProtocols.MANDATORY, sMandatory);
                values.put(StructureFullFieldProtocols.ISBLOCKINPUT, sIsBlockinPut);
                values.put(StructureFullFieldProtocols.DIAGTYPE, sDiagType);
                values.put(StructureFullFieldProtocols.DEFAULTVALUEID, sDefaultValueId);
                values.put(StructureFullFieldProtocols.DEFAULTVALUE, sDefaultValue);
                values.put(StructureFullFieldProtocols.FORMRESULTVALUEID, sForResultValue);
                values.put(StructureFullFieldProtocols.CTEXT, sCText);
                values.put(StructureFullFieldProtocols.FILTERID, sFilterId);

                sqlHelper.getWritableDatabase().insertWithOnConflict(StructureFullFieldProtocols.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                if (sFormItemId != null) {
                    if (!sFormItemId.isEmpty() && !sFormItemId.equals("null")) {
                        if (sValueType != null) {
                            if (sValueType.equals("2") || sValueType.equals("3")) {
                                oLoadGuidesForSpinnerData.LoadGuidesForSpinnerData(sFormItemId, this);
                            }
                        }
                    }
                }
            }
        }

        if (iCountToLoadSubReport == 0) {
            underLoadComplete(sProtocolId);
        }

    }

    @Override
    public void underLoadComplete(String sFormId) {

        synchronized (iLockCallBack) {
            iLockCallBack--;
            if (iLockCallBack <= 0) {
                if (iLoadedComleteCallBack != null) {
                    iLoadedComleteCallBack.loadCompleted();
                }
            }
        }

    }

    private void saveEnableFieldProtocolsByVisit(ArrayList<JSONObject> dataAboutLoadedObjects, ControllClassLoading oControllClassLoading) {

        String sVisitId = null, sLevel = null, sFormid = null, sRootId = null,
                sCode = null, sText = null, sIsGroup = null, sSortCode = null;

        EnableProtocols.removeAllInfornationAboutTables(sqlHelper);

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {

                    sVisitId = String.valueOf(oControllClassLoading.getsValueVisitId());
                    sLevel = String.valueOf(oItemGuides.get(EnableProtocols.LEVEL));
                    sFormid = String.valueOf(oItemGuides.get(EnableProtocols.FORMID));
                    sRootId = String.valueOf(oItemGuides.get(EnableProtocols.ROOTID));
                    sCode = String.valueOf(oItemGuides.get(EnableProtocols.CODE));
                    sText = String.valueOf(oItemGuides.get(EnableProtocols.TEXT));
                    sIsGroup = String.valueOf(oItemGuides.get(EnableProtocols.ISGROUP));
                    sSortCode = String.valueOf(oItemGuides.get(EnableProtocols.SORTCODE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();
                values.put(EnableProtocols.VISITID, sVisitId);
                values.put(EnableProtocols.LEVEL, sLevel);
                values.put(EnableProtocols.FORMID, sFormid);
                values.put(EnableProtocols.ROOTID, sRootId);
                values.put(EnableProtocols.CODE, sCode);
                values.put(EnableProtocols.TEXT, sText);
                values.put(EnableProtocols.ISGROUP, sIsGroup);
                values.put(EnableProtocols.SORTCODE, sSortCode);

                sqlHelper.getWritableDatabase().insertWithOnConflict(EnableProtocols.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
        }

        oControllClassLoading.getiLoginEnableAccess().enableAccessLoadFinished(oLoginAccount);

    }

    private void saveStructureEnableFieldProtocolsByVisit(ArrayList<JSONObject> dataAboutLoadedObjects, ControllClassLoading oControllClassLoading) {

        String sFormItemId = null, sTyp = null, sValueType = null,
                sDataType = null, sIsMulti = null, sIsListStc = null, sColor = null,
                sText = null, sStatus = null, sMandatory = null,
                sIsBlockinPut = null, sDiagType = null, sDefaultValueId = null,
                sDefaultValue = null, sProtocolId = null, sFilterId = null;

        ArrayList<JSONObject> listOfJSONObject = new ArrayList<>();
        int iCountToLoadSubReport = 0;

        if (oControllClassLoading != null) {
            sProtocolId = String.valueOf(oControllClassLoading.getsValueIdProtocols());
        }

        StructureFullFieldProtocols.removeAllInfornationAboutTables(sqlHelper, sProtocolId);

        if (dataAboutLoadedObjects != null) {

            LoadGuidesForSpinnerData oLoadGuidesForSpinnerData = null;

            if (dataAboutLoadedObjects.size() == 0) {
                underLoadComplete(sProtocolId);
                return;
            } else {

                for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {
                    try {

                        JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                        listOfJSONObject.add(oItemGuides);

                        sValueType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.VALUETYPE));
                        if (sValueType != null) {
                            if (sValueType.equals("2") || sValueType.equals("3")) {
                                iCountToLoadSubReport++;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                oLoadGuidesForSpinnerData = new LoadGuidesForSpinnerData(oLoginAccount, sqlHelper, sAddress, iCountToLoadSubReport, mContext);
            }

            synchronized (iLockCallBack) {
                iLockCallBack = 1;//iCountToLoadSubReport;// dataAboutLoadedObjects.size();
            }


            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {

                    sFormItemId = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.FORMITEMID));
                    sTyp = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.TYP));
                    sValueType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.VALUETYPE));
                    sDataType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DATATYPE));
                    sIsMulti = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.ISMULTI));
                    sIsListStc = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.ISLISTCHKR));
                    sColor = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.COLOR));
                    sText = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.TEXT));
                    sStatus = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.STATUS));
                    sMandatory = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.MANDATORY));
                    sIsBlockinPut = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.ISBLOCKINPUT));
                    sDiagType = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DIAGTYPE));
                    sDefaultValueId = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DEFAULTVALUEID));
                    sDefaultValue = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.DEFAULTVALUE));
                    sFilterId = String.valueOf(oItemGuides.get(StructureFullFieldProtocols.FILTERID));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(StructureFullFieldProtocols.PROTOCOLID, sProtocolId);
                values.put(StructureFullFieldProtocols.FORMITEMID, sFormItemId);
                values.put(StructureFullFieldProtocols.TYP, sTyp);
                values.put(StructureFullFieldProtocols.EXTRAFIELD, "-");
                values.put(StructureFullFieldProtocols.VALUETYPE, sValueType);
                values.put(StructureFullFieldProtocols.DATATYPE, sDataType);
                values.put(StructureFullFieldProtocols.ISMULTI, sIsMulti);
                values.put(StructureFullFieldProtocols.ISLISTCHKR, sIsListStc);
                values.put(StructureFullFieldProtocols.COLOR, sColor);
                values.put(StructureFullFieldProtocols.TEXT, sText);
                values.put(StructureFullFieldProtocols.STATUS, sStatus);
                values.put(StructureFullFieldProtocols.MANDATORY, sMandatory);
                values.put(StructureFullFieldProtocols.ISBLOCKINPUT, sIsBlockinPut);
                values.put(StructureFullFieldProtocols.DIAGTYPE, sDiagType);
                values.put(StructureFullFieldProtocols.DEFAULTVALUEID, sDefaultValueId);
                values.put(StructureFullFieldProtocols.DEFAULTVALUE, sDefaultValue);
                values.put(StructureFullFieldProtocols.FILTERID, sFilterId);


                sqlHelper.getWritableDatabase().insertWithOnConflict(StructureFullFieldProtocols.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                if (sFormItemId != null) {
                    if (!sFormItemId.isEmpty() && !sFormItemId.equals("null")) {
                        if (sValueType != null) {
                            if (sValueType.equals("2") || sValueType.equals("3")) {
                                oLoadGuidesForSpinnerData.LoadGuidesForSpinnerData(sFormItemId, this);
                            }
                        }
                    }
                }
            }
        }

        if (iCountToLoadSubReport == 0) {
            underLoadComplete(sProtocolId);
        }

    }

    /**
     * Перечисление всех возможных типов работы с протоколоами
     */

    enum TYPE_DATA {
        FULL_FIELD_PROTOCOLS, STRUCTURE_FULL_FIELD_PROTOCOLS, ENABLE_PROTOCOLS, STRUCTURE_ENABLE_PROTOCOLS
    }

    class ControllClassLoading {

        private TYPE_DATA enumType_data;
        private String sValueVisitId;
        private String sValueIdProtocols;
        private ILoginEnableAccess iLoginEnableAccess;

        ControllClassLoading() {
        }

        ControllClassLoading(TYPE_DATA enumType_data, String sValueVisitId) {
            this.enumType_data = enumType_data;
            this.sValueVisitId = sValueVisitId;
        }

        ControllClassLoading(TYPE_DATA enumType_data, String sValueVisitId, String sValueIdProtocols) {
            this.enumType_data = enumType_data;
            this.sValueVisitId = sValueVisitId;
            this.sValueIdProtocols = sValueIdProtocols;
        }

        public ILoginEnableAccess getiLoginEnableAccess() {
            return iLoginEnableAccess;
        }

        public void setiLoginEnableAccess(ILoginEnableAccess iLoginEnableAccess) {
            this.iLoginEnableAccess = iLoginEnableAccess;
        }

        public TYPE_DATA getEnumType_data() {
            return enumType_data;
        }

        public void setEnumType_data(TYPE_DATA enumType_data) {
            this.enumType_data = enumType_data;
        }

        public String getsValueVisitId() {
            return sValueVisitId;
        }

        public void setsValueVisitId(String sValueVisitId) {
            this.sValueVisitId = sValueVisitId;
        }

        public String getsValueIdProtocols() {
            return sValueIdProtocols;
        }

        public void setsValueIdProtocols(String sValueIdProtocols) {
            this.sValueIdProtocols = sValueIdProtocols;
        }
    }

}
