package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

/**
 * Created by kirichenko on 29.04.2015.
 * Каждый класс который загружает информацию в background
 */
//todo Сделать проверку на пустой token
public class AsyncLoadingInformation extends Thread implements Runnable {

    private static String LOG_TAG = "ASYNC_LOADING_INFO";
    private String sRequest;
    private ICallbackForLoadingMainInformation callBackWithInformation;
    private String sCurrentToken;

    //Id объекта который будет возвращен.
    private Object iIdentificatieNummer;

    public AsyncLoadingInformation(String sRequest, ICallbackForLoadingMainInformation callBackWithInformation, Object iIdentificatieNummer) {
        this.callBackWithInformation = callBackWithInformation;
        this.sRequest = sRequest;
        this.iIdentificatieNummer = iIdentificatieNummer;
    }

    public AsyncLoadingInformation(String sRequest, ICallbackForLoadingMainInformation callBackWithInformation) {
        this(sRequest, callBackWithInformation, 0);
    }

    public void setsCurrentToken(String sCurrentToken) {
        this.sCurrentToken = sCurrentToken;
    }

    public void setsRequest(String sRequest) {
        this.sRequest = sRequest;
    }

    @Override
    public void run() {

        JSONObject oJsonObject = null;

        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(sRequest);

            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            httpGet.setHeader("Authorization", "TOKEN " + sCurrentToken);

            HttpResponse httpResponse = httpclient.execute(httpGet);

            String sResponseJson = EntityUtils.toString(httpResponse.getEntity());

            oJsonObject = new JSONObject(sResponseJson);

        } catch (Exception e) {
            Log.d(LOG_TAG, "Error in reading response");
        }

        if (callBackWithInformation != null) {
            callBackWithInformation.getLoadedInformation(oJsonObject, iIdentificatieNummer);
        }

    }
}
