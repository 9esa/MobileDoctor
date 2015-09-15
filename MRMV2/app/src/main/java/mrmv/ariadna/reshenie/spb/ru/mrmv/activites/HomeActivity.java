package mrmv.ariadna.reshenie.spb.ru.mrmv.activites;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.attention.AttentionFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.bookRLS.BookRLSFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.documentation.DocumentationFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.findEmk.FindEmkFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.MyCallsMenuFragmet;

/**
 * Created by kirichenko on 23.04.2015.
 */
public class HomeActivity extends FragmentActivity {

    private Button bExite;

    private TextView tvUserName;
    private TextView tvNameDepartment;
    private static TextView tvNameOfMenu;

    private static LoginAccount oLoginAccount;

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

    private Drawer.Result drawerResult = null;

    private static Toolbar toolbar;

    public BroadcastReceiver oBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast.makeText(context, intent.getStringExtra(MESSAGE_TO_VIEW), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        toolbar.setLogoDescription("Doctor");

        drawerResult = createCommonDrawer(HomeActivity.this, toolbar);
        drawerResult.setSelectionByIdentifier(1, false); // Set proper selection

        // Покажем drawer автоматически при запуске
        //drawerResult.openDrawer();

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


        MyCallsMenuFragmet oMyCallsMenuFragmet = new MyCallsMenuFragmet();
        oMyCallsMenuFragmet.setLogin(getoLoginAccount());
        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .replace(R.id.frame_container, oMyCallsMenuFragmet,"meCallsMenu")
                .addToBackStack("fragmentStack")
                .commit();

        tvNameOfMenu.setText(R.string.section_my_calls);
    }

