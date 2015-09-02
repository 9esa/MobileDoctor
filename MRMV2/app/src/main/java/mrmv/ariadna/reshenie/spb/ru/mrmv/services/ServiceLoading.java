package mrmv.ariadna.reshenie.spb.ru.mrmv.services;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.ILoadedComleted;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.LoadMedicalGuides;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuidesMkb10.LoadMedicalGuidesMKB10;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.LoadCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.LoadDiagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.doctors.LoadDoctor;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.historyAction.LoadHistory;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.informationAboutPatient.LoadInformationAboutPatient;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction.LoadEnableDoctors;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.LoadMes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers.LoadNumbers;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.LoadProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocolsFiltersAction.LoadProtocolsFilters;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.specials.LoadSpecial;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.statusDataBase.StatusDatabase;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction.LoadVisit;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk.ItemEmk;

/**
 * Created by kirichenko on 01.05.2015.
 * Сервис загрузки данных
 */
public class ServiceLoading extends Service {

    public static String LOG_TAG = "ServiceLoading";

    private DataBaseHelper oDataBaseHelper;
    private SQLiteDatabase sdb;

    private ServiceBinder serviceBinder = new ServiceBinder();

    private SharedPreferences sharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();

        Log.d(LOG_TAG, LOG_TAG + " OnCreate");

        // Инициализируем наш класс-обёртку
        oDataBaseHelper = DataBaseHelper.getInstance(this);

