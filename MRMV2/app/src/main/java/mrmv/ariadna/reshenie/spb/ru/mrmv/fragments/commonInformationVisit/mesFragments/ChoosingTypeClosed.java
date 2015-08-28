package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments;

import android.database.Cursor;
import android.widget.AdapterView;
import android.widget.Button;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.MedicalCommonConstants;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.IRouteDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request.SelectDialogWithRequest;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.MedicalGuides;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.Mes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 28.06.2015.
 */
public class ChoosingTypeClosed implements IRouteDialogWithRequest {

    private MesFragment oMesFragment;
    private MesTypeClosedAdapter oMesTypeClosedAdapter;

    private SelectDialogWithRequest selectDialogWithRequest;

    private DataBaseHelper oDataBaseHelper;

    private LoginAccount oLoginAccount;

    private Button btnSourceButton;

    private String sVisitId;


    public ChoosingTypeClosed(MesFragment oMesFragment,String sVisitId, LoginAccount oLoginAccount, Button btnSourceButton) {

        this.oMesFragment = oMesFragment;
        this.oLoginAccount = oLoginAccount;
        this.btnSourceButton = btnSourceButton;
        this.sVisitId = sVisitId;

        oDataBaseHelper = DataBaseHelper.getInstance(oMesFragment.getActivity());

        oMesTypeClosedAdapter = new MesTypeClosedAdapter(oMesFragment.getActivity(), null);

        selectDialogWithRequest = new SelectDialogWithRequest();
    }

    public void selectTypeClosed(){

        if (selectDialogWithRequest != null) {
            selectDialogWithRequest.setiRouteDialogWithRequest(this);
            selectDialogWithRequest.setsValueTitle(oMesFragment.getActivity().getString(R.string.choose_type_closed));
            selectDialogWithRequest.setoCursorAdapter(oMesTypeClosedAdapter);
            selectDialogWithRequest.setoLoginAccount(oLoginAccount);
            selectDialogWithRequest.setoRequestValue(MedicalCommonConstants.TYPE_OF_CLOSE_MES);
        }

        if(oMesFragment.getFragmentManager() != null){

            if(selectDialogWithRequest != null){
                selectDialogWithRequest.show(oMesFragment.getFragmentManager(), "addTypeClosed");
            }
        }

    }

    public void setsVisitId(String sVisitId) {
        this.sVisitId = sVisitId;
    }

    public String getsVisitId() {
        return sVisitId;
    }

    @Override
    public void getInformationByObject(ServiceLoading oServiceLoading, LoginAccount oLoginAccount, Object oObjectForRequest, ICommonLoadComplete iCommonLoadComplete) {
        if (oServiceLoading != null) {
            if (oObjectForRequest != null) {
               if(selectDialogWithRequest != null){
                   selectDialogWithRequest.loadCompleted();
               }
            }
        }
    }

    @Override
    public Cursor loadInformationFromDataBase(DataBaseHelper oDataBaseHelper, Object oRequest) {
        return MedicalGuides.getLabelMedicalGuideViaCursor(oDataBaseHelper, String.valueOf(oRequest));
    }

    @Override
    public void returnSelectedValue(AdapterView<?> adapterView, int iItemSelected) {
        Cursor oCurrentSelectedElements = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

        if (oCurrentSelectedElements != null) {

            String sName = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuides.TEXT));
            String sCode = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuides.IDGUIDES));


            if(btnSourceButton != null){
                btnSourceButton.setText(sName);
            }

            Mes.saveTypeInfo(oDataBaseHelper, getsVisitId(),sCode);

        }
    }
}
