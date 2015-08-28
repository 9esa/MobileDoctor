package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments;

import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.IRouteDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.SelectDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.Mes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.MesForDiagnoses;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 19.06.2015.
 */
public class ChoosingNewMes implements IRouteDialogWithRequest {

    private MesFragment oMesFragment;
    private MesAdapter oMesAdapter;

    private SelectDialogWithRequest selectDialogWithRequest;

    private DataBaseHelper oDataBaseHelper;

    private LoginAccount oLoginAccount;

    private Button btnSourceButton;

    private TextView tvMesName;

    private String sVisitId;

    public ChoosingNewMes(MesFragment oMesFragment, String sVisitId, LoginAccount oLoginAccount, Button btnSourceButton, TextView tvMesName) {
        this.oMesFragment = oMesFragment;
        this.sVisitId = sVisitId;
        this.oLoginAccount = oLoginAccount;
        this.btnSourceButton = btnSourceButton;
        this.tvMesName = tvMesName;

        oDataBaseHelper = DataBaseHelper.getInstance(oMesFragment.getActivity());

        oMesAdapter = new MesAdapter(oMesFragment.getActivity(), null);

        selectDialogWithRequest = new SelectDialogWithRequest();
    }

    public void selectNewMesForDiagnoses(){

        if (selectDialogWithRequest != null) {
            selectDialogWithRequest.setsValueTitle(oMesFragment.getActivity().getString(R.string.choose_mes_for_diagnoses));
            selectDialogWithRequest.setiRouteDialogWithRequest(this);
            selectDialogWithRequest.setoCursorAdapter(oMesAdapter);
            selectDialogWithRequest.setoLoginAccount(oLoginAccount);
            String sValueDiagnoseId = getMaintDiagnoses();

            if (sValueDiagnoseId != null) {
                selectDialogWithRequest.setoRequestValue(sValueDiagnoseId);
            }else{
                return;
            }
        }

        if(oMesFragment.getFragmentManager() != null){

            if(selectDialogWithRequest != null){
                selectDialogWithRequest.show(oMesFragment.getFragmentManager(), "addNewMess");
            }

        }

    }

    private String getMaintDiagnoses(){

        if (oDataBaseHelper != null) {
            return Diagnose.getInformationAboutMainDiagnose(oDataBaseHelper);
        }else{
            return null;
        }

    }

    @Override
    public void getInformationByObject(ServiceLoading oServiceLoading, LoginAccount oLoginAccount, Object oObjectForRequest, ICommonLoadComplete iCommonLoadComplete) {
        if (oServiceLoading != null) {
            if (oObjectForRequest != null) {
                oServiceLoading.getInformationAboutMesByDiagnoses(oLoginAccount, String.valueOf(oObjectForRequest), iCommonLoadComplete);
            }
        }
    }

    @Override
    public Cursor loadInformationFromDataBase(DataBaseHelper oDataBaseHelper, Object oRequest) {
        return MesForDiagnoses.getInfoByDiagnoses(oDataBaseHelper, String.valueOf(oRequest));
    }

    @Override
    public void returnSelectedValue(AdapterView<?> adapterView, int iItemSelected) {

        Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

        if (oCurrentSelectedElements != null) {


            String sId =  (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MesForDiagnoses.MESID));
            String sCode = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MesForDiagnoses.CODE));
            String sName = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MesForDiagnoses.TEXT));

            tvMesName.setText(sName);
            btnSourceButton.setText(sCode);
            Mes.saveMesInfo(oDataBaseHelper, sVisitId, sId, sCode, sName);

        }
    }

}
