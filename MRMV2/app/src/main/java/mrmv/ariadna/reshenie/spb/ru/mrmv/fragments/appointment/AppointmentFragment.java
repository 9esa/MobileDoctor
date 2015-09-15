package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.IRouteDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.SelectDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction.EnableDoctors;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers.Numbers;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments.DoctorAdapter;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk.FindEmkFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceSending;

/**
 * Created by kirichenko on 25.08.2015.
 */
public class AppointmentFragment extends Fragment implements ICommonLoadComplete, LoaderManager.LoaderCallbacks<Cursor>, IRouteDialogWithRequest, IDialogConfirmWorkWithNumb {

    private CalendarView getAllViewNumbers;

    private TextView tvNameOfDoctor;
    private TextView tvSpecName;
    private TextView tvPacientName;

    private ListView lvListOfNumbers;

    private FrameLayout flProgressLoadNumbs, flNotFoundSchedule;

    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionChecking;
    private boolean bConnectToServiceDownload = false;
    private Intent intentCheckingData;

    private ServiceConnection sConnectionTransfer;
    private Intent intentTransferData;
    private Boolean bConnectToServiceUpload;
    private ServiceSending oServiceTransfer;


    private DataBaseHelper oDataBaseHelper;

    private DoctorAdapter oDoctorAdapter;

    private AppointmentAdapter oAppointmentAdapter;

    private SelectDialogWithRequest selectDialogWithRequest;

    static private LoginAccount oLoginAccount;

    private String sName, sSpecName, sRequestDate, sFio, sIdPacient, sDocDepId, sIdNumber = "", sStatusNumber = "";

    public String getsDocDepId() {
        return sDocDepId;
    }

    public void setsDocDepId(String sDocDepId) {
        this.sDocDepId = sDocDepId;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oView = inflater.inflate(R.layout.choose_free_number, null, false);

        if(getArguments() != null){
            sFio =  getArguments().getString(FindEmkFragment.KEY_FOR_GET_FIO);
            sIdPacient =  getArguments().getString(FindEmkFragment.KEY_FOR_GET_USER_ID);

        }

        getAllViewElements(oView);

        startAllServices();

        initializeCalendar();

        if(getsFio() != null){
            tvPacientName.setText(getsFio());
        }

        setInfoForDoctor();

        return oView;
    }

