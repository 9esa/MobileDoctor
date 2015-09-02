package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments;

import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.Button;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.IRouteDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.SelectDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction.EnableDoctors;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.Mes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 23.06.2015.
 */
public class ChoosingNewDoctor implements IRouteDialogWithRequest {
    private MesFragment oMesFragment;
    private DoctorAdapter oDoctorAdapter;

    private SelectDialogWithRequest selectDialogWithRequest;

    private DataBaseHelper oDataBaseHelper;

    private LoginAccount oLoginAccount;

    private Button btnSourceButton;

    private String sVisitId;
    private int iTypeMode;

    public ChoosingNewDoctor(MesFragment oMesFragment, LoginAccount oLoginAccount, Button btnSourceButton, String sVisitId) {
        this.oMesFragment = oMesFragment;
        this.oLoginAccount = oLoginAccount;
        this.btnSourceButton = btnSourceButton;
        this.sVisitId = sVisitId;

        oDataBaseHelper = DataBaseHelper.getInstance(oMesFragment.getActivity());

        oDoctorAdapter = new DoctorAdapter(oMesFragment.getActivity(), null);

        selectDialogWithRequest = new SelectDialogWithRequest();
    }

    public void selectNewMesForDiagnoses(){

        if (selectDialogWithRequest != null) {
            selectDialogWithRequest.setiRouteDialogWithRequest(this);
            selectDialogWithRequest.setsValueTitle(oMesFragment.getActivity().getString(R.string.choose_doctor_for_dialog));
            selectDialogWithRequest.setoCursorAdapter(oDoctorAdapter);
            selectDialogWithRequest.setoLoginAccount(oLoginAccount);
            selectDialogWithRequest.setoRequestValue(new Object());
            selectDialogWithRequest.show(oMesFragment.getFragmentManager(), "addNewDoctor");
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

            String sName = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.NAME));
            String sDocdepId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableDoctors.IDDOCTOR));

            btnSourceButton.setText(sName);

            if(iTypeMode == MesFragment.OPENDOCTOR){
                Mes.saveOpenDocdepId(oDataBaseHelper, sVisitId, sDocdepId);
            }else if(iTypeMode ==  MesFragment.CLOSEDOCTOR){
                Mes.saveCloseDocdepId(oDataBaseHelper, sVisitId, sDocdepId);
            }
        }

    }

    public void setTypeMode(int typeMode) {
        this.iTypeMode = typeMode;
    }
}
