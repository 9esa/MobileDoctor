package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction;

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

/**
 * Created by kirichenko on 18.06.2015.
 */
public class LoadMes extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private static int MES = 0;
    private static int MESFORVISIT = 1;
    private static int MESFORVISITWITHCONTROL = 2;

    private Context mContext;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    public LoadMes(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress,Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationAboutMesByDiagnoses(String sDiagnoseId, Context mContext, ICommonLoadComplete iLoadedCompleteCallBack) {

        this.mContext = mContext;

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/mkb10/" + sDiagnoseId + "/meslist";

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, new ControlLoadingInformation(sDiagnoseId,MES));
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();

    }

    public void startLoadInformationAboutMesByVisit(String sVisitId, Context mContext, ICommonLoadComplete iLoadedCompleteCallBack) {

        this.mContext = mContext;

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/" + sVisitId + "/mes";

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, new ControlLoadingInformation(sVisitId,MESFORVISIT));
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();

    }

    public void startLoadInformationAboutMesByVisitWithEmptyControl(String sVisitId, String sDiagnoseId, Context mContext, ICommonLoadComplete iLoadedCompleteCallBack) {


        this.mContext = mContext;

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/" + sVisitId + "/mes";

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, new ControlLoadingInformation(sVisitId,MESFORVISITWITHCONTROL, sDiagnoseId));
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();
    }


    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificationNumber) {

        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificationNumber);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificationNumber) {

        if(identificationNumber != null){
            if(identificationNumber instanceof ControlLoadingInformation ){
                ControlLoadingInformation oReturnObject = (ControlLoadingInformation) identificationNumber;

                if(oReturnObject.getiTypeRequest() == MES ){
                    saveMesValueByDiagnoses(dataAboutLoadedObjects, oReturnObject);
                }else if ((oReturnObject.getiTypeRequest() == MESFORVISIT )){
                    saveMesValueByVisit(dataAboutLoadedObjects, oReturnObject);
                }else if((oReturnObject.getiTypeRequest() == MESFORVISITWITHCONTROL )) {
                    saveMesValueByVisitWithControl(dataAboutLoadedObjects, oReturnObject);
                }else{
                    if (iLoadedCompleteCallBack != null) {
                        iLoadedCompleteCallBack.loadCompleted();
                    }
                }

            }
        }
    }

    private void saveMesValueByDiagnoses(ArrayList<JSONObject> dataAboutLoadedObjects, ControlLoadingInformation oReturnObject){

        String sMesId = null, sCode = null, sText = null;

        if(sqlHelper != null){
            MesForDiagnoses.removeAllInfornationAboutTables(sqlHelper);
        }

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                try {

                    sMesId = String.valueOf(jsonObjectLoaderItem.get(MesForDiagnoses.MESID));
                    sCode = String.valueOf(jsonObjectLoaderItem.get(MesForDiagnoses.CODE));
                    sText = String.valueOf(jsonObjectLoaderItem.get(MesForDiagnoses.TEXT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(MesForDiagnoses.MESID, sMesId);
                values.put(MesForDiagnoses.DIAGNOSEID, oReturnObject.getsIdRequestItem());
                values.put(MesForDiagnoses.CODE, sCode);
                values.put(MesForDiagnoses.TEXT, sText);

                sqlHelper.getWritableDatabase().insertWithOnConflict(MesForDiagnoses.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
        }

        if (iLoadedCompleteCallBack != null) {
            iLoadedCompleteCallBack.loadCompleted();
        }

    }

    private void saveMesValueByVisit(ArrayList<JSONObject> dataAboutLoadedObjects, ControlLoadingInformation oReturnObject){

        String sMesId = null, sMesCode = null, sMesText = null, sMesOpenDate = null, sOpenDocdepId = null,
               sOpenDocdepText = null,sMesCloseDate = null, sCloseDocdepId = null, sCloseDocdepText =  null, sCloseTypeId = null, sCloseTypetext = null,
               sNote = null;

        if(sqlHelper != null){
            Mes.removeAllInfornationAboutTables(sqlHelper);
        }

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                try {

                    sMesId = String.valueOf(jsonObjectLoaderItem.get(Mes.MESID));
                    sMesCode = String.valueOf(jsonObjectLoaderItem.get(Mes.MESCODE));
                    sMesText = String.valueOf(jsonObjectLoaderItem.get(Mes.MESTEXT));
                    sMesOpenDate = String.valueOf(jsonObjectLoaderItem.get(Mes.MESOPENDATE));
                    sOpenDocdepId = String.valueOf(jsonObjectLoaderItem.get(Mes.OPENDOCDEPID));
                    sOpenDocdepText = String.valueOf(jsonObjectLoaderItem.get(Mes.OPENDOCDEPTEXT));
                    sMesCloseDate = String.valueOf(jsonObjectLoaderItem.get(Mes.MESCLOSEDATE));
                    sCloseDocdepId = String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSEDOCDEPID));
                    sCloseDocdepText =  String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSEDOCDEPTEXT));
                    sCloseTypeId = String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSETYPEID));
                    sCloseTypetext = String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSETYPETEXT));
                    sNote = String.valueOf(jsonObjectLoaderItem.get(Mes.NOTE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(Mes.VISITID, oReturnObject.getsIdRequestItem());
                values.put(Mes.MESID, sMesId);
                values.put(Mes.MESCODE, sMesCode);
                values.put(Mes.MESTEXT, sMesText);
                values.put(Mes.MESOPENDATE, sMesOpenDate);
                values.put(Mes.OPENDOCDEPID, sOpenDocdepId);
                values.put(Mes.OPENDOCDEPTEXT, sOpenDocdepText);
                values.put(Mes.MESCLOSEDATE, sMesCloseDate);
                values.put(Mes.CLOSEDOCDEPID, sCloseDocdepId);
                values.put(Mes.CLOSEDOCDEPTEXT, sCloseDocdepText);
                values.put(Mes.CLOSETYPEID, sCloseTypeId);
                values.put(Mes.CLOSETYPETEXT, sCloseTypetext);
                values.put(Mes.NOTE, sNote);

                sqlHelper.getWritableDatabase().insertWithOnConflict(Mes.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
        }

        if (iLoadedCompleteCallBack != null) {
            iLoadedCompleteCallBack.loadCompleted();
        }

    }

    public void saveMesValueByVisitWithControl(ArrayList<JSONObject> dataAboutLoadedObjects, ControlLoadingInformation oReturnObject){
        String sMesId = null, sMesCode = null, sMesText = null, sMesOpenDate = null, sOpenDocdepId = null,
                sOpenDocdepText = null,sMesCloseDate = null, sCloseDocdepId = null, sCloseDocdepText =  null, sCloseTypeId = null, sCloseTypetext = null,
                sNote = null;

        if(sqlHelper != null){
            Mes.removeAllInfornationAboutTables(sqlHelper);
        }

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                try {

                    sMesId = String.valueOf(jsonObjectLoaderItem.get(Mes.MESID));
                    sMesCode = String.valueOf(jsonObjectLoaderItem.get(Mes.MESCODE));
                    sMesText = String.valueOf(jsonObjectLoaderItem.get(Mes.MESTEXT));
                    sMesOpenDate = String.valueOf(jsonObjectLoaderItem.get(Mes.MESOPENDATE));
                    sOpenDocdepId = String.valueOf(jsonObjectLoaderItem.get(Mes.OPENDOCDEPID));
                    sOpenDocdepText = String.valueOf(jsonObjectLoaderItem.get(Mes.OPENDOCDEPTEXT));
                    sMesCloseDate = String.valueOf(jsonObjectLoaderItem.get(Mes.MESCLOSEDATE));
                    sCloseDocdepId = String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSEDOCDEPID));
                    sCloseDocdepText =  String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSEDOCDEPTEXT));
                    sCloseTypeId = String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSETYPEID));
                    sCloseTypetext = String.valueOf(jsonObjectLoaderItem.get(Mes.CLOSETYPETEXT));
                    sNote = String.valueOf(jsonObjectLoaderItem.get(Mes.NOTE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(Mes.VISITID, oReturnObject.getsIdRequestItem());
                values.put(Mes.MESID, sMesId);
                values.put(Mes.MESCODE, sMesCode);
                values.put(Mes.MESTEXT, sMesText);
                values.put(Mes.MESOPENDATE, sMesOpenDate);
                values.put(Mes.OPENDOCDEPID, sOpenDocdepId);
                values.put(Mes.OPENDOCDEPTEXT, sOpenDocdepText);
                values.put(Mes.MESCLOSEDATE, sMesCloseDate);
                values.put(Mes.CLOSEDOCDEPID, sCloseDocdepId);
                values.put(Mes.CLOSEDOCDEPTEXT, sCloseDocdepText);
                values.put(Mes.CLOSETYPEID, sCloseTypeId);
                values.put(Mes.CLOSETYPETEXT, sCloseTypetext);
                values.put(Mes.NOTE, sNote);

                sqlHelper.getWritableDatabase().insertWithOnConflict(Mes.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
        }

        startLoadInformationAboutMesByDiagnoses(oReturnObject.getsDiagnoses(), mContext, iLoadedCompleteCallBack);

    }

    class ControlLoadingInformation{

        String sIdRequestItem;
        String sDiagnoses;
        int iTypeRequest;

        ControlLoadingInformation(String sIdRequestItem, int iTypeRequest) {
            this.sIdRequestItem = sIdRequestItem;
            this.iTypeRequest = iTypeRequest;
        }

        ControlLoadingInformation(String sIdRequestItem, int iTypeRequest, String sDiagnoses) {
           this(sIdRequestItem,iTypeRequest);
           this.sDiagnoses = sDiagnoses;

        }

        public String getsIdRequestItem() {
            return sIdRequestItem;
        }

        public void setsIdRequestItem(String sIdRequestItem) {
            this.sIdRequestItem = sIdRequestItem;
        }

        public int getiTypeRequest() {
            return iTypeRequest;
        }

        public void setiTypeRequest(int iTypeRequest) {
            this.iTypeRequest = iTypeRequest;
        }

        public String getsDiagnoses() {
            return sDiagnoses;
        }
    }
}
