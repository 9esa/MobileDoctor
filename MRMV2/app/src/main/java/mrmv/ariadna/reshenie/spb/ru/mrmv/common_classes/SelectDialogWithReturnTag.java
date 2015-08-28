package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuidesMkb10.MedicalGuidesMKB10;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.EnableProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.FullFieldProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards.DiagnosisFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards.ISelectedValue;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards.NewDiagnosesAdapter;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.questionListFragment.QuestionListFragment;

/**
 * Created by kirichenko on 11.05.2015.
 */
public class SelectDialogWithReturnTag extends DialogFragment implements DialogInterface.OnClickListener, LoaderManager.LoaderCallbacks<Cursor> {

    static private HashMap<Integer, Integer> mapStackSelected;

    public static int CURRENT_MODE = 0;
    public static final int SELECT_DIAGNOSIS = 0;
    public static final int SELECT_PROTOCOLS = 1;

    private String LOG_TAG = "SelectDialogWithReturnTag";
    private String ARG_LVL = "arg_lvl";
    private String ARG_ROOT_ID = "arg_root_id";
    private String ARG_TEXT_FOR_SEARCH = "arg_search_text";

    private DataBaseHelper oDataBaseHelper;
    private ListView listViewDiagnoses;
    private NewDiagnosesAdapter adapterMenuDiagnoses;

    private Cursor oCurrentSelectedElements;
    private int iCurrentSelectedDiagnoses;

    private String sSelectedCode, sSelectedText,
                    sVisitId, sFormResultId,sFormId, sDate;

    private EditText etSearch;
    private Button btnSearch;

    private ISelectedValue linkCallBack;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapStackSelected = new HashMap<>();
        mapStackSelected.put(2, 1);

        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        adapterMenuDiagnoses = new NewDiagnosesAdapter(getActivity(), null);
        adapterMenuDiagnoses.setMode(CURRENT_MODE);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        if(CURRENT_MODE == SELECT_DIAGNOSIS){
            getDialog().setTitle("МКБ-10");
        }else if (CURRENT_MODE == SELECT_PROTOCOLS){
            getDialog().setTitle("Доступные протоколы");
        }
        getDialog().setCancelable(false);

        View oView = inflater.inflate(R.layout.list_for_selection, null);

        Bundle bundle = new Bundle();

        if(CURRENT_MODE == SELECT_DIAGNOSIS){
            bundle.putInt(ARG_LVL, 1);
            bundle.putInt(ARG_ROOT_ID, 1);
        }else if (CURRENT_MODE == SELECT_PROTOCOLS){
            bundle.putInt(ARG_LVL, 1);
            bundle.putInt(ARG_ROOT_ID, 0);
        }

        etSearch = (EditText) oView.findViewById(R.id.edit_text_search);

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    startSearch();
                    return true;
                }
                return false;
            }
        });

        listViewDiagnoses = (ListView) oView.findViewById(R.id.list_of_diagnoses);

        btnSearch = (Button) oView.findViewById(R.id.btnSelect);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnSearchOnClick(view);
            }
        });


        Button btnCancel = (Button) oView.findViewById(R.id.btn_cancel_diagnoses);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDialog().dismiss();
            }
        });

        listViewDiagnoses.setAdapter(adapterMenuDiagnoses);

        listViewDiagnoses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int iItemSelected, long l) {
                selectCurrentItemMenu(adapterView, iItemSelected);
            }
        });

        getLoaderManager().restartLoader(0, bundle, this);

        return oView;
    }

    public void btnSearchOnClick(View view) {
        startSearch();
    }

    private void startSearch(){
        if (etSearch.getText().toString().isEmpty()) {

            Bundle bundle = new Bundle();
            bundle.putInt(ARG_LVL, 1);
            bundle.putInt(ARG_ROOT_ID, 1);
            getLoaderManager().restartLoader(0, bundle, this);

        } else {

            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                    Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

            startSearch(etSearch.getText().toString());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);

        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

    }

    private void selectCurrentItemMenu(AdapterView<?> adapterView, int iItemSelected) {

        (oCurrentSelectedElements) = ((Cursor) adapterView.getAdapter().getItem(iItemSelected));

        if (oCurrentSelectedElements != null) {

            if (CURRENT_MODE == SELECT_DIAGNOSIS) {
                int iCurrentLvl = Integer.valueOf((oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuidesMKB10.LEVEL)));
                int iReturnId = Integer.valueOf((oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuidesMKB10.ROOTID)));

                iCurrentSelectedDiagnoses = Integer.valueOf((oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuidesMKB10.IDGUIDES)));
                sSelectedCode = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuidesMKB10.CODE));
                sSelectedText = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(MedicalGuidesMKB10.TEXT));
                startLoadInformationFromDataBase(iCurrentLvl, iReturnId);
            } else if (CURRENT_MODE == SELECT_PROTOCOLS) {
                int iCurrentLvl = Integer.valueOf((oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableProtocols.LEVEL)));
                int iReturnId = Integer.valueOf((oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableProtocols.ROOTID)));

                iCurrentSelectedDiagnoses = Integer.valueOf((oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableProtocols.FORMID)));
                sSelectedCode = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableProtocols.CODE));
                sSelectedText = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(EnableProtocols.TEXT));
                sVisitId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(FullFieldProtocols.VISITID));
              //  sFormResultId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(FullFieldProtocols.FORMRESULTID));
                sFormId = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(FullFieldProtocols.FORMID));
              //  sDate = (oCurrentSelectedElements).getString(oCurrentSelectedElements.getColumnIndex(FullFieldProtocols.DATE));

                 startLoadInformationFromDataBase(iCurrentLvl, iReturnId);
            }
        }
    }

    private void startLoadInformationFromDataBase(int iCurrentLvl, int iReturnId) {
        Bundle bundle = new Bundle();

        if (iCurrentSelectedDiagnoses == -1) {
            iCurrentSelectedDiagnoses = mapStackSelected.get(iCurrentLvl);

            bundle.putInt(ARG_LVL, --iCurrentLvl);

            bundle.putInt(ARG_ROOT_ID, iCurrentSelectedDiagnoses);

        } else {

            if(CURRENT_MODE == SELECT_DIAGNOSIS){
                if (iCurrentLvl > 1) {
                    mapStackSelected.put(iCurrentLvl + 1, iReturnId);
                }
            }else if (CURRENT_MODE == SELECT_PROTOCOLS){
                if (iCurrentLvl > 0) {
                    mapStackSelected.put(iCurrentLvl + 1, iReturnId);
                }
            }

            bundle.putInt(ARG_LVL, ++iCurrentLvl);

            bundle.putInt(ARG_ROOT_ID, iCurrentSelectedDiagnoses);
        }

        getLoaderManager().destroyLoader(0);
        getLoaderManager().restartLoader(0, bundle, this);
    }

    private void startSearch(String sValueForSerach) {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_TEXT_FOR_SEARCH, sValueForSerach);
        getLoaderManager().restartLoader(0, bundle, this);
    }

    private HashMap<String, String> getMapOfDiagnoses(){
        HashMap<String, String> mapOfValuesDiagnoses = new HashMap<>();
        mapOfValuesDiagnoses.put(Diagnose.DIAGNOSID, null);
        mapOfValuesDiagnoses.put(Diagnose.VISITID, "");
        mapOfValuesDiagnoses.put(Diagnose.DIAGNOSTYPE, "2");
        mapOfValuesDiagnoses.put(Diagnose.DIAGNOSID, String.valueOf(iCurrentSelectedDiagnoses));
        mapOfValuesDiagnoses.put(Diagnose.DIAGNOSCODE, sSelectedCode);
        mapOfValuesDiagnoses.put(Diagnose.DIAGNOSTEXT, sSelectedText);
        mapOfValuesDiagnoses.put(Diagnose.ILLTYPEID, "");
        mapOfValuesDiagnoses.put(Diagnose.ILLTYPETEXT, "");
        mapOfValuesDiagnoses.put(Diagnose.WORSEID, "");
        mapOfValuesDiagnoses.put(Diagnose.WORSETEXT, "");
        mapOfValuesDiagnoses.put(Diagnose.CRIMEID, "");
        mapOfValuesDiagnoses.put(Diagnose.CRIMETEXT, "");
        mapOfValuesDiagnoses.put(Diagnose.STAGEID, "");
        mapOfValuesDiagnoses.put(Diagnose.STAGETEXT, "");
        mapOfValuesDiagnoses.put(Diagnose.DISPID, "");
        mapOfValuesDiagnoses.put(Diagnose.DISPTEXT, "");
        mapOfValuesDiagnoses.put(Diagnose.DISPOFFID, "");
        mapOfValuesDiagnoses.put(Diagnose.DISPOFFTEXT, "");
        mapOfValuesDiagnoses.put(Diagnose.STATING, "1");
        return mapOfValuesDiagnoses;
    }

    private HashMap<String, String> getMapOfProtocols(Cursor oCurrentSelectedElements){

        HashMap<String, String> mapOfValuesProtocols = new HashMap<>();

        if(oCurrentSelectedElements != null){
            if(oCurrentSelectedElements.moveToNext()){

                mapOfValuesProtocols.put(FullFieldProtocols.VISITID, sVisitId);
                mapOfValuesProtocols.put(FullFieldProtocols.FORMRESULTID, null);
                mapOfValuesProtocols.put(FullFieldProtocols.FORMID,sFormId);
                mapOfValuesProtocols.put(FullFieldProtocols.TEXT, sSelectedText);
            //    mapOfValuesProtocols.put(FullFieldProtocols.DATE, sDate);
            }
        }
        return mapOfValuesProtocols;
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {

    }

    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle bundleArgs) {

        if (bundleArgs != null) {

            int iSourceLevel = bundleArgs.getInt(ARG_LVL);
            int iRootId = bundleArgs.getInt(ARG_ROOT_ID);
            String sValueForSearch = bundleArgs.getString(ARG_TEXT_FOR_SEARCH);

            if(sValueForSearch == null){
                sValueForSearch = "";
            }

            if (iSourceLevel != 0) {
                return new CursorLoaderForDialogs(getActivity(), oDataBaseHelper, iSourceLevel, iRootId);
            } else {
                return new CursorLoaderForDialogs(getActivity(), oDataBaseHelper, sValueForSearch);
            }

        } else {
            return null;
        }


    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null) {

                if (!((CursorLoaderForDialogs) loader).getsValueForSearch().isEmpty()) {
                    adapterMenuDiagnoses.swapCursor(data);
                    return;
                }

                if (data.getCount() > 1) {

                    adapterMenuDiagnoses.swapCursor(data);
                } else {
                    linkCallBack.addNewTag(createNewTag(data));
                    getDialog().dismiss();
                }

        }
    }

    private Tag createNewTag(Cursor data) {
        if (oCurrentSelectedElements != null) {

            Tag oTag = new Tag(iCurrentSelectedDiagnoses, sSelectedCode);

            if (CURRENT_MODE == SELECT_DIAGNOSIS) {
                oTag.setAttrs(getMapOfDiagnoses());
            } else if (CURRENT_MODE == SELECT_PROTOCOLS) {
                oTag = new Tag(iCurrentSelectedDiagnoses, sSelectedText);
                oTag.setAttrs(getMapOfProtocols(data));
            }

            return oTag;

        }
        return null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

//        if (listViewDiagnoses != null) {
//            Cursor oCurrentCursor = ((CursorAdapter) listViewDiagnoses.getAdapter()).getCursor();
//
//            if (oCurrentCursor != null) {
//                oCurrentCursor.close();
//            }
//
//            if (oCurrentSelectedElements != null) {
//                oCurrentSelectedElements.close();
//            }
//        }
//
//        if(adapterMenuDiagnoses != null){
//            if(adapterMenuDiagnoses.getCursor()!= null){
//                adapterMenuDiagnoses.getCursor().close();
//            }
//        }

        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapterMenuDiagnoses.swapCursor(null);
    }

    public void setLinkCallBack(ISelectedValue linkCallBack) {
        this.linkCallBack = linkCallBack;

        if (linkCallBack != null) {
            if (linkCallBack instanceof DiagnosisFragment) {
                CURRENT_MODE = SELECT_DIAGNOSIS;
            } else if (linkCallBack instanceof QuestionListFragment) {
                CURRENT_MODE = SELECT_PROTOCOLS;
            }
        }
    }

    static class CursorLoaderForDialogs extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;
        private int iSourceLevel, iRootId;
        private String sValueForSearch = "";

        public CursorLoaderForDialogs(Context context, DataBaseHelper oDataBaseHelper, int iSourceLevel, int iRootId) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.iRootId = iRootId;
            this.iSourceLevel = iSourceLevel;
        }

        public CursorLoaderForDialogs(Context context, DataBaseHelper oDataBaseHelper, String sValueForSearch) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.sValueForSearch = sValueForSearch;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = null;

            if (CURRENT_MODE == SELECT_DIAGNOSIS) {
                if (iSourceLevel != 0 && iRootId != 0 && sValueForSearch.isEmpty()) {
                    cursor = MedicalGuidesMKB10.getOnLevel(oDataBaseHelper, iSourceLevel, iRootId);
                } else {
                    cursor = MedicalGuidesMKB10.getSearchValue(oDataBaseHelper, sValueForSearch);
                }
            } else if (CURRENT_MODE == SELECT_PROTOCOLS) {
                if (iSourceLevel != 0 && sValueForSearch.isEmpty()) {
                    cursor = EnableProtocols.getOnLevel(oDataBaseHelper, iSourceLevel, iRootId);
                } else {
                    cursor = EnableProtocols.getSearchValue(oDataBaseHelper, sValueForSearch);
                }
            }

            return cursor;
        }

        public String getsValueForSearch() {
            return sValueForSearch;
        }
    }

}