    private void setInfoForDoctor(){

        if(getsDocDepId() != null) {
            if(getsName() != null){
                tvNameOfDoctor.setText(getsName());
            }

            if(getsSpecName() != null){
                tvSpecName.setText(getsSpecName());
            }
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        oAppointmentAdapter  = new AppointmentAdapter(getActivity(), null);

        oDoctorAdapter = new DoctorAdapter(getActivity(), null);

        selectDialogWithRequest = new SelectDialogWithRequest();

    }

    public void selectNewDoctorForNumbers(){

        if (selectDialogWithRequest != null) {
            selectDialogWithRequest.setiRouteDialogWithRequest(this);
            selectDialogWithRequest.setsValueTitle(getActivity().getString(R.string.choose_doctor_for_dialog));
            selectDialogWithRequest.setoCursorAdapter(oDoctorAdapter);
            selectDialogWithRequest.setoLoginAccount(oLoginAccount);
            selectDialogWithRequest.setoRequestValue(new Object());
            selectDialogWithRequest.show(getFragmentManager(), "addNewDoctor");
        }
    }

    private void startAllServices() {

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {

                oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                bConnectToServiceDownload = true;

                if( getsDocDepId() == null){
                    selectNewDoctorForNumbers();
                }

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

        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);
        getActivity().bindService(intentTransferData, sConnectionTransfer, getActivity().BIND_AUTO_CREATE);


    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsSpecName() {
        return sSpecName;
    }

    public void setsSpecName(String sSpecName) {
        this.sSpecName = sSpecName;
    }

    public String getsIdPacient() {
        return sIdPacient;
    }

    private void getAllViewElements(View oView){
        getAllViewNumbers = (CalendarView) oView.findViewById(R.id.cwChooseDate);

        lvListOfNumbers = (ListView) oView.findViewById(R.id.lvNumbers);

        tvNameOfDoctor = (TextView) oView.findViewById(R.id.tvNameOfDoctor);

        tvSpecName = (TextView) oView.findViewById(R.id.tvSpecName);

        tvPacientName = (TextView) oView.findViewById(R.id.tvPacientName);

        flProgressLoadNumbs = (FrameLayout)oView.findViewById(R.id.flProgressLoadNumbs);

        flNotFoundSchedule = (FrameLayout)oView.findViewById(R.id.flNotFoundSchedule);

        lvListOfNumbers.setAdapter(oAppointmentAdapter);

        lvListOfNumbers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int iItemSelected, long l) {

                Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

                if (oCurrentSelectedElements != null) {

                    sIdNumber = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(Numbers.IDNUMBER));
                    sStatusNumber = oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(Numbers.STATUS));

                    actionClickOnItem(oCurrentSelectedElements.getString(oCurrentSelectedElements.getColumnIndex(Numbers.TIME)));

                }
            }
        });

    }

    private void actionClickOnItem(String sTimeNumber){

        Bundle bundle = new Bundle();

        DialogConfirmWorkWithNumb dialogConfirmAction =  new DialogConfirmWorkWithNumb();

        if(sStatusNumber.equals(AppointmentAdapter.ACTIVE)){
            bundle.putString(DialogConfirmWorkWithNumb.KEY_FOR_TAKE_TITLE, oServiceLoading.getString(R.string.book_pacient));
            bundle.putString(DialogConfirmWorkWithNumb.KEY_FOR_TAKE_MESSAGE, oServiceLoading.getString(R.string.confirm_book_on) + sTimeNumber + " ?");
            dialogConfirmAction.setArguments(bundle);
            dialogConfirmAction.setInterfaceForReturn(this);
            dialogConfirmAction.show(getFragmentManager(), "dialogConfirmAction");
        }else{
            bundle.putString(DialogConfirmWorkWithNumb.KEY_FOR_TAKE_TITLE, oServiceLoading.getString(R.string.free_numb_pacient) );
            bundle.putString(DialogConfirmWorkWithNumb.KEY_FOR_TAKE_MESSAGE, oServiceLoading.getString(R.string.free_booking_numb) + sTimeNumber + " ?");
            dialogConfirmAction.setArguments(bundle);
            dialogConfirmAction.setInterfaceForReturn(this);
            dialogConfirmAction.show(getFragmentManager(), "dialogConfirmAction");
        }
    }

    private void startRequestServer(String sIdNumber , String sStatusNumber){
        oServiceTransfer.startSendingInformationaAboutBookingNumb(getoLoginAccount(),this,  sIdNumber, getsIdPacient(), sStatusNumber, sRequestDate, getsDocDepId());
    }

    private void initializeCalendar(){

        getAllViewNumbers.setShowWeekNumber(false);

        getAllViewNumbers.setFirstDayOfWeek(2);

        getAllViewNumbers.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int day) {

                sRequestDate = "";

                if (day <= 8) {
                    sRequestDate += "0" + (day);
                } else {
                    sRequestDate += "" + day;
                }

                if (month <= 8) {
                    sRequestDate += ".0" + (month + 1);
                } else {
                    sRequestDate += "." + (month + 1);
                }

                sRequestDate += "." + year;

                requestObjectFromServer(sRequestDate);

            }

        });
    }

    private void requestObjectFromServer(String sDate){

        if(bConnectToServiceDownload){

            if(getsDocDepId()== null){
                selectNewDoctorForNumbers();
                Toast.makeText(getActivity(), R.string.clarify__specialty, Toast.LENGTH_LONG).show();
            }else{
                lvListOfNumbers.setVisibility(View.GONE);
                flNotFoundSchedule.setVisibility(View.GONE);
                flProgressLoadNumbs.setVisibility(View.VISIBLE);
                flProgressLoadNumbs.bringToFront();

                oServiceLoading.getListNumbers(getoLoginAccount(), sDate, getsDocDepId(), this, sIdPacient);
            }
        }else{
            Toast.makeText(getActivity(), R.string.service_found_not_ready, Toast.LENGTH_LONG).show();
        }

    }

    public String getsFio() {
        return sFio;
    }

    public static LoginAccount getoLoginAccount() {
        return oLoginAccount;
    }

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        AppointmentFragment.oLoginAccount = oLoginAccount;
    }

    private void initLoadListFromDataBase() {
        getLoaderManager().restartLoader(0, new Bundle(), this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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
        return new CursorLoaderForNumbers(getActivity(), oDataBaseHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, final Cursor oCursor) {

        oAppointmentAdapter.setsIdPacient(getsIdPacient());
        oAppointmentAdapter.swapCursor(oCursor);

        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    flProgressLoadNumbs.setVisibility(View.GONE);

                    if(oCursor.moveToNext()){

                        lvListOfNumbers.setVisibility(View.VISIBLE);
                    }else{

                        flNotFoundSchedule.setVisibility(View.VISIBLE);

                        lvListOfNumbers.setVisibility(View.GONE);

                        flProgressLoadNumbs.bringToFront();
                    }
                }
            });
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void clickYes() {
        if(bConnectToServiceUpload){

            startRequestServer(sIdNumber, sStatusNumber);
            lvListOfNumbers.setVisibility(View.GONE);
            flNotFoundSchedule.setVisibility(View.GONE);
            flProgressLoadNumbs.setVisibility(View.VISIBLE);
            flProgressLoadNumbs.bringToFront();

        }else{
            Toast.makeText(getActivity(), R.string.service_booking_not_ready, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void clickNo() {

    }

    static class CursorLoaderForNumbers extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;

        public CursorLoaderForNumbers(FragmentActivity context, DataBaseHelper oDataBaseHelper) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
        }

        @Override
        public Cursor loadInBackground() {
            return Numbers.getInfoAboutNumb(oDataBaseHelper);
        }
    }

    @Override
    public void getInformationByObject(ServiceLoading oServiceLoading, LoginAccount oLoginAccount, Object oObjectForRequest, ICommonLoadComplete iCommonLoadComplete) {
        if (oServiceLoading != null) {
            if (oObjectForRequest != null) {
                oServiceLoading.getInformationAboutEnableDoctors(oLoginAccount, iCommonLoadComplete);
            }
        }
    }

    @Override
    public Cursor loadInformationFromDataBase(DataBaseHelper oDataBaseHelper, Object oRequest) {
        return EnableDoctors.getInfoAboutEnableDoctors(oDataBaseHelper);
    }

    @Override
    public void returnSelectedValue(AdapterView<?> adapterView, int iItemSelected) {
        Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

        if (oCurrentSelectedElements != null) {

           final String sName = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.NAME));
           final String sDocdepId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.IDDOCTOR));
           final String sSpecName = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.SPECNAME));

            if(getActivity() != null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        tvNameOfDoctor.setText(sName);
                        tvSpecName.setText(sSpecName);

                        setsDocDepId(sDocdepId);

                        if(getAllViewNumbers!= null){
                            Date oDate=new Date(getAllViewNumbers.getDate());
                            SimpleDateFormat oSimpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
                            sRequestDate = oSimpleDateFormat.format(oDate);
                            requestObjectFromServer(sRequestDate);
                        }
                    }
                });
            }
        }
    }

}
