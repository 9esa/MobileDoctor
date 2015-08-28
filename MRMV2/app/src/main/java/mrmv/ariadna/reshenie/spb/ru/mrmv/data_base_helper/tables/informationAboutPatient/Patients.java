package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.informationAboutPatient;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 26.08.2015.
 */
public class Patients {

    final public static String TABLE_NAME  = "patients";

    final public static String ID = "_id";
    final public static String IDPATIENT = "id";
    final public static String PATIENT_NAME = "firstName";
    final public static String PATIENT_SECONDNAME = "lastName";
    final public static String PATIENT_THIRD_NAME = "secondName";
    final public static String NUMBER_CARD = "number_card";
    final public static String PHONE = "phone";
    final public static String ADDRESS = "address";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IDPATIENT + " VARCHAR(255),"
            + PATIENT_NAME + " VARCHAR(255),"
            + PATIENT_SECONDNAME + " VARCHAR(255),"
            + PATIENT_THIRD_NAME + " VARCHAR(255),"
            + NUMBER_CARD + " VARCHAR(255),"
            + PHONE + " VARCHAR(255),"
            + ADDRESS + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    public static Cursor getInformationAboutPatients(DataBaseHelper dbHelper){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME ;

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
