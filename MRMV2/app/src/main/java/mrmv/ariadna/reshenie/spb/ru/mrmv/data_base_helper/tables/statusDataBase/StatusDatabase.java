package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.statusDataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 22.05.2015.
 */
public class StatusDatabase {

    final public static String TABLE_NAME  = "status_data_base";

    final public static String ID = "id";
    final public static String VERSION = "version";
    final public static String TABLE = "table_name";
    final public static String DATE_UPDATE = "date_update";
    final public static String LOADED = "loaded";
    final public static String ESSENTIAL = "essential";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + VERSION + " VARCHAR(255),"
            + TABLE + " VARCHAR(255),"
            + DATE_UPDATE + " VARCHAR(255),"
            + ESSENTIAL + " VARCHAR(255),"
            + LOADED + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    /**
     * Получения списка справочников
     * @param dbHelper
     * @return
     */
    public static int getInformationAboutStatusDataBase(DataBaseHelper dbHelper){

        int iValueCount = 0;

        String selectQuery = "SELECT COUNT(*) AS COUNT_VALUES FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor != null){

            if(cursor.moveToNext()){
                do{

                     iValueCount = cursor.getInt(cursor.getColumnIndex("COUNT_VALUES"));
                     break;
                }while(cursor.moveToNext());
            }
        }

      //  cursor.close();
        return iValueCount;
    }

}
