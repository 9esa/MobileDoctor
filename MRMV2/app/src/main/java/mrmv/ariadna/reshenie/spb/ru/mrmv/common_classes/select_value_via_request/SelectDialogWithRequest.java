package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 19.06.2015.
 * Класс для выбора элемента из списка на основе данных полученых от сервера
 */

public class SelectDialogWithRequest extends DialogFragment  implements DialogInterface.OnClickListener, LoaderManager.LoaderCallbacks<Cursor>, ICommonLoadComplete {

    /**
     * Необходимо для подключения к сервису для загрузки
     */
    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionServer;
    private boolean bConnectToServiceDownload = false;
    private Intent intentCheckingData;
    /****/

    /**
     * Интерфейс позволяющий управлять диалогом
     */
    private static IRouteDialogWithRequest iRouteDialogWithRequest;

    private CursorAdapter oCursorAdapter;

    private DataBaseHelper oDataBaseHelper;

    private ListView listViewElements;
    private Button btnCancel;
    private LinearLayout topOfDialogSelected;

    private String sValueTitle;
    private Object oRequestValue;

    private LoginAccount oLoginAccount;
    private boolean bFastMode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        startAllServices();

    }

    public int getTopOfDialogSelected() {
        return topOfDialogSelected.getVisibility();
    }

    public void setVisibleForTopOfDialogSelected(int iView) {
        if(topOfDialogSelected != null){
            topOfDialogSelected.setVisibility(iView);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        getDialog().setTitle(getsValueTitle());

        getDialog().setCancelable(false);

        View oView = inflater.inflate(R.layout.list_for_selection, null);

        listViewElements = (ListView)oView.findViewById(R.id.list_of_diagnoses);

        btnCancel = (Button)oView.findViewById(R.id.btn_cancel_diagnoses);

        topOfDialogSelected = (LinearLayout)oView.findViewById(R.id.llTopOfDialogSelected);

        setVisibleForTopOfDialogSelected(View.GONE);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelDialog();
            }
        });

        if (listViewElements != null) {
            if (getoCursorAdapter() != null) {
                listViewElements.setAdapter(getoCursorAdapter());
            }

        }

        listViewElements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int iItemSelected, long l) {
                /*
                    Возвращаем выбранное значение
                 */
                cancelDialog();
                iRouteDialogWithRequest.returnSelectedValue(adapterView, iItemSelected);

            }
        });

        getActivity().bindService(intentCheckingData, sConnectionServer, Context.BIND_AUTO_CREATE);

        return oView;
    }

    private void cancelDialog(){
        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getLoaderManager().destroyLoader(0);
    }

    private void startAllServices() {

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionServer = new ServiceConnection() {

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

        startLoadDataInDialog();
    }

    public void startLoadDataInDialog(){
        //Todo Добавить прогрес бар
        if(getiRouteDialogWithRequest() != null){
            if(oServiceLoading != null){
                getiRouteDialogWithRequest().getInformationByObject(oServiceLoading, getoLoginAccount(), getoRequestValue(), this);
            }

        }
    }

    public CursorAdapter getoCursorAdapter() {
        return oCursorAdapter;
    }

    public void setoCursorAdapter(CursorAdapter oCursorAdapter) {
        this.oCursorAdapter = oCursorAdapter;
    }

    public String getsValueTitle() {
        return sValueTitle;
    }

    public void setsValueTitle(String sValueTitle) {
        this.sValueTitle = sValueTitle;
    }

    public Object getoRequestValue() {
        return oRequestValue;
    }

    public void setoRequestValue(Object oRequestValue) {
        this.oRequestValue = oRequestValue;
    }

    public LoginAccount getoLoginAccount() {
        return oLoginAccount;
    }

    public void setoLoginAccount(LoginAccount oLoginAccount) {
        this.oLoginAccount = oLoginAccount;
    }

    public static IRouteDialogWithRequest getiRouteDialogWithRequest() {
        return iRouteDialogWithRequest;
    }

    /*
       Устанавливаем Callback для возврата
     */
    public void setiRouteDialogWithRequest(IRouteDialogWithRequest iRouteDialogWithRequest) {
        this.iRouteDialogWithRequest = iRouteDialogWithRequest;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoaderForList(getActivity(), oDataBaseHelper, getoRequestValue());
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (getoCursorAdapter() != null) {
            getoCursorAdapter().swapCursor(data);

//            if(bFastMode){
//                listViewElements.cli
//            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        if (getoCursorAdapter() != null) {
            getoCursorAdapter().swapCursor(null);
        }
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
            //todo CallBack с возвращаемой информацией надо запилить
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

    private void initLoadListFromDataBase() {
        Bundle oBundle = new Bundle();
        getLoaderManager().restartLoader(0, oBundle, this);
    }

    public void setbFastMode(boolean bFastMode) {

        this.bFastMode = bFastMode;
    }


    static class CursorLoaderForList extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;
        private Object oRequest;


        public CursorLoaderForList(Context context, DataBaseHelper oDataBaseHelper, Object oRequest) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.oRequest = oRequest;
        }

        @Override
        public Cursor loadInBackground() {

            if(getiRouteDialogWithRequest() != null){
                return getiRouteDialogWithRequest().loadInformationFromDataBase(oDataBaseHelper,oRequest);
            }else{
                return null;
            }
        }
    }

}
