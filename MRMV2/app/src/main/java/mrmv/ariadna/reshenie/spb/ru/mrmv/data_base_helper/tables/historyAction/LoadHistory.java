package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.historyAction;

import android.content.Context;

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
 * Created by kirichenko on 14.05.2015.
 */
public class LoadHistory  extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ICommonLoadComplete iLoadedComleteCallBack;

    public LoadHistory(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }


    public void startLoadInformationAboutHistory(String sPatientId, Context mContext, ICommonLoadComplete iLoadedComleteCallBack) {

        this.mContext = mContext;

        this.iLoadedComleteCallBack = iLoadedComleteCallBack;
        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/patient/" + sPatientId + "/htmlhistory";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sPatientId);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }


    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {

        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);

        /**
         * Сообщаем о том, что мы загузили данные с сервера и можно получить информацию из базы данных
         */
        if (iLoadedComleteCallBack != null) {
            iLoadedComleteCallBack.loadCompleted();
        }

    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sId = null, sData = null;

        String cleanRequest = "DELETE FROM " + History.TABLE_NAME;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {

                    sData = String.valueOf(oItemGuides.get(History.TEXT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String insertQuery = "INSERT INTO " + History.TABLE_NAME
                        + " (" + History.TEXT + "," + History.PACIENTID +") VALUES ('"
                        + sData + "','" + String.valueOf(identificatieNummer) + "')";

                sqlHelper.getWritableDatabase().execSQL(insertQuery);

            }

        }
    }
}
