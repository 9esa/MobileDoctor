package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 28.08.2015.
 */
public class Numbers {

    final public static String TABLE_NAME  = "number_item";

    final public static String ID = "_id";
    final public static String IDNUMBER = "id";
    final public static String IDDOCTOR = "idDoctor";
    final public static String TIME = "time";
    final public static String STATUS = "status";
    final public static String DATE = "date";
    final public static String PATIENTID = "patientId";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IDNUMBER +" VARCHAR(255),"
            + IDDOCTOR + " VARCHAR(255),"
            + TIME + " VARCHAR(255),"
            + STATUS + " VARCHAR(255),"
            + DATE + " VARCHAR(255),"
            + PATIENTID + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    public static Cursor getInfoAboutNumb(DataBaseHelper dbHelper){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

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
