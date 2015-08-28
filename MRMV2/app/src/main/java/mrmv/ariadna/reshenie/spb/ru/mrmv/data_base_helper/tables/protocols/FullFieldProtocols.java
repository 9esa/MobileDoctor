package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.entity.Tag;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 17.05.2015.
 */
public class FullFieldProtocols {

    final public static String TABLE_NAME  = "full_field_protocols";

    final public static String ID = "id";
    final public static String VISITID = "visitid";
    final public static String FORMRESULTID = "formResultId";
    final public static String FORMID = "formId";
    final public static String TEXT = "text";
    final public static String DATE = "date";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + VISITID + " VARCHAR(255),"
            + FORMRESULTID + " VARCHAR(255),"
            + FORMID + " VARCHAR(255),"
            + TEXT + " VARCHAR(255),"
            + DATE + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    public static String getResultIdById(DataBaseHelper dbHelper, String sId) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + ID + " = " + sId;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String sFormResultId = null;

        if (cursor.moveToFirst()) {
            do {

                sFormResultId  = cursor.getString(cursor.getColumnIndex(FORMRESULTID));

            } while (cursor.moveToNext());
        }

        return sFormResultId;

    }

    public static String getFormIdById(DataBaseHelper dbHelper, String sId) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + ID + " = " + sId;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        String sFormId = null;

        if (cursor.moveToFirst()) {
            do {

                sFormId  = cursor.getString(cursor.getColumnIndex(FORMID));

            } while (cursor.moveToNext());
        }

        return sFormId;

    }


    public static void updateFullFieldProtocolsFormResultId(DataBaseHelper dbHelper, String sId, String sFormResultId) {

        String selectQuery = "UPDATE " + TABLE_NAME + " SET " + FORMRESULTID + " = " + sFormResultId  +
                            " WHERE " + ID + " = " + sId;

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);

    }


    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sIdVisit Номер визита для получения информации
     * @return Курсор с полученными данными
     */
    public static Cursor getInformationAboutFullFieldProtocol(DataBaseHelper dbHelper, String sIdVisit){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + VISITID + "=" + sIdVisit;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public static void removeInfornationAboutFormId(DataBaseHelper dbHelper, String sId){

        String selectQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + ID + " = " + sId;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }


    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    /**
     * Функция записи информации по протоколу
     * @param oDataBaseHelper Класс для работы с базой данных
     * @param oNewTag Тэг с информацией для записи
     * @param sIdVisit Номер визита для получения информации
     */
    public static String addProtocols(DataBaseHelper oDataBaseHelper, Tag oNewTag, String sIdVisit) {

        HashMap<String, String> mapOfAttr = (HashMap<String, String>) oNewTag.getAttrs();

        ContentValues values = new ContentValues();

        values.put(VISITID, sIdVisit);
        values.put(FORMRESULTID, mapOfAttr.get(FORMRESULTID));
        values.put(FORMID, mapOfAttr.get(FORMID));
        values.put(TEXT, mapOfAttr.get(TEXT));
        values.put(DATE, mapOfAttr.get(DATE));

       Long lId = oDataBaseHelper.getWritableDatabase().insertWithOnConflict(TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        return String.valueOf(lId);
    }

}
