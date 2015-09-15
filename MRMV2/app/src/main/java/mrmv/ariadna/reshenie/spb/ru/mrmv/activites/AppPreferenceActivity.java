package mrmv.ariadna.reshenie.spb.ru.mrmv.activites;

import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 05.05.2015.
 */
public class AppPreferenceActivity extends PreferenceActivity implements OnSharedPreferenceChangeListener, ILoginEnableAccess {

    public static final String LOG_TAG = "AppPreferenceActivity";

    private Preference btnSync, btnUpdate;

    private EditTextPreference edtTimeOut, edtMaxValuePacient;

    private EditTextPreference edtAddressServers;

    private ProgressDialog nDialog;

    /*
     * Интерфейс для callBack о завершении загрузки
     */
    private  ILoginEnableAccess iLoginEnableAccess;

    //* Переменные для работы с сервисом
    private ServiceConnection sConnectionChecking;
    private Intent intentCheckingData;
    private Boolean bConnectToService = false;
    private ServiceLoading oServiceLoading;

    private int iCountRequestToServer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        btnSync = (Preference) findPreference(getResources().getString(R.string.update_guides_button));

        btnSync.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {

                updateAllGuides();

                return true;
            }
        });

        btnUpdate  = (Preference) findPreference(getResources().getString(R.string.information_about_version));

        btnUpdate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference arg0) {

                //updateApk();

                return true;
            }
        });

        edtAddressServers = (EditTextPreference)findPreference(getResources().getString(R.string.address_connection));
        edtAddressServers.setSummary(edtAddressServers.getText());

        edtTimeOut = (EditTextPreference)findPreference(getResources().getString(R.string.timeout_for_update_guides));
        edtMaxValuePacient = (EditTextPreference) findPreference(getResources().getString(R.string.max_value_for_view_users));

        edtMaxValuePacient.setSummary(edtMaxValuePacient.getText());
        edtTimeOut.setSummary(edtTimeOut.getText() + " мин.");


        /**
         * Копируем текущий клас в интерфейс
         */
        iLoginEnableAccess = this;

        intentCheckingData = new Intent(getApplication(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                bConnectToService = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToService = false;
            }
        };

        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            String sVersion = pInfo.versionName;

            btnSync = (Preference) findPreference(getResources().getString(R.string.information_about_version));
            btnSync.setSummary(sVersion);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        getApplication().bindService(intentCheckingData, sConnectionChecking, getApplication().BIND_AUTO_CREATE);

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (!bConnectToService){
            return;
        }else{
            getApplication().unbindService(sConnectionChecking);
            bConnectToService = false;
        }

    }

    private void updateAllGuides(){
        if(bConnectToService){

            iCountRequestToServer =  oServiceLoading.startUpdateGuides(iLoginEnableAccess);

            nDialog = new ProgressDialog(this);
            nDialog.setMessage("Загрузка..");
            nDialog.setTitle(getString(R.string.update_data));
            nDialog.setMax(iCountRequestToServer);
            nDialog.setIndeterminate(false);
            nDialog.setCancelable(true);
            nDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            nDialog.show();

        }else{
            Toast.makeText(getApplication(), getString(R.string.sorry_service_unavailable), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String sKeyValue) {

        if (sKeyValue.equals(getResources().getString(R.string.timeout_for_update_guides))) {
            edtTimeOut.setSummary(edtTimeOut.getText() + " мин.");

        }else if(sKeyValue.equals(getResources().getString(R.string.max_value_for_view_users))){
            edtMaxValuePacient.setSummary(edtMaxValuePacient.getText());

        }else if(sKeyValue.equals(getResources().getString(R.string.address_connection))){
            edtAddressServers.setSummary(edtAddressServers.getText());

            if(bConnectToService){
                oServiceLoading.deleteDataBase();
            }

        }

    }

    public void updateApk(){

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        final String[] sAddress = new String[1];

        if (sharedPreferences != null) {
            sAddress[0] = sharedPreferences.getString("address_connection", "");
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getApplicationContext(),getString(R.string.error_set_address_update), Toast.LENGTH_LONG).show();
                }
            });
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    String sValue = "";

                    if(sAddress [0] != null){
                        if(!sAddress[0].isEmpty()){
                            sValue = sAddress[0].substring(0,sAddress[0].indexOf(":"));
                        }
                    }

                    URL url = new URL("ftp://" + sValue + "/apk/MobileDoctor.apk");

                    URLConnection conn = url.openConnection();

                    String PATH = Environment.getExternalStorageDirectory() + "/download/";
                    File file = new File(PATH);
                    file.mkdirs();
                    File outputFile = new File(file, "MobileDoctor.apk");
                    FileOutputStream fos = new FileOutputStream(outputFile);

                    InputStream is = conn.getInputStream();

                    byte[] buffer = new byte[1024];
                    int len1 = 0;
                    while ((len1 = is.read(buffer)) != -1) {
                        fos.write(buffer, 0, len1);
                    }
                    fos.close();
                    is.close();

                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + "MobileDoctor.apk")), "application/vnd.android.package-archive");
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } catch (IOException e) {

                    runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(), getString(R.string.error_update_info), Toast.LENGTH_LONG).show();
                            }
                    });

                }
            }
        }).start();
    }


    @Override
    protected void onResume() {
        super.onResume();

        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void enableAccessLoadFinished(LoginAccount oLoginAccount) {
        iCountRequestToServer--;

        if(nDialog != null){
            nDialog.incrementProgressBy(1);
        }

        //Говорит о том что мы все скачали и доступ разрешен
        if(iCountRequestToServer == 0){
                nDialog.dismiss();
        }
    }
}
