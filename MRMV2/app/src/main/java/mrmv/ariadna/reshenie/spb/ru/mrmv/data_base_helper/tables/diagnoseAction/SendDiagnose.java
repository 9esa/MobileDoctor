package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.deleting_information.AsyncDeletingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.AsyncSendingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.ICallbackForSendingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;

/**
 * Created by kirichenko on 09.05.2015.
 */
public class SendDiagnose extends CommonMainLoading implements ICallbackForSendingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    public SendDiagnose (Context mContext, LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
    }

    public void startSend(String sVisitId){

        Cursor cursor = Diagnose.getInformationAboutDiagnose(sqlHelper, sVisitId);

        ArrayList <HashMap<String, String>>listOfDiagnoses = new ArrayList();

        //HashMap<String, String> sValuesMap = new HashMap<String, String>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> sValuesMap = new HashMap<String, String>();
                sValuesMap.put(Diagnose.DIAGNOSEID,isNotValueNull( cursor.getString(cursor.getColumnIndex(Diagnose.DIAGNOSEID))));
                sValuesMap.put(Diagnose.VISITID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.VISITID))));
                sValuesMap.put(Diagnose.DIAGNOSTYPE,isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.DIAGNOSTYPE))));
                sValuesMap.put(Diagnose.DIAGNOSID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.DIAGNOSID))));
                sValuesMap.put(Diagnose.ILLTYPEID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.ILLTYPEID))));
                sValuesMap.put(Diagnose.WORSEID,isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.WORSEID))));
                sValuesMap.put(Diagnose.CRIMEID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.CRIMEID))));
                sValuesMap.put(Diagnose.STAGEID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.STAGEID))));
                sValuesMap.put(Diagnose.DISPID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.DISPID))));
                sValuesMap.put(Diagnose.STATING, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.STATING))));
                sValuesMap.put(Diagnose.DISPOFFID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.DISPOFFID))));
                sValuesMap.put(Diagnose.TRAVMAID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Diagnose.TRAVMAID))));
                listOfDiagnoses.add(sValuesMap);
            } while (cursor.moveToNext());

        }

        for(HashMap<String, String> itemMaps : listOfDiagnoses){
           String sValueStating =  itemMaps.get(Diagnose.STATING);

            if(sValueStating != null){

                itemMaps.remove(Diagnose.STATING);

                if(sValueStating.equals(Diagnose.NEW_DIAGNOS)){
                    String sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/" + sVisitId+ "/diagnos";
                    AsyncSendingInformation sendingInfromation = new AsyncSendingInformation(this, Diagnose.NEW_DIAGNOS);
                    sendingInfromation.setsCurrentToken(oLoginAccount.getsToken());
                    sendingInfromation.setsRequest(sValueAddressForRequest);
                    sendingInfromation.setMaps(itemMaps);
                    sendingInfromation.setDaemon(true);
                    sendingInfromation.start();
                }else if(sValueStating.equals(Diagnose.DELETE_DIAGNOS)){
                    String sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/" + sVisitId+ "/diagnos/" + itemMaps.get(Diagnose.DIAGNOSEID);
                    AsyncDeletingInformation deletingInfromation = new AsyncDeletingInformation(this,Diagnose.DELETE_DIAGNOS);
                    deletingInfromation.setsCurrentToken(oLoginAccount.getsToken());
                    deletingInfromation.setsRequest(sValueAddressForRequest);
                    deletingInfromation.setMaps(itemMaps);
                    deletingInfromation.setDaemon(true);
                    deletingInfromation.start();
                }
            }
        }

     //   Diagnose.updateAllDiagnosesAfterSend(sqlHelper);
          Diagnose.removeAllInfornationDiagnosesWithoutMain(sqlHelper);
//        String cleanRequest = "DELETE FROM " + Diagnose.TABLE_NAME;
//        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

    }

    private String isNotValueNull(String sValue){

       if(sValue != null){
           if(sValue.equals("null")){
               return null;
           }else if(sValue.isEmpty()){
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
        if(String.valueOf(oIdentifyObject).equals(Diagnose.NEW_DIAGNOS)){
            CommonMainLoading.converJsonToList(mContext, jsonObject);
        }else if(String.valueOf(oIdentifyObject).equals(Diagnose.DELETE_DIAGNOS)){
            CommonMainLoading.converJsonToList(mContext, jsonObject);
        }
    }
}
