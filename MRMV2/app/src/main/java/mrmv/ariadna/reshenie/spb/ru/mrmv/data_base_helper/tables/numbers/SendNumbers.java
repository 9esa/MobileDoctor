package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.AsyncSendingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information.ICallbackForSendingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment.AppointmentAdapter;

/**
 * Created by kirichenko on 28.08.2015.
 */
public class SendNumbers  extends CommonMainLoading implements ICallbackForSendingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper oDataBaseHelper;

    private Context mContext;

    private String sAddress;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    private String sRequestDate = "", sDocDepId = "";

    private static String SUCCESS_RESPONSE = "0";

    private static String ERROR_CODE = "errorCode";
    private static String ERROR_MSG = "errorMsg";

    private String sIdPacient;

    public SendNumbers (Context mContext, LoginAccount oLoginAccount, DataBaseHelper oDataBaseHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.oDataBaseHelper = oDataBaseHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
    }

    public void startRequestToServer(String sIdNumber, String sPacientId, String sStatusNumber, ICommonLoadComplete iLoadedCompleteCallBack, String sRequestDate, String sDocDepId){

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;
        this.sRequestDate = sRequestDate;
        this.sDocDepId = sDocDepId;
        this.sIdPacient = sPacientId;

        String sValueAddressForRequest = "";

        HashMap<String, String> mapOfMes = new HashMap<String, String>();

        mapOfMes.put(Numbers.IDNUMBER, sIdNumber);

        if(AppointmentAdapter.ACTIVE.equals(sStatusNumber)){
            sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/patient/" + sPacientId+ "/reg/rnumb/occupy";
        }else{
            sValueAddressForRequest = "http://"+ sAddress +"/doctor-web/api/patient/reg/rnumb/free";
        }

        AsyncSendingInformation sendingInfromation = new AsyncSendingInformation(this, sStatusNumber);
        sendingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        sendingInfromation.setsRequest(sValueAddressForRequest);
        sendingInfromation.setMaps(mapOfMes);
        sendingInfromation.setDaemon(true);
        sendingInfromation.start();


    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object oIdentifyObject) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), oIdentifyObject);
    }

    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object oIdentifyObject) {

        String sErrorCode = "", sErrorMsg = null;
        String sIdentifyObject = "";

        if(oIdentifyObject != null){
            sIdentifyObject =  String.valueOf(oIdentifyObject);
        }

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);

                try {

                    sErrorCode = String.valueOf(jsonObjectLoaderItem.get(ERROR_CODE));
                    sErrorMsg = String.valueOf(jsonObjectLoaderItem.get(ERROR_MSG));

                    actionWithResponse(sIdentifyObject, sErrorCode, sErrorMsg);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }

        startUpdateNumbersList();

    }

    private void startUpdateNumbersList(){
        if(iLoadedCompleteCallBack != null) {
            new LoadNumbers(oLoginAccount, oDataBaseHelper, sAddress, mContext).startLoadInformationAboutEnableDoctors(sRequestDate, sDocDepId, iLoadedCompleteCallBack, sIdPacient);
        }
    }

    private void actionWithResponse(String sIdentifyObject, String sErrorCode,String sErrorMsg){

        if(sIdentifyObject.equals(AppointmentAdapter.ACTIVE)){

            if(sErrorCode.equals(SUCCESS_RESPONSE)){
                if(mContext != null){
                        Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                        intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.pacient_save_successful));
                        LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }else{
                if(mContext != null){
                    Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                    intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, sErrorMsg);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        }else{

            if(sErrorCode.equals(SUCCESS_RESPONSE)){
                if(mContext != null){
                    Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                    intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, mContext.getString(R.string.number_free_successfuly));
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }else{
                if(mContext != null){
                    Intent intent = new Intent(HomeActivity.BROADCAST_ACTION_TAKING_INFORMATION);
                    intent.putExtra(HomeActivity.MESSAGE_TO_VIEW, sErrorMsg);
                    LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent);
                }
            }
        }

    }

}