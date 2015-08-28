package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 02.05.2015.
 */
public class Calls {

    final public static String TABLE_NAME  = "user_calls";

    final public static String ID = "_id";
    final public static String IDCALLS = "id";
    final public static String STATUS = "status";
    final public static String PROTOCOL_STATUS = "protocolStatus";
    final public static String CALL_DATE = "callDate";
    final public static String PATIENTID = "patientId";
    final public static String LAST_NAME = "lastName";
    final public static String FIRST_NAME = "firstName";
    final public static String SECOND_NAME = "secondName";
    final public static String SEX = "sex";
    final public static String BIRTHDATE = "birthDate";
    final public static String AGE = "age";
    final public static String REGADDRESS = "regAddress";
    final public static String LOCADDRESS = "locAddress";
    final public static String NOTE = "note";
    final public static String PHONE = "phone";
    final public static String CELLULAR = "cellular";

    final public static String REGADDRESSFULL = "regAddressFull";
    final public static String LOCADDRESSFULL = "locAddressFull";

    final public static String TAG = "tag";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IDCALLS + " VARCHAR(255),"
            + STATUS + " VARCHAR(255),"
            + PROTOCOL_STATUS + " VARCHAR(255),"
            + CALL_DATE + " VARCHAR(255),"
            + PATIENTID + " VARCHAR(255),"
            + LAST_NAME + " VARCHAR(255),"
            + FIRST_NAME + " VARCHAR(255),"
            + SECOND_NAME + " VARCHAR(255),"
            + SEX + " VARCHAR(255),"
            + BIRTHDATE + " VARCHAR(255),"
            + AGE + " VARCHAR(255),"
            + REGADDRESS + " VARCHAR(255),"
            + LOCADDRESS + " VARCHAR(255),"
            + NOTE + " VARCHAR(255),"
            + PHONE + " VARCHAR(255),"
            + CELLULAR + " VARCHAR(255),"
            + REGADDRESSFULL + " VARCHAR(255),"
            + LOCADDRESSFULL + " VARCHAR(255),"
            + TAG + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param iTAG Номер состояния которое необходимо загрузить
     * @return
     */
    public static Cursor getMyCalls(DataBaseHelper dbHelper, int iTAG){

        String selectQuery = "";

        if(iTAG == 0 ){
            selectQuery = "SELECT  * FROM " + TABLE_NAME;
        }else if(iTAG == 1){
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + STATUS + "=" + (iTAG - 1);
        }else if(iTAG == 2){
            selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + STATUS + "=" + (iTAG - 1);
        }

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param idCalls Номер справочника который необходимо загрузить
     * @return
     */
    public static Cursor getInfoByVisit(DataBaseHelper dbHelper, String idCalls){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + IDCALLS + "=" + idCalls;

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
