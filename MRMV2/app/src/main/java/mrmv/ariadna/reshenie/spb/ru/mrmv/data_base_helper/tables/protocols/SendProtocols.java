package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols;

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
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.AsyncSendingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.ICallbackForSendingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;

/**
 * Created by kirichenko on 04.06.2015.
 */
public class SendProtocols implements ICallbackForSendingMainInformation {
    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    private static String SEPARATOR = "0";

    public SendProtocols(Context mContext, LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
    }

    public void startSendValueProtocols(String sId, String sVisitId, String sFormId, String sFormResultId){

        ArrayList<ItemProtocols> ExtraProtocolsVisit;

        if(sFormResultId == null){
            ExtraProtocolsVisit =  (ArrayList<ItemProtocols>) StructureFullFieldProtocols.getListEnableProtocolsToSend(sqlHelper, sFormId);
        }else{
            ExtraProtocolsVisit =  (ArrayList<ItemProtocols>) StructureFullFieldProtocols.getListEnableProtocolsToSend(sqlHelper, sFormResultId);

        }

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


        String sValueAddressForRequest;

        if(sFormResultId == null){
            sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/" + sVisitId + "/form/" + sFormId + "/add";
        }else {
            sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/housevisit/protocol/" + sFormResultId+ "/formitems" ;
       }

        AsyncSendingInformation sendingInfromation = new AsyncSendingInformation(this, sId);
        sendingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        sendingInfromation.setsRequest(sValueAddressForRequest);
        sendingInfromation.setArrays(jsArrayToSend);

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
        if(oIdentifyObject != null){

            if(mContext != null){
                Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);

                ArrayList<JSONObject> listOfAnswer = CommonMainLoading.converJsonToList(mContext, jsonObject);

                if(listOfAnswer!= null){
                    if(listOfAnswer.size() > 0){
                        try {
                            String sFormResultId = String.valueOf( listOfAnswer.get(0).get(FullFieldProtocols.FORMRESULTID));

                            FullFieldProtocols.updateFullFieldProtocolsFormResultId(sqlHelper,String.valueOf(oIdentifyObject), sFormResultId);

                            intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.protocol_was_save));
                        } catch (JSONException e) {
                            intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.error_saving));
                        }
                    }else{
                        intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.protocol_was_save));
                    }
                }

                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
            }

        }
    }
}
