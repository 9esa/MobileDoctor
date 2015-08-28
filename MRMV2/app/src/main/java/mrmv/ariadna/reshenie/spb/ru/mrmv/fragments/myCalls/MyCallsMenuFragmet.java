package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.MapActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.DatePicker;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.SlidingTabLayout;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.LoadCalls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 28.04.2015.
 * Фрагмент который просто загружает фрагмент мои вызовы
 */
public class MyCallsMenuFragmet extends Fragment implements IDateChanged, ICommonLoadComplete {

    public static final String LOG_TAG = "MyCallsMenuFragmet";

    /*
     * layout для списков
     */
    private SlidingTabLayout slidingTabLayout;

    /*
     *  День недели
     */
    private TextView tvDayOfWeek;

    private FragmentActivity myContext;
    private LoginAccount loginAccount;

    private Button btUpdate;
    private FrameLayout flProgressBar;

    private LinearLayout llOpenMaps;

    /**
     * Адаптер содержащий в себе фрагменты мои вызовы
     */
    private MyCallsPageAdapterFragment myCallsPageAdapterFragment;
    private ViewPager vpMyCalls;

    /**
     * Необходимо для подключения к сервису
     */
    private ServiceLoading oServiceLoading;
    private ServiceConnection sConnectionChecking;
    private Intent intentCheckingData;
    private boolean bConnectToService = false;

    private IDateChanged iDateChanged;

    //private Date dCurrentDate;
    private DateFormat dfForServer;

    /**
     * Настройки для хранения даты
     */
    private SharedPreferences sharedPref;


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        myContext = (FragmentActivity) activity;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iDateChanged = this;

        dfForServer = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        /**
         * Подключение к сервису
         */
        connectionToService();

        /**
         * Передаем так же ссылку на получение сервиса сохранения
         */
        myCallsPageAdapterFragment = new MyCallsPageAdapterFragment(myContext.getSupportFragmentManager(), myContext.getBaseContext(), loginAccount);

        sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.my_calls_menu_fragment, null);


        getUIComponents(oView, myCallsPageAdapterFragment);


        vpMyCalls.setVisibility(View.INVISIBLE);
        return oView;
    }

    private void getUIComponents(View oView, MyCallsPageAdapterFragment myCallsPageAdapterFragment) {

        vpMyCalls = (ViewPager) oView.findViewById(R.id.pager);
        slidingTabLayout = (SlidingTabLayout) oView.findViewById(R.id.sliding_tabs);
        tvDayOfWeek = (TextView) oView.findViewById(R.id.week_day);
        btUpdate = (Button) oView.findViewById(R.id.update_button);
        flProgressBar  = (FrameLayout) oView.findViewById(R.id.progress_bar_my_calls);

        llOpenMaps = (LinearLayout) oView.findViewById(R.id.layout_opem_maps);

        setCurrentDate();

        //Прекручиваем адаптер
        vpMyCalls.setAdapter(myCallsPageAdapterFragment);
        vpMyCalls.setHorizontalFadingEdgeEnabled(false);

        slidingTabLayout.delegateClickInterface(myCallsPageAdapterFragment);
        slidingTabLayout.setViewPager(vpMyCalls);

        setClickListeners();

    }

    private void setClickListeners(){

        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startLoadMyCalls();
            }
        });

        llOpenMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startViewMap();
            }
        });

        tvDayOfWeek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DatePicker dateDialog = new DatePicker();
                ((DatePicker) dateDialog).setServiceObject(iDateChanged);
                dateDialog.show(getActivity().getSupportFragmentManager(), "datePicker");

            }
        });

    }

    private void startViewMap(){
        Intent oIntentStartHomeActivity = new Intent(myContext, MapActivity.class);
        startActivity(oIntentStartHomeActivity);
    }

    private void setCurrentDate() {

        String sValueForTitle = "";

        long lCurrentTime = sharedPref.getLong(getActivity().getString(R.string.current_value_date), Calendar.getInstance().getTime().getTime());

        DateFormat dfForCurrent = new SimpleDateFormat("dd.MM.yyyy");

        //Получаем текущую дату
        Calendar cCurrentCalendar = Calendar.getInstance();

        boolean sValue = false;

        if (cCurrentCalendar.getTime().equals(lCurrentTime)) {
            sValue = true;
        } else {
            sValue = false;
        }

        sValueForTitle = dfForCurrent.format(lCurrentTime);

        cCurrentCalendar.setTimeInMillis(lCurrentTime);

        if (sValue) {
            tvDayOfWeek.setText("Сегодня, " + sValueForTitle + ", " + getDayOfWeekByInt(cCurrentCalendar.get(Calendar.DAY_OF_WEEK)));
        } else {
            tvDayOfWeek.setText(sValueForTitle + ", " + getDayOfWeekByInt(cCurrentCalendar.get(Calendar.DAY_OF_WEEK)));
        }
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    public void onStart() {
        super.onStart();
        /**
         * Если фрагмент запущен подключаемся к сервису
         */

        if (getActivity() != null) {
            getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);
        }
    }

    public void onResume() {
        super.onResume();
    }

    public void onPause() {
        super.onPause();
    }

    public void onStop() {
        super.onStop();
        /**
         * Если фрагмент остановлен отключаемся от сервиса
         */
        if (!bConnectToService) {
            return;
        } else {
            if (getActivity() != null) {
                getActivity().unbindService(sConnectionChecking);
                bConnectToService = false;
            }
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
    }


    private void connectionToService() {
        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);
        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                bConnectToService = true;

                startLoadMyCalls();
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToService = false;
            }
        };
    }

    private void startLoadMyCalls() {

        if (getActivity() != null) {

            long lCurrentTime = sharedPref.getLong(getActivity().getString(R.string.current_value_date), Calendar.getInstance().getTime().getTime());

            Date dCurrentDate = new Date(lCurrentTime);

            oServiceLoading.getCallByDate(loginAccount, dfForServer.format(dCurrentDate), LoadCalls.ALL_CALLS, this);

            flProgressBar.setVisibility(View.VISIBLE);
            flProgressBar.bringToFront();


        }
    }

    /*
     * Информация для загрузки информации об авторизованом враче
     */
    public void setLogin(LoginAccount loginAccount) {
        this.loginAccount = loginAccount;
    }

    @Override
    public void dateChanged(int year, int month, int day) {
        String sValueForServer = "";
        String sValueForTitle = "";

        Date dSelectedDate = null;

        sValueForServer += year;

        if (month <= 8) {
            sValueForServer += "-0" + (month + 1);
        } else {
            sValueForServer += "-" + (month + 1);
        }

        if (day <= 8) {
            sValueForServer += "-0" + (day);
        } else {
            sValueForServer += "-" + day;
        }

        try {

            dSelectedDate = dfForServer.parse(sValueForServer);

            //  dCurrentDate.setTime(dSelectedDate.getTime());

            DateFormat dfForCurrent = new SimpleDateFormat("dd.MM.yyyy");

            sValueForTitle = dfForCurrent.format(dSelectedDate);

            //Получаем текущую дату
            Calendar cCurrentCalendar = Calendar.getInstance();

            boolean sValue = false;

            if (dfForCurrent.format(cCurrentCalendar.getTime()).equals(sValueForTitle)) {
                sValue = true;
            } else {
                sValue = false;
            }

            cCurrentCalendar.set(year, month, day);

            if (sValue) {
                tvDayOfWeek.setText(myContext.getString(R.string.today_top_my_calls) + sValueForTitle + ", " + getDayOfWeekByInt(cCurrentCalendar.get(Calendar.DAY_OF_WEEK)));
            } else {
                tvDayOfWeek.setText(sValueForTitle + ", " + getDayOfWeekByInt(cCurrentCalendar.get(Calendar.DAY_OF_WEEK)));
            }

        } catch (Exception ex) {
            tvDayOfWeek.setText("-");
        }

        /**
         * Записываем выбранную дату в память
         */
        SharedPreferences.Editor editor = sharedPref.edit();

        if (dSelectedDate != null) {
            editor.putLong(getActivity().getString(R.string.current_value_date), dSelectedDate.getTime());
        }
        editor.commit();

        startActionWithDate();

    }

    private void startActionWithDate() {
        if (bConnectToService) {
            if (oServiceLoading != null) {
                if(tvDayOfWeek != null){
                    if (tvDayOfWeek.getText().toString().equals("-")) {
                        if(getActivity()!= null) {
                            Toast.makeText(getActivity(), myContext.getString(R.string.need_correct_date), Toast.LENGTH_LONG).show();
                        }
                    } else {
                        startLoadMyCalls();
                    }
                }
            }
        } else {

            if(getActivity()!= null){
                Toast.makeText(getActivity(), myContext.getString(R.string.problem_to_connect_service_download), Toast.LENGTH_LONG).show();
            }

        }
    }

    private String getDayOfWeekByInt(int iDayOfWeek) {

        switch (iDayOfWeek) {
            case 2:
                return "понедельник";
            case 3:
                return "вторник";
            case 4:
                return "среда";
            case 5:
                return "четверг";
            case 6:
                return "пятница";
            case 7:
                return "суббота";
            case 1:
                return "воскресенье";

        }
        return "-";
    }

    @Override
    public void loadCompleted() {

        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    myCallsPageAdapterFragment.notifyDataSetChanged();
                    flProgressBar.setVisibility(View.GONE);
                    vpMyCalls.setVisibility(View.VISIBLE);
                }
            });
        }

    }
}
