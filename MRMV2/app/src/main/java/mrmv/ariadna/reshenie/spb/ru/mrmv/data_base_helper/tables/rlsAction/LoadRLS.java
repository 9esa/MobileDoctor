package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.rlsAction;

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
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.bookRLS.ItemRLS;

/**
 * Created by kirichenko on 12.09.2015.
 */
public class LoadRLS extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private String sAddress;

    private Context mContext;

    private ICommonLoadComplete iLoadedCompleteCallBack;

    private static int DEFAULT_VALUE_VIEW = 50;

    public LoadRLS(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationProducts(ItemRLS oItemRLS, ICommonLoadComplete iLoadedCompleteCallBack) {

        int iMaxValueToView = DEFAULT_VALUE_VIEW;

        this.iLoadedCompleteCallBack = iLoadedCompleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/rls/search?";

        if (oItemRLS != null) {

            if (oItemRLS.getsProductName() != null) {
                sValueAddressForRequest += "&tradename=" + oItemRLS.getsProductName();
            }

            if (oItemRLS.getsNameOfActiveComponent() != null) {
                sValueAddressForRequest += "&ndv=" + oItemRLS.getsNameOfActiveComponent();
            }

            if (oItemRLS.getsNameOfGroupComponent() != null) {
                sValueAddressForRequest += "&grpname=" + oItemRLS.getsNameOfGroupComponent();
            }

            sValueAddressForRequest += "&maxrows=" + iMaxValueToView;
        }

        AsyncLoadingInformation loadingInformation = new AsyncLoadingInformation("", this, null);
        loadingInformation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInformation.setsRequest(sValueAddressForRequest);
        loadingInformation.setDaemon(true);
        loadingInformation.start();

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {
        String sCode = null, sTradename = null, sLatname = null,
                sNdv = null, sLive = null, sForm = null, sDosage = null,
                sFilling = null, sPacking = null, sManufacturer = null,
                sMcountry = null, sRegnum = null, sRegdate = null,
                sRegistrator = null, sRcountry = null, sAge = null, sGrpname = null, sAtc = null;

        RLSItem.removeAllInfornationAboutTables(sqlHelper);

        for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

            JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);

            try {

                sCode = String.valueOf(oItemGuides.get(RLSItem.CODE));
                sTradename = String.valueOf(oItemGuides.get(RLSItem.TRADENAME));
                sLatname = String.valueOf(oItemGuides.get(RLSItem.LATNAME));
                sNdv = String.valueOf(oItemGuides.get(RLSItem.NDV));
                sLive = String.valueOf(oItemGuides.get(RLSItem.LIFE));
                sForm = String.valueOf(oItemGuides.get(RLSItem.FORM));
                sDosage = String.valueOf(oItemGuides.get(RLSItem.DOSAGE));
                sFilling = String.valueOf(oItemGuides.get(RLSItem.FILLING));
                sPacking = String.valueOf(oItemGuides.get(RLSItem.PACKING));
                sManufacturer = String.valueOf(oItemGuides.get(RLSItem.MANUFACTURER));
                sMcountry = String.valueOf(oItemGuides.get(RLSItem.MCOUNTRY));
                sRegnum = String.valueOf(oItemGuides.get(RLSItem.REGNUM));
                sRegdate = String.valueOf(oItemGuides.get(RLSItem.REGDATE));
                sRegistrator = String.valueOf(oItemGuides.get(RLSItem.REGISTRATOR));
                sRcountry = String.valueOf(oItemGuides.get(RLSItem.RCOUNTRY));
                sAge = String.valueOf(oItemGuides.get(RLSItem.AGE));
                sGrpname = String.valueOf(oItemGuides.get(RLSItem.GRPNAME));
                sAtc = String.valueOf(oItemGuides.get(RLSItem.ATC));

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ContentValues values = new ContentValues();

            values.put(RLSItem.CODE, sCode);
            values.put(RLSItem.TRADENAME, sTradename);
            values.put(RLSItem.LATNAME, sLatname);
            values.put(RLSItem.NDV, sNdv);
            values.put(RLSItem.LIFE, sLive);
            values.put(RLSItem.FORM, sForm);
            values.put(RLSItem.DOSAGE, sDosage);
            values.put(RLSItem.FILLING, sFilling);
            values.put(RLSItem.PACKING, sPacking);
            values.put(RLSItem.MANUFACTURER, sManufacturer);
            values.put(RLSItem.MCOUNTRY, sMcountry);
            values.put(RLSItem.REGNUM, sRegnum);
            values.put(RLSItem.REGDATE, sRegdate);
            values.put(RLSItem.REGISTRATOR, sRegistrator);
            values.put(RLSItem.RCOUNTRY, sRcountry);
            values.put(RLSItem.AGE, sAge);
            values.put(RLSItem.GRPNAME, sGrpname);
            values.put(RLSItem.ATC, sAtc);

            sqlHelper.getWritableDatabase().insertWithOnConflict(RLSItem.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        }

        if (iLoadedCompleteCallBack != null) {
            iLoadedCompleteCallBack.loadCompleted();
        }

    }
}
