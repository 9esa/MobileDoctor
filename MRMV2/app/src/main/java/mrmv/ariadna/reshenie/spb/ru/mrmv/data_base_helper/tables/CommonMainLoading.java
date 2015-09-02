package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;

/**
 * Created by kirichenko on 07.05.2015.
 */
public class CommonMainLoading {

    public static ArrayList<JSONObject> converJsonToList(Context mContext, JSONObject oJsonObject){

        ArrayList <JSONObject> arrayJSONObjects = new ArrayList<JSONObject>();

        if (Boolean.valueOf(oJsonObject != null)) {
            try {
                Boolean bResultRequest = new Boolean(String.valueOf(oJsonObject.get("success")));

                if (Boolean.valueOf(oJsonObject.get("data") != null)) {

                    if (oJsonObject instanceof JSONObject) {

                        if (bResultRequest) {

                            if( oJsonObject.get("data") instanceof JSONArray){

                                JSONArray oJsonObjectData = (JSONArray) oJsonObject.get("data");

                                for (int iCount = 0; iCount < oJsonObjectData.length(); iCount++) {

                                    Object oGuides = oJsonObjectData.get(iCount);

                                    if(oGuides != null){

                                        if(oGuides instanceof JSONObject){
                                            JSONObject oGuidesJSON = (JSONObject) oGuides;
                                            arrayJSONObjects.add(oGuidesJSON);

                                        }
                                    }
                                }
                            }else if(oJsonObject.get("data") instanceof JSONObject){
                                JSONObject oJsonObjectData = (JSONObject) oJsonObject.get("data");

                                arrayJSONObjects.add(oJsonObjectData);

                            }

                        } else {
                            //Toast.makeText(, "Не удалось авторизоваться", Toast.LENGTH_SHORT).show();
                            if(mContext != null){

                                if( oJsonObject.get("data") instanceof JSONObject) {

                                    JSONObject oJsonObjectData = (JSONObject) oJsonObject.get("data");

                                        if (oJsonObjectData != null) {

                                                String sErrorCode = String.valueOf(oJsonObjectData.get("errorCode"));

                                                String sErrorMessage = String.valueOf(oJsonObjectData.get("errorMsg"));

                                                if(mContext != null){
                                                    Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                                                    intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, sErrorMessage);
                                                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                                                }
                                            }
                                        }
                            }

                        }
                    }
                }

                if (Boolean.valueOf(oJsonObject.get("msg") != null)) {

                    if (oJsonObject instanceof JSONObject) {
                            if (oJsonObject.get("msg") instanceof JSONObject) {
                                JSONObject oGuidesJSON = (JSONObject) oJsonObject.get("msg");

                                Object oGuides = oGuidesJSON.get("status");

                                String sValue = oGuides.toString();

                                String sConstant = "LIMIT_EXCEEDED";

                            }
                    }
                }


            } catch (JSONException e) {
                // return null;
            }
        }

        return arrayJSONObjects;

    }

}
