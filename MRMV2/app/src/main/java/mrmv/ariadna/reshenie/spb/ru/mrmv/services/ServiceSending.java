package mrmv.ariadna.reshenie.spb.ru.mrmv.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.SendDiagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon.SendExtraField;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.SendMes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers.SendNumbers;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.SendProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction.SendVisit;

/**
 * Created by kirichenko on 05.05.2015.
 */
public class ServiceSending extends Service {

    public static String LOG_TAG = "ServiceSending";

    private DataBaseHelper oDataBaseHelper;

    private ServiceBinder serviceBinder = new ServiceBinder();

    private SharedPreferences sharedPreferences;

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, LOG_TAG + " OnCreate");

        // Инициализируем наш класс-обёртку
        oDataBaseHelper = DataBaseHelper.getInstance(this);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private String getAddressForRequest(){
        String sAddress = sharedPreferences.getString("address_connection", "");
        return sAddress;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, LOG_TAG + " onDestroy");

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, LOG_TAG + " onRebind");
    }

    public void startSendingInformationAboutExtraFields(LoginAccount oLoginAccount, String sVisitId) {
        new SendExtraField(this,oLoginAccount, oDataBaseHelper, getAddressForRequest()).startSend(sVisitId);
    }

    public void startSendingInformationAboutVisit(LoginAccount oLoginAccount, String sVisitId) {
        new SendVisit(this,oLoginAccount, oDataBaseHelper, getAddressForRequest()).startSend(sVisitId);
    }

    public void startSendingInformationAboutMesbyVisit(LoginAccount oLoginAccount, String sVisitId) {
        new SendMes(this,oLoginAccount, oDataBaseHelper,  getAddressForRequest()).startSend(sVisitId);
    }

    public void startSendingInformationAboutDiagnoses(LoginAccount oLoginAccount, String sVisitId) {
        new SendDiagnose(this,oLoginAccount, oDataBaseHelper,  getAddressForRequest()).startSend(sVisitId);
    }

    public void startDeleteInformationaAboutProtocols(LoginAccount oLoginAccount, String sFormResultId, String sVisitId) {
        new SendExtraField(this,oLoginAccount, oDataBaseHelper, getAddressForRequest()).startDelete(sFormResultId, sVisitId);
    }

    public void startSendingInformationaAboutProtocols(LoginAccount oLoginAccount, String sVisitId,String sId, String sFormId, String sFormResultId){
        new SendProtocols(this,oLoginAccount, oDataBaseHelper,  getAddressForRequest()).startSendValueProtocols(sId, sVisitId, sFormId, sFormResultId);
    }

    public void startSendingInformationaAboutBookingNumb(LoginAccount oLoginAccount, ICommonLoadComplete iLoadedCompleteCallBack, String sIdNumber, String sPacientId, String sStatusNumber, String sRequestDate, String sDocDepId ){
        new SendNumbers(this,oLoginAccount, oDataBaseHelper,  getAddressForRequest()).startRequestToServer(sIdNumber, sPacientId, sStatusNumber, iLoadedCompleteCallBack, sRequestDate, sDocDepId);
    }


    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, LOG_TAG + " onBind");
        return serviceBinder;
    }

    public class ServiceBinder extends Binder {
        public ServiceSending getService() {
            return ServiceSending.this;
        }
    }

}
