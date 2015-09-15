package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers;

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
 * Created by kirichenko on 28.08.2015.
 */
public class LoadNumbers extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    public LoadNumbers(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationAboutEnableDoctors(String sDate, String sDocDepId, ICommonLoadComplete iLoadedCompleteCallBack,String sIdPacient) {

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/patient/reg/rnumblist/docdep/" + sDocDepId + "/date/" + sDate + "?patientid=" + sIdPacient ;

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, new RequestObject(sDate, sDocDepId));
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

        String sId = null, sIdDoctor = null, sTime = null, sStatus = null, sDate = null, sPacientId = null;

        if(sqlHelper != null){
            Numbers.removeAllInfornationAboutTables(sqlHelper);
        }

        if (dataAboutLoadedObjects != null) {

            if(identificatieNummer != null){

                if(identificatieNummer instanceof RequestObject){

                    RequestObject oRequestObject = (RequestObject) identificatieNummer;

                    for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                        JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                        try {

                            sId = String.valueOf(jsonObjectLoaderItem.get(Numbers.IDNUMBER));
                            sIdDoctor = oRequestObject.getsIdDoctor();
                            sTime = String.valueOf(jsonObjectLoaderItem.get(Numbers.TIME));
                            sStatus = String.valueOf(jsonObjectLoaderItem.get(Numbers.STATUS));
                            sDate = oRequestObject.getsDate();
                            sPacientId = String.valueOf(jsonObjectLoaderItem.get(Numbers.PATIENTID));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        ContentValues values = new ContentValues();

                        values.put(Numbers.IDNUMBER, sId);
                        values.put(Numbers.IDDOCTOR, sIdDoctor);
                        values.put(Numbers.TIME, sTime);
                        values.put(Numbers.STATUS, sStatus);
                        values.put(Numbers.DATE, sDate);
                        values.put(Numbers.PATIENTID, sPacientId);

                        sqlHelper.getWritableDatabase().insertWithOnConflict(Numbers.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    }
                }
            }
        }

        if (iLoadedCompleteCallBack != null) {
            iLoadedCompleteCallBack.loadCompleted();
        }

    }



    class RequestObject{
        String sDate;
        String sIdDoctor;

        public RequestObject(String sDate, String sIdDoctor) {
            this.sDate = sDate;
            this.sIdDoctor = sIdDoctor;
        }

        public String getsDate() {
            return sDate;
        }

        public String getsIdDoctor() {
            return sIdDoctor;
        }
    }

}
