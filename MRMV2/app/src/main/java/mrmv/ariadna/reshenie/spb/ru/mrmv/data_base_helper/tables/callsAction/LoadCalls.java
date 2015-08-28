package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;

/**
 * Created by kirichenko on 02.05.2015.
 */
public class LoadCalls extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    /**
     * Интерфейс для предоставления доступа
     */
    private ILoginEnableAccess iLoginEnableAccess;

    /**
     * Интерфейс об уведомлении об загрузки
     */
    private ICommonLoadComplete iCommonLoadComplete;

    private String sAddress;

    public static String ALL_CALLS = "0";

    public LoadCalls(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    /**
     * Загрузка списка вызовов
     *
     * @param iLoginEnableAccess Интерфйс для callBack
     * @return Количество загружаемых таблиц
     */
    public int startLoadCalls(ILoginEnableAccess iLoginEnableAccess) {

        this.iLoginEnableAccess = iLoginEnableAccess;

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/date/" + dateFormat.format(cal.getTime()) + "/status/0";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, 0);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

        return 1;
    }


    public void startLoadCalls(String sDate, Context mContext) {

        this.mContext = mContext;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/date/" + sDate + "/status/0";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, ALL_CALLS);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    public void startLoadCalls(String sPage, String sDate, Context mContext, ICommonLoadComplete iCommonLoadComplete) {

        this.iCommonLoadComplete = iCommonLoadComplete;
        this.mContext = mContext;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/date/" + sDate + "/status/0";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sPage);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {

        saveLoadedItemToDB(converJsonToList(mContext,jsonObject), identificatieNummer);

//        if (mContext != null) {
//            Uri URI_MY_TABLE = Uri.fromFile(mContext.getDatabasePath(MedicalCommonConstants.DATABASE_NAME));
//            mContext.getContentResolver().notifyChange(URI_MY_TABLE, null);
//        }


        if (iLoginEnableAccess != null) {
            iLoginEnableAccess.enableAccessLoadFinished(oLoginAccount);
        }

         if(iCommonLoadComplete != null){
            iCommonLoadComplete.loadCompleted();
        }

    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        //*Сперва удаляем все записи по этому справочнику

        String cleanRequest = "DELETE FROM " + Calls.TABLE_NAME;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject callItem = dataAboutLoadedObjects.get(iCount);
                try {

                    String sId, sStatus, sProtocolStatus, sCallDate, sLastName, sFirstName, sSecondName, sSex,
                            sBirthDate, sAge, sRegAddress, sLocAddress, sNote,
                            sPatientId, sPhone, sCellular, sRegAddressFull, sLocAddressFull;

                    sId = String.valueOf(callItem.get(Calls.IDCALLS));
                    sStatus = String.valueOf(callItem.get(Calls.STATUS));
                    sProtocolStatus = String.valueOf(callItem.get(Calls.PROTOCOL_STATUS));

                    if (String.valueOf(callItem.get(Calls.CALL_DATE)).equals("null")) {
                        sCallDate = "-";
                    } else {
                        sCallDate = String.valueOf(callItem.get(Calls.CALL_DATE));
                    }

                    if (String.valueOf(callItem.get(Calls.PATIENTID)).equals("null")) {
                        sPatientId = "-";
                    } else {
                        sPatientId = String.valueOf(callItem.get(Calls.PATIENTID));
                    }

                    if (String.valueOf(callItem.get(Calls.PHONE)).equals("null")) {
                        sPhone = "-";
                    } else {
                        sPhone = String.valueOf(callItem.get(Calls.PHONE));
                    }

                    if (String.valueOf(callItem.get(Calls.CELLULAR)).equals("null")) {
                        sCellular = "-";
                    } else {
                        sCellular = String.valueOf(callItem.get(Calls.CELLULAR));
                    }

                    if (String.valueOf(callItem.get(Calls.LAST_NAME)).equals("null")) {
                        sLastName = "-";
                    } else {
                        sLastName = String.valueOf(callItem.get(Calls.LAST_NAME));
                    }

                    if (String.valueOf(callItem.get(Calls.FIRST_NAME)).equals("null")) {
                        sFirstName = "-";
                    } else {
                        sFirstName = String.valueOf(callItem.get(Calls.FIRST_NAME));
                    }

                    if (String.valueOf(callItem.get(Calls.SECOND_NAME)).equals("null")) {
                        sSecondName = "-";
                    } else {
                        sSecondName = String.valueOf(callItem.get(Calls.SECOND_NAME));
                    }

                    if (String.valueOf(callItem.get(Calls.SEX)).equals("null")) {
                        sSex = "-";
                    } else {
                        sSex = String.valueOf(callItem.get(Calls.SEX));
                    }

                    if (String.valueOf(callItem.get(Calls.BIRTHDATE)).equals("null")) {
                        sBirthDate = "-";
                    } else {
                        sBirthDate = String.valueOf(callItem.get(Calls.BIRTHDATE));
                    }

                    if (String.valueOf(callItem.get(Calls.AGE)).equals("null")) {
                        sAge = "-";
                    } else {
                        sAge = String.valueOf(callItem.get(Calls.AGE));
                    }

                    if (String.valueOf(callItem.get(Calls.REGADDRESS)).equals("null")) {
                        sRegAddress = "-";
                    } else {
                        sRegAddress = String.valueOf(callItem.get(Calls.REGADDRESS));
                    }

                    if (String.valueOf(callItem.get(Calls.LOCADDRESS)).equals("null")) {
                        sLocAddress = "-";
                    } else {
                        sLocAddress = String.valueOf(callItem.get(Calls.LOCADDRESS));
                    }

                    if (String.valueOf(callItem.get(Calls.NOTE)).equals("null")) {
                        sNote = "-";
                    } else {
                        sNote = String.valueOf(callItem.get(Calls.NOTE));
                    }

                    if (String.valueOf(callItem.get(Calls.REGADDRESSFULL)).equals("null")) {
                        sRegAddressFull = "-";
                    } else {
                        sRegAddressFull = String.valueOf(callItem.get(Calls.REGADDRESSFULL));
                    }

                    if (String.valueOf(callItem.get(Calls.LOCADDRESSFULL)).equals("null")) {
                        sLocAddressFull = "-";
                    } else {
                        sLocAddressFull = String.valueOf(callItem.get(Calls.LOCADDRESSFULL));
                    }

                    String insertQuery = "INSERT INTO " + Calls.TABLE_NAME
                            + " (" + Calls.IDCALLS + "," + Calls.STATUS + "," + Calls.PROTOCOL_STATUS + "," + Calls.CALL_DATE + "," + Calls.TAG + ","
                            + Calls.LAST_NAME + "," + Calls.FIRST_NAME + "," + Calls.SECOND_NAME + "," + Calls.SEX + "," + Calls.BIRTHDATE + ","
                            + Calls.AGE + "," + Calls.REGADDRESS + "," + Calls.LOCADDRESS + "," + Calls.NOTE + ","
                            + Calls.PATIENTID + "," + Calls.PHONE + "," + Calls.CELLULAR + "," + Calls.REGADDRESSFULL + "," + Calls.LOCADDRESSFULL +
                            ") VALUES ('"
                            + sId + "','" + sStatus + "','" + sProtocolStatus + "','" + sCallDate + "','" + identificatieNummer + "','"
                            + sLastName + "','" + sFirstName + "','" + sSecondName + "','" + sSex + "','" + sBirthDate + "','"
                            + sAge + "','" + sRegAddress + "','" + sLocAddress + "','" + sNote + "','" + sPatientId + "','" + sPhone + "','"
                            + sCellular + "','" + sRegAddressFull + "','" + sLocAddressFull + "')";

                    sqlHelper.getWritableDatabase().execSQL(insertQuery);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
