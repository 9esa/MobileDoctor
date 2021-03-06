package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.informationAboutPatient.Patients;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment.ChooseDoctorFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.historyIllFragments.HistoryFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 24.08.2015.
 */
public class FindEmkFragment extends Fragment implements ICommonLoadComplete, LoaderManager.LoaderCallbacks<Cursor> {

    private EditText etNumberCard, etPhone, etDate, etFirstName,
            etSecondName, etThirdName, etPoliceSerial, etNumberPolice;

    private Button btnFind;

    private ListView lvListOfUsers;

    private TextView tvNumber, tvFIO, tvDateBorn, tvUserPhone,tvAddress;

    //��������� �����. �.�. ���������� ������������� ������� ������
    static private LoginAccount oLoginAccount;

    /**
     * ���������� ��� ����������� � ������� ��� ��������
     */
    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionChecking;
    private boolean bConnectToServiceDownload = false;
    private Intent intentCheckingData;

    private DataBaseHelper oDataBaseHelper;

    private EmkAdapter oEmkAdapter;

    private int iCurrentMode;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //������������ ��� ������ ��� ���� ������
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        oEmkAdapter  = new EmkAdapter(getActivity(), null);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.find_emk_fragment, container, false);

        iCurrentMode =  getArguments().getInt(HomeActivity.KEY_FOR_GET_MODE);

        startAllServices();

        getAllViewElements(oView);

        return oView;
    }

    private void getAllViewElements(View oView){

        etNumberCard = (EditText) oView.findViewById(R.id.etNumberCard);
        etPhone = (EditText) oView.findViewById(R.id.etPhone);
        etDate = (EditText) oView.findViewById(R.id.etDate);
        etFirstName = (EditText) oView.findViewById(R.id.etFirstName);
        etSecondName = (EditText) oView.findViewById(R.id.etSecondName);
        etThirdName = (EditText) oView.findViewById(R.id.etThirdName);
        etPoliceSerial = (EditText) oView.findViewById(R.id.etPoliceSerial);
        etNumberPolice = (EditText) oView.findViewById(R.id.etNumberPolice);

        tvNumber = (TextView) oView.findViewById(R.id.tvNumberEmk);
        tvFIO = (TextView) oView.findViewById(R.id.tvFIOEmk);
        tvDateBorn = (TextView) oView.findViewById(R.id.tvDateBornEmk);
        tvUserPhone = (TextView) oView.findViewById(R.id.tvUserPhoneEmk);
        tvAddress = (TextView) oView.findViewById(R.id.tvAddressEmk);

        btnFind = (Button) oView.findViewById(R.id.btnFind);

        lvListOfUsers = (ListView) oView.findViewById(R.id.lvListOfUsers);

        lvListOfUsers.setAdapter(oEmkAdapter);

        lvListOfUsers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(view != null){

                    if(iCurrentMode == HomeActivity.FIND_EMK){

                        HistoryFragment oHistoryFragment = new HistoryFragment();

                        EmkAdapter.MenuHolder menuHolder = (EmkAdapter.MenuHolder) view.getTag();
                        oHistoryFragment.setsPatientId(menuHolder.sIdPatient);
                        oHistoryFragment.setoLoginAccount(oLoginAccount);

                        FragmentManager oFragmentManager = getFragmentManager();
                        FragmentTransaction oFragmentTransaction =  oFragmentManager.beginTransaction();
                        oFragmentTransaction.replace(R.id.main_active_layout, oHistoryFragment);
                        oFragmentTransaction.addToBackStack("fragmentStack");
                        oFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        oFragmentTransaction.commit();


                    }else if(iCurrentMode == HomeActivity.BOOKING_VISIT){

                        ChooseDoctorFragment oChooseDoctorFragment = new ChooseDoctorFragment();

                        FragmentManager oFragmentManager = getFragmentManager();
                        FragmentTransaction oFragmentTransaction =  oFragmentManager.beginTransaction();
                        oFragmentTransaction.replace(R.id.main_active_layout, oChooseDoctorFragment);
                        oFragmentTransaction.addToBackStack("fragmentStack");
                        oFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        oFragmentTransaction.commit();

                    }

                }
            }
        });

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makeObjectForRequest();
            }
        });

    }

    private void startAllServices() {

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {

                oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                bConnectToServiceDownload = true;

            }

            public void onServiceDisconnected(ComponentName name) {

                bConnectToServiceDownload = false;

            }
        };

        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);

    }

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        FindEmkFragment.oLoginAccount = oLoginAccount;
    }

    public static LoginAccount getoLoginAccount() {
        return oLoginAccount;
    }

    private void requestObjectFromServer(ItemEmk oItemEmk){

        if(bConnectToServiceDownload){
            oServiceLoading.getListPatient(getoLoginAccount(), oItemEmk, this);
        }else{
            Toast.makeText(getActivity(), R.string.service_found_not_ready, Toast.LENGTH_LONG).show();
        }

    }

    private void makeObjectForRequest(){

        ItemEmk oItemEmk =  new ItemEmk();

        try {

            if(etNumberCard.getText() != null){
                int iPermanentValue = Integer.valueOf(etNumberCard.getText().toString());
                oItemEmk.setiNumberCard(iPermanentValue);
            }

        }catch (Exception ex){}


        try {

            if(etPhone.getText() != null){
                int iPermanentValue = Integer.valueOf(etPhone.getText().toString());
                oItemEmk.setiPhone(iPermanentValue);
            }

        }catch (Exception ex){}


        /**
         *
         */
        if(etFirstName.getText() != null){
            String sValue = etFirstName.getText().toString();

            if(sValue.equals("")){
                oItemEmk.setsName(null);
            }else{
                oItemEmk.setsName(sValue);
            }

        }

        if(etSecondName.getText() != null){
            String sValue = etSecondName.getText().toString();

            if(sValue.equals("")){
                oItemEmk.setsName(null);
            }else {
                oItemEmk.setsName(sValue);
            }

        }

        if(etThirdName.getText() != null){

            String sValue = etThirdName.getText().toString();

            if(sValue.equals("")){
                oItemEmk.setsName(null);
            }else {
                oItemEmk.setsName(sValue);
            }

        }

        try {

            if(etPoliceSerial.getText() != null){
                int iPermanentValue = Integer.valueOf(etPoliceSerial.getText().toString());
                oItemEmk.setiSerialPolice(iPermanentValue);
            }

        }catch (Exception ex){}

        try {

            if(etNumberPolice.getText() != null){
                int iPermanentValue = Integer.valueOf(etNumberPolice.getText().toString());
                oItemEmk.setiNumberPolice(iPermanentValue);
            }

        }catch (Exception ex){}

        requestObjectFromServer(oItemEmk);

    }

    private void initLoadListFromDataBase() {
        getLoaderManager().restartLoader(0, new Bundle(), this);
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
        return  new CursorLoaderForPatients(getActivity(), oDataBaseHelper);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor oCursor) {
        oEmkAdapter.swapCursor(oCursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().destroyLoader(0);
    }

    static class CursorLoaderForPatients extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;

        public CursorLoaderForPatients(FragmentActivity context, DataBaseHelper oDataBaseHelper) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
        }

        @Override
        public Cursor loadInBackground() {
            return Patients.getInformationAboutPatients(oDataBaseHelper);
        }
    }
}
