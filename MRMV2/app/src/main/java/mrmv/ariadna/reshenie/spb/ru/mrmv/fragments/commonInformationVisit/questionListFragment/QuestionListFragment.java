package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.questionListFragment;

import android.content.ComponentName;
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
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TableLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.SelectDialogWithReturnTag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.widget.TagCloudLinkView;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.FullFieldProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ItemProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.StructureFullFieldProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.ConstructViewProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards.ISelectedValue;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceSending;

/**
 * Created by kirichenko on 14.05.2015.
 * Не стоить объединять в общий класс с другими вкладками
 */
public class QuestionListFragment extends Fragment implements ICommonLoadComplete, LoaderManager.LoaderCallbacks<Cursor>, ISelectedValue {

    public static String OBJECT = "object";
    public static String MODE = "mode";
    public static String LIST = "list";
    public static String STRUCTURE = "structure";
    public static String EMPTY_STRUCTURE = "empty_structure";

    static private LoginAccount oLoginAccount;
    static private String sVisitId;

    private static AdapterMenuMyCalls.MenuHolder menuHolder;
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
    private boolean bConnectToServiceUpload;
    private ServiceSending oServiceTransfer;

    /**
     * Список дополнительных элементов
     */
    private List<View> listProtocols;
    private List<ItemProtocols> listItemProtocols;

    private String sId;
    private String sFormId;
    private String sFormResultId;

    private FrameLayout frame_for_progress_bar;
    private TagCloudLinkView tags_of_protocols;
    private ScrollView svAllElementsQuestionList;
    private TableLayout paneToAddQuestion;
    private Button btnSaveProtocols;
    private ImageButton btnCreate;

    private boolean bIsNewValue = false;

    private DataBaseHelper oDataBaseHelper;

