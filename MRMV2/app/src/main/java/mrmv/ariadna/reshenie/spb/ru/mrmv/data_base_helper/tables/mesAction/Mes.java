package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 18.06.2015.
 */
public class Mes {

    final public static String TABLE_NAME  = "mes_item";

    final public static String ID = "id";
    final public static String VISITID = "visitId";
    final public static String MESID = "mesId";
    final public static String MESCODE = "mesCode";
    final public static String MESTEXT = "mesText";
    final public static String MESOPENDATE = "mesOpenDate";
    final public static String OPENDOCDEPID = "openDocDepId";
    final public static String OPENDOCDEPTEXT = "openDocDepText";
    final public static String MESCLOSEDATE = "mesCloseDate";
    final public static String CLOSEDOCDEPID = "closeDocDepId";
    final public static String CLOSEDOCDEPTEXT = "closeDocDepText";
    final public static String CLOSETYPEID = "closeTypeId";
    final public static String CLOSETYPETEXT = "closeTypeText";
    final public static String NOTE = "note";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + VISITID +" VARCHAR(255),"
            + MESID + " VARCHAR(255),"
            + MESCODE + " VARCHAR(255),"
            + MESTEXT + " VARCHAR(255),"
            + MESOPENDATE + " VARCHAR(255),"
            + OPENDOCDEPID + " VARCHAR(255),"
            + OPENDOCDEPTEXT + " VARCHAR(255),"
            + MESCLOSEDATE + " VARCHAR(255),"
            + CLOSEDOCDEPID + " VARCHAR(255),"
            + CLOSEDOCDEPTEXT + " VARCHAR(255),"
            + CLOSETYPEID + " VARCHAR(255),"
            + CLOSETYPETEXT + " VARCHAR(255),"
            + NOTE + " VARCHAR(255));";


    public static void createMesValueMesValue(DataBaseHelper dbHelper,
                                      String sVisitId){

        removeAllInfornationAboutTables(dbHelper);

        ContentValues values = new ContentValues();

        values.put(VISITID, sVisitId);

        dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public static boolean controlMesValue(DataBaseHelper dbHelper, String sVisit){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + VISITID + " = " + sVisit;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

       if(cursor != null) {
           if (cursor.getCount() > 0) {
                return true;
           }
       }
        return false;
    }


    public static void insertMesValue(DataBaseHelper dbHelper,
                                      String sVisitId,
                                      String sMesOpenDate,
                                      String sMesOpenDocdepId,
                                      String sMesId,
                                      String sMesCloseDate,
                                      String sMesCloseDocdepId,
//                                      String sCloseTypeId,
                                      String sNote){

        ContentValues values = new ContentValues();

        values.put(VISITID, sVisitId);
        values.put(MESOPENDATE, sMesOpenDate);
        values.put(OPENDOCDEPID, sMesOpenDocdepId);
        values.put(MESID, sMesId);
        values.put(MESCLOSEDATE, sMesCloseDate);
        values.put(CLOSEDOCDEPID, sMesCloseDocdepId);
        //values.put(CLOSETYPEID, sCloseTypeId);
        values.put(NOTE, sNote);

        dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public static void saveMesInfo(DataBaseHelper dbHelper, String sVisit, String sNote){
        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + NOTE + " = '" + sNote + " ' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(NOTE, sNote);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }
    }

    public static void saveTypeInfo(DataBaseHelper dbHelper, String sVisit, String sType){

        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + CLOSETYPEID + " = '" + sType + " ' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(CLOSETYPEID, sType);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }


    /**
     * Класс Сохраняющий инфомрацию по МЭС
     * @param dbHelper Класс для работы с базой данных
     * @param sVisit Номер визита
     * @param sMesId Идентификатор МЭС
     * @param sMesCode Код МЭС
     * @param sMesText Текст МЭС
     */
    public static void saveMesInfo(DataBaseHelper dbHelper, String sVisit, String sMesId, String sMesCode, String sMesText){


        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + MESID + " = '" + sMesId + "', " + MESCODE  + " = ' " + sMesCode  + "', " + MESTEXT + " = ' " + sMesText
                    + " ' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(MESID, sMesId);
            values.put(MESCODE, sMesCode);
            values.put(MESTEXT, sMesText);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }


    }


    /**
     * Сохранение врача открывшего мэс
     * @param dbHelper Класс для работы с базой данных
     * @param sVisit Номер визита
     * @param sDocdep Врач открывший мэс
     */
    public static void saveOpenDocdepId(DataBaseHelper dbHelper, String sVisit, String sDocdep){

        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + OPENDOCDEPID + " = '" + sDocdep + "' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(OPENDOCDEPID, sDocdep);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }


    }

    /**
     * Сохранение врача закрывшего мэс
     * @param dbHelper Класс для работы с базой данных
     * @param sVisit Номер визита
     * @param sDocdep Врач закрывший мэс
     */
    public static void saveCloseDocdepId(DataBaseHelper dbHelper, String sVisit, String sDocdep){

        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + CLOSEDOCDEPID + " = '" + sDocdep + "' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(CLOSEDOCDEPID, sDocdep);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }


    /**
     * Сохранение даты открытия меса
     * @param dbHelper Класс для работы с базой данных
     * @param sVisit Номер визита
     * @param sDate Дата открытия меса
     */
    public static void saveOpenDate(DataBaseHelper dbHelper, String sVisit, String sDate){

        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + MESOPENDATE + " = '" + sDate + "' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(MESOPENDATE, sDate);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }

    }

    /**
     * Сохранение даты закрытия меса
     * @param dbHelper Класс для работы с базой данных
     * @param sVisit Номер визита
     * @param sDate Дата закрытия меса
     */
    public static void saveCloseDate(DataBaseHelper dbHelper, String sVisit, String sDate){


        if(controlMesValue(dbHelper, sVisit)){

            String selectQuery = "UPDATE " + TABLE_NAME + " SET " + MESCLOSEDATE + " = '" + sDate + "' WHERE " + VISITID + " = " + sVisit;
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL(selectQuery);

        }else{

            ContentValues values = new ContentValues();

            values.put(VISITID, sVisit);
            values.put(MESCLOSEDATE, sDate);

            dbHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        }


    }

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sVisit Номер визита
     * @return
     */
    public static Cursor getInfoByVisit(DataBaseHelper dbHelper, String sVisit){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + VISITID + "=" + sVisit;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }


    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }
}
