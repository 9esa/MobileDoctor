package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 18.06.2015.
 */
public class MesForDiagnoses {

    final public static String TABLE_NAME  = "mes_for_diagnoses_item";


    final public static String ID = "_id";
    final public static String MESID = "id";
    final public static String DIAGNOSEID = "diagnoseId";
    final public static String CODE = "code";
    final public static String TEXT = "text";


    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + MESID + " VARCHAR(255),"
            + DIAGNOSEID + " VARCHAR(255),"
            + CODE + " VARCHAR(255),"
            + TEXT + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;



    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sDiagnosesId Номер диагноза
     * @return
     */
    public static Cursor getInfoByDiagnoses(DataBaseHelper dbHelper, String sDiagnosesId){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + DIAGNOSEID + " = " + sDiagnosesId;

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
