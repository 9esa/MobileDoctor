package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocolsFiltersAction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 24.06.2015.
 */
public class ProtocolsFilter {

    final public static String TABLE_NAME  = "protoclos_filter";

    final public static String ID = "_id";
    final public static String FILTERID = "filterId";
    final public static String FILTERNAME = "filterName";
    final public static String FORMITEMID = "formItemId";


    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FILTERID +" VARCHAR(255),"
            + FILTERNAME + " VARCHAR(255),"
            + FORMITEMID + " VARCHAR(255));";


    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sFilterId Идентификатор справочника
     * @return
     */
    public static Cursor getInfoByVisit(DataBaseHelper dbHelper, String sFilterId){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + FILTERID + " = " + sFilterId;

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
