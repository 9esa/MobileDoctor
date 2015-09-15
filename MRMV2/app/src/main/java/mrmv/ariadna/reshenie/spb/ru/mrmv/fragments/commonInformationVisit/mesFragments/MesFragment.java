package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments;


import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.Mes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.MesForDiagnoses;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceSending;

/**
 * Created by kirichenko on 14.05.2015.
 */
public class MesFragment extends Fragment implements ICommonLoadComplete, LoaderManager.LoaderCallbacks<Cursor>{

    static private LoginAccount oLoginAccount;
    static private String sVisitId = "";

    /**
     * Необходимо для подключения к сервису для загрузки
     */
    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionServer;
    private boolean bConnectToServiceDownload;
    private Intent intentCheckingData;

    //* Необходимо для подключения к сервису для выгрузки
    private ServiceConnection sConnectionTransfer;
    private Intent intentTransferData;
    private Boolean bConnectToServiceUpload;
    private ServiceSending oServiceTransfer;

    private FrameLayout frame_for_progress_bar;
    private FrameLayout frame_for_empty_mes;
    private ScrollView scrollForMes;

    private Button bt_add_date_open_mes, bt_add_doctor_open_mes, bt_add_code_mes,
            bt_add_date_close_mes, bt_add_doctor_close_mes, bt_add_type_closes_mes, btnSaveMes;

    private TextView tvNameOfMes;

    private EditText etComments;

    private DataBaseHelper oDataBaseHelper;

