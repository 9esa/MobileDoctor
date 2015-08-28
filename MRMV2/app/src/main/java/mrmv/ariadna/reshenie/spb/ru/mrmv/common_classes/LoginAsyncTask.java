package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.LoginFragment;

/**
 * Created by kirichenko on 22.04.2015.
 * Асинхронаая задача для логина
 * Возможно лучше вынести её в клас для загрузке обычных данных
 *
 */
public class LoginAsyncTask extends AsyncTask<String, Void, LoginAccount>{

    final String LOG_TAG = "LoginAsyncTask";

    private FrameLayout fFrameLayout;


    String sAddress;

    //Object for callBack
    private LoginFragment loginFragment;
    /*
     * Загрузка всех фреймов
     */
    public LoginAsyncTask(FrameLayout fFrameLayout, LoginFragment loginFragment) {
        this.fFrameLayout = fFrameLayout;
        this.loginFragment = loginFragment;

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(loginFragment.getActivity());

        sAddress = sharedPreferences.getString("address_connection", "");

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        /*
            Запускаем отоброажение прогресс бара
         */
        fFrameLayout.setVisibility(View.VISIBLE);
        fFrameLayout.bringToFront();
    }

    @Override
    protected LoginAccount doInBackground(String... params) {

        String sValueLogin = params[0];
        String sValuePassword = params[1];

        Log.d(LOG_TAG, "Try to grant access for " + sValueLogin + " via " + sValuePassword);

        if(sValueLogin == null){
            return tryAutorization("http://"+ sAddress + "/doctor-web/api/loginbypasswd", sValueLogin, sValuePassword);
        }else{
            return tryAutorization("http://"+ sAddress + "/doctor-web/api/login", sValueLogin, sValuePassword);
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);

    }

    @Override
    protected void onPostExecute(LoginAccount result) {
        super.onPostExecute(result);

        /*
            Закрываем отоброажение прогресс бара
         */
        fFrameLayout.setVisibility(View.GONE);
        loginFragment.resultLogin(result);

    }

    private LoginAccount tryAutorization(String url, String sValueLogin, String sValuePassword){

        String sJson = "";

        try {

            // 1. create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();

            if(sValueLogin != null){
                jsonObject.accumulate("userName", sValueLogin);
            }

            jsonObject.accumulate("passwd", sValuePassword);

            // 4. convert JSONObject to JSON to String
            sJson = jsonObject.toString();

            // 5. set json to StringEntity
            StringEntity oStringEntity = new StringEntity(sJson,HTTP.UTF_8);

            // 6. set httpPost Entity
            httpPost.setEntity(oStringEntity);

            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost);

            String sResponseJson = EntityUtils.toString(httpResponse.getEntity());

            JSONObject oJsonObject= new JSONObject(sResponseJson);

            return constructObjectFromJson (oJsonObject);

        } catch (Exception e) {
            Log.d(LOG_TAG, "Error in reading response");
        }

        return null;
    }

    private LoginAccount constructObjectFromJson(JSONObject oJsonObject) {

        if (oJsonObject != null) {
            try {
                Boolean bResultRequest = new Boolean(String.valueOf(oJsonObject.get("success")));

                if (oJsonObject.get("data") != null) {
                    if (oJsonObject instanceof JSONObject) {

                        JSONObject oJsonObjectData = (JSONObject) oJsonObject.get("data");

                        if (bResultRequest) {
                            String sDoctorId = String.valueOf(oJsonObjectData.get("doctorId"));
                            String sValueName = String.valueOf(oJsonObjectData.get("doctorName"));
                            String sValueDepName = String.valueOf(oJsonObjectData.get("depName"));
                            String sToken = String.valueOf(oJsonObjectData.get("token"));

                            return new LoginAccount(bResultRequest, sDoctorId, sValueName, sValueDepName, sToken);

                        } else {
                            String sErrorCode = String.valueOf(oJsonObjectData.get("errorCode"));
                            String sErrorMessage = String.valueOf(oJsonObjectData.get("errorMsg"));

                            return new LoginAccount(sErrorCode, sErrorMessage, bResultRequest);

                        }
                    }
                }

            } catch (JSONException e) {
                return null;
            }
        } else {
            return null;
        }

        return null;
    }

    private String convertInputStreamToString(InputStream inputStream) throws IOException {

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    private void connectToServer(){

        URL url = null;
        try {
//            url = new URL("https://reshenie-soft.ru:8443/doctor-web/api/login");
            url = new URL("https://10.0.0.210/doctor-web/api/login");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        HttpsURLConnection conn = null;
        try {
            conn = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a trust manager that does not validate certificate chains
        TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
            public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                return new java.security.cert.X509Certificate[] {};
            }

            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }

            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
        } };

        // Install the all-trusting trust manager
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //    conn.setHostnameVerifier(DO_NOT_VERIFY);


//
//        // Create the SSL connection
//        SSLContext sc;
//        try {
//            sc = SSLContext.getInstance("TLS");
//
//            try {
//                sc.init(null, null, new java.security.SecureRandom());
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//            }
//
//            conn.setSSLSocketFactory(sc.getSocketFactory());
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }

        // Use this if you need SSL authentication
//        String userpass = params[0] + ":" + params[1];
//        String basicAuth = "Basic " + Base64.encodeToString(userpass.getBytes(), Base64.DEFAULT);
//        conn.setRequestProperty("Authorization", basicAuth);

//
        // set Timeout and method
        conn.setReadTimeout(7000);
        conn.setConnectTimeout(7000);
        try {
            conn.setRequestMethod("POST");
        } catch (ProtocolException e) {
            e.printStackTrace();
        }
        conn.setDoInput(true);

        // Add any data you wish to post here

        try {
            conn.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        InputStream is = null;
        try {
            Log.d(LOG_TAG,conn.getContentEncoding());
            Log.d(LOG_TAG,conn.getResponseMessage());
            Log.d(LOG_TAG, String.valueOf(conn.getContent()));
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        if(is == null){
//            return null;
//        }

        BufferedReader in = new BufferedReader(new InputStreamReader(is));

        String sValue = "";

        String inputLine;
        try {
            while ((inputLine = in.readLine()) != null) {


                sValue += inputLine;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Log.d(LOG_TAG,sValue);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

    }

    // always verify the host - dont check for certificate
    final static HostnameVerifier DO_NOT_VERIFY = new HostnameVerifier() {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    };


    /*
                // 9. receive response as inputStream
//            inputStream = httpResponse.getEntity().getContent();
//
//            // 10. convert inputstream to string
//            if(inputStream != null){
//                result = convertInputStreamToString(inputStream);
//            }else{
//               // result = "Did not work!";
//            }

     */
}
