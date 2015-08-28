package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAsyncTask;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 22.04.2015.
 * Фрагмент для отображения фрагмента
 */
public class LoginFragment extends Fragment implements ILoginEnableAccess {

    public static String LOG_TAG = "LoginFragment";

    /**
     * Кнопка входа
     */
    private Button bButtonLogin;
    /**
     * Поля ввода
     */
    private EditText eLoginName,ePassword;
    private FrameLayout fFrameLayout;

    //* Переменные для работы с сервисом
    private ServiceConnection sConnectionChecking;
    private Intent intentCheckingData;
    private Boolean bConnectToService;
    private ServiceLoading oServiceLoading;

    /*
     * Интерфейс для callBack о завершении загрузки
     */
    private  ILoginEnableAccess iLoginEnableAccess;

    /*
     * Количество запросов отправленых к серверу
     */
    private static Integer iCountRequestToServer;

    private ProgressDialog nDialog;

    private LoginAccount oLoginAccount;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        iCountRequestToServer = new Integer(0);

        /**
         * Копируем текущий клас в интерфейс
         */
        iLoginEnableAccess = this;

        intentCheckingData = new Intent(getActivity(), ServiceLoading.class);

        sConnectionChecking = new ServiceConnection() {

            public void onServiceConnected(ComponentName name, IBinder binder) {
                oServiceLoading = ((ServiceLoading.ServiceBinder) binder).getService();
                bConnectToService = true;
            }

            public void onServiceDisconnected(ComponentName name) {
                bConnectToService = false;
            }
        };
    }


    @Override
    public void onStart() {
        super.onStart();
        getActivity().bindService(intentCheckingData, sConnectionChecking, getActivity().BIND_AUTO_CREATE);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());

        eLoginName.setEnabled(!prefs.getBoolean("switch_state_login", false));

    }

    @Override
    public void onStop() {
        super.onStop();

        if(bConnectToService != null){
            if (!bConnectToService){
                return;
            }else{
                if(getActivity()!= null){
                    getActivity().unbindService(sConnectionChecking);
                    bConnectToService = false;
                }
            }
        }else{
            return;
        }

    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.authentication_fragment, null);

        getUIComponents(oView);

        return oView;
    }

    public LoginAccount getoLoginAccount() {
        return oLoginAccount;
    }

    public void setoLoginAccount(LoginAccount oLoginAccount) {
        this.oLoginAccount = oLoginAccount;
    }

    public FrameLayout getfFrameLayout() {
        return fFrameLayout;
    }

    private void getUIComponents(View oView){
        //Инициализация кнопки
        bButtonLogin = (Button) oView.findViewById(R.id.authentication_login_btn);
        bButtonLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tryLogin();
               // Intent oIntentStartHomeActivity = new Intent(getActivity(), AppPreferenceActivity.class);
               // startActivity(oIntentStartHomeActivity);
            }
        });

        eLoginName = (EditText) oView.findViewById(R.id.authentication_login);
        ePassword = (EditText) oView.findViewById(R.id.authentication_password);
        fFrameLayout = (FrameLayout) oView.findViewById(R.id.authentication_progress_bar);

    }
    /**
     * Пытаемся войти по нажатию кнопки
     */
    private void tryLogin(){
        String sValueLogin = "", sValuePassword = "";

        if(eLoginName != null){

            if(eLoginName.isEnabled()){
                sValueLogin = eLoginName.getText().toString().trim();

                if(TextUtils.isEmpty(sValueLogin)){
                    Toast.makeText(getActivity(), R.string.message_you_need_to_input_login, Toast.LENGTH_LONG).show();
                    eLoginName.setHint(getActivity().getString(R.string.wrong_password));
                    eLoginName.setHintTextColor(getResources().getColor(R.color.color_error_enter));
                }
            }else{
                sValueLogin = null;
            }

        }

        if(ePassword != null){
            sValuePassword = ePassword.getText().toString().trim();

            if(TextUtils.isEmpty(sValuePassword)){
                Toast.makeText(getActivity(), R.string.message_you_need_to_input_password, Toast.LENGTH_LONG).show();
                ePassword.setHint(getActivity().getString(R.string.wrong_password));
                ePassword.setHintTextColor(getResources().getColor(R.color.color_error_enter));
            }
        }

     //   new LoginAsyncTask(getfFrameLayout()).execute("mobdoctest","6479");

        LoginAsyncTask loginAsyncTask = new LoginAsyncTask(getfFrameLayout(),this);
        loginAsyncTask.execute(sValueLogin, sValuePassword);

    }

    private void checkDataBase(final LoginAccount oLoginAccount){

        if(getActivity()!= null){

            boolean bEnableAccess;

            synchronized (iCountRequestToServer){

                if(iCountRequestToServer > 0){
                    bEnableAccess = false;
                }else{
                    bEnableAccess = true;
                }
            }

            if(!bEnableAccess){

                Toast.makeText(getActivity(), getActivity().getString(R.string.loading_not_ready), Toast.LENGTH_LONG).show();

            }else {

                if (oServiceLoading.checkDataBase()) {
                    new AlertDialog.Builder(getActivity())
                            .setTitle(getActivity().getString(R.string.update_data_base))
                            .setMessage(getActivity().getString(R.string.old_data_base))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    synchronized (iCountRequestToServer) {
                                        iCountRequestToServer = oServiceLoading.startFirstLoadDataBase(oLoginAccount, iLoginEnableAccess);
                                    }

                                    nDialog = new ProgressDialog(getActivity());
                                    nDialog.setMessage("Загрузка..");
                                    nDialog.setTitle("Обновление данных");

                                    synchronized (iCountRequestToServer) {
                                        nDialog.setMax(iCountRequestToServer);
                                    }

                                    nDialog.setIndeterminate(false);
                                    nDialog.setCancelable(true);
                                    nDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    nDialog.show();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    Toast.makeText(getActivity(), getResources().getString(R.string.work_unvailable), Toast.LENGTH_LONG).show();

                                   // oServiceLoading.deleteDataBase();

                                }
                            })
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                } else {

                    /**
                     * Обновлять нет необходимости, предоставляем доступ.
                     */
                    Intent oIntentStartHomeActivity = new Intent(getActivity(), HomeActivity.class);
                    oIntentStartHomeActivity.putExtra(LoginAccount.class.getCanonicalName(), oLoginAccount);
                    startActivity(oIntentStartHomeActivity);
                    getActivity().finish();

                }
            }



        }
    }

    //todo Сделать запись в preference
    public void resultLogin(LoginAccount oLoginAccount){

        setoLoginAccount(oLoginAccount);

        if(getoLoginAccount() != null){
              /*
               Времено
              */
              if(getoLoginAccount().getbStatus()){

                    checkDataBase(getoLoginAccount());

              }else{
                  if(getActivity()!= null){
                      Toast.makeText(getActivity(), getoLoginAccount().getsErrorMessage(), Toast.LENGTH_LONG).show();
                      ePassword.setText("");
                      shakeButtonLogin();
                  }
              }
        }else{

            if(isConnected()){
                Toast.makeText(getActivity(), "Не удалось авторизоваться", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(getActivity(), "Соединение отсутствует", Toast.LENGTH_SHORT).show();
            }

            shakeButtonLogin();

        }

    }

    private void shakeButtonLogin(){
        Animation shake = AnimationUtils.loadAnimation(getActivity(), R.animator.shake);
        bButtonLogin.startAnimation(shake);
    }

    public boolean isConnected(){


        if( getActivity()!= null){
            ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isConnected()){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    @Override
    public void enableAccessLoadFinished(LoginAccount oLoginAccount) {

        synchronized(iCountRequestToServer){

        iCountRequestToServer--;

            if(getActivity()!= null){
                if(nDialog != null){
                    nDialog.incrementProgressBy(1);
                    //Говорит о том что мы все скачали и доступ разрешен
                    if(iCountRequestToServer == 0){

                        if(oLoginAccount!= null){
                            //Запускаем домашнее активити
                            Intent oIntentStartHomeActivity = new Intent(getActivity(), HomeActivity.class);
                            oIntentStartHomeActivity.putExtra(LoginAccount.class.getCanonicalName(), oLoginAccount);
                            startActivity(oIntentStartHomeActivity);
                            nDialog.dismiss();
                            getActivity().finish();

                        }else{
                            nDialog.dismiss();
                        }

                    }
                }
            }
        }

    }
}


