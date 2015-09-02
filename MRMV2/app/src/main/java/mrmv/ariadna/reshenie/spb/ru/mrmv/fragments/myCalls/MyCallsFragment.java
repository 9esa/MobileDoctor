package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.Calls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.CommonDoctorCard;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;

/**
 * Created by kirichenko on 23.04.2015.
 */
public class MyCallsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    private FragmentActivity myContext;

    private AdapterMenuMyCalls adapterMenuMyCalls;

    private ListView listViewMyCalls;

    private DataBaseHelper oDataBaseHelper;

    /**
     * Малоли что но диалог должен быть один
     */
    public static ProgressDialog oDialogProgress;

    private LoginAccount loginAccount;

    public static MyCallsFragment newInstance(int page, LoginAccount loginAccount) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MyCallsFragment fragment = new MyCallsFragment();
        fragment.setloginAccount(loginAccount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext=(FragmentActivity) activity;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(getArguments() != null){
            mPage = getArguments().getInt(ARG_PAGE);
        }


        oDataBaseHelper = DataBaseHelper.getInstance(getActivity());

        adapterMenuMyCalls = new AdapterMenuMyCalls(getActivity(), null);

        oDialogProgress = new ProgressDialog(getActivity());
        oDialogProgress.setMessage("Загрузка...");
        oDialogProgress.setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.my_call_list, container, false);

        listViewMyCalls = (ListView)view.findViewById(android.R.id.list);
        listViewMyCalls.setAdapter(adapterMenuMyCalls);

        listViewMyCalls.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View oView, int i, long l) {
                openDoctorsCard(oView);
                return false;
            }
        });

        startLoadFromDataBase();

        return view;
    }

    private void openDoctorsCard(View oView){

        AdapterMenuMyCalls.MenuHolder menuHolder = (AdapterMenuMyCalls.MenuHolder) oView.getTag();

        CommonDoctorCard oMyCallsMenuFragmet = new CommonDoctorCard();

        if(getActivity() != null){
            try{
                String sValueCurrentDay = ((TextView)getActivity().findViewById(R.id.week_day)).getText().toString();
                oMyCallsMenuFragmet.setCurrentDay(sValueCurrentDay);
            }catch (Exception ex){

            }

        }

        oMyCallsMenuFragmet.setMenuHolder(menuHolder);
        oMyCallsMenuFragmet.setLoginAccount(loginAccount);

        FragmentManager fragmentManager = myContext.getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.main_active_layout, oMyCallsMenuFragmet)
                .addToBackStack("fragmentStack")
                .commit();

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        int iCurrentPage = args.getInt(ARG_PAGE);

        return new CursorLoaderForMyCalls(getActivity(), oDataBaseHelper, iCurrentPage);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor oCursor) {

        adapterMenuMyCalls.swapCursor(oCursor);

        listViewMyCalls.setAdapter(adapterMenuMyCalls);
        adapterMenuMyCalls.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapterMenuMyCalls.swapCursor(null);
    }

    public void setloginAccount(LoginAccount loginAccount) {
        this.loginAccount = loginAccount;
    }

    protected void startLoadFromDataBase(){

        Bundle oBundle = new Bundle();
        oBundle.putInt(ARG_PAGE, mPage);
        getLoaderManager().restartLoader(mPage, oBundle, this);

    }

    static class CursorLoaderForMyCalls extends CursorLoader {

        private DataBaseHelper oDataBaseHelper;
        private int iCurrentPage;


        public CursorLoaderForMyCalls(Context context, DataBaseHelper oDataBaseHelper, int iCurrentPage) {
            super(context);
            this.oDataBaseHelper = oDataBaseHelper;
            this.iCurrentPage = iCurrentPage;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = Calls.getMyCalls(oDataBaseHelper,iCurrentPage);

            return cursor;
        }
    }

}
