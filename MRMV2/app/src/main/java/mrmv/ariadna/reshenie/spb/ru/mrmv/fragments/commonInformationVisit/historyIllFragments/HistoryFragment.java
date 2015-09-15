package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.historyIllFragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.historyAction.History;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 14.05.2015.
 */
public class HistoryFragment extends Fragment  implements ICommonLoadComplete,LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Необходимо для подключения к сервису для загрузки
     */
    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionChecking;
    private boolean bConnectToServiceDownload = false;
    private Intent intentCheckingData;

    private FrameLayout frame_for_progress_bar, flHistoryIsEmpty;

    private DataBaseHelper oDataBaseHelper;

    static private String sPatientId;
    static private LoginAccount oLoginAccount;

    private WebView wvHistory;

    private TextView tvEmptyHistory;

    public static void setMenuHolder(AdapterMenuMyCalls.MenuHolder menuHolder) {

       if(menuHolder != null){
           HistoryFragment.sPatientId = menuHolder.getsPacientId();
       }
    }

    public static void setsPatientId(String sPatientId) {
        HistoryFragment.sPatientId = sPatientId;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.history_health_care_fragments, null);

        wvHistory = (WebView) oView.findViewById(R.id.webView);
        wvHistory.getSettings().setJavaScriptEnabled(true);

        frame_for_progress_bar = (FrameLayout) oView.findViewById(R.id.flProgressBar);
        flHistoryIsEmpty = (FrameLayout) oView.findViewById(R.id.flHistoryIsEmpty);

        tvEmptyHistory = (TextView)oView.findViewById(R.id.tvEmptyHistory);

        return oView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Активизируем наш хелпер для базы данных
        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

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
        /*
                     Запускаем отоброажение прогресс бара
        */

        wvHistory.setVisibility(View.GONE);
        frame_for_progress_bar.setVisibility(View.VISIBLE);
        frame_for_progress_bar.bringToFront();

        oServiceLoading.getInformationAboutHistory(oLoginAccount, sPatientId, this);

    }


    @Override
    public void onStart() {
        super.onStart();

        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);

    }

    @Override
    public void onStop() {
        super.onStop();

        if (!bConnectToServiceDownload) {
            return;
        } else {
            getActivity().unbindService(sConnectionChecking);
            bConnectToServiceDownload = false;
        }

    }

    public static void setoLoginAccount(LoginAccount oLoginAccount) {
        HistoryFragment.oLoginAccount = oLoginAccount;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoaderForHistory(getActivity(), oDataBaseHelper, sPatientId);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        //toDo
        if (cursor.moveToFirst()) {
            do {
                String sHTML = cursor.getString(cursor.getColumnIndex(History.TEXT));
                String mime = "text/html";
                String encoding = "utf-8";

                if(sHTML != null){

                    int iStartParse = sHTML.indexOf("<body>");

                    if(iStartParse  > -1){
                        sHTML  = sHTML.substring(iStartParse);

                        int iIndexDef = sHTML.indexOf("</body>");

                        if(iIndexDef >= 6 && iIndexDef <= 16){

                            frame_for_progress_bar.setVisibility(View.GONE);
                            tvEmptyHistory.setText(R.string.not_found_history);

                            flHistoryIsEmpty.setVisibility(View.VISIBLE);
                        }else{
                            wvHistory.loadDataWithBaseURL(null, sHTML, mime, encoding, null);
                        }
                    }
                }

            } while (cursor.moveToNext());

            wvHistory.setVisibility(View.VISIBLE);
            frame_for_progress_bar.setVisibility(View.GONE);
        }else{
            frame_for_progress_bar.setVisibility(View.GONE);
            flHistoryIsEmpty.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private void initLoadFromDataBase(){
        getLoaderManager().initLoader(0, new Bundle(), this);
    }

    @Override
    public void loadCompleted() {
        if(getActivity()!= null){
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initLoadFromDataBase();
                }
            });
        }
    }


    static class CursorLoaderForHistory extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;
        private String sPatientId;


        public CursorLoaderForHistory(Context context, DataBaseHelper oDataBaseHelper, String sPatientId) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.sPatientId = sPatientId;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = History.getInformationAboutHistory(oDataBaseHelper, sPatientId);
            return cursor;
        }
    }

}