    private ISelectedValue ISelectedValue;

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        QuestionListFragment.oLoginAccount = oLoginAccount;
    }

    public static void setMenuHolder(AdapterMenuMyCalls.MenuHolder menuHolder) {

        if (menuHolder != null) {
            QuestionListFragment.menuHolder = menuHolder;
            sVisitId = menuHolder.getTextIdCalls().getText().toString();
        }

    }

    public String getsId() {
        return sId;
    }

    public void setsId(String sId) {
        this.sId = sId;
    }

    public static String getsVisitId() {
        return sVisitId;
    }

    public static void setsVisitId(String sVisitId) {
        QuestionListFragment.sVisitId = sVisitId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.questions_list_fragments, null);

        tags_of_protocols = (TagCloudLinkView) oView.findViewById(R.id.tags_of_protocols);
        frame_for_progress_bar = (FrameLayout) oView.findViewById(R.id.frame_layout_for_progress_bar_question);
        paneToAddQuestion = (TableLayout) oView.findViewById(R.id.paneToAddQuestion);
        svAllElementsQuestionList = (ScrollView) oView.findViewById(R.id.scrollView);
        btnCreate = (ImageButton) oView.findViewById(R.id.btnCreate);
        btnSaveProtocols = (Button) oView.findViewById(R.id.btnSaveProtocols);

        btnSaveProtocols.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                saveFullFieldProtocol();
            }
        });

        btnCreate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                bIsNewValue = true;

                saveFullFieldProtocol();
                SelectDialogWithReturnTag dialogFragment = new SelectDialogWithReturnTag();
                dialogFragment.setLinkCallBack(ISelectedValue);
                dialogFragment.show(getFragmentManager(), "addNewProtocols");
            }
        });


        actionWithProtocolTags(tags_of_protocols);

        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);
        getActivity().bindService(intentTransferData, sConnectionTrasfer, getActivity().BIND_AUTO_CREATE);

        return oView;
    }

    private void startShowProgressBar(Boolean bFoolGone) {
        /*
                     Запускаем отоброажение прогресс бара
        */
        if (bFoolGone) {
            svAllElementsQuestionList.setVisibility(View.GONE);
        } else {
            svAllElementsQuestionList.setVisibility(View.GONE);
        }

        btnCreate.setEnabled(false);
        frame_for_progress_bar.setVisibility(View.VISIBLE);
        frame_for_progress_bar.bringToFront();
    }

    private void stopShowProgressBar() {
        /*
                     Останавливаем отоброажение прогресс бара
        */
        btnCreate.setEnabled(true);
        frame_for_progress_bar.setVisibility(View.GONE);
        paneToAddQuestion.setVisibility(View.VISIBLE);
        svAllElementsQuestionList.setVisibility(View.VISIBLE);
    }

    public List<View> getListProtocols() {
        return listProtocols;
    }

    public List<ItemProtocols> getListItemProtocols() {
        return listItemProtocols;
    }

    private void getForIdAndResultId(){
        sFormResultId = FullFieldProtocols.getResultIdById(oDataBaseHelper, sId);
        sFormId = FullFieldProtocols.getFormIdById(oDataBaseHelper, sId);
    }

    private void saveFullFieldProtocol() {

        for (int iCount = 0; iCount < listProtocols.size(); iCount++) {

            ConstructViewProtocols.parceValueFromView(getListProtocols().get(iCount), getListItemProtocols().get(iCount), oDataBaseHelper, ConstructViewProtocols.QUESTION);
        }

        ConstructViewProtocols.cleanAllElements();

        if (listProtocols.size() > 0) {
            if (bConnectToServiceUpload) {

                getForIdAndResultId();

                oServiceTransfer.startSendingInformationaAboutProtocols(oLoginAccount, sVisitId, sId, sFormId, sFormResultId);
            } else {
                if (getActivity() != null) {
                    Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                    intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, getActivity().getString(R.string.sending_service_unavailable));
                    LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(intent);
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.ISelectedValue = this;

        listProtocols = new ArrayList<>();
        listItemProtocols = new ArrayList<>();

        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        startAllServices();
    }

    private void startAllServices() {

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {

                tryConnectToServiceAndLoad(binder);
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToServiceDownload = false;
            }
        };

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
    }

    private void tryConnectToServiceAndLoad(IBinder binder) {

        oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
        bConnectToServiceDownload = true;

        startShowProgressBar(true);

        oServiceLoading.getInformationAboutProtocols(oLoginAccount, getsVisitId(), this);

    }

    private void actionWithProtocolTags(final TagCloudLinkView tclvDiagnoses) {

        tclvDiagnoses.setOnTagSelectListener(new TagCloudLinkView.OnTagSelectListener() {

            @Override
            public void onTagSelected(Tag oTag, int position) {

                saveFullFieldProtocol();

                startLoadStructure(oTag);

                startShowProgressBar(false);
            }

        });

        tclvDiagnoses.setOnTagDeleteListener(new TagCloudLinkView.OnTagDeleteListener() {

            @Override
            public void onTagDeleted(Tag oTag, int position) {
                if (bConnectToServiceUpload) {

                   HashMap<String, String> mapOfValuesProtocols = (HashMap<String, String>) oTag.getAttrs();

                   String sValueId = mapOfValuesProtocols.get(FullFieldProtocols.ID);

                   setsId(sValueId);

                   getForIdAndResultId();

                   oServiceTransfer.startDeleteInformationaAboutProtocols(oLoginAccount, sFormResultId, sVisitId);

                   listProtocols.clear();

                   FullFieldProtocols.removeInfornationAboutFormId(oDataBaseHelper ,sId);

                    if (tags_of_protocols.getTags().size() > 0) {

                        tags_of_protocols.setiSelectedTagIndex(tags_of_protocols.getTags().size() - 1);
                        startLoadStructure(getTagsList().get(tags_of_protocols.getTags().size() - 1));
                    } else {
                        if (paneToAddQuestion != null) {
                            paneToAddQuestion.removeAllViews();
                        }
                    }

                }
            }
        });

    }

    private void constructProtocols(Cursor cursotOfData) {

        listProtocols.clear();
        listItemProtocols.clear();

        if (cursotOfData.moveToFirst()) {

            int iIndexId = cursotOfData.getColumnIndex(StructureFullFieldProtocols.ID);
            int iProtocolId = cursotOfData.getColumnIndex(StructureFullFieldProtocols.PROTOCOLID);
            int iExtraField = cursotOfData.getColumnIndex(StructureFullFieldProtocols.EXTRAFIELD);
            int iVisit = cursotOfData.getColumnIndex(StructureFullFieldProtocols.VISITID);
            int iFormItemId = cursotOfData.getColumnIndex(StructureFullFieldProtocols.FORMITEMID);
            int iTyp = cursotOfData.getColumnIndex(StructureFullFieldProtocols.TYP);
            int iValueType = cursotOfData.getColumnIndex(StructureFullFieldProtocols.VALUETYPE);
            int iDataType = cursotOfData.getColumnIndex(StructureFullFieldProtocols.DATATYPE);
            int iIsMulti = cursotOfData.getColumnIndex(StructureFullFieldProtocols.ISMULTI);
            int iIsListchkr = cursotOfData.getColumnIndex(StructureFullFieldProtocols.ISLISTCHKR);
            int iColor = cursotOfData.getColumnIndex(StructureFullFieldProtocols.COLOR);
            int iText = cursotOfData.getColumnIndex(StructureFullFieldProtocols.TEXT);
            int iStatus = cursotOfData.getColumnIndex(StructureFullFieldProtocols.STATUS);
            int iMandatory = cursotOfData.getColumnIndex(StructureFullFieldProtocols.MANDATORY);
            int iIsBlockInput = cursotOfData.getColumnIndex(StructureFullFieldProtocols.ISBLOCKINPUT);
            int iDiagType = cursotOfData.getColumnIndex(StructureFullFieldProtocols.DIAGTYPE);
            int iDefaultValueId = cursotOfData.getColumnIndex(StructureFullFieldProtocols.DEFAULTVALUEID);
            int iDefaultValue = cursotOfData.getColumnIndex(StructureFullFieldProtocols.DEFAULTVALUE);
            int iFormResultValueId = cursotOfData.getColumnIndex(StructureFullFieldProtocols.FORMRESULTVALUEID);
            int iFilterId = cursotOfData.getColumnIndex(StructureFullFieldProtocols.FILTERID);
            int iCText = cursotOfData.getColumnIndex(StructureFullFieldProtocols.CTEXT);

            do {

                ItemProtocols oItemProtocols = new ItemProtocols();
                oItemProtocols.setsId(cursotOfData.getString(iIndexId));
                oItemProtocols.setsProtocolId(cursotOfData.getString(iProtocolId));
                oItemProtocols.setsExtraField(cursotOfData.getString(iExtraField));
                oItemProtocols.setsVisitId(cursotOfData.getString(iVisit));
                oItemProtocols.setsFormitemId(cursotOfData.getString(iFormItemId));
                oItemProtocols.setsTyp(cursotOfData.getString(iTyp));
                oItemProtocols.setsValueType(cursotOfData.getString(iValueType));
                oItemProtocols.setsDataType(cursotOfData.getString(iDataType));
                oItemProtocols.setsIsmulti(cursotOfData.getString(iIsMulti));
                oItemProtocols.setsIslistchkr(cursotOfData.getString(iIsListchkr));
                oItemProtocols.setsColor(cursotOfData.getString(iColor));
                oItemProtocols.setsText(cursotOfData.getString(iText));
                oItemProtocols.setsStatus(cursotOfData.getString(iStatus));
                oItemProtocols.setsMandatory(cursotOfData.getString(iMandatory));
                oItemProtocols.setsIsBlockinput(cursotOfData.getString(iIsBlockInput));
                oItemProtocols.setsDiagType(cursotOfData.getString(iDiagType));
                oItemProtocols.setsDefaultValueId(cursotOfData.getString(iDefaultValueId));
                oItemProtocols.setsDefaultValue(cursotOfData.getString(iDefaultValue));
                oItemProtocols.setsFormResultValueId(cursotOfData.getString(iFormResultValueId));
                oItemProtocols.setsFilterId(cursotOfData.getString(iFilterId));
                oItemProtocols.setsCtext(cursotOfData.getString(iCText));

                listItemProtocols.add(oItemProtocols);

            } while (cursotOfData.moveToNext());
        } else {
            stopShowProgressBar();
        }

        if (paneToAddQuestion != null) {
            paneToAddQuestion.removeAllViews();
        }

        ConstructViewProtocols.cleanAllElements();

        for (ItemProtocols oItemProtocols : listItemProtocols) {
            View newCustomView = ConstructViewProtocols.constructNewRow(paneToAddQuestion, getActivity(), oItemProtocols, oDataBaseHelper, bIsNewValue);
            listProtocols.add(newCustomView);
        }
        bIsNewValue = false;
        ConstructViewProtocols.setSpinnerListeners();
    }

    private void startLoadStructure(Tag oTag) {

        if (oTag != null) {
            HashMap<String, String> mapOfAttr = (HashMap<String, String>) oTag.getAttrs();

            if (mapOfAttr != null) {
                Bundle oBundle = new Bundle();
                oBundle.putString(MODE, STRUCTURE);

                if (mapOfAttr.get(FullFieldProtocols.FORMRESULTID) == null) {
                    oBundle.putBoolean(EMPTY_STRUCTURE, true);

                    setsId(mapOfAttr.get(FullFieldProtocols.ID));
                    getForIdAndResultId();

                    oBundle.putString(OBJECT, sFormId);

                } else {
                    oBundle.putBoolean(EMPTY_STRUCTURE, false);

                    setsId(mapOfAttr.get(FullFieldProtocols.ID));
                    getForIdAndResultId();

                    oBundle.putString(OBJECT, sFormResultId);

                }

                getLoaderManager().restartLoader(0, oBundle, this);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        saveFullFieldProtocol();

        if (bConnectToServiceDownload) {
            if (getActivity() != null) {
                getActivity().unbindService(sConnectionChecking);
                bConnectToServiceDownload = false;
            }
        }

        if (bConnectToServiceUpload) {
            if (getActivity() != null) {
                getActivity().unbindService(sConnectionTrasfer);
                bConnectToServiceUpload = false;
            }
        }

        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initLoadListFromDataBase() {
        Bundle oBundle = new Bundle();
        oBundle.putString(MODE, LIST);
        oBundle.putString(OBJECT, getsVisitId());
        oBundle.putBoolean(EMPTY_STRUCTURE, false);
        getLoaderManager().restartLoader(0, oBundle, this);
    }

    @Override
    public void loadCompleted() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLoadListFromDataBase();
                }
            });
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle oBundle) {

        String sMode = oBundle.getString(MODE);

        if (sMode.equals(STRUCTURE)) {
            return new CursorLoaderForHistory(getActivity(), oDataBaseHelper, oBundle.getString(OBJECT), sMode, oBundle.getBoolean(EMPTY_STRUCTURE));
        } else {
            return new CursorLoaderForHistory(getActivity(), oDataBaseHelper, oBundle.getString(OBJECT), sMode, oBundle.getBoolean(EMPTY_STRUCTURE));
        }
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursotOfData) {

        if (cursotOfData != null) {

            if (((CursorLoaderForHistory) loader).sMode.equals(LIST)) {

                constructTop(cursotOfData);

            } else if (((CursorLoaderForHistory) loader).sMode.equals(STRUCTURE)) {

                constructProtocols(cursotOfData);
                stopShowProgressBar();
            }
        } else {
            stopShowProgressBar();
        }
    }

    private void constructTop(Cursor cursotOfData) {

        int iCount = 0;
        int iSizeOfObjects = tags_of_protocols.getTags().size();

        for(; iCount < iSizeOfObjects; iCount ++){
            tags_of_protocols.remove(0);
        }

        iCount = 0;

        if (cursotOfData.moveToFirst()) {

            do {

                mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag oTag = new Tag(iCount, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.TEXT)));

                HashMap<String, String> mapOfValuesProtocols = new HashMap<>();
                mapOfValuesProtocols.put(FullFieldProtocols.ID, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.ID)));
                mapOfValuesProtocols.put(FullFieldProtocols.VISITID, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.VISITID)));
                mapOfValuesProtocols.put(FullFieldProtocols.FORMRESULTID, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.FORMRESULTID)));
                mapOfValuesProtocols.put(FullFieldProtocols.FORMID, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.FORMID)));
                mapOfValuesProtocols.put(FullFieldProtocols.TEXT, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.TEXT)));
                mapOfValuesProtocols.put(FullFieldProtocols.DATE, cursotOfData.getString(cursotOfData.getColumnIndex(FullFieldProtocols.DATE)));
                oTag.setAttrs(mapOfValuesProtocols);

                tags_of_protocols.add(oTag);

                iCount++;

            } while (cursotOfData.moveToNext());
        }

        tags_of_protocols.drawTags();

        if (tags_of_protocols.getTags().size() > 0) {

            tags_of_protocols.setiSelectedTagIndex(tags_of_protocols.getTags().size() - 1 );

            HashMap<String, String> mapOfValuesProtocols = (HashMap<String, String>) getTagsList().get(tags_of_protocols.getTags().size() - 1).getAttrs();
            setsId(mapOfValuesProtocols.get(FullFieldProtocols.ID));
            getForIdAndResultId();

            startLoadStructure(getTagsList().get(tags_of_protocols.getTags().size() - 1));

            tags_of_protocols.drawTags();

        } else {
            stopShowProgressBar();
        }
    }

    private ArrayList<Tag> getTagsList() {
        if (tags_of_protocols != null) {
            return (ArrayList) tags_of_protocols.getTags();
        } else {
            return null;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        getLoaderManager().destroyLoader(0);
    }

    @Override
    public void addNewTag(Tag oNewTag) {

        if (oNewTag != null) {

            setsId(FullFieldProtocols.addProtocols(oDataBaseHelper, oNewTag, getsVisitId()));

            startLoadNewStructure(oNewTag);
        }
    }

    private void startLoadNewStructure(Tag oNewTag) {

            if (bConnectToServiceDownload) {
                if (oServiceLoading != null) {

                    startShowProgressBar(false);

                    getForIdAndResultId();

                    oServiceLoading.getStructureEnableProtocols(oLoginAccount, sFormId, this);
                }
            }

    }

    static class CursorLoaderForHistory extends CursorLoader {

        public static String MODE = "mode";
        public static String LIST = "list";
        public static String STRUCTURE = "structure";


        private DataBaseHelper oDataBaseHelper;
        private String sObjectForRequest;
        private String sMode;
        private boolean bIsEmpyStructure;

        public CursorLoaderForHistory(FragmentActivity context, DataBaseHelper oDataBaseHelper, String sObjectForRequest, String sMode, boolean bIsEmpyStructure) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.sObjectForRequest = sObjectForRequest;
            this.sMode = sMode;
            this.bIsEmpyStructure = bIsEmpyStructure;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = null;

            if (sMode.equals(LIST)) {
                cursor = FullFieldProtocols.getInformationAboutFullFieldProtocol(oDataBaseHelper, sObjectForRequest);
            } else if (sMode.equals(STRUCTURE)) {
                if (bIsEmpyStructure) {
                    cursor = StructureFullFieldProtocols.getInformationAboutEmptyProtocol(oDataBaseHelper, sObjectForRequest);
                } else {
                    cursor = StructureFullFieldProtocols.getInformationAboutFullFieldProtocol(oDataBaseHelper, sObjectForRequest);
                }
            }

            return cursor;
        }
    }

}
