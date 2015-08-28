package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction.ItemVisit;

/**
 * Created by kirichenko on 09.05.2015.
 */
public class Diagnose {

    final public static String BASE_DIAGNOS = "0";
    final public static String NEW_DIAGNOS = "1";
    final public static String DELETE_DIAGNOS = "2";

    final public static String TABLE_NAME  = "diagnose_item";

    final public static String ID = "_id";
    final public static String DIAGNOSEID = "id";
    final public static String VISITID = "visitId";
    final public static String DIAGNOSTYPE = "diagnosType";
    final public static String DIAGNOSID = "diagnosId";
    final public static String DIAGNOSCODE = "diagnosCode";
    final public static String DIAGNOSTEXT = "diagnosText";
    final public static String ILLTYPEID = "illTypeId";
    final public static String ILLTYPETEXT = "illTypeText";
    final public static String WORSEID = "worseId";
    final public static String WORSETEXT = "worseText";
    final public static String CRIMEID = "crimeId";
    final public static String CRIMETEXT = "crimeText";
    final public static String STAGEID = "stageId";
    final public static String STAGETEXT = "stageText";
    final public static String DISPID = "dispId";
    final public static String DISPTEXT = "dispText";
    final public static String DISPOFFID = "dispOffId";
    final public static String DISPOFFTEXT = "dispOffText";
    final public static String TRAVMAID = "travmaId";
    final public static String TRAVMATEXT = "travmaText";
    final public static String STATING = "stating";


    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + DIAGNOSEID + " VARCHAR(255),"
            + VISITID + " VARCHAR(255),"
            + DIAGNOSTYPE + " VARCHAR(255),"
            + DIAGNOSID + " VARCHAR(255),"
            + DIAGNOSCODE + " VARCHAR(255),"
            + DIAGNOSTEXT + " VARCHAR(255),"
            + ILLTYPEID + " VARCHAR(255),"
            + ILLTYPETEXT + " VARCHAR(255),"
            + WORSEID + " VARCHAR(255),"
            + WORSETEXT + " VARCHAR(255),"
            + CRIMEID + " VARCHAR(255),"
            + CRIMETEXT + " VARCHAR(255),"
            + STAGEID + " VARCHAR(255),"
            + STAGETEXT + " VARCHAR(255),"
            + DISPID + " VARCHAR(255),"
            + DISPTEXT + " VARCHAR(255),"
            + TRAVMAID + " VARCHAR(255),"
            + TRAVMATEXT + " VARCHAR(255),"
            + DISPOFFID + " VARCHAR(255),"
            + STATING + " VARCHAR(255),"
            + DISPOFFTEXT + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sIdVisit Номер визита для получения информации
     * @return Курсор с полученными данными
     */
    public static Cursor getInformationAboutDiagnose(DataBaseHelper dbHelper, String sIdVisit){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + VISITID + "=" + sIdVisit;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @return
     */
    public static String getInformationAboutMainDiagnose(DataBaseHelper dbHelper){

        String sDianoseId = null;

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + DIAGNOSTYPE + " = 1";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                sDianoseId = cursor.getString(cursor.getColumnIndex(DIAGNOSID));
            } while (cursor.moveToNext());
        }