        // База нам нужна для записи и чтения
        sdb = oDataBaseHelper.getWritableDatabase();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    }

    private String getAddressForRequest() {

        if (sharedPreferences != null) {
            String sAddress = sharedPreferences.getString("address_connection", "");
            return sAddress;
        } else {
            return "";
        }

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d(LOG_TAG, LOG_TAG + " onDestroy");
        //todo Надо подумать как закрывать базу после остановки сервиса.
        // закрываем соединения с базой данных
        //  sdb.close();
        //  oDataBaseHelper.close();

    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d(LOG_TAG, LOG_TAG + " onRebind");
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, LOG_TAG + " onBind");
        return serviceBinder;
    }

    //Необходимо добавить проверку первичности базы данных
    public boolean checkDataBase() {

        if (oDataBaseHelper.isbNewDataBase()) {
            return true;
        } else {

            if (StatusDatabase.getInformationAboutStatusDataBase(oDataBaseHelper) == 0) {
                return true;
            } else {
                return false;
            }
        }

    }

    /**
     * Удаляем структуру базы данных
     */
    public void deleteDataBase() {

        oDataBaseHelper.deleteAllDataBase(sdb);

    }

    public boolean getInformationAboutDiagnoses(LoginAccount oLoginAccount, String sVisit, ILoadedComleted iLoadedComleteCallBack) {
        new LoadDiagnose(oLoginAccount, oDataBaseHelper, getAddressForRequest()).startLoadInformationAboutDiagnoses(sVisit, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }

    public boolean getInformationAboutProtocols(LoginAccount oLoginAccount, String sVisit, ICommonLoadComplete iLoadedComleteCallBack) {
        new LoadProtocols(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadFullFieldProtocolsByVisit(sVisit, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }

    public boolean getInformationAboutMesByVisit(LoginAccount oLoginAccount, String sVisit, ICommonLoadComplete iLoadedComleteCallBack) {
        new LoadMes(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutMesByVisit(sVisit, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }

    public boolean getInformationAboutMesByVisitWithEmptyControl(LoginAccount oLoginAccount, String sVisit, String sDiagnose, ICommonLoadComplete iLoadedComleteCallBack) {
        new LoadMes(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutMesByVisitWithEmptyControl(sVisit, sDiagnose, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }


    public boolean getInformationAboutEnableDoctors(LoginAccount oLoginAccount, ICommonLoadComplete iLoadedComleteCallBack) {
        new LoadEnableDoctors(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutEnableDoctors(getBaseContext(), iLoadedComleteCallBack);
        return true;
    }

    public boolean getInformationAboutMesByDiagnoses(LoginAccount oLoginAccount, String sDiagnoses, ICommonLoadComplete iLoadedComleteCallBack) {
        new LoadMes(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutMesByDiagnoses(sDiagnoses, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }


    public boolean getInformationAboutHistory(LoginAccount oLoginAccount, String sPatientId, ICommonLoadComplete iLoadedComleteCallBack) {
        new LoadHistory(oLoginAccount, oDataBaseHelper, getAddressForRequest()).startLoadInformationAboutHistory(sPatientId, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }

    public boolean getInformationAboutVisit(LoginAccount oLoginAccount, String sVisit, ILoadedComleted iLoadedComleteCallBack) {

        new LoadVisit(oLoginAccount, oDataBaseHelper, getAddressForRequest()).startLoadInformationAboutVisit(sVisit, getBaseContext(), iLoadedComleteCallBack);
        return true;
    }

    public boolean getCallByDate(LoginAccount oLoginAccount, String sDate, String sPage, ICommonLoadComplete iCommonLoadComplete) {

        new LoadCalls(oLoginAccount, oDataBaseHelper, getAddressForRequest()).startLoadCalls(sPage, sDate, getBaseContext(), iCommonLoadComplete);
        return true;
    }

    public boolean getCallByDate(LoginAccount oLoginAccount, String sDate) {

        new LoadCalls(oLoginAccount, oDataBaseHelper, getAddressForRequest()).startLoadCalls(sDate, getBaseContext());
        return true;
    }

    public boolean getStructureEnableProtocols(LoginAccount oLoginAccount, String sFormId, ICommonLoadComplete iLoadedComleteCallBack) {

        new LoadProtocols(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadStructureEnabledProtocols(sFormId, iLoadedComleteCallBack);

        return true;
    }

    public boolean getListPatient(LoginAccount oLoginAccount, ItemEmk oItemEmk, ICommonLoadComplete iLoadedComleteCallBack){

        new LoadInformationAboutPatient(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadFoundPatients(oItemEmk, iLoadedComleteCallBack);

        return true;
    }

    public boolean getListNumbers(LoginAccount oLoginAccount, String sDate, String sDocDepId, ICommonLoadComplete iLoadedComleteCallBack){

        new LoadNumbers(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutEnableDoctors(sDate, sDocDepId, iLoadedComleteCallBack);

        return true;
    }

    public boolean getListSpecials(LoginAccount oLoginAccount, ICommonLoadComplete iLoadedComleteCallBack){

        new LoadSpecial(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutSpecials(iLoadedComleteCallBack);

        return true;
    }

    public boolean getListDoctorsViaSpecials(LoginAccount oLoginAccount, String sIdSpecial, ICommonLoadComplete iLoadedComleteCallBack){

        new LoadDoctor(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadInformationAboutListDoctor(sIdSpecial, iLoadedComleteCallBack);

        return true;
    }


    public int startUpdateGuides(ILoginEnableAccess iLoginEnableAccess) {

        int iCountToRequest = 0;

        oDataBaseHelper.deleteAllDataBase(sdb);


      //   iCountToRequest += new LoadProtocols(null, sqlHelper, sAddress).startLoadEnabledProtocols(iLoginEnableAccess);

        iCountToRequest += new LoadMedicalGuidesMKB10(null, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadMedicalGuides(iLoginEnableAccess);
        iCountToRequest += new LoadMedicalGuides(null, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadMedicalGuides(iLoginEnableAccess);
  //      iCountToRequest += new LoadEnableStructureDataField(null, oDataBaseHelper, getAddressForRequest()).startLoadEnableStructureDataField(iLoginEnableAccess);


        return iCountToRequest;

    }

    public int startFirstLoadDataBase(LoginAccount oLoginAccount, ILoginEnableAccess iLoginEnableAccess) {

        int iCountToRequest = 0;

        iCountToRequest += new LoadProtocols(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadEnabledProtocols(iLoginEnableAccess);
        iCountToRequest += new LoadMedicalGuidesMKB10(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadMedicalGuides(iLoginEnableAccess);
        iCountToRequest += new LoadMedicalGuides(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadMedicalGuides(iLoginEnableAccess);
        iCountToRequest += new LoadProtocolsFilters(oLoginAccount, oDataBaseHelper, getAddressForRequest(), getBaseContext()).startLoadProtocolsFilters(iLoginEnableAccess);

        //      iCountToRequest += new LoadCalls(oLoginAccount, sqlHelper, sAddress).startLoadCalls(iLoginEnableAccess);

        return iCountToRequest;

    }

    public class ServiceBinder extends Binder {
        public ServiceLoading getService() {
            return ServiceLoading.this;
        }
    }
}
