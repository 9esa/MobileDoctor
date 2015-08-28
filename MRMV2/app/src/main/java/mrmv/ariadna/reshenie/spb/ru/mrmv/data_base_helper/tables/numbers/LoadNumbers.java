package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers;

import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;

/**
 * Created by kirichenko on 28.08.2015.
 */
public class LoadNumbers extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    public LoadNumbers(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress,Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationAboutEnableDoctors(Context mContext, ICommonLoadComplete iLoadedCompleteCallBack) {

    }


    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {

    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

    }
}
