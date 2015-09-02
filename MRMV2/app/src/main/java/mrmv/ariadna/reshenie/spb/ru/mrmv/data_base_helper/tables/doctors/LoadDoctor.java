package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.doctors;

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
 * Created by kirichenko on 01.09.2015.
 */
public class LoadDoctor extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    public LoadDoctor(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationAboutListDoctor (String sIdSpecial, ICommonLoadComplete iLoadedCompleteCallBack) {

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/patient/reg/docdeplist/spec/" + sIdSpecial;

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, null);
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();

    }


    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sId = null, sText = null;

        if(sqlHelper != null){
            Doctor.removeAllInfornationAboutTables(sqlHelper);
        }

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);

                try {

                    sId = String.valueOf(jsonObjectLoaderItem.get(Doctor.IDNUMBER));
                    sText = String.valueOf(jsonObjectLoaderItem.get(Doctor.TEXT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(Doctor.IDNUMBER, sId);
                values.put(Doctor.TEXT, sText);

                sqlHelper.getWritableDatabase().insertWithOnConflict(Doctor.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }

        }

        if (iLoadedCompleteCallBack != null) {
            iLoadedCompleteCallBack.loadCompleted();
        }

    }

}

