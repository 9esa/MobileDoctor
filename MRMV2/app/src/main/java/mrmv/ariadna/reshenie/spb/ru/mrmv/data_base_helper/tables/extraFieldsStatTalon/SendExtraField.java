package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.deleting_information.AsyncDeletingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.AsyncSendingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.ICallbackForSendingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ItemProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.StructureFullFieldProtocols;

/**
 * Created by kirichenko on 03.06.2015.
 */
public class SendExtraField implements ICallbackForSendingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    private static String SEPARATOR = "0";

    private String sValueDelete = "delete";

    public SendExtraField(Context mContext, LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
    }

    public void startSend(String sValue){

        ArrayList <ItemProtocols> ExtraProtocolsVisit = (ArrayList<ItemProtocols>) StructureFullFieldProtocols.getListEnableProtocols(sqlHelper, sValue);
        ArrayList<JSONObject> listOfJsonObject = new ArrayList<>();

        for(ItemProtocols localItemProtocol : ExtraProtocolsVisit){

            if(!localItemProtocol.getsTyp().equals(SEPARATOR)){
               JSONObject oJsonObject = new JSONObject();

                try {
                    oJsonObject.accumulate(StructureFullFieldProtocols.FORMITEMID,localItemProtocol.getsFormitemId());
                    oJsonObject.accumulate("value",isNotValueNull(localItemProtocol.getsCtext()));
                } catch (JSONException e) {
                    continue;
                }

               listOfJsonObject.add(oJsonObject);
           }

        }

        JSONArray jsArrayToSend = new JSONArray(listOfJsonObject);

        String sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/" + sValue + "/addfields";

        AsyncSendingInformation sendingInfromation = new AsyncSendingInformation(this,sValue);
        sendingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        sendingInfromation.setsRequest(sValueAddressForRequest);
        sendingInfromation.setArrays(jsArrayToSend);

        sendingInfromation.setDaemon(true);
        sendingInfromation.start();

    }

    public void startDelete(String sValue){

        String sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/protocol/" + sValue;
        AsyncDeletingInformation deletingInfromation = new AsyncDeletingInformation(this, sValueDelete);
        deletingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        deletingInfromation.setsRequest(sValueAddressForRequest);
        deletingInfromation.setDaemon(true);
        deletingInfromation.start();

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

                if(oIdentifyObject != null){
                    if(String.valueOf(oIdentifyObject).equals(sValueDelete)){
                        Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                        intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.protocol_delete_successfuly));
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                    }
                }
//                Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
//                intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.succesfull_deleted_diagnoses));
//                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }
    }
}
