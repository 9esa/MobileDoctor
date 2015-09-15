package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.IRouteDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.SelectDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.doctors.Doctor;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.specials.Special;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk.FindEmkFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 28.08.2015.
 */
public class ChooseDoctorFragment extends Fragment implements IRouteDialogWithRequest {

    private Button btnSaveMe, btnSaveAnother;
    private FrameLayout flChooseDoctorForSave;
    private LinearLayout llContainaAsk;

    static private LoginAccount oLoginAccount;

    /**
     * Инициалы пациента
     */
    private static String sFio;

    /**
     * Идентификатор пациента
     */
    private static String sIdPacient;

    /**
     * Адаптер для выбора врачей
     */
    private ChooseAdapter oChooseAdapter;

    /**
     * Объект для выбора специальности или врача
     */
    private SelectDialogWithRequest selectDialogWithRequest;

    /**
     * Переменная овечающая за начало диалога выбора врача
     */
    private boolean bNewPerson = false;

    /**
     * id Специальности
     */
    private String sIdSpecial;

    /**
     * id Доктора на отделении
     */
    private String sIdDocDepId;

    /**
     * Имя специальности
     */
    private String sNameSpecial;

    /**
     * Имя врача
     */
    private String sNameDoctor;

    public static void setMenuHolder(AdapterMenuMyCalls.MenuHolder menuHolder) {
        sFio = menuHolder.getTextNamePacient().getText().toString();
        sIdPacient = menuHolder.getsPacientId();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View oView = inflater.inflate(R.layout.change_doctor_to_save, null, false);

        if(getArguments() != null){
            sFio =  getArguments().getString(FindEmkFragment.KEY_FOR_GET_FIO);
            sIdPacient =  getArguments().getString(FindEmkFragment.KEY_FOR_GET_USER_ID);
        }

        getAllViewElements(oView);

        addLIstenersForButton();

        return oView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        oChooseAdapter = new ChooseAdapter(getActivity(), null);

        selectDialogWithRequest = new SelectDialogWithRequest();
    }

    public void selectNewProfecional(){

        if (selectDialogWithRequest != null) {

            bNewPerson =  true;

            selectDialogWithRequest.setiRouteDialogWithRequest(this);
            selectDialogWithRequest.setsValueTitle("Выберите специальность");
            selectDialogWithRequest.setoCursorAdapter(oChooseAdapter);
            selectDialogWithRequest.setoLoginAccount(oLoginAccount);
            selectDialogWithRequest.setoRequestValue(new Object());
            selectDialogWithRequest.show(getFragmentManager(), "addNewDoctor");
        }
    }

    public void selectNewDoctor(){

        if (selectDialogWithRequest != null) {
            selectDialogWithRequest.setsValueTitle(getActivity().getString(R.string.choose_doctor_for_dialog));
            selectDialogWithRequest.startLoadDataInDialog();
            selectDialogWithRequest.show(getFragmentManager(), "addNewDoctor");
        }
    }


    private void getAllViewElements(View oView){

        btnSaveMe = (Button) oView.findViewById(R.id.btnCurrentDoctor);
        btnSaveAnother = (Button) oView.findViewById(R.id.btAnotherDoctor);

        flChooseDoctorForSave = (FrameLayout) oView.findViewById(R.id.flChooseDoctorForSave);
        llContainaAsk = (LinearLayout) oView.findViewById(R.id.llContainaAsk);

    }

    public String getsIdSpecial() {
        return sIdSpecial;
    }

    public void setsIdSpecial(String sIdSpecial) {
        this.sIdSpecial = sIdSpecial;
    }

    public String getsIdPacient() {
        return sIdPacient;
    }

    public String getsFio() {
        return sFio;
    }

    public static LoginAccount getoLoginAccount() {
        return oLoginAccount;
    }

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        ChooseDoctorFragment.oLoginAccount = oLoginAccount;
    }

    private void addLIstenersForButton(){

        btnSaveAnother.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectNewProfecional();
            }
        });

        btnSaveMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openChooseDate();

            }
        });

    }

    private void openChooseDate(){

        Bundle bundle = new Bundle();

        bundle.putString(FindEmkFragment.KEY_FOR_GET_FIO, getsFio());
        bundle.putString(FindEmkFragment.KEY_FOR_GET_USER_ID, getsIdPacient());


        AppointmentFragment oAppointmentFragment = new AppointmentFragment();
        oAppointmentFragment.setArguments(bundle);

        oAppointmentFragment.setsDocDepId(sIdDocDepId);
        oAppointmentFragment.setsSpecName(sNameSpecial);
        oAppointmentFragment.setsName(sNameDoctor);

        oAppointmentFragment.setoLoginAccount(getoLoginAccount());

        llContainaAsk.setVisibility(View.GONE);

        FragmentManager oFragmentManager = getFragmentManager();
        FragmentTransaction oFragmentTransaction =  oFragmentManager.beginTransaction();

        /**
         * Должен работать выбор контейнера для вставки, вроде он работает
         */

        //android.R.id.tabcontent
        oFragmentTransaction.replace(R.id.flChooseDoctorForSave, oAppointmentFragment);
        // oFragmentTransaction.replace(((ViewGroup) getView().getParent()).getId(), oAppointmentFragment);

        // oFragmentTransaction.replace(R.id.main_active_layout, oAppointmentFragment);
        oFragmentTransaction.addToBackStack("fragmentStack");
        oFragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        oFragmentTransaction.commit();
    }

    @Override
    public void getInformationByObject(ServiceLoading oServiceLoading, LoginAccount oLoginAccount, Object oObjectForRequest, ICommonLoadComplete iCommonLoadComplete) {
        if (oServiceLoading != null) {
            if (oObjectForRequest != null) {
                if(bNewPerson){
                    oServiceLoading.getListSpecials(oLoginAccount, iCommonLoadComplete);
                }else{
                    oServiceLoading.getListDoctorsViaSpecials(oLoginAccount,getsIdSpecial(),iCommonLoadComplete);
                }

            }
        }
    }

    @Override
    public Cursor loadInformationFromDataBase(DataBaseHelper oDataBaseHelper, Object oRequest) {

        if(bNewPerson){
            return Special.getInfoAboutSpecials(oDataBaseHelper);
        }else{
            return Doctor.getInfoAboutDoctors(oDataBaseHelper);
        }

    }

    @Override
    public void returnSelectedValue(AdapterView<?> adapterView, int iItemSelected) {
        if(bNewPerson){

            Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

            if (oCurrentSelectedElements != null) {

                sNameSpecial = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(Special.TEXT));
                String sId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(Special.IDNUMBER));

                setsIdSpecial(sId);

                bNewPerson = false;

                selectNewDoctor();
            }

        }else{

            Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

            if (oCurrentSelectedElements != null) {

                sNameDoctor = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(Doctor.TEXT));
                sIdDocDepId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(Doctor.IDNUMBER));

                openChooseDate();

            }
        }
    }
}
