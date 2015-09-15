package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.rlsAction;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 12.09.2015.
 */
public class RLSItem {

    final public static String TABLE_NAME = "item_rls";

    final public static String ID = "_id";
    final public static String CODE = "code";
    final public static String TRADENAME = "tradename";
    final public static String LATNAME = "latname";
    final public static String NDV = "ndv";
    final public static String KIND = "kind";
    final public static String LIFE = "life";
    final public static String FORM = "form";
    final public static String DOSAGE = "dosage";
    final public static String FILLING = "filling";
    final public static String PACKING = "packing";
    final public static String MANUFACTURER = "manufacturer";
    final public static String MCOUNTRY = "mcountry";
    final public static String DISTRIBUTOR = "distributor";
    final public static String DCOUNTRY = "dcountry";
    final public static String PACKER = "packer";
    final public static String PCOUNTRY = "pcountry";
    final public static String BARCODE = "barcode";
    final public static String REGNUM = "regnum";
    final public static String REGDATE = "regdate";
    final public static String REGISTRATOR = "registrator";
    final public static String RCOUNTRY = "rcountry";
    final public static String PRICE = "price";
    final public static String AGE = "age";
    final public static String GRPNAME = "grpname";
    final public static String MKB = "mkb";
    final public static String ATC = "atc";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + CODE + " VARCHAR(255),"
            + TRADENAME + " VARCHAR(255),"
            + LATNAME + " VARCHAR(255),"
            + NDV + " VARCHAR(255),"
            + KIND + " VARCHAR(255),"
            + LIFE + " VARCHAR(255),"
            + FORM + " VARCHAR(255),"
            + DOSAGE + " VARCHAR(255),"
            + FILLING + " VARCHAR(255),"
            + PACKING + " VARCHAR(255),"
            + MANUFACTURER + " VARCHAR(255),"
            + MCOUNTRY + " VARCHAR(255),"
            + DISTRIBUTOR + " VARCHAR(255),"
            + DCOUNTRY + " VARCHAR(255),"
            + PACKER + " VARCHAR(255),"
            + PCOUNTRY + " VARCHAR(255),"
            + BARCODE + " VARCHAR(255),"
            + REGNUM + " VARCHAR(255),"
            + REGDATE + " VARCHAR(255),"
            + REGISTRATOR + " VARCHAR(255),"
            + RCOUNTRY + " VARCHAR(255),"
            + PRICE + " VARCHAR(255),"
            + AGE + " VARCHAR(255),"
            + GRPNAME + " VARCHAR(255),"
            + MKB + " VARCHAR(255),"
            + ATC + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    public static Cursor getSearchValue(DataBaseHelper oDataBaseHelper) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = oDataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;

    }

    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper) {

        String selectQuery = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

}
