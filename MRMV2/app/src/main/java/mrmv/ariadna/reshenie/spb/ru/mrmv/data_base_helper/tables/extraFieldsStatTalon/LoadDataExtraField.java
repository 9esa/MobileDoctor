package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.ICallBackForUnderDownLoad;

/**
 * Created by kirichenko on 26.05.2015.
 * Класс для получения значений справочников для дополнительных полей
 */
public class LoadDataExtraField extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;
    private DataBaseHelper sqlHelper;
    private String sAddress;
    private Context mContext;

    private ICallBackForUnderDownLoad iLoadedComleteCallBack;
    private Integer iSizeOfRequests;

    public LoadDataExtraField(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Integer iSizeOfRequestsLocal, Context mContext) {

        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;

        iSizeOfRequests = new Integer(0);

        synchronized (iSizeOfRequests){
            iSizeOfRequests += iSizeOfRequestsLocal;
        }

    }

    public void startLoadDataForExtraField(String sValueFormId, ICallBackForUnderDownLoad iLoadedComleteCallBack) {

        this.iLoadedComleteCallBack = iLoadedComleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/addfieldsstruct/list/formitem/" + sValueFormId;

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sValueFormId);

        if(oLoginAccount  != null){
            loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        }

        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sFormItemId = null, sIdExtraField = null, sCode = null, sText =  null;

        if (dataAboutLoadedObjects != null) {

            String sql = "INSERT INTO " + ExtraFieldStatTalon.TABLE_NAME + " VALUES (NULL,?,?,?,?,?);";
            SQLiteStatement statement = sqlHelper.getWritableDatabase().compileStatement(sql);
            sqlHelper.getWritableDatabase().beginTransaction();

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {
                    sFormItemId = String.valueOf(identificatieNummer);
                    sIdExtraField = String.valueOf(oItemGuides.get(ExtraFieldStatTalon.ID));
                    sCode = String.valueOf(oItemGuides.get(ExtraFieldStatTalon.CODE));
                    sText = String.valueOf(oItemGuides.get(ExtraFieldStatTalon.TEXT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                statement.clearBindings();

                statement.bindString(1,sFormItemId);
                statement.bindString(2,sIdExtraField);
                statement.bindString(3,"-");
                statement.bindString(4,sCode);
                statement.bindString(5,sText);

                statement.execute();

//                ContentValues values = new ContentValues();
//
//                values.put(ExtraFieldStatTalon.FORMID, sFormItemId);
//                values.put(ExtraFieldStatTalon.IDEXTRAFIELD, sIdExtraField);
//                values.put(ExtraFieldStatTalon.EXTRAFIELD, "-");
//                values.put(ExtraFieldStatTalon.CODE, sCode);
//                values.put(ExtraFieldStatTalon.TEXT, sText);

 //               sqlHelper.getWritableDatabase().insertWithOnConflict(ExtraFieldStatTalon.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }

            sqlHelper.getWritableDatabase().setTransactionSuccessful();
            sqlHelper.getWritableDatabase().endTransaction();
        }

        synchronized (iSizeOfRequests){
            iSizeOfRequests--;

            if(iSizeOfRequests == 0){
                /**
                 * Сообщаем карточки доктора, что мы загузили данные с сервера и можно получить информацию из базы данных
                 */
                if (iLoadedComleteCallBack != null) {
                    iLoadedComleteCallBack.underLoadComplete(sFormItemId);
                }
            }
        }

    }
}

