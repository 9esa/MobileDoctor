package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.deleting_information;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.ICallbackForSendingMainInformation;


/**
 * Created by kirichenko on 05.05.2015.
 * Класс который отправляет информацию на сервер
 */
public class AsyncDeletingInformation extends Thread implements Runnable {

    private static String LOG_TAG = "ASYNC_DELETING_INFO";

    private String sRequest;
    private String sCurrentToken;

    private HashMap<String, String> sMapOfParameters;

    private Object oIdentificatieObject;
    private ICallbackForSendingMainInformation iCallbackForSendingMainInformation;

    public AsyncDeletingInformation(ICallbackForSendingMainInformation iCallbackForSendingMainInformation, Object oIdentificatieObject) {
        this.iCallbackForSendingMainInformation = iCallbackForSendingMainInformation;
        this.oIdentificatieObject = oIdentificatieObject;
    }


    @Override
    public void run() {

        JSONObject oJsonObject = null;

        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpDelete httpDelete= new HttpDelete(sRequest);

            httpDelete.setHeader("Accept", "application/json");
            httpDelete.setHeader("Content-type", "application/json");
            httpDelete.setHeader("Authorization", "TOKEN " + sCurrentToken);

            HttpResponse httpResponse = httpclient.execute(httpDelete);

            String sResponseJson = EntityUtils.toString(httpResponse.getEntity());

            oJsonObject = new JSONObject(sResponseJson);

        } catch (Exception e) {
            Log.d(LOG_TAG, "Error in reading response");
        }

        if (iCallbackForSendingMainInformation != null) {
            iCallbackForSendingMainInformation.getLoadedInformation(oJsonObject, oIdentificatieObject);
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
}
