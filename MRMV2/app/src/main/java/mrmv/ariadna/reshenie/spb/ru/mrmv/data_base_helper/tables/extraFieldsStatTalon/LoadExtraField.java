package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.ILoadedComleted;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ICallBackForUnderDownLoad;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.StructureFullFieldProtocols;

/**
 * Created by kirichenko on 26.05.2015.
 * Загрузка уже выставленных данных дополнительных полей в стат талоне
 */
public class LoadExtraField extends CommonMainLoading implements ICallbackForLoadingMainInformation, ICallBackForUnderDownLoad {

    private LoginAccount oLoginAccount;
    private DataBaseHelper sqlHelper;
    private String sAddress;
    private Context mContext;
    private ILoadedComleted iLoadedComleteCallBack;

    public LoadExtraField(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
    }

    public void startLoadInformationExtraField(String sVisitId, ILoadedComleted iLoadedComleteCallBack) {

        this.iLoadedComleteCallBack = iLoadedComleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/" + sVisitId + "/addfields";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sVisitId);

        if(oLoginAccount  != null){
            loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        }

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

        //*Сперва удаляем все записи по этому справочнику
        String cleanRequest = "DELETE FROM " + StructureFullFieldProtocols.TABLE_NAME + " WHERE " + StructureFullFieldProtocols.EXTRAFIELD + " = " + "EXTRAFIELD" ;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        cleanRequest = "DELETE FROM " + ExtraFieldStatTalon.TABLE_NAME + " WHERE " + ExtraFieldStatTalon.EXTRAFIELD + " = " + "EXTRAFIELD" ;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        String sFormItemId = null, sTyp = null, sValueType = null,
                sDataType = null, sIsMulti = null, sIsListStc = null, sColor = null,
                sText = null, sStatus = null, sMandatory = null,
                sIsBlockinPut = null, sDiagType = null, sDefaultValueId = null,
                sDefaultValue = null, sForResultValue = null, sCText = null, sProtocolId =  null,
                sVisitId = null, sFilterId = null;

        if (oControllClassLoading != null) {
            sVisitId = String.valueOf(oControllClassLoading);
        }

        ArrayList<JSONObject> listOfJSONObject = new ArrayList<>();
        int iCountToLoadSubReport = 0;

        if (dataAboutLoadedObjects != null) {

           LoadDataExtraField loadDataExtraField = null;

            if (dataAboutLoadedObjects.size() == 0) {
                underLoadComplete(sVisitId);
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

                loadDataExtraField = new LoadDataExtraField(oLoginAccount, sqlHelper, sAddress, iCountToLoadSubReport, mContext);
            }


            if(dataAboutLoadedObjects.size()> 0 ){

                for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                    JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                    try {


                        sVisitId = String.valueOf(oControllClassLoading);
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

                    /**
                     * Говорим о том, что это дополнительный протокол
                     */
                    values.put(StructureFullFieldProtocols.EXTRAFIELD, "EXTRAFIELD");
                    values.put(StructureFullFieldProtocols.VISITID, sVisitId);
                    values.put(StructureFullFieldProtocols.PROTOCOLID, sProtocolId);
                    values.put(StructureFullFieldProtocols.FORMITEMID, sFormItemId);
                    values.put(StructureFullFieldProtocols.TYP, sTyp);
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

                    if(sFormItemId!= null){
                        if(!sFormItemId.isEmpty() && !sFormItemId.equals("null")){
                            if(sValueType!= null){
                                if( sValueType.equals("2") || sValueType.equals("3")){
                                    loadDataExtraField.startLoadDataForExtraField(sFormItemId, this);
                                }
                            }
                        }
                    }

                }
            }
        }
    }

    @Override
    public void underLoadComplete(String sFormId) {
        if (iLoadedComleteCallBack != null) {
            iLoadedComleteCallBack.loadedVisitComleted();
        }
    }
}
