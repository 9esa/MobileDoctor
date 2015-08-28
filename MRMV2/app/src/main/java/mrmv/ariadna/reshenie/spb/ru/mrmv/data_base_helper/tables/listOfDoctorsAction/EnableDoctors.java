package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 22.06.2015.
 */
public class EnableDoctors {

    final public static String TABLE_NAME  = "enable_doctors";

    final public static String ID = "_id";
    final public static String IDDOCTOR = "id";
    final public static String STATUS = "status";
    final public static String CODE = "code";
    final public static String NAME = "name";
    final public static String SPECNAME = "specName";
    final public static String CATEGNAME = "categName";
    final public static String DEPID = "depId";
    final public static String DEPNAME = "depName";
    final public static String NOTE = "note";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IDDOCTOR +" VARCHAR(255),"
            + STATUS + " VARCHAR(255),"
            + CODE + " VARCHAR(255),"
            + NAME + " VARCHAR(255),"
            + SPECNAME + " VARCHAR(255),"
            + CATEGNAME + " VARCHAR(255),"
            + DEPID + " VARCHAR(255),"
            + DEPNAME + " VARCHAR(255),"
            + NOTE + " VARCHAR(255));";


    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @return Курсор со списком врачей
     */
    public static Cursor getInfoAboutEnableDoctors(DataBaseHelper dbHelper){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;

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
