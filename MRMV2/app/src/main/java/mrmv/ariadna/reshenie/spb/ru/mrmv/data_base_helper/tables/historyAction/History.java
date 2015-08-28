package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.historyAction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 14.05.2015.
 */
public class History {

    final public static String TABLE_NAME  = "history_item";

    final public static String ID = "id";
    final public static String TEXT = "text";
    final public static String PACIENTID = "pacient_id";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TEXT + " VARCHAR(5000),"
            + PACIENTID + " VARCHAR(255)" + ");";


    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sPacientId Номер пациента
     * @return Курсор с полученными данными
     */
    public static Cursor getInformationAboutHistory(DataBaseHelper dbHelper, String sPacientId){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME  + " WHERE " + PACIENTID + "=" + sPacientId;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }
}
