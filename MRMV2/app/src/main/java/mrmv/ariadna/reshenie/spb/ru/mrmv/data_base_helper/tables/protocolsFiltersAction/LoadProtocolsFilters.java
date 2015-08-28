package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocolsFiltersAction;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon.LoadDataExtraField;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ICallBackForUnderDownLoad;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;

/**
 * Created by kirichenko on 24.06.2015.
 */
public class LoadProtocolsFilters extends CommonMainLoading implements ICallbackForLoadingMainInformation, ICallBackForUnderDownLoad {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ILoginEnableAccess iLoginEnableAccess;

    public LoadProtocolsFilters(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public int startLoadProtocolsFilters(ILoginEnableAccess iLoginEnableAccess) {

        this.iLoginEnableAccess = iLoginEnableAccess;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/protocolfilters";

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, 0);
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();

        return 1;
    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificationNumber) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificationNumber);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {
        String sFilterId = null, sFilterName = null, sFormItemId = null;

        if(sqlHelper != null) {
            ProtocolsFilter.removeAllInfornationAboutTables(sqlHelper);

            if (dataAboutLoadedObjects != null) {


                LoadDataExtraField loadDataExtraField = null;

                if (dataAboutLoadedObjects.size() == 0) {

                    underLoadComplete("");
                    return;
                } else {
                    loadDataExtraField = new LoadDataExtraField(oLoginAccount, sqlHelper, sAddress, dataAboutLoadedObjects.size(), mContext);
                }

                for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                    JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                    try {

                        sFilterId = String.valueOf(jsonObjectLoaderItem.get(ProtocolsFilter.FILTERID));
                        sFilterName = String.valueOf(jsonObjectLoaderItem.get(ProtocolsFilter.FILTERNAME));
                        sFormItemId = String.valueOf(jsonObjectLoaderItem.get(ProtocolsFilter.FORMITEMID));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    ContentValues values = new ContentValues();

                    values.put(ProtocolsFilter.FILTERID, sFilterId);
                    values.put(ProtocolsFilter.FILTERNAME, sFilterName);
                    values.put(ProtocolsFilter.FORMITEMID, sFormItemId);

                    sqlHelper.getWritableDatabase().insertWithOnConflict(ProtocolsFilter.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

                    loadDataExtraField.startLoadDataForExtraField(sFormItemId, this);

                }
            }
        }else{
            underLoadComplete("");
        }
    }

    @Override
    public void underLoadComplete(String sFormId) {
        if(iLoginEnableAccess!= null){
            iLoginEnableAccess.enableAccessLoadFinished(oLoginAccount);
        }
    }
}
