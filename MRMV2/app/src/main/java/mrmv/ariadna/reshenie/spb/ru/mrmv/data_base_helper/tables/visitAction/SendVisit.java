package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONObject;

import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.AsyncSendingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.ICallbackForSendingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;

/**
 * Created by kirichenko on 05.05.2015.
 */
public class SendVisit extends CommonMainLoading implements ICallbackForSendingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    public SendVisit(Context context, LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = context;
    }

    public void startSend(String sValue){

        Cursor cursor = Visit.getInformationAboutVisit(sqlHelper, sValue);
        HashMap <String, String> sValuesMap = new HashMap<String, String>();

        if (cursor.moveToFirst()) {
            do {

                sValuesMap.put(Visit.VISITID,isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.VISITID))));
                sValuesMap.put(Visit.VISITTYPEID,isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.VISITTYPEID))));
                sValuesMap.put(Visit.VISITPLACEID,isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.VISITPLACEID))));
                sValuesMap.put(Visit.VISTYPEID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.VISTYPEID))));
                sValuesMap.put(Visit.VISITPROFID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.VISITPROFID))));
                sValuesMap.put(Visit.CASEID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.CASEID))));
                sValuesMap.put(Visit.CASEFINALITYID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.CASEFINALITYID))));
                sValuesMap.put(Visit.CASEOUTCOMEID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.CASEOUTCOMEID))));
                sValuesMap.put(Visit.CASERESULTID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Visit.CASERESULTID))));
                sValuesMap.put(Visit.MESID, null);
                sValuesMap.put(Visit.MESOPENDATE, null);

            } while (cursor.moveToNext());
        }

      //  cursor.close();

        String sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/" + sValue + "/info";

        AsyncSendingInformation sendingInfromation = new AsyncSendingInformation(this, sValue);
        sendingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        sendingInfromation.setsRequest(sValueAddressForRequest);
        sendingInfromation.setMaps(sValuesMap);

        sendingInfromation.setDaemon(true);
        sendingInfromation.start();


    }

    private String isNotValueNull(String sValue){

        if(sValue != null){
            if(sValue.equals("null")){
                return null;
            }else if(sValue.isEmpty()) {
                return null;
            }else{
                return sValue;
            }
        }else{
            return sValue;
        }

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object oIdentifyObject) {

            if(mContext != null){
                CommonMainLoading.converJsonToList(mContext, jsonObject);
                Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.save_info_about_visit));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }

    }
}
