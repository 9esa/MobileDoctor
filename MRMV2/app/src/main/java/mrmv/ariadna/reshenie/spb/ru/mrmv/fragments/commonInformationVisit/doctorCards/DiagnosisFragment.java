package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.ILoadedComleted;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.MedicalCommonConstants;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.SelectDialogWithReturnTag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.widget.TagCloudLinkView;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.ItemGuides;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;

/**
 * Created by kirichenko on 30.04.2015.
 */
public class DiagnosisFragment extends CommonFragment implements ILoadedComleted, LoaderManager.LoaderCallbacks<Cursor>, ISelectedValue {

    private ISelectedValue ISelectedValue;

    private FrameLayout frame_for_progress_bar;

    private TagCloudLinkView tclvDiagnoses;

    private TextView tvDescribe, tvSummary;

    private Spinner spNatureOfDisease, spIllegalActions, spAccident, spDispenser, spReasonRemove;

    private RadioGroup rgTypeDiagnoses;

    private Button btAddDiagnoses;

    private DataBaseHelper oDataBaseHelper;

    private AdapterMenuMyCalls.MenuHolder menuHolder;

    /**
     * Константа о главном диагнозе
     */
    private String MAIN_DIAGNOSES = "1";

    private String sVisitID;
    private String sStatus;

    /**
     * Текущий выбранный тег
     */
    private Tag oCurrentTag;

    /**
     * Список типов диагнозов
     */
    private HashMap<String, RadioButton> mapOfTypes;

    private boolean bIsHasMainDiagnoses =  false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ISelectedValue = this;

        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getVisitIDFromMenuHolder();

        View oView = inflater.inflate(R.layout.diagnosis_fragment, null);

        tclvDiagnoses = (TagCloudLinkView) oView.findViewById(R.id.tags_of_diagnosis);
        tvDescribe = (TextView) oView.findViewById(R.id.describe_diagnoses);
        tvSummary = (TextView) oView.findViewById(R.id.summary_diagnoses);
        rgTypeDiagnoses = (RadioGroup) oView.findViewById(R.id.radio_group_diagnoses);
        btAddDiagnoses = (Button) oView.findViewById(R.id.button_add_diagnoses);
        frame_for_progress_bar = (FrameLayout) oView.findViewById(R.id.frame_layout_for_progress_bar);

        setupListeners(oView);