        return sDianoseId;
    }



    public static void updateStateByVisit(DataBaseHelper oDataBaseHelper, String sVisit, Tag itemOfDiagnoses) {

        SQLiteDatabase db = oDataBaseHelper.getWritableDatabase();

        HashMap <String, String> mapOfAttr = (HashMap<String, String>) itemOfDiagnoses.getAttrs();

        ContentValues values = new ContentValues();

        values.put(VISITID, sVisit);
        values.put(DIAGNOSEID, mapOfAttr.get(DIAGNOSEID));
        values.put(DIAGNOSTYPE, mapOfAttr.get(DIAGNOSTYPE));
        values.put(DIAGNOSID, mapOfAttr.get(DIAGNOSID));
        values.put(DIAGNOSCODE, mapOfAttr.get(DIAGNOSCODE));
        values.put(DIAGNOSTEXT,mapOfAttr.get(DIAGNOSTEXT));
        values.put(ILLTYPEID, mapOfAttr.get(ILLTYPEID));
        values.put(ILLTYPETEXT,mapOfAttr.get(ILLTYPETEXT));
        values.put(WORSEID,mapOfAttr.get(WORSEID));
        values.put(WORSETEXT, mapOfAttr.get(WORSETEXT));
        values.put(CRIMEID, mapOfAttr.get(CRIMEID));
        values.put(CRIMETEXT, mapOfAttr.get(CRIMETEXT));
        values.put(STAGEID, mapOfAttr.get(STAGEID));
        values.put(STAGETEXT,mapOfAttr.get(STAGETEXT));
        values.put(DISPID,mapOfAttr.get(DISPID));
        values.put(DISPTEXT, mapOfAttr.get(DISPTEXT));
        values.put(DISPOFFID,mapOfAttr.get(DISPOFFID));
        values.put(DISPOFFTEXT, mapOfAttr.get(DISPOFFTEXT));
        values.put(TRAVMAID,mapOfAttr.get(TRAVMAID));
        values.put(TRAVMATEXT, mapOfAttr.get(TRAVMATEXT));
        values.put(STATING, DELETE_DIAGNOS);

        db.updateWithOnConflict(Diagnose.TABLE_NAME,values,  DIAGNOSID +  " = " + mapOfAttr.get(DIAGNOSID), null, SQLiteDatabase.CONFLICT_REPLACE);

    }

    public static void saveInfoByVisit(DataBaseHelper oDataBaseHelper, ItemVisit oItemVisit, ArrayList<Tag> listOfDiagnoses) {

        SQLiteDatabase db = oDataBaseHelper.getWritableDatabase();

        for(Tag itemTag : listOfDiagnoses){

            HashMap <String, String> mapOfAttr = (HashMap<String, String>) itemTag.getAttrs();

            ContentValues values = new ContentValues();
            values.put(VISITID, oItemVisit.getsVisitId());

            values.put(DIAGNOSEID, mapOfAttr.get(DIAGNOSEID));
            values.put(DIAGNOSTYPE, mapOfAttr.get(DIAGNOSTYPE));
            values.put(DIAGNOSID, mapOfAttr.get(DIAGNOSID));
            values.put(DIAGNOSCODE, mapOfAttr.get(DIAGNOSCODE));
            values.put(DIAGNOSTEXT,mapOfAttr.get(DIAGNOSTEXT));
            values.put(ILLTYPEID, mapOfAttr.get(ILLTYPEID));
            values.put(ILLTYPETEXT,mapOfAttr.get(ILLTYPETEXT));
            values.put(WORSEID,mapOfAttr.get(WORSEID));
            values.put(WORSETEXT, mapOfAttr.get(WORSETEXT));
            values.put(CRIMEID, mapOfAttr.get(CRIMEID));
            values.put(CRIMETEXT, mapOfAttr.get(CRIMETEXT));
            values.put(STAGEID, mapOfAttr.get(STAGEID));
            values.put(STAGETEXT,mapOfAttr.get(STAGETEXT));
            values.put(DISPID,mapOfAttr.get(DISPID));
            values.put(DISPTEXT, mapOfAttr.get(DISPTEXT));
            values.put(DISPOFFID,mapOfAttr.get(DISPOFFID));
            values.put(DISPOFFTEXT, mapOfAttr.get(DISPOFFTEXT));
            values.put(TRAVMAID,mapOfAttr.get(TRAVMAID));
            values.put(TRAVMATEXT, mapOfAttr.get(TRAVMATEXT));
            values.put(STATING, NEW_DIAGNOS);

            db.insertWithOnConflict(Diagnose.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        }
    }

    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    public static void removeAllInfornationDiagnosesWithoutMain(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + DIAGNOSTYPE + " <> 1";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }
}
