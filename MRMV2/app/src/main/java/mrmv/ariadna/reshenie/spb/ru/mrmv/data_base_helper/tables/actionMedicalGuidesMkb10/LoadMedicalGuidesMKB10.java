package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuidesMkb10;

import android.content.Context;
import android.database.sqlite.SQLiteStatement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.MedicalCommonConstants;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.statusDataBase.StatusDatabase;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.ILoginEnableAccess;

/**
 * Created by kirichenko on 06.05.2015.
 */
public class LoadMedicalGuidesMKB10 extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    /**
     * Информация о логине
     */
    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private ILoginEnableAccess iLoginEnableAccess;

    private String sAddress;

    private Context mContext;

    public LoadMedicalGuidesMKB10(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.mContext = mContext;
        this.oLoginAccount =  oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    /**
     *  Загрузка медецинских справочников
     * @param iLoginEnableAccess Интерфйс для callBack
     * @return Количество загружаемых таблиц
     */
    public int startLoadMedicalGuides(ILoginEnableAccess iLoginEnableAccess){

        String sValueAddressForRequest = "http://"+ sAddress + "/doctor-web/api/mkb10/list";

        this.iLoginEnableAccess = iLoginEnableAccess;

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, 0);

        if(oLoginAccount == null){
            loadingInfromation.setsCurrentToken(null);
        }else{
            loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        }

        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

        return 1;
    }


    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {

        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);

        if(iLoginEnableAccess!= null){
            iLoginEnableAccess.enableAccessLoadFinished(oLoginAccount);
        }

    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutGuides, Object identificatieNummer) {

        String sId = null, sLevel = null, sRootId = null, sCode = null, sText = null, sNote = null,
                sChronicstatus = null, sSexStatus = null, sNotMainStatus = null, sIsGroup = null;
        /**
         * Сперва удаляем все записи по этому справочнику
         */
        String cleanRequest = "DELETE FROM " + MedicalGuidesMKB10.TABLE_NAME ;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        if (dataAboutGuides != null) {

            String sql = "INSERT INTO "+ MedicalGuidesMKB10.TABLE_NAME +" VALUES (NULL,?,?,?,?,?,?,?,?,?,?);";
            SQLiteStatement statement = sqlHelper.getWritableDatabase().compileStatement(sql);
            sqlHelper.getWritableDatabase().beginTransaction();

            for (int iCount = 0; iCount < dataAboutGuides.size(); iCount++) {

                JSONObject oItemGuides = dataAboutGuides.get(iCount);
                try {

                    sId = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.IDGUIDES));
                    sLevel = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.LEVEL));
                    sRootId = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.ROOTID));
                    sCode = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.CODE));
                    sText = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.TEXT));
                    sNote = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.NOTE));
                    sChronicstatus = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.CHRONICSTATUS));
                    sSexStatus = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.SEXSTATUS));
                    sNotMainStatus = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.NOTMAINSTATUS));
                    sIsGroup = String.valueOf(oItemGuides.get(MedicalGuidesMKB10.ISGROUP));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                    statement.clearBindings();

                    statement.bindString(1,sId);
                    statement.bindString(2,sLevel);
                    statement.bindString(3,sRootId);
                    statement.bindString(4,sCode);
                    statement.bindString(5,sText);
                    statement.bindString(6,sNote);
                    statement.bindString(7,sChronicstatus);
                    statement.bindString(8,sSexStatus);
                    statement.bindString(9,sNotMainStatus);
                    statement.bindString(10,sIsGroup);

                    statement.execute();

            }

            sqlHelper.getWritableDatabase().setTransactionSuccessful();
            sqlHelper.getWritableDatabase().endTransaction();

            String sInsertQuery = "INSERT INTO " + StatusDatabase.TABLE_NAME + " (" + StatusDatabase.VERSION + "," + StatusDatabase.TABLE + "," + StatusDatabase.DATE_UPDATE + "," + StatusDatabase.LOADED
                    + ") VALUES ('" +  MedicalCommonConstants.DATABASE_VERSION + "','" + MedicalGuidesMKB10.TABLE_NAME + "','" +  Calendar.getInstance().getTime()+ "','" + true + "')";

            sqlHelper.getWritableDatabase().execSQL(sInsertQuery);
        }

    }
}