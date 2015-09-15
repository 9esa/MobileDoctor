package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.ILoadedComleted;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.MedicalCommonConstants;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.Calls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ItemProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.StructureFullFieldProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction.ItemVisit;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction.Visit;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.ConstructViewProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceSending;

/**
 * Created by kirichenko on 29.04.2015.
 */
public class DoctorCardFragment extends CommonFragment implements ILoadedComleted, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String LOG_TAG = "DoctorCardFragment";
    public static String NEW_CALL = "0";
    private String sStatusCall = NEW_CALL;
    public static String UPDATE_CALL = "1";

    //Статичный логин. Т.к. необходимо гарантировать наличие токена
    static private LoginAccount oLoginAccount;

    /**
     * Выбранная информация для элемента, содержит в себе визит
     */
    static private AdapterMenuMyCalls.MenuHolder menuHolder;

    /**
     * Необходимо для подключения к сервису для загрузки
     */
    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionChecking;
    private boolean bConnectToServiceDownload = false;
    private Intent intentCheckingData;

    //* Необходимо для подключения к сервису для выгрузки
    private ServiceConnection sConnectionTrasfer;
    private Intent intentTransferData;
    private Boolean bConnectToServiceUpload;
    private ServiceSending oServiceTransfer;

    private FragmentActivity myContext;
    private TableLayout tlExtraView;
    private FrameLayout frame_for_progress_bar;
    private TextView tvSexAndAge, tvAddress, tvPhone, tvDateCalls;
    private Spinner spTypeVisit, spPlaceService, spPurposeVisite,
            spCaseResult, spCompleteness, spOutcome, spResultHealthCare;
    private Button btSaveVisit;
    private String sVisitID = "";
    private String sStatus = "0";
    private DataBaseHelper oDataBaseHelper;
    /**
     * Список дополнительных полей
     */
    private List<ItemProtocols> listItemProtocols;
    /**
     * Список дополнительных элементов
     */
    private List<View> listExtraView;
    private DiagnosisFragment oDiagnosisFragment;
    private static String currentDay;

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        DoctorCardFragment.oLoginAccount = oLoginAccount;
    }

    public static void setMenuHolder(AdapterMenuMyCalls.MenuHolder localMenuHolder) {
        menuHolder = localMenuHolder;
    }

    public static void setCurrentDay(String currentDay) {
        DoctorCardFragment.currentDay = currentDay;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        myContext = (FragmentActivity) activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

    }

    private void startAllServices() {

        intentTransferData = new Intent(getActivity(), ServiceSending.class);

        sConnectionTrasfer = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                oServiceTransfer = ((ServiceSending.ServiceBinder) binder).getService();
                bConnectToServiceUpload = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToServiceUpload = false;
            }
        };


        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {

                tryConnectToServiceAndLoad(binder);
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToServiceDownload = false;
            }
        };

    }

    private void tryConnectToServiceAndLoad(IBinder binder) {

        oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
        bConnectToServiceDownload = true;

        if (menuHolder != null) {
            sStatusCall = menuHolder.getsStatus();

            frame_for_progress_bar.setVisibility(View.VISIBLE);
            frame_for_progress_bar.bringToFront();
            oServiceLoading.getInformationAboutVisit(oLoginAccount, sVisitID, this);

//               if(sValue.equals(NEW_CALL)){
//
//                    setEmptyValueSpinners();
//               }

        } else {
            Toast.makeText(getActivity(), myContext.getString(R.string.error_while_download_data), Toast.LENGTH_LONG).show();
        }
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.doctor_cards, null);

        btSaveVisit = (Button) oView.findViewById(R.id.button_save_visit);

        frame_for_progress_bar = (FrameLayout) oView.findViewById(R.id.frame_for_progress_bar);

        loadInrormationFromItemMenu(oView);
        loadAllSpinners(oView);
        loadTextView(oView);
        loadExtraFields(oView);
        //setValueSpinners ();

        btSaveVisit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickSaveVisit();

                FragmentManager fragmentManager = getFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.space_for_doctor_cards, oDiagnosisFragment)
                        .commit();

            }
        });

        startAllServices();

        //После загрузки стат талона, загружаем диагноз
        oDiagnosisFragment = new DiagnosisFragment();
        oDiagnosisFragment.setMenuHolder(menuHolder);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.space_for_doctor_cards, oDiagnosisFragment)
                .commit();


        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);
        getActivity().bindService(intentTransferData, sConnectionTrasfer, getActivity().BIND_AUTO_CREATE);

        return oView;
    }

    private void clickSaveVisit() {
        ArrayList<Tag> listOfDiagnoses = null;

        if (oDiagnosisFragment != null) {
            listOfDiagnoses = oDiagnosisFragment.saveData();
        }

        saveInformationAboutVisit(listOfDiagnoses);

    }

    private void saveInformationAboutVisit(ArrayList<Tag> listOfDiagnoses) {

        if (!sVisitID.isEmpty()) {

            ItemVisit oItemVisit = new ItemVisit();

            oItemVisit.setsVisitId(sVisitID);

            //Необходимо жобавить тип визита
            oItemVisit.setsVisitTypeId(getIdFromSpinner(spTypeVisit.getSelectedItem()));
            oItemVisit.setsVisitPlaceId(getIdFromSpinner(spPlaceService.getSelectedItem()));
            oItemVisit.setsVisTypeId(getIdFromSpinner(spPurposeVisite.getSelectedItem()));
            oItemVisit.setsCaseId(getIdFromSpinner(spCaseResult.getSelectedItem()));
            oItemVisit.setsCaseFinalityId(getIdFromSpinner(spCompleteness.getSelectedItem()));
            oItemVisit.setsCaseOutcomeId(getIdFromSpinner(spOutcome.getSelectedItem()));
            oItemVisit.setsCaseResultId(getIdFromSpinner(spResultHealthCare.getSelectedItem()));

            boolean bValue = Visit.saveInfoByVisit(oDataBaseHelper, oItemVisit);

            /**
             * Сохраняем список диагнозов в бд
             */
            if (listOfDiagnoses != null) {
                Diagnose.saveInfoByVisit(oDataBaseHelper, oItemVisit, listOfDiagnoses);
            }

            if (bConnectToServiceUpload && bValue) {

                saveExtraValueInDataBase();

                if (listOfDiagnoses != null) {
                    oServiceTransfer.startSendingInformationAboutDiagnoses(oLoginAccount, oItemVisit.getsVisitId());
                }

                oServiceTransfer.startSendingInformationAboutExtraFields(oLoginAccount, oItemVisit.getsVisitId());
                oServiceTransfer.startSendingInformationAboutVisit(oLoginAccount, oItemVisit.getsVisitId());
            }

            //getActivity().onBackPressed();
        }


    }

    private void saveExtraValueInDataBase() {
        for (int iCount = 0; iCount < listExtraView.size(); iCount++) {

            ConstructViewProtocols.parceValueFromView(listExtraView.get(iCount), getListItemProtocols().get(iCount), oDataBaseHelper, ConstructViewProtocols.EXTRAFIELD);

        }
    }

    private void setEmptyValueSpinners() {

        spTypeVisit.setSelection(getDefaultValueForSpinner(spTypeVisit));
        spPlaceService.setSelection(getDefaultValueForSpinner(spPlaceService));
        spPurposeVisite.setSelection(getDefaultValueForSpinner(spPurposeVisite));
        spCaseResult.setSelection(getDefaultValueForSpinner(spCaseResult));
        spCompleteness.setSelection(getDefaultValueForSpinner(spCompleteness));
        spOutcome.setSelection(getDefaultValueForSpinner(spOutcome));
        spResultHealthCare.setSelection(getDefaultValueForSpinner(spResultHealthCare));

    }

    private void setFullValueSpinners(Cursor cursotOfData) {
        spTypeVisit.setSelection(setValueForEachSpinner(spTypeVisit, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.VISITTYPEID))));
        spPlaceService.setSelection(setValueForEachSpinner(spPlaceService, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.VISITPLACEID))));
        spPurposeVisite.setSelection(setValueForEachSpinner(spPurposeVisite, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.VISTYPEID))));
        spCaseResult.setSelection(setValueForEachSpinner(spCaseResult, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.CASEID))));
        spCompleteness.setSelection(setValueForEachSpinner(spCompleteness, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.CASEFINALITYID))));
        spOutcome.setSelection(setValueForEachSpinner(spOutcome, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.CASEOUTCOMEID))));
        spResultHealthCare.setSelection(setValueForEachSpinner(spResultHealthCare, cursotOfData.getString(cursotOfData.getColumnIndex(Visit.CASERESULTID))));
    }

    private void loadInrormationFromItemMenu(View oView) {

        TextView tvPacient = (TextView) oView.findViewById(R.id.user_name_header_of_stattalon);
        TextView tvAddress = (TextView) oView.findViewById(R.id.address_header_of_stattalon);
        TextView tvSexAndAge = (TextView) oView.findViewById(R.id.sex_and_age_header_of_stattalon);
        TextView tvPhoneNumber = (TextView) oView.findViewById(R.id.phone_number_header_of_stattalon);
        tvDateCalls = (TextView) oView.findViewById(R.id.week_day);

        if(currentDay != null){
            tvDateCalls.setText(currentDay);
        }

        String sValue;

        if (menuHolder != null) {
            tvPacient.setText(menuHolder.getTextNamePacient().getText());
            tvAddress.setText(menuHolder.getTextAddress().getText());
            tvDateCalls.setText("Здесь должна быть дата");
            tvPhoneNumber.setText("-");
            sVisitID = menuHolder.getTextIdCalls().getText().toString();

            if (bConnectToServiceDownload) {
                Cursor cursor = loadMainInfoByVisit(sVisitID);
                if (cursor != null) {
                    if (cursor.moveToFirst()) {
                        do {
                            sStatus = cursor.getString(cursor.getColumnIndex(Calls.STATUS));

                            sValue = cursor.getString(cursor.getColumnIndex(Calls.SEX));

                            sValue = sValue.toLowerCase();

                            if (sValue.equals("0")) {
                                sValue = "Мужчина";
                            } else if (sValue.equals("1")) {
                                sValue = "Женщина";
                            } else {
                                sValue = "";
                            }

                            sValue += " " + cursor.getString(cursor.getColumnIndex(Calls.BIRTHDATE));

                            sValue += " (" + cursor.getString(cursor.getColumnIndex(Calls.AGE)) + ")";

                        } while (cursor.moveToNext());

                        tvSexAndAge.setText(sValue);
                    }
                }
            }
        }
    }

    private Cursor loadMainInfoByVisit(String sVisitID) {
        return Calls.getInfoByVisit(oDataBaseHelper, sVisitID);
    }

    private void loadTextView(View oView) {

        tvSexAndAge = (TextView) oView.findViewById(R.id.sex_and_age_header_of_stattalon);
        tvAddress = (TextView) oView.findViewById(R.id.address_header_of_stattalon);
        tvPhone = (TextView) oView.findViewById(R.id.phone_number_header_of_stattalon);

    }

    private void loadAllSpinners(View oView) {

        spTypeVisit = (Spinner) oView.findViewById(R.id.spinner_type_visit_outpatient_talon);
        loadSpinnersData(spTypeVisit, MedicalCommonConstants.TYPE_VISIT, oDataBaseHelper);

        spPlaceService = (Spinner) oView.findViewById(R.id.spinner_service_place_outpatient_talon);
        loadSpinnersData(spPlaceService, MedicalCommonConstants.SERVICE_PLACE, oDataBaseHelper);

        spPurposeVisite = (Spinner) oView.findViewById(R.id.spinner_purpose_visit_outpatient_talon);
        loadSpinnersData(spPurposeVisite, MedicalCommonConstants.GOAL_VISIT, oDataBaseHelper);

        spCaseResult = (Spinner) oView.findViewById(R.id.spinner_case_result_of_request);
        loadSpinnersData(spCaseResult, MedicalCommonConstants.CASE, oDataBaseHelper);

        spCompleteness = (Spinner) oView.findViewById(R.id.spinner_completeness_result_of_request);
        loadSpinnersData(spCompleteness, MedicalCommonConstants.COMPLETENESS, oDataBaseHelper);

        spOutcome = (Spinner) oView.findViewById(R.id.spinner_outcome_result_of_request);
        loadSpinnersData(spOutcome, MedicalCommonConstants.OUTCOME, oDataBaseHelper);

        spResultHealthCare = (Spinner) oView.findViewById(R.id.spinner_result_healthcare_result_of_request);
        loadSpinnersData(spResultHealthCare, MedicalCommonConstants.RESULT_CARE, oDataBaseHelper);

    }

    private void loadExtraFields(View oView) {

        tlExtraView = (TableLayout) oView.findViewById(R.id.table_ambulance_talon);

   //     listExtraView = new ArrayList<>();

//        if(menuHolder.getsStatus().equals(NEW_CALL)){
//            for(ItemProtocols oItemProtocols : getListItemProtocols()){
//                View newCustomView = ConstructViewProtocols.constructNewRow(tlExtraView, getActivity(),oItemProtocols, oDataBaseHelper);
//                listExtraView.add(newCustomView);
//            }
//        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
    }

    public void onDestroyView() {
        super.onDestroyView();

        clickSaveVisit();

        if (!bConnectToServiceUpload) {
            return;
        } else {
            getActivity().unbindService(sConnectionTrasfer);
            bConnectToServiceUpload = false;
        }

        /**
         * Если фрагмент остановлен отключаемся от сервиса
         */
        if (!bConnectToServiceDownload) {
            return;
        } else {
            getActivity().unbindService(sConnectionChecking);
            bConnectToServiceDownload = false;
        }

        getLoaderManager().destroyLoader(0);

    }

    public String getsVisitID() {
        return sVisitID;
    }

    public void setsVisitID(String sVisitID) {
        this.sVisitID = sVisitID;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoaderForVisits(getActivity(), oDataBaseHelper, sVisitID);
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> cursorLoaderData, Cursor cursotOfData) {

        if (cursotOfData.moveToFirst()) {
            do {

                String sTempValue = cursotOfData.getString(cursotOfData.getColumnIndex(Visit.SEX));
                String sValue = "";

                if(sTempValue!= null){
                    if (sTempValue.equals("1")) {
                        sValue += "Женщина ";
                    } else {
                        sValue += "Мужчина ";
                    }
                }
                sTempValue = cursotOfData.getString(cursotOfData.getColumnIndex(Visit.BIRTHDATE));

                if(sTempValue != null){
                    if (sTempValue.equals("null")) {
                        sValue += "- ";
                    } else {
                        sValue += sTempValue + " ";
                    }
                }

                sTempValue = cursotOfData.getString(cursotOfData.getColumnIndex(Visit.AGE));

                if(sTempValue != null) {

                    sValue += "(";

                    if (sTempValue.equals("null")) {
                        sValue += "-";
                    } else {
                        sValue += sTempValue;
                    }

                    sValue += ")";

                }

                tvSexAndAge.setText(sValue);

                sTempValue = cursotOfData.getString(cursotOfData.getColumnIndex(Visit.LOCADDRESS));

                if(sTempValue != null) {

                    if (sTempValue.equals("null")) {
                        sTempValue = cursotOfData.getString(cursotOfData.getColumnIndex(Visit.REGADDRESS));

                        if (sTempValue.equals("null")) {
                            tvAddress.setText("-");
                        } else {
                            tvAddress.setText(sTempValue);
                        }

                    } else {
                        tvAddress.setText(sTempValue);
                    }

                }

                sTempValue = cursotOfData.getString(cursotOfData.getColumnIndex(Visit.PHONE));

                if(sTempValue != null) {

                    if (sTempValue.equals("null")) {
                        tvPhone.setText("-");
                    } else {
                        tvPhone.setText(sTempValue);
                    }

                }

                if (sStatusCall.equals(NEW_CALL)) {
                    setEmptyValueSpinners();
                } else if (sStatusCall.equals(UPDATE_CALL)) {
                    setFullValueSpinners(cursotOfData);
                }

            } while (cursotOfData.moveToNext());
        }

        setValueForExtraView();

        if(currentDay != null){
            tvDateCalls.setText(currentDay);
        }

        frame_for_progress_bar.setVisibility(View.GONE);

    }

    private void setValueForExtraView() {

        listExtraView = new ArrayList<>();

        setListItemProtocols(StructureFullFieldProtocols.getListEnableProtocols(oDataBaseHelper, sVisitID));

        for (ItemProtocols oItemProtocols : getListItemProtocols()) {
            View newCustomView = ConstructViewProtocols.constructNewRow(tlExtraView, getActivity(), oItemProtocols, oDataBaseHelper, false);
            listExtraView.add(newCustomView);
        }

        ConstructViewProtocols.setSpinnerListeners();

    }

    public List<ItemProtocols> getListItemProtocols() {
        return listItemProtocols;
    }

    public void setListItemProtocols(List<ItemProtocols> listItemProtocols) {
        this.listItemProtocols = listItemProtocols;
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        Log.d("Test", "test");
    }

    private void initLoadFromDataBase() {
        getLoaderManager().restartLoader(0, new Bundle(), this);
    }

    @Override
    public void loadedVisitComleted() {

        if(getActivity() != null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLoadFromDataBase();
                    oServiceLoading.getInformationAboutDiagnoses(oLoginAccount, sVisitID, oDiagnosisFragment);
                }
            });
        }

    }

    @Override
    public void loadedDiagnosesComleted() {
        //Ничего не делаем в фрагменте стат талон
    }

    static class CursorLoaderForVisits extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;
        private String sVisitId;


        public CursorLoaderForVisits(Context context, DataBaseHelper oDataBaseHelper, String sVisitId) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.sVisitId = sVisitId;
        }

        @Override
        public Cursor loadInBackground() {
            return Visit.getInformationAboutVisit(oDataBaseHelper, sVisitId);
        }
    }

}
