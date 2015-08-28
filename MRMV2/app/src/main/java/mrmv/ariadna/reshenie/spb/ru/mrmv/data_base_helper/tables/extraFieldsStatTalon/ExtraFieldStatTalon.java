package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.ItemGuides;

/**
 * Created by kirichenko on 26.05.2015.
 */
public class ExtraFieldStatTalon {

    final public static String TABLE_NAME  = "extra_field_stat_talon";

    final public static String ID = "id";
    final public static String FORMID = "formId";
    final public static String IDEXTRAFIELD = "id_extra_field_stat_talon";
    final public static String EXTRAFIELD = "extraField";
    final public static String CODE = "code";
    final public static String TEXT = "text";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + FORMID + " VARCHAR(255),"
            + EXTRAFIELD + " VARCHAR(255),"
            + IDEXTRAFIELD + " VARCHAR(255),"
            + CODE + " VARCHAR(255)," + TEXT + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }


    public static List<ItemGuides> getLabelExtraFieldStatTalon(DataBaseHelper dbHelper, String sFormId){

        List<ItemGuides> oItemsGuides = new ArrayList<ItemGuides>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + FORMID + "=" + sFormId + " AND " + EXTRAFIELD + " = " + "EXTRAFIELD"  ;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ItemGuides oItemGuide;// = new ItemGuides();

//        oItemGuide.setsIdGuides("");
//        oItemGuide.setsCode("");
//        oItemGuide.setsText("-");
//        oItemGuide.setsShortText("-");
//        oItemGuide.setsTag(sFormId);
//        oItemGuide.setsIsDefault("0");
//        oItemsGuides.add(oItemGuide);

        if (cursor.moveToFirst()) {
            do {
                oItemGuide = new ItemGuides();
                oItemGuide.setsIdGuides(cursor.getString(cursor.getColumnIndex(ID)));
                oItemGuide.setsCode(cursor.getString(cursor.getColumnIndex(CODE)));
                oItemGuide.setsText(cursor.getString(cursor.getColumnIndex(TEXT)));
                oItemGuide.setsShortText("-");
                oItemGuide.setsTag(cursor.getString(cursor.getColumnIndex(FORMID)));
                oItemGuide.setsIsDefault("0");
                oItemsGuides.add(oItemGuide);
            } while (cursor.moveToNext());
        }

        return oItemsGuides;
    }

    public static List<ItemGuides> getLabelExtraFieldEnableProtocols(DataBaseHelper dbHelper, String sFormId){

        List<ItemGuides> oItemsGuides = new ArrayList<ItemGuides>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + FORMID + "=" + sFormId + " AND " + EXTRAFIELD + " = " + "EXTRAFIELD"  ;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ItemGuides oItemGuide;// = new ItemGuides();

//        oItemGuide.setsIdGuides("");
//        oItemGuide.setsCode("");
//        oItemGuide.setsText("-");
//        oItemGuide.setsShortText("-");
//        oItemGuide.setsTag(sFormId);
//        oItemGuide.setsIsDefault("0");
//        oItemsGuides.add(oItemGuide);

        if (cursor.moveToFirst()) {
            do {
                oItemGuide = new ItemGuides();
                oItemGuide.setsIdGuides(cursor.getString(cursor.getColumnIndex(ID)));
                oItemGuide.setsCode(cursor.getString(cursor.getColumnIndex(CODE)));
                oItemGuide.setsText(cursor.getString(cursor.getColumnIndex(TEXT)));
                oItemGuide.setsShortText("-");
                oItemGuide.setsTag(cursor.getString(cursor.getColumnIndex(FORMID)));
                oItemGuide.setsIsDefault("0");
                oItemsGuides.add(oItemGuide);
            } while (cursor.moveToNext());
        }

        return oItemsGuides;
    }

}
