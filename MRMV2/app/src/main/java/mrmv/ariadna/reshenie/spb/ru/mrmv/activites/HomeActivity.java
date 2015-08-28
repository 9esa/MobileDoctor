package mrmv.ariadna.reshenie.spb.ru.mrmv.activites;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.drawerMenu.AdapterMenuItem;
import mrmv.ariadna.reshenie.spb.ru.mrmv.drawerMenu.MenuItem;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk.FindEmkFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.MyCallsMenuFragmet;

/**
 * Created by kirichenko on 23.04.2015.
 */
public class HomeActivity extends FragmentActivity {

    private Button bExite;

    private TextView tvUserName;
    private TextView tvNameDepartment;
    private TextView tvNameSelectedMenu;

    private DrawerLayout dlSlideMenu;

    private FrameLayout flMainActiveLayout;
    private FrameLayout flCommonMainLayout;

    private LinearLayout llHomeToolBar;

    private LoginAccount oLoginAccount;

    private ListView lvListDrawer;

    private ImageButton route_menu_button;

    public final static String BROADCAST_ACTION_TAKING_INFORMATION = "mrmv.ariadna.reshenie.spb.ru.mrmv.activites.action_complite";
    public final static String MESSAGE_TO_VIEW = "message";


    public static int MY_CALLS_MODE = 0;
    public static int FIND_EMK = 1;
    public static int DOCUMENTATION = 2;
    public static int BOOK_RLS = 3;
    public static int MEDICAL_HELP = 4;
    public static int BOOKING_VISIT = 5;
    public static int REANIMATION = 6;
    public static int WARNING = 7;
    public static int PHARMACY = 8;

    public static String KEY_FOR_GET_MODE = "key_for_get_mode";

    public BroadcastReceiver oBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getStringExtra(MESSAGE_TO_VIEW), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        /*
            Получение всех объектов на view должно быть полученно сразу
         */
        getAllViewObject();

        bExite.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                onPushExite();
            }
        });

        Object oObjectFromLoginActivity = getIntent().getParcelableExtra(LoginAccount.class.getCanonicalName());

        if (oObjectFromLoginActivity != null) {
            if (oObjectFromLoginActivity instanceof LoginAccount) {
                LoginAccount oLoginAccount = (LoginAccount) oObjectFromLoginActivity;
                loadMainInformation(oLoginAccount);
            }
        }

        /*
         * Временный выбор мои вызовы
         */
        MyCallsMenuFragmet oMyCallsMenuFragmet = new MyCallsMenuFragmet();
        oMyCallsMenuFragmet.setLogin(oLoginAccount);
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.main_active_layout, oMyCallsMenuFragmet)
                .addToBackStack("fragmentStack")
                .commit();

        loadMenuDrawer();

    }

    public LoginAccount getoLoginAccount() {
        return oLoginAccount;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();

        int size = fragmentManager.getBackStackEntryCount();
        if (size > 1) {

            /*
         * Временный выбор мои вызовы
         */
            fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

            MyCallsMenuFragmet oMyCallsMenuFragmet = new MyCallsMenuFragmet();
            oMyCallsMenuFragmet.setLogin(getoLoginAccount());
            fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.main_active_layout, oMyCallsMenuFragmet)
                    .addToBackStack("fragmentStack")
                    .commit();
        } else {
            finish();
        }
    }

    private void onPushExite() {
        Intent oIntentStartLauncherActivity = new Intent(this, LauncherActivity.class);
        startActivity(oIntentStartLauncherActivity);
        this.finish();
    }

    private void getAllViewObject() {
        // vpMyCalls = (ViewPager) findViewById(R.id.viewpager);
        bExite = (Button) findViewById(R.id.exite_button);
        tvUserName = (TextView) findViewById(R.id.user_name);
        tvNameDepartment = (TextView) findViewById(R.id.name_departament);
        dlSlideMenu = (DrawerLayout) findViewById(R.id.drawer_layout);
        flMainActiveLayout = (FrameLayout) findViewById(R.id.main_active_layout);
        flCommonMainLayout = (FrameLayout) findViewById(R.id.common_home_layout);
        llHomeToolBar = (LinearLayout) findViewById(R.id.home_toolbar);
        tvNameSelectedMenu = (TextView) findViewById(R.id.selected_menu_header);

        route_menu_button = (ImageButton) findViewById(R.id.route_menu_button);
    }


    private void loadMainInformation(LoginAccount oLoginAccount) {

        this.oLoginAccount = oLoginAccount;
        tvUserName.setText(oLoginAccount.getsValueName());
        tvNameDepartment.setText(oLoginAccount.getsValueDepName());
    }

    /**
     * Загрузка боковой меню боковой панели
     */
    private void loadMenuDrawer() {
        final MenuItem oMenuItemList[] = new MenuItem[]
                {
                        new MenuItem(R.drawable.my_calls_menu, getResources().getString(R.string.section_my_calls)),
                        new MenuItem(R.drawable.emk_search, getResources().getString(R.string.section_emk)),
                        new MenuItem(R.drawable.docs, getResources().getString(R.string.section_documentation)),
                        new MenuItem(R.drawable.drugs, getResources().getString(R.string.section_rls)),
                        new MenuItem(R.drawable.medhelp, getResources().getString(R.string.section_health_care)),
                        new MenuItem(R.drawable.write, getResources().getString(R.string.section_arrange_to_visit)),
                        new MenuItem(R.drawable.reanim, getResources().getString(R.string.section_reanimation)),
                        new MenuItem(R.drawable.danger, getResources().getString(R.string.section_danger)),
                        new MenuItem(R.drawable.map, getResources().getString(R.string.section_pharmacies))
                };

        AdapterMenuItem adapter = new AdapterMenuItem(this,
                R.layout.element_of_list_drawer, oMenuItemList);

        lvListDrawer = (ListView) findViewById(R.id.left_drawer);
        lvListDrawer.addHeaderView(createHeader());
        lvListDrawer.addFooterView(createFooter());
        lvListDrawer.setAdapter(adapter);

        //Типо выбрали мои вызовы =)
        tvNameSelectedMenu.setText(oMenuItemList[0].getTitle());

        lvListDrawer.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Bundle bundle = new Bundle();

                if(position > 0){
                    tvNameSelectedMenu.setText(oMenuItemList[position - 1].getTitle());

                    if ((position - 1) == MY_CALLS_MODE) {

                        MyCallsMenuFragmet oMyCallsMenuFragmet = new MyCallsMenuFragmet();
                        oMyCallsMenuFragmet.setLogin(oLoginAccount);
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.main_active_layout, oMyCallsMenuFragmet)
                                .addToBackStack("fragmentStack")
                                .commit();

                    } else if ((position - 1) == FIND_EMK || (position - 1) == BOOKING_VISIT) {

                        if((position - 1) == FIND_EMK){
                            bundle.putInt(KEY_FOR_GET_MODE, FIND_EMK);
                        }else{
                            bundle.putInt(KEY_FOR_GET_MODE, BOOKING_VISIT);
                        }

                        FindEmkFragment oFindEmkFragment = new FindEmkFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();

                        oFindEmkFragment.setoLoginAccount(getoLoginAccount());

                        oFindEmkFragment.setArguments(bundle);

                        fragmentManager.beginTransaction()
                                .replace(R.id.main_active_layout, oFindEmkFragment)
                                .addToBackStack("fragmentStack")
                                .commit();

                    }
//                    else  if ((position - 1) == BOOKING_VISIT) {
//
//                        bundle.putInt(KEY_FOR_GET_MODE, BOOKING_VISIT);
//
//                        AppointmentFragment oAppointmentFragment = new AppointmentFragment();
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//
//                        oAppointmentFragment.setArguments(bundle);
//
//                        fragmentManager.beginTransaction()
//                                .replace(R.id.main_active_layout, oAppointmentFragment)
//                                .addToBackStack("fragmentStack")
//                                .commit();
//
//
//
//                    }
                }else if(position == 0){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            dlSlideMenu.closeDrawers();
                        }
                    });
                }
            }
        });

        dlSlideMenu.setDrawerListener(new RightMenuListener(dlSlideMenu));


        route_menu_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        dlSlideMenu.openDrawer(lvListDrawer);
                    }
                });
            }
        });


