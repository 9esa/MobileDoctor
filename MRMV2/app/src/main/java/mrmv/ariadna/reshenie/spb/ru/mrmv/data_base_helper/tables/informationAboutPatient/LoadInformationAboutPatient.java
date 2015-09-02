package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.informationAboutPatient;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk.ItemEmk;

/**
 * Created by kirichenko on 26.08.2015.
 */
public class LoadInformationAboutPatient extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private ICommonLoadComplete iLoadedCompleteCallBack;

    private LoginAccount oLoginAccount;
    private DataBaseHelper oDataBaseHelper;
    private Context mContext;

    private String sAddress;

    private ItemEmk oItemEmk;

    public LoadInformationAboutPatient(LoginAccount oLoginAccount, DataBaseHelper oDataBaseHelper, String sAddress, Context mContext) {

        this.oLoginAccount = oLoginAccount;
        this.oDataBaseHelper = oDataBaseHelper;
        this.mContext = mContext;
        this.sAddress = sAddress;
    }

    public void startLoadFoundPatients(ItemEmk oItemEmk, ICommonLoadComplete iLoadedCompleteCallBack) {

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;
        this.oItemEmk = oItemEmk;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/patient/search?";

        if(oItemEmk != null){

            if(oItemEmk.getiNumberCard() != null){
                sValueAddressForRequest += "&medcardnumber=" + oItemEmk.getiNumberCard();
            }

            if(oItemEmk.getsName() != null){
                sValueAddressForRequest += "&firstname=" + oItemEmk.getsName();
            }

            if(oItemEmk.getsSecondName() != null){
                sValueAddressForRequest += "&lastname=" + oItemEmk.getsSecondName();
            }

            if (oItemEmk.getsThirdName() != null){
                sValueAddressForRequest += "&secondname=" + oItemEmk.getsThirdName();
            }

            if(oItemEmk.getiPhone() != null){
                sValueAddressForRequest += "&phone=" + oItemEmk.getiPhone();
            }

        }

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, new Object());
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
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sId = "", sName = "", sSecondName = "",
                sThirdName = "", sNumberCard = "", sPhone = "", sAddress =  "", sBirthDate = "";

        if (dataAboutLoadedObjects != null) {

            Patients.removeAllInfornationAboutTables(oDataBaseHelper);

            String sql = "INSERT INTO " + Patients.TABLE_NAME + " VALUES (NULL,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = oDataBaseHelper.getWritableDatabase().compileStatement(sql);
            oDataBaseHelper.getWritableDatabase().beginTransaction();

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {
                    sId = String.valueOf(oItemGuides.get(Patients.IDPATIENT));
                    sName = String.valueOf(oItemGuides.get(Patients.PATIENT_NAME));
                    sSecondName = String.valueOf(oItemGuides.get(Patients.PATIENT_SECONDNAME));
                    sThirdName = String.valueOf(oItemGuides.get(Patients.PATIENT_THIRD_NAME));
                    sNumberCard = String.valueOf(oItemGuides.get(Patients.NUMBER_CARD));
                    sPhone = String.valueOf(oItemGuides.get(Patients.PHONE));
                    sAddress = String.valueOf(oItemGuides.get(Patients.ADDRESS));
                    sBirthDate = String.valueOf(oItemGuides.get(Patients.BIRTHDATE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                statement.clearBindings();

                statement.bindString(1, sId);
                statement.bindString(2,sName);
                statement.bindString(3,sSecondName);
                statement.bindString(4,sThirdName);
                statement.bindString(5,sNumberCard);
                statement.bindString(6,sPhone);
                statement.bindString(7,sAddress);
                statement.bindString(8,sBirthDate);

                statement.execute();

            }

            oDataBaseHelper.getWritableDatabase().setTransactionSuccessful();
            oDataBaseHelper.getWritableDatabase().endTransaction();
        }

        if(iLoadedCompleteCallBack != null){
            iLoadedCompleteCallBack.loadCompleted();
        }

    }
}
