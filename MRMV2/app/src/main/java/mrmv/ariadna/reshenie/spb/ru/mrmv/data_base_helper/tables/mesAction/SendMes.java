package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction;

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
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;

/**
 * Created by kirichenko on 18.06.2015.
 */
public class SendMes extends CommonMainLoading implements ICallbackForSendingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    public SendMes (Context mContext, LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
    }

    public void startSend(String sVisitId){

       Cursor cursor = Mes.getInfoByVisit(sqlHelper, sVisitId);

       HashMap<String, String> mapOfMes = null;

       if (cursor.moveToFirst()) {
            do {

                mapOfMes = new HashMap<String, String>();
                String sMesId = isNotValueNull(cursor.getString(cursor.getColumnIndex(Mes.MESID)));
                mapOfMes.put(Mes.MESID, sMesId);
                if(sMesId == null){
                    Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                    intent.putExtra(HomeActivity.MESSAGE_TO_VIEW,"Не выбран код МЭС, сохранение не возможно");
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    return;
                }
                mapOfMes.put(Mes.MESOPENDATE,isNotValueNull( cursor.getString(cursor.getColumnIndex(Mes.MESOPENDATE))));
                mapOfMes.put(Mes.OPENDOCDEPID, isNotValueNull(cursor.getString(cursor.getColumnIndex(Mes.OPENDOCDEPID))));
                mapOfMes.put(Mes.MESCLOSEDATE, isNotValueNull(cursor.getString(cursor.getColumnIndex(Mes.MESCLOSEDATE))));
                mapOfMes.put(Mes.CLOSEDOCDEPID,isNotValueNull( cursor.getString(cursor.getColumnIndex(Mes.CLOSEDOCDEPID))));
                mapOfMes.put(Mes.CLOSETYPEID,isNotValueNull(cursor.getString(cursor.getColumnIndex(Mes.CLOSETYPEID))));
                mapOfMes.put(Mes.NOTE,isNotValueNull( cursor.getString(cursor.getColumnIndex(Mes.NOTE))));
            } while (cursor.moveToNext());

       }

        if(mapOfMes != null){
            String sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/" + sVisitId+ "/mes";
            AsyncSendingInformation sendingInfromation = new AsyncSendingInformation(this, Diagnose.NEW_DIAGNOS);
            sendingInfromation.setsCurrentToken(oLoginAccount.getsToken());
            sendingInfromation.setsRequest(sValueAddressForRequest);
            sendingInfromation.setMaps(mapOfMes);
            sendingInfromation.setDaemon(true);
            sendingInfromation.start();
        }

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

            if(mContext != null){

                CommonMainLoading.converJsonToList(mContext, jsonObject);

                Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.mes_was_save));
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);

            }

    }
}