//        dlSlideMenu.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View view, MotionEvent e) {
//                flCommonMainLayout.bringChildToFront(dlSlideMenu);
//                return false;
//            }
//        });

    }

    /**
     * Cоздание (Заголовка)
     *
     * @return
     */
    View createHeader() {
        View oLocalView = getLayoutInflater().inflate(R.layout.header_drawer, null);

        ImageView ivMenu = (ImageView) oLocalView.findViewById(R.id.iv_menu_item);

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tets", "sdcdcsc");
                //dlSlideMenu.openDrawer(1);
            }
        });

        return oLocalView;
    }

    @Override
    protected void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(oBroadcastReceiver,
                new IntentFilter(BROADCAST_ACTION_TAKING_INFORMATION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(oBroadcastReceiver);
        super.onPause();
    }

    // создание Footer (Логотипа)
    View createFooter() {
        View v = getLayoutInflater().inflate(R.layout.footer_drawer, null);
        return v;
    }


    private class RightMenuListener implements android.support.v4.widget.DrawerLayout.DrawerListener {

        private DrawerLayout drawer;

        public RightMenuListener(DrawerLayout drawer) {
            this.drawer = drawer;
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            Log.d("TEST", "onDrawerSlide " + slideOffset);

            if (slideOffset > 0.2) {
                flCommonMainLayout.bringChildToFront(dlSlideMenu);
                dlSlideMenu.bringToFront();
            } else {
                flCommonMainLayout.bringChildToFront(llHomeToolBar);
                llHomeToolBar.bringToFront();
            }
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            Log.d("TEST", "onDrawerOpened");
        }

        @Override
        public void onDrawerClosed(View view) {
            //drawer.setVisibility(View.INVISIBLE);
            Log.d("TEST", "onDrawerClosed");
            // flMainActiveLayout.bringToFront();
            //flMainActiveLayout.bringToFront();
        }

        @Override
        public void onDrawerStateChanged(int newState) {
            Log.d("TEST", "  " + newState);

        }

    }

    public void do_open_settings(android.view.MenuItem item){

        Intent oIntentStartHomeActivity = new Intent(this, AppPreferenceActivity.class);
        startActivity(oIntentStartHomeActivity);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);

        return super.onCreateOptionsMenu(menu);
    }
//
//    private void registerBroadcastReceiver(){
//
//        /*
//            Добавляем интент фильтр
//         */
//        IntentFilter oIntentFilter = new IntentFilter(BROADCAST_ACTION_TAKING_INFORMATION);
//        /*
//            Регестрируем наш приемник
//         */
//
//        registerReceiver(oBroadcastReceiver, oIntentFilter);
//
//    }


}
