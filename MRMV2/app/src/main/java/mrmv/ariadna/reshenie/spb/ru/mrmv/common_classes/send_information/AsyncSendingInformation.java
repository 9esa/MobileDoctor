package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;


/**
 * Created by kirichenko on 05.05.2015.
 * Класс который отправляет информацию на сервер
 */
public class AsyncSendingInformation extends Thread implements Runnable {

    private static String LOG_TAG = "ASYNC_SENDING_INFO";

    private String sRequest;
    private String sCurrentToken;

    private HashMap<String, String> sMapOfParameters;

    private JSONArray jsonArrayToSend;

    //Id объекта который будет возвращен.
    private Object oIdentificatieObject;
    private JSONArray arrays;

    private ICallbackForSendingMainInformation iCallbackForSendingMainInformation;

    public AsyncSendingInformation(ICallbackForSendingMainInformation iCallbackForSendingMainInformation, Object oIdentificatieObject) {
        this.iCallbackForSendingMainInformation = iCallbackForSendingMainInformation;
        this.oIdentificatieObject = oIdentificatieObject;
    }


    @Override
    public void run() {

        JSONObject oJsonObject = null;
        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(sRequest);

            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setHeader("Authorization", "TOKEN " + sCurrentToken);

            if(sMapOfParameters != null){
                setObjectForSend(httpPost);
            }else if (jsonArrayToSend != null){
                setMapForSend(httpPost);
            }

            HttpResponse httpResponse = httpclient.execute(httpPost);
            String sResponseJson = EntityUtils.toString(httpResponse.getEntity());
            oJsonObject = new JSONObject(sResponseJson);

        } catch (Exception e) {
            Log.d(LOG_TAG, "Error in reading response");
        }

        if (iCallbackForSendingMainInformation != null) {
            iCallbackForSendingMainInformation.getLoadedInformation(oJsonObject, oIdentificatieObject);
        }

    }

    private void setMapForSend(HttpPost httpPost) {
        try {
            if(jsonArrayToSend != null){
                httpPost.setEntity(new StringEntity(jsonArrayToSend.toString(), HTTP.UTF_8));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void setObjectForSend(HttpPost httpPost) {
        JSONObject oJsonObject = new JSONObject();

        if (sMapOfParameters != null) {
            if (!sMapOfParameters.isEmpty()) {

                for (String sValue : sMapOfParameters.keySet()) {
                    try {
                        oJsonObject.accumulate(sValue,sMapOfParameters.get(sValue));
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                }

                try {
                    httpPost.setEntity(new StringEntity(oJsonObject.toString(), HTTP.UTF_8));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                    return;
                }

            }
        }

    }

    public void setsCurrentToken(String sCurrentToken) {
        this.sCurrentToken = sCurrentToken;
    }

    public void setsRequest(String sRequest) {
        this.sRequest = sRequest;
    }

    public void setMaps(HashMap<String, String> maps) {
        this.sMapOfParameters = maps;
    }

    public void setArrays(JSONArray jsonArrayToSend) {
        this.jsonArrayToSend = jsonArrayToSend;
    }
}