    public static LoginAccount getoLoginAccount() {
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
                    .replace(R.id.frame_container, oMyCallsMenuFragmet)
                    .addToBackStack("fragmentStack")
                    .commit();
        } else {
            dialogConfirmClose();
        }
    }

    private void dialogConfirmClose(){
        AlertDialog.Builder alerDialog = new AlertDialog.Builder(this);
        alerDialog.setTitle("Подтверждение выхода");  // заголовок
        alerDialog.setMessage("Вы действительно хотите закрыть приложение?"); // сообщение

        alerDialog.setPositiveButton("Да", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                finish();
            }
        });

        alerDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        alerDialog.setCancelable(false);
        alerDialog.show();
    }

    private void onPushExite() {
        Intent oIntentStartLauncherActivity = new Intent(this, LauncherActivity.class);
        startActivity(oIntentStartLauncherActivity);
        this.finish();
    }

    private void getAllViewObject() {

        bExite = (Button) findViewById(R.id.exite_button);
        tvUserName = (TextView) findViewById(R.id.user_name);
        tvNameDepartment = (TextView) findViewById(R.id.name_departament);
        tvNameOfMenu = (TextView) findViewById(R.id.tvNameOfMenu);

    }


    private void loadMainInformation(LoginAccount oLoginAccount) {

        this.oLoginAccount = oLoginAccount;
        tvUserName.setText(oLoginAccount.getsValueName());
        tvNameDepartment.setText(oLoginAccount.getsValueDepName());
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

    public void do_open_settings(android.view.MenuItem item){

        Intent oIntentStartHomeActivity = new Intent(this, AppPreferenceActivity.class);
        startActivity(oIntentStartHomeActivity);
    }


    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            //
        }

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public static Drawer.OnDrawerItemClickListener handlerOnClick(final Drawer.Result drawerResult, final FragmentActivity activity) {
        return new Drawer.OnDrawerItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {

                if (drawerItem != null) {

                    if (drawerItem.getIdentifier() == 1) {
                        MyCallsMenuFragmet oMyCallsMenuFragmet = new MyCallsMenuFragmet();
                        oMyCallsMenuFragmet.setLogin(getoLoginAccount());
                        FragmentManager fragmentManager = activity. getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, oMyCallsMenuFragmet)
                                .addToBackStack("fragmentStack")
                                .commit();

                        tvNameOfMenu.setText(R.string.section_my_calls);
                        //  activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Fragment1()).commit();
                    } else if (drawerItem.getIdentifier() == 2) {
                        //    activity.getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, new Fragment2()).commit();
                        Bundle bundle = new Bundle();
                        bundle.putInt(KEY_FOR_GET_MODE, FIND_EMK);

                        FindEmkFragment oFindEmkFragment = new FindEmkFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();

                        oFindEmkFragment.setoLoginAccount(getoLoginAccount());

                        oFindEmkFragment.setArguments(bundle);

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, oFindEmkFragment)
                                .addToBackStack("fragmentStack")
                                .commit();


                        tvNameOfMenu.setText(R.string.section_emk);
                    } else if (drawerItem.getIdentifier() == 3) {

                        DocumentationFragment oAppointmentFragment = new DocumentationFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, oAppointmentFragment)
                                .addToBackStack("fragmentStack")
                                .commit();

                        tvNameOfMenu.setText(R.string.section_documentation);
                    } else if (drawerItem.getIdentifier() == 4) {

                        BookRLSFragment oBookRLSFragment = new BookRLSFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();
                        oBookRLSFragment.setoLoginAccount(getoLoginAccount());

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, oBookRLSFragment)
                                .addToBackStack("fragmentStack")
                                .commit();

                        tvNameOfMenu.setText(R.string.section_rls);
                    }else if (drawerItem.getIdentifier() == 5) {

                        Bundle bundle = new Bundle();
                        bundle.putInt(KEY_FOR_GET_MODE, BOOKING_VISIT);

                        FindEmkFragment oFindEmkFragment = new FindEmkFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();

                        oFindEmkFragment.setoLoginAccount(getoLoginAccount());

                        oFindEmkFragment.setArguments(bundle);

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, oFindEmkFragment)
                                .addToBackStack("fragmentStack")
                                .commit();

                        tvNameOfMenu.setText(R.string.section_arrange_to_visit);
                    }else if (drawerItem.getIdentifier() == 6) {

                        AttentionFragment oAttentionFragment = new AttentionFragment();
                        FragmentManager fragmentManager = activity.getSupportFragmentManager();

                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, oAttentionFragment)
                                .addToBackStack("fragmentStack")
                                .commit();

                        tvNameOfMenu.setText(R.string.section_danger);
                    }else if (drawerItem.getIdentifier() == 7) {

                        tvNameOfMenu.setText(R.string.section_pharmacies);
                    }
                }
            }
        };
    }

    public static Drawer.Result createCommonDrawer(final FragmentActivity activity, Toolbar toolbar) {

        Drawer.Result drawerResult = new Drawer()
                .withActivity(activity)
                .withHeader(R.layout.drawer_header)
                .withFooter(R.layout.footer_drawer)
                .withToolbar(toolbar)
                .addDrawerItems(

                        new PrimaryDrawerItem().withName(R.string.section_my_calls).withIcon(R.drawable.my_calls_menu_selected).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.section_emk).withIcon(R.drawable.emk_search_selected).withIdentifier(2),
                        new PrimaryDrawerItem().withName(R.string.section_documentation).withIcon(R.drawable.docs_selected).withIdentifier(3),
                        new PrimaryDrawerItem().withName(R.string.section_rls).withIcon(R.drawable.drugs_selected).withIdentifier(4),
                        new PrimaryDrawerItem().withName(R.string.section_arrange_to_visit).withIcon(R.drawable.write_selected).withIdentifier(5),
                        new PrimaryDrawerItem().withName(R.string.section_danger).withIcon(R.drawable.danger_selected).withIdentifier(6),
                        new PrimaryDrawerItem().withName(R.string.section_pharmacies).withIcon(R.drawable.map_selected).withIdentifier(7)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public boolean equals(Object o) {
                        return super.equals(o);
                    }

                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                        hideSoftKeyboard(activity);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        //Toast.makeText(MainActivity.this, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();


        drawerResult.setOnDrawerItemClickListener(handlerOnClick(drawerResult, activity));

        return drawerResult;
    }

}
