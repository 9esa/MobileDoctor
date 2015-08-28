package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides;

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
 * Created by kirichenko on 01.05.2015.
 */
public class LoadMedicalGuides extends CommonMainLoading implements ICallbackForLoadingMainInformation {


    /**
     * Информация о логине
     */
    private LoginAccount oLoginAccount;

    private Context mContext;

    private ArrayList<Integer> tagsArrayList;

    private DataBaseHelper sqlHelper;

    private int iSizeOfGuides = 0;

    private ILoginEnableAccess iLoginEnableAccess;

    private String sAddress;

    public LoadMedicalGuides(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress, Context mContext) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
        this.mContext = mContext;
        iSizeOfGuides = loadListGuides();
    }

    //Внимание опасность!!!! Как это повлияет на производительность без понятия

    /**
     * Загрузка медецинских справочников
     *
     * @param iLoginEnableAccess Интерфйс для callBack
     * @return Количество загружаемых таблиц
     */
    public int startLoadMedicalGuides(ILoginEnableAccess iLoginEnableAccess) {

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/lu/tag/";

        this.iLoginEnableAccess = iLoginEnableAccess;

        String cleanRequest = "DELETE FROM " + MedicalGuides.TABLE_NAME;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        for (int iCount = 0; iCount < tagsArrayList.size(); iCount++) {

            AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, tagsArrayList.get(iCount));

            if(oLoginAccount != null){
                loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
            }
            
            loadingInfromation.setsRequest(sValueAddressForRequest + tagsArrayList.get(iCount));
            loadingInfromation.setDaemon(true);
            loadingInfromation.start();

        }

        return getiSizeOfGuides();

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

        if (dataAboutGuides != null) {

            String sql = "INSERT INTO "+ MedicalGuides.TABLE_NAME +" VALUES (NULL,?,?,?,?,?,?);";
            SQLiteStatement statement = sqlHelper.getWritableDatabase().compileStatement(sql);
            sqlHelper.getWritableDatabase().beginTransaction();


            for (int iCount = 0; iCount < dataAboutGuides.size(); iCount++) {

                JSONObject oItemGuides = dataAboutGuides.get(iCount);
                try {

                    String sId, sCode, sText, sNote,sDefault;

                    sId = String.valueOf(oItemGuides.get("id"));
                    sCode = String.valueOf(oItemGuides.get(MedicalGuides.CODE));

                    if (String.valueOf(oItemGuides.get(MedicalGuides.TEXT)).equals("null")) {
                        sText = "-";
                    } else {
                        sText = String.valueOf(oItemGuides.get("text"));
                    }

                    if (String.valueOf(oItemGuides.get("shortText")).equals("null")) {
                        sNote = "-";
                    } else {
                        sNote = String.valueOf(oItemGuides.get("shortText"));
                    }

                    if (String.valueOf(oItemGuides.get("isDefault")).equals("null")) {
                        sDefault = "0";
                    } else {
                        sDefault = String.valueOf(oItemGuides.get("isDefault"));
                    }

//                    String insertQuery = "INSERT INTO " + MedicalGuides.TABLE_NAME
//                            + " (" + MedicalGuides.IDGUIDES + "," + MedicalGuides.CODE + "," + MedicalGuides.TEXT + "," + MedicalGuides.ISDEFAULT + "," + MedicalGuides.SHORTTEXT + "," + MedicalGuides.TAG + ") VALUES ('"
//                            + sId + "','" + sCode + "','" + sText + "','" + sDefault + "','" + sNote + "','" + identificatieNummer + "')";
//
//                    sqlHelper.getWritableDatabase().execSQL(insertQuery);

                    statement.clearBindings();

                    statement.bindString(1,sId);
                    statement.bindString(2,String.valueOf(identificatieNummer));
                    statement.bindString(3,sDefault);
                    statement.bindString(4,sCode);
                    statement.bindString(5,sText);
                    statement.bindString(6,sNote);

                    statement.execute();


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }


            sqlHelper.getWritableDatabase().setTransactionSuccessful();
            sqlHelper.getWritableDatabase().endTransaction();


            String sInsertQuery = "INSERT INTO " + StatusDatabase.TABLE_NAME + " (" + StatusDatabase.VERSION + "," + StatusDatabase.TABLE + "," + StatusDatabase.DATE_UPDATE + "," + StatusDatabase.LOADED
                    + ") VALUES ('" +  MedicalCommonConstants.DATABASE_VERSION + "','" + MedicalGuides.TABLE_NAME + "','" +  Calendar.getInstance().getTime()+ "','" + true + "')";

            sqlHelper.getWritableDatabase().execSQL(sInsertQuery);
        }
    }



    /**
     * Загружаем все теги справочников
     * @return Размер списка необходимых тегов
     */
    private int loadListGuides(){

        if( tagsArrayList == null){
            tagsArrayList = new ArrayList<>();
        }

        tagsArrayList.clear();
//        //Тип посещения
       tagsArrayList.add(MedicalCommonConstants.TYPE_VISIT);
//        //Место обслуживания
        tagsArrayList.add(MedicalCommonConstants.SERVICE_PLACE);
        //Цель посещения
        tagsArrayList.add(MedicalCommonConstants.GOAL_VISIT);
//        //Профиль паталогии
        tagsArrayList.add(MedicalCommonConstants.PROFILE_POTOLOGI);
//        //Случай
        tagsArrayList.add(MedicalCommonConstants.CASE);
//        //Законченность
        tagsArrayList.add(MedicalCommonConstants.COMPLETENESS);
//        //Исход
        tagsArrayList.add(MedicalCommonConstants.OUTCOME);
//        //Результат лечения
        tagsArrayList.add(MedicalCommonConstants.RESULT_CARE);
//        //Характер заболевания
        tagsArrayList.add(MedicalCommonConstants.CHARACTER_OF_DISEASE);
//        //Обострнеие
        tagsArrayList.add(MedicalCommonConstants.EXACERBATION);
//        //Противоправные дейсвия
        tagsArrayList.add(MedicalCommonConstants.WRONGFUL_ACTS);
//        //Стадия
        tagsArrayList.add(MedicalCommonConstants.STAGE);
//        //Диспансерный учет
        tagsArrayList.add(MedicalCommonConstants.CLINICAL_ACCOUNT);
//        //Причина снятия с диспансерного учета
        tagsArrayList.add(MedicalCommonConstants.REASON_OF_WITHDRAWAL);
//        //Травма
        tagsArrayList.add(MedicalCommonConstants.TRAUMA);
//        //Тип диагноза
        tagsArrayList.add(MedicalCommonConstants.DIAGNOSE_TYPE);
//        //Тип закрытия мэса
        tagsArrayList.add(MedicalCommonConstants.TYPE_OF_CLOSE_MES);


        return tagsArrayList.size();
    }

    public int getiSizeOfGuides() {
        return iSizeOfGuides;
    }
}