        if(sStatus != null){
            if(sStatus.equals(DoctorCardFragment.NEW_CALL)){

            }else{
                frame_for_progress_bar.setVisibility(View.VISIBLE);
                frame_for_progress_bar.bringToFront();
            }
        }
        return oView;
    }

    private void setupListeners(View oView){

        btAddDiagnoses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SelectDialogWithReturnTag dialogFragment = new SelectDialogWithReturnTag();
                dialogFragment.setLinkCallBack(ISelectedValue);
                dialogFragment.show(getFragmentManager(), "addNewDiagnoses");
            }
        });

        setupSpinnerData(oView);

        actionWithDiagnosesTags(tclvDiagnoses);
    }


    private void setupSpinnerData(View oView){

        spNatureOfDisease = (Spinner) oView.findViewById(R.id.spinner_nature_of_disease);
        loadSpinnersData(spNatureOfDisease, MedicalCommonConstants.CHARACTER_OF_DISEASE, oDataBaseHelper);

        spNatureOfDisease.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (getoCurrentTag() != null) {
                    HashMap<String, String> mapOfAttr = (HashMap<String, String>) getoCurrentTag().getAttrs();
                    mapOfAttr.put(Diagnose.ILLTYPEID, getIdFromSpinner(spNatureOfDisease.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });

        spIllegalActions = (Spinner) oView.findViewById(R.id.spinner_illegal_actions);
        loadSpinnersData(spIllegalActions, MedicalCommonConstants.WRONGFUL_ACTS, oDataBaseHelper);

        spIllegalActions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (getoCurrentTag() != null) {
                    HashMap<String, String> mapOfAttr = (HashMap<String, String>) getoCurrentTag().getAttrs();
                    mapOfAttr.put(Diagnose.CRIMEID, getIdFromSpinner(spIllegalActions.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spAccident = (Spinner) oView.findViewById(R.id.spinner_accident);
        loadSpinnersData(spAccident, MedicalCommonConstants.TRAUMA,  oDataBaseHelper);

        spAccident.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (getoCurrentTag() != null) {
                    HashMap <String, String> mapOfAttr = (HashMap<String, String>) getoCurrentTag().getAttrs();
                    mapOfAttr.put(Diagnose.TRAVMAID, getIdFromSpinner(spAccident.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spDispenser = (Spinner) oView.findViewById(R.id.spinner_dispenser_account);
        loadSpinnersData(spDispenser, MedicalCommonConstants.CLINICAL_ACCOUNT,  oDataBaseHelper);

        spDispenser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (getoCurrentTag() != null) {
                    HashMap<String, String> mapOfAttr = (HashMap<String, String>) getoCurrentTag().getAttrs();
                    mapOfAttr.put(Diagnose.DISPID, getIdFromSpinner(spDispenser.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        spReasonRemove = (Spinner) oView.findViewById(R.id.spinner_reason_remove_dispenser_account);
        loadSpinnersData(spReasonRemove, MedicalCommonConstants.REASON_OF_WITHDRAWAL,  oDataBaseHelper);

        spReasonRemove.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                if (getoCurrentTag() != null) {
                    HashMap<String, String> mapOfAttr = (HashMap<String, String>) getoCurrentTag().getAttrs();
                    mapOfAttr.put(Diagnose.DISPOFFID, getIdFromSpinner(spReasonRemove.getSelectedItem()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });


        rgTypeDiagnoses.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {

                if (getoCurrentTag() != null) {
                    HashMap<String, String> mapOfAttr = (HashMap<String, String>) getoCurrentTag().getAttrs();

                    if (getMapOfTypes() != null){
                        for(String sItemType : getMapOfTypes().keySet()){

                            String sValueIdElement =  String.valueOf(getMapOfTypes().get(sItemType).getId());
                            String sValueRadioButton =  String.valueOf(radioGroup.getCheckedRadioButtonId());

                            if(sValueIdElement.equals(sValueRadioButton)){
                                mapOfAttr.put(Diagnose.DIAGNOSTYPE, sItemType);
                                if(sItemType.equals(MAIN_DIAGNOSES)){
                                    setbIsHasMainDiagnoses(true);
                                }
                            }

                        }
                    }
                }
            }
        });

        addRadioButtonsTypeDiagnoses(loadRadioButtonData(MedicalCommonConstants.DIAGNOSE_TYPE,  oDataBaseHelper));
    }

    private void addRadioButtonsTypeDiagnoses( List<ItemGuides> listOfTypeDiagnoses) {

        setMapOfTypes(new HashMap<String, RadioButton>());

            for (int iCount = 0; iCount <= listOfTypeDiagnoses.size() - 1 ; iCount++) {

                RadioButton rbTypeDiagnoses = new RadioButton(getActivity());

                rbTypeDiagnoses.setId(iCount);
                rbTypeDiagnoses.setText(listOfTypeDiagnoses.get(iCount).getsText());

                rgTypeDiagnoses.addView(rbTypeDiagnoses);

                getMapOfTypes().put(listOfTypeDiagnoses.get(iCount).getsCode(),rbTypeDiagnoses);

            }

    }

    private void actionWithDiagnosesTags(final TagCloudLinkView tclvDiagnoses){

        tclvDiagnoses.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener(){

            @Override
            public void onTagSelected(Tag oTag, int position) {

                setValueForSelectedDiagnoses(oTag);

            }

        });

        tclvDiagnoses.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {
            @Override
            public void onTagDeleted(Tag oTag, int iD) {

                HashMap <String, String> mapOfAttrsDeleted  = (HashMap<String, String>) oTag.getAttrs();

                ArrayList <Tag> listOfTags =  getTagsList();

                /**
                 * Если диагноз загруженный мы его обновляем в базе
                 */
                 if(mapOfAttrsDeleted.get(Diagnose.STATING).equals(Diagnose.BASE_DIAGNOS)){
                      Diagnose.updateStateByVisit(oDataBaseHelper, sVisitID, oTag);
                 }

                if(listOfTags.size() > 0){
                    tclvDiagnoses.setiSelectedTagIndex(0);
                    setValueForSelectedDiagnoses(listOfTags.get(0));
                }else{
                    setNoDiagnoses();
                }

                findMainDiagnoses();

            }
        });
    }

    private void setNoDiagnoses(){

        tvDescribe.setText("-");
        tvSummary.setText("-");
        spNatureOfDisease.setSelection(0);
        spIllegalActions.setSelection(0);
        spDispenser.setSelection(0);
        spReasonRemove.setSelection(0);
        spAccident.setSelection(0);
        rgTypeDiagnoses.clearCheck();

        if (getMapOfTypes() != null){
            for(String sItemType : getMapOfTypes().keySet()){
                getMapOfTypes().get(sItemType).setEnabled(false);
            }
        }
    }

    private void findMainDiagnoses(){

        for (int iCount = 0; iCount < getTagsList().size(); iCount++){

            HashMap <String, String> mapOfAttr = (HashMap<String, String>) getTagsList().get(iCount).getAttrs();
            String sTypeOfDiagnoses = mapOfAttr.get(Diagnose.DIAGNOSTYPE);

            if(sTypeOfDiagnoses.equals(MAIN_DIAGNOSES)){
                setbIsHasMainDiagnoses(true);
                return;
            }
        }
        setbIsHasMainDiagnoses(false);
    }

    private void setValueForSelectedDiagnoses(Tag oTag){

        /**
         * Коллекция со списком параметров для диагноза
         */
        HashMap <String, String> mapOfValuesDiagnoses = (HashMap<String, String>) oTag.getAttrs();

        setoCurrentTag(oTag);

        tvDescribe.setText(mapOfValuesDiagnoses.get(Diagnose.DIAGNOSTEXT));
        tvSummary.setText(mapOfValuesDiagnoses.get(Diagnose.DIAGNOSTEXT));

        int sValueSelectionItem = setValueForEachSpinner(spNatureOfDisease,mapOfValuesDiagnoses.get(Diagnose.ILLTYPEID));
        spNatureOfDisease.setSelection(sValueSelectionItem);

        sValueSelectionItem = setValueForEachSpinner(spIllegalActions,mapOfValuesDiagnoses.get(Diagnose.CRIMEID));
        spIllegalActions.setSelection(sValueSelectionItem);

        sValueSelectionItem = setValueForEachSpinner(spAccident,mapOfValuesDiagnoses.get(Diagnose.TRAVMAID));
        spAccident.setSelection(sValueSelectionItem);

        sValueSelectionItem = setValueForEachSpinner(spDispenser,mapOfValuesDiagnoses.get(Diagnose.DISPID));
        spDispenser.setSelection(sValueSelectionItem);

        sValueSelectionItem = setValueForEachSpinner(spReasonRemove,mapOfValuesDiagnoses.get(Diagnose.DISPOFFID));
        spReasonRemove.setSelection(sValueSelectionItem);

        setValueForTypeDiagnoses(mapOfValuesDiagnoses);

    }

    private void setValueForTypeDiagnoses(HashMap <String, String> mapOfValuesDiagnoses){

        /**
         * Получаем тип диагноза
         */
        String sValueSelectionItemString = mapOfValuesDiagnoses.get(Diagnose.DIAGNOSTYPE);

        if (getMapOfTypes() != null){

            /**
             * Перебираем список доступных типов диагнозов
             */
            for(String sItemType : getMapOfTypes().keySet()){

                if(sItemType.equals(sValueSelectionItemString)){

                    getMapOfTypes().get(sItemType).setEnabled(true);
                    getMapOfTypes().get(sItemType).toggle();

                } else{

                    if(sValueSelectionItemString.equals(MAIN_DIAGNOSES)){

                        getMapOfTypes().get(sItemType).setEnabled(true);

                    }else{

                        if(isbIsHasMainDiagnoses()){
                            if(sItemType.equals(MAIN_DIAGNOSES)){
                                getMapOfTypes().get(sItemType).setEnabled(false);
                            }else{
                                getMapOfTypes().get(sItemType).setEnabled(true);
                            }
                        }else{
                            getMapOfTypes().get(sItemType).setEnabled(true);
                        }
                    }
                }



            }
        }

    }

    private void getVisitIDFromMenuHolder(){

        if(menuHolder != null){
            sStatus = menuHolder.getsStatus();
            sVisitID = menuHolder.getTextIdCalls().getText().toString();
        }
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
    }

    @Override
    public void loadedVisitComleted() {
        //С визитами здесь ничего не делаем
    }

    private void initLoadFromDataBase(){
        getLoaderManager().initLoader(0, new Bundle(), this);
    }

    /**
     * Получение списка всех типов диагнозов
     * @return Коллекция со списком типов радио кнопок
     */
    public HashMap<String, RadioButton> getMapOfTypes() {
        return mapOfTypes;
    }

    public void setMapOfTypes(HashMap<String, RadioButton> mapOfTypes) {
        this.mapOfTypes = mapOfTypes;
    }

    public Tag getoCurrentTag() {
        return oCurrentTag;
    }

    public void setoCurrentTag(Tag oCurrentTag) {
        this.oCurrentTag = oCurrentTag;
    }

    @Override
    public void loadedDiagnosesComleted() {

        if(getActivity()!= null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLoadFromDataBase();

                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoaderForVisits(getActivity(), oDataBaseHelper, sVisitID);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursotOfData) {

        int iCount = 0;

        if (cursotOfData.moveToFirst()) {
            do {

                mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag oTag = new Tag(iCount,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSCODE)));

                HashMap <String, String> mapOfValuesDiagnoses = new HashMap<>();

                mapOfValuesDiagnoses.put(Diagnose.DIAGNOSEID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSEID)));
                mapOfValuesDiagnoses.put(Diagnose.VISITID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.VISITID)));
                mapOfValuesDiagnoses.put(Diagnose.DIAGNOSTYPE,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSTYPE)));
                mapOfValuesDiagnoses.put(Diagnose.DIAGNOSID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSID)));
                mapOfValuesDiagnoses.put(Diagnose.DIAGNOSCODE,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSCODE)));
                mapOfValuesDiagnoses.put(Diagnose.DIAGNOSTEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSTEXT)));
                mapOfValuesDiagnoses.put(Diagnose.ILLTYPEID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.ILLTYPEID)));
                mapOfValuesDiagnoses.put(Diagnose.ILLTYPETEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.ILLTYPETEXT)));
                mapOfValuesDiagnoses.put(Diagnose.WORSEID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.WORSEID)));
                mapOfValuesDiagnoses.put(Diagnose.WORSETEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.WORSETEXT)));
                mapOfValuesDiagnoses.put(Diagnose.CRIMEID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.CRIMEID)));
                mapOfValuesDiagnoses.put(Diagnose.CRIMETEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.CRIMETEXT)));
                mapOfValuesDiagnoses.put(Diagnose.STAGEID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.STAGEID)));
                mapOfValuesDiagnoses.put(Diagnose.STAGETEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.STAGETEXT)));
                mapOfValuesDiagnoses.put(Diagnose.DISPID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DISPID)));
                mapOfValuesDiagnoses.put(Diagnose.DISPTEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DISPTEXT)));
                mapOfValuesDiagnoses.put(Diagnose.DISPOFFID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DISPOFFID)));
                mapOfValuesDiagnoses.put(Diagnose.DISPOFFTEXT,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DISPOFFTEXT)));
                mapOfValuesDiagnoses.put(Diagnose.STATING,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.STATING)));
                mapOfValuesDiagnoses.put(Diagnose.TRAVMAID,cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.TRAVMAID)));

                String sTypeOfDiagnoses = cursotOfData.getString(cursotOfData.getColumnIndex(Diagnose.DIAGNOSTYPE));

                if(sTypeOfDiagnoses.equals(MAIN_DIAGNOSES)){
                    setbIsHasMainDiagnoses(true);
                }


                oTag.setAttrs(mapOfValuesDiagnoses);

                tclvDiagnoses.add(oTag);

                iCount++;
            } while (cursotOfData.moveToNext());
        }

        ArrayList <Tag> listOfTags =  getTagsList();

        if(listOfTags.size() > 0){
            tclvDiagnoses.setiSelectedTagIndex(0);
            setValueForSelectedDiagnoses(listOfTags.get(0));
        }else{
            setNoDiagnoses();
        }

        tclvDiagnoses.drawTags();

        frame_for_progress_bar.setVisibility(View.GONE);

    }

    private ArrayList <Tag> getTagsList(){
        if(tclvDiagnoses != null){
            return (ArrayList) tclvDiagnoses.getTags();
        }else{
            return null;
        }
    }

    public boolean isbIsHasMainDiagnoses() {
        return bIsHasMainDiagnoses;
    }

    public void setbIsHasMainDiagnoses(boolean bIsHasMainDiagnoses) {
        this.bIsHasMainDiagnoses = bIsHasMainDiagnoses;
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    public void setMenuHolder(AdapterMenuMyCalls.MenuHolder menuHolder) {
        this.menuHolder = menuHolder;
    }

    @Override
    public void addNewTag(Tag oNewTag) {

        if(oNewTag != null){
            tclvDiagnoses.add(oNewTag);
            tclvDiagnoses.drawTags();

            if(getTagsList().size() == 1 ){
                HashMap <String, String> mapOfAttr = (HashMap<String, String>) getTagsList().get(0).getAttrs();
                mapOfAttr.put(Diagnose.DIAGNOSTYPE, MAIN_DIAGNOSES);
                setValueForSelectedDiagnoses(getTagsList().get(0));
            }else{
                setValueForSelectedDiagnoses(oNewTag);
            }

            findMainDiagnoses();
        }
    }

    /**
     * Запрос на получения списка диагнозов
     */
    public ArrayList<Tag> saveData() {

        if(tclvDiagnoses != null){
            return (ArrayList<Tag>) tclvDiagnoses.getTags();
        }else{
            return null;
        }

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
            Cursor cursor = Diagnose.getInformationAboutDiagnose(oDataBaseHelper, sVisitId);
            return cursor;
        }
    }

}
