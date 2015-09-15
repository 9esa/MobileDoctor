package mrmv.ariadna.reshenie.spb.ru.mrmv.activites;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.PersistableBundle;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Observable;
import java.util.Observer;

import mrmv.ariadna.reshenie.spb.ru.mrmv.BuildConfig;
import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.AutoUpdateApk;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.LoginFragment;

/**
 * Created by kirichenko on 22.04.2015.
 */
public class LauncherActivity extends Activity implements Observer {

    final String LOG_TAG = "LauncherActivity";

    /*
           Проверка, что данный девайс таблетка
    */
    private boolean isDeviceTablet() {
        return BuildConfig.DEBUG
                || getResources().getConfiguration().smallestScreenWidthDp >= 600;
    }


    @SuppressWarnings("unused")
    private AutoUpdateApk aua;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        aua = new AutoUpdateApk(getApplicationContext());	// <-- don't forget to instantiate

        aua.addObserver(this);	// see the remark below, next to update() method



        loadPreference();

        Log.d(LOG_TAG, "MainActivity onCreate");

        /*
            Сперва проверка
         */
        if (!isDeviceTablet()) {
            setContentView(R.layout.small_screen_warning);
        } else {
            setContentView(R.layout.combining_space);

            LoginFragment oLoginFragment = new LoginFragment();
            android.app.FragmentManager fragmentManager = getFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.space_for_frame, oLoginFragment,"loginFragment")
                    .addToBackStack("fragmentStack")
                    .commit();

            if(isConnected()){
                Toast.makeText(this, "Соединение установлено", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this, "Соединение отсутствует", Toast.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Загружаем настройки нашего приложения
     */
    private void loadPreference(){

        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putLong(getApplication().getString(R.string.current_value_date), Calendar.getInstance().getTime().getTime());
        editor.commit();

    }

    public boolean isConnected(){

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }else{
            return false;
        }

    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    public void openSetting(View view){
        Intent oIntentStartHomeActivity = new Intent(getBaseContext(), AppPreferenceActivity.class);
        startActivity(oIntentStartHomeActivity);
    }


    public void do_open_settings(MenuItem item){

        Intent oIntentStartHomeActivity = new Intent(this, AppPreferenceActivity.class);
        startActivity(oIntentStartHomeActivity);

    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_login, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void update(Observable observable, Object data) {
        if( ((String)data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_GOT_UPDATE) ) {
            android.util.Log.i("AutoUpdateApkActivity", "Have just received update!");
        }
        if( ((String)data).equalsIgnoreCase(AutoUpdateApk.AUTOUPDATE_HAVE_UPDATE) ) {
            android.util.Log.i("AutoUpdateApkActivity", "There's an update available!");
        }
    }
}
