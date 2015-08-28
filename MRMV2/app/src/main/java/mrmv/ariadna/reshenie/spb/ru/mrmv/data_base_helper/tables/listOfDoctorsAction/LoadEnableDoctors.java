package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction;

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
 * Created by kirichenko on 22.06.2015.
 */
public class LoadEnableDoctors extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    public LoadEnableDoctors(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress,Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sId = null, sStatusId = null, sCode = null, sName = null, sSpecname = null,
                sCategname = null,sDepid = null, sDepname = null, sNote =  null;

        if(sqlHelper != null){
            EnableDoctors.removeAllInfornationAboutTables(sqlHelper);
        }

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                try {

                    sId = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.IDDOCTOR));
                    sStatusId = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.STATUS));
                    sCode = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.CODE));
                    sName = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.NAME));
                    sSpecname = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.SPECNAME));
                    sCategname = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.CATEGNAME));
                    sDepid = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.DEPID));
                    sDepname = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.DEPNAME));
                    sNote = String.valueOf(jsonObjectLoaderItem.get(EnableDoctors.NOTE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(EnableDoctors.IDDOCTOR, sId);
                values.put(EnableDoctors.STATUS, sStatusId);
                values.put(EnableDoctors.CODE, sCode);
                values.put(EnableDoctors.NAME, sName);
                values.put(EnableDoctors.SPECNAME, sSpecname);
                values.put(EnableDoctors.CATEGNAME, sCategname);
                values.put(EnableDoctors.DEPID, sDepid);
                values.put(EnableDoctors.DEPNAME, sDepname);
                values.put(EnableDoctors.NOTE, sNote);

                sqlHelper.getWritableDatabase().insertWithOnConflict(EnableDoctors.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
        }

        if (iLoadedCompleteCallBack != null) {
            iLoadedCompleteCallBack.loadCompleted();
        }

    }

    public void startLoadInformationAboutEnableDoctors(Context mContext, ICommonLoadComplete iLoadedCompleteCallBack) {

        this.mContext = mContext;

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/lpu/userdocdeplist";

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this,"");
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();
    }
}
