package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon.ExtraFieldStatTalon;

/**
 * Created by kirichenko on 29.05.2015.
 */

public class LoadGuidesForSpinnerData extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;
    private DataBaseHelper sqlHelper;
    private String sAddress;
    private Context mContext;
    private Integer iSizeOfRequests;

    private ICallBackForUnderDownLoad iCallBackForUnderDownLoad;

    public LoadGuidesForSpinnerData(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Integer iSizeOfRequestsLocal, Context mContext) {
            this.oLoginAccount = oLoginAccount;
            this.sqlHelper = sqlHelper;
            this.sAddress = sAddress;
            this.mContext = mContext;

            iSizeOfRequests = new Integer(0);

            synchronized (iSizeOfRequests){
                iSizeOfRequests += iSizeOfRequestsLocal;
            }
    }

    public void LoadGuidesForSpinnerData(String sValueFormId,ICallBackForUnderDownLoad iCallBackForUnderDownLoad) {

        this.iCallBackForUnderDownLoad = iCallBackForUnderDownLoad;



        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/protocolstruct/list/formitem/" + sValueFormId;

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sValueFormId);

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
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sFormItemId = null, sIdExtraField = null, sCode = null, sText =  null;

        if (dataAboutLoadedObjects != null) {

            sqlHelper.getWritableDatabase().beginTransaction();

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {
                    sFormItemId = String.valueOf(identificatieNummer);
                    sIdExtraField = String.valueOf(oItemGuides.get(ExtraFieldStatTalon.ID));
                    sCode = String.valueOf(oItemGuides.get(ExtraFieldStatTalon.CODE));
                    sText = String.valueOf(oItemGuides.get(ExtraFieldStatTalon.TEXT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(ExtraFieldStatTalon.FORMID, sFormItemId);
                values.put(ExtraFieldStatTalon.EXTRAFIELD, "EXTRAFIELD");
                values.put(ExtraFieldStatTalon.IDEXTRAFIELD, sIdExtraField);
                values.put(ExtraFieldStatTalon.CODE, sCode);
                values.put(ExtraFieldStatTalon.TEXT, sText);

               // sqlHelper.getWritableDatabase().insertWithOnConflict(ExtraFieldStatTalon.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                sqlHelper.getWritableDatabase().insert(ExtraFieldStatTalon.TABLE_NAME, null, values);
            }

            sqlHelper.getWritableDatabase().setTransactionSuccessful();
            sqlHelper.getWritableDatabase().endTransaction();
        }

        synchronized (iSizeOfRequests){
            iSizeOfRequests--;

            if(iSizeOfRequests == 0){
                if(iCallBackForUnderDownLoad != null){
                    iCallBackForUnderDownLoad.underLoadComplete(String.valueOf(identificatieNummer));
                }
            }
        }

    }
}