    public static int OPENDOCTOR = 0;
    public static int CLOSEDOCTOR = 1;

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        MesFragment.oLoginAccount = oLoginAccount;
    }


    public static void setMenuHolder(AdapterMenuMyCalls.MenuHolder menuHolder) {

        if(menuHolder != null){
            sVisitId = menuHolder.getTextIdCalls().getText().toString();
        }

    }

    public static String getsVisitId() {
        return sVisitId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        startAllServices();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.mes_fragments, null);

        initAllViews(oView);

        if(btnSaveMes != null){
            btnSaveMes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    saveInformationAboutMes();
                }
            });
        }

        getActivity().bindService(intentCheckingData, sConnectionServer, Context.BIND_AUTO_CREATE);
        getActivity().bindService(intentTransferData, sConnectionTransfer, Context.BIND_AUTO_CREATE);

        return oView;
    }

    private void saveInformationAboutMes() {
        if(bConnectToServiceUpload != null){
            if(bConnectToServiceUpload){

                    Mes.saveMesInfo(oDataBaseHelper, getsVisitId(), etComments.getText().toString());
                    oServiceTransfer.startSendingInformationAboutMesbyVisit(oLoginAccount, getsVisitId());
            }
        }
    }

    private void initAllViews(View oView){

        frame_for_progress_bar = (FrameLayout) oView.findViewById(R.id.frameLayoutForMesFragment);
        frame_for_empty_mes = (FrameLayout) oView.findViewById(R.id.frameLayoutForEmptyMes);

        scrollForMes = (ScrollView) oView.findViewById(R.id.scrollViewMesItem);

        etComments = (EditText) oView.findViewById(R.id.edite_text_comments);

        etComments.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    Mes.saveMesInfo(oDataBaseHelper, getsVisitId(), etComments.getText().toString());

                    return false;
                }
                return false;
            }
        });

        tvNameOfMes = (TextView) oView.findViewById(R.id.view_name_of_mes);

        bt_add_date_open_mes = (Button) oView.findViewById(R.id.bt_add_date_open_mes);

        if(bt_add_date_open_mes != null){
            bt_add_date_open_mes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    chooseNewDate(bt_add_date_open_mes, true);


                }
            });
        }

        bt_add_doctor_open_mes = (Button) oView.findViewById(R.id.bt_add_doctor_open_mes);

        if(bt_add_doctor_open_mes != null){
            bt_add_doctor_open_mes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    chooseNewDoctor(bt_add_doctor_open_mes, OPENDOCTOR);
                }
            });
        }

        bt_add_code_mes = (Button) oView.findViewById(R.id.bt_add_code_mes);

        if (bt_add_code_mes != null) {
            bt_add_code_mes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startLoadMes(bt_add_code_mes);
                }
            });
        }

        bt_add_date_close_mes = (Button) oView.findViewById(R.id.bt_add_date_close_mes);

        if(bt_add_date_close_mes != null){
            bt_add_date_close_mes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    chooseNewDate(bt_add_date_close_mes, false);
                }
            });
        }

        bt_add_doctor_close_mes = (Button) oView.findViewById(R.id.bt_add_doctor_close_mes);

        if(bt_add_doctor_close_mes != null){
            bt_add_doctor_close_mes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    chooseNewDoctor(bt_add_doctor_close_mes, CLOSEDOCTOR);
                }
            });
        }

        bt_add_type_closes_mes = (Button) oView.findViewById(R.id.bt_add_type_closes_mes);

        if(bt_add_type_closes_mes != null){
            bt_add_type_closes_mes.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    startChangeTypeClosed(bt_add_type_closes_mes);
                }
            });
        }

        btnSaveMes = (Button) oView.findViewById(R.id.btnSaveMes);
    }


    private void startChangeTypeClosed(Button bt_add_type_closes_mes){
        ChoosingTypeClosed oChoosingTypeClosed = new ChoosingTypeClosed(this, getsVisitId(), oLoginAccount, bt_add_type_closes_mes);
        oChoosingTypeClosed.selectTypeClosed();
    }

    private void startLoadMes(Button bt_add_code_mes){
        ChoosingNewMes oChoosingNewMes = new ChoosingNewMes(this, getsVisitId(), oLoginAccount, bt_add_code_mes, tvNameOfMes);
        oChoosingNewMes.selectNewMesForDiagnoses();
    }

    private void chooseNewDoctor(Button bntForChooseDoctor, int iMode){
            if(bConnectToServiceDownload){

                ChoosingNewDoctor oChoosingNewDoctor = new ChoosingNewDoctor(this, oLoginAccount, bntForChooseDoctor, getsVisitId());
                oChoosingNewDoctor.setTypeMode(iMode);
                oChoosingNewDoctor.selectNewMesForDiagnoses();

            }
    }

    private void chooseNewDoctorFastSelect(Button bntForChooseDoctor, int iMode, boolean bFast){
        if(bConnectToServiceDownload){

            ChoosingNewDoctor oChoosingNewDoctor = new ChoosingNewDoctor(this, oLoginAccount, bntForChooseDoctor, getsVisitId());
            oChoosingNewDoctor.setTypeMode(iMode);
            oChoosingNewDoctor.setFastMode(bFast);
            oChoosingNewDoctor.selectNewMesForDiagnoses();


        }
    }

    private void chooseNewDate(final Button bntForChooseDate, final Boolean bIsOpen){

        bntForChooseDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                String sValueForServer = "";

                                if (dayOfMonth <= 8) {
                                    sValueForServer += "0" + (dayOfMonth);
                                } else {
                                    sValueForServer += dayOfMonth;
                                }

                                if (monthOfYear <= 8) {
                                    sValueForServer += ".0" + (monthOfYear + 1);
                                } else {
                                    sValueForServer += "." + (monthOfYear + 1);
                                }

                                bntForChooseDate.setText( sValueForServer + "." + year);

                                if(bIsOpen){
                                    Mes.saveOpenDate(oDataBaseHelper, getsVisitId(), bntForChooseDate.getText().toString());
                                }else{
                                    Mes.saveCloseDate(oDataBaseHelper, getsVisitId(), bntForChooseDate.getText().toString());
                                }

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });
    }

    private void startAllServices(){

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionServer = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {

                tryConnectToServiceAndLoad(binder);
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToServiceDownload = false;
            }
        };

        intentTransferData = new Intent(getActivity(), ServiceSending.class);

        sConnectionTransfer = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                oServiceTransfer = ((ServiceSending.ServiceBinder) binder).getService();
                bConnectToServiceUpload = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToServiceUpload = false;
            }
        };
    }

    private void startShowProgressBar(Boolean bFoolGone){

        scrollForMes.setVisibility(View.GONE);

        frame_for_progress_bar.setVisibility(View.VISIBLE);
        frame_for_progress_bar.bringToFront();
    }

    private void stopShowProgressBar(){

        scrollForMes.setVisibility(View.VISIBLE);
        frame_for_progress_bar.setVisibility(View.GONE);
    }


    private void tryConnectToServiceAndLoad(IBinder binder) {

        oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
        bConnectToServiceDownload = true;

        startShowProgressBar(true);

        oServiceLoading.getInformationAboutMesByVisitWithEmptyControl(oLoginAccount, getsVisitId(), getMaintDiagnoses(), this);

    }

    private String getMaintDiagnoses(){

        if (oDataBaseHelper != null) {
            return Diagnose.getInformationAboutMainDiagnose(oDataBaseHelper);
        }else{
            return null;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(frame_for_empty_mes.getVisibility() != View.VISIBLE){
            saveInformationAboutMes();
        }

        if (bConnectToServiceDownload) {
            if(getActivity()!= null){
                getActivity().unbindService(sConnectionServer);
                bConnectToServiceDownload = false;
            }
        }

        if(bConnectToServiceUpload){
            if(getActivity()!= null){
                getActivity().unbindService(sConnectionTransfer);
                bConnectToServiceUpload = false;
            }
        }
        getLoaderManager().destroyLoader(0);

    }

    private void initLoadListFromDataBase() {
        Bundle oBundle = new Bundle();
        getLoaderManager().restartLoader(0, oBundle, this);
    }

    @Override
    public void loadCompleted() {
        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLoadListFromDataBase();
                }
            });
        }
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoaderForMes(getActivity(), oDataBaseHelper,getsVisitId());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursorOfData) {
        if (cursorOfData != null) {

            if (cursorOfData.moveToFirst()) {
                do {

                    String sTempString = "";

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.MESOPENDATE));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        bt_add_date_open_mes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.OPENDOCDEPTEXT));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        bt_add_doctor_open_mes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.MESCODE));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        bt_add_code_mes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.MESTEXT));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        tvNameOfMes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.MESCLOSEDATE));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        bt_add_date_close_mes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.CLOSEDOCDEPTEXT));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        bt_add_doctor_close_mes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.CLOSETYPETEXT));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        bt_add_type_closes_mes.setText(sTempString);
                    }

                    sTempString = cursorOfData.getString(cursorOfData.getColumnIndex(Mes.NOTE));

                    sTempString = nullControl(sTempString);

                    if(sTempString != null){
                        etComments.setText(sTempString);
                    }
                } while (cursorOfData.moveToNext());
            }else{
                if (oDataBaseHelper != null) {
                    Cursor oCursor = MesForDiagnoses.getInfoByDiagnoses(oDataBaseHelper,getMaintDiagnoses());
                    if(oCursor.getCount()>0){
                        if(oCursor.getCount() <= 0){
                            Mes.createMesValueMesValue(oDataBaseHelper, getsVisitId());
                        }
                        setupDefaultValue();
                    }else{
                        scrollForMes.setVisibility(View.GONE);
                        frame_for_empty_mes.setVisibility(View.VISIBLE);
                        frame_for_empty_mes.bringToFront();
                    }
                }
            }


            stopShowProgressBar();

        }else{
            stopShowProgressBar();
        }



    }

    private void setupDefaultValue(){

        DateFormat dfForCurrent = new SimpleDateFormat("dd.MM.yyyy");

        String sDefaultDateValue = dfForCurrent.format(new Date());

        bt_add_date_open_mes.setText(sDefaultDateValue);

        Mes.saveOpenDate(oDataBaseHelper, getsVisitId(), bt_add_date_open_mes.getText().toString());

        chooseNewDoctorFastSelect(bt_add_doctor_open_mes, OPENDOCTOR, true);

//        String sName = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.NAME));
//        String sDocdepId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.IDDOCTOR));
//
//        btnSourceButton.setText(sName);

//       Mes.saveOpenDocdepId(oDataBaseHelper,  getsVisitId(), sDocdepId);

    }

    private String nullControl(String sSourceValue){

        if(sSourceValue != null){
            if(sSourceValue.isEmpty()){
                return null;
            }else{
                if(sSourceValue.equals("null") || sSourceValue.equals("Null") || sSourceValue.equals("NULL")){
                    return null;
                }else{
                    return sSourceValue;
                }
            }
        }else{
            return null;
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().destroyLoader(0);
    }


    static class CursorLoaderForMes extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;
        private String sObjectForRequest;

        public CursorLoaderForMes(FragmentActivity context, DataBaseHelper oDataBaseHelper, String sObjectForRequest) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.sObjectForRequest = sObjectForRequest;
        }

        @Override
        public Cursor loadInBackground() {
           return Mes.getInfoByVisit(oDataBaseHelper, sObjectForRequest);
        }
    }

}
