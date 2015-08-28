package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.MedicalCommonConstants;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 01.05.2015.
 */
public class MedicalGuides {

    final public static String TABLE_NAME  = "medical_guides";

    final public static String ID = "_id";
    final public static String IDGUIDES = "id_guides";
    final public static String CODE = "code";
    final public static String TEXT = "text";
    final public static String SHORTTEXT = "short_text";
    final public static String ISDEFAULT = "isDefault";
    final public static String TAG = "tag";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IDGUIDES + " VARCHAR(255),"
            + TAG + " VARCHAR(255),"
            + ISDEFAULT + " VARCHAR(255),"
            + CODE + " VARCHAR(255),"
            + TEXT + " VARCHAR(255),"
            + SHORTTEXT + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param iTAG Номер справочника который необходимо загрузить
     * @return
     */
    public static List<ItemGuides> getLabelMedicalGuide(DataBaseHelper dbHelper, int iTAG){

        List<ItemGuides> oItemsGuides = new ArrayList<ItemGuides>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + TAG + "=" + iTAG;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        ItemGuides oItemGuide = new ItemGuides();

        if(MedicalCommonConstants.GOAL_VISIT != iTAG){
            oItemGuide.setsIdGuides("");
            oItemGuide.setsCode("");
            oItemGuide.setsText("-");
            oItemGuide.setsShortText("-");
            oItemGuide.setsTag(String.valueOf(iTAG));
            oItemGuide.setsIsDefault("0");
            oItemsGuides.add(oItemGuide);
        }

        if (cursor.moveToFirst()) {
            do {
                oItemGuide = new ItemGuides();
                oItemGuide.setsIdGuides(cursor.getString(cursor.getColumnIndex(IDGUIDES)));
                oItemGuide.setsCode(cursor.getString(cursor.getColumnIndex(CODE)));
                oItemGuide.setsText(cursor.getString(cursor.getColumnIndex(TEXT)));
                oItemGuide.setsShortText(cursor.getString(cursor.getColumnIndex(SHORTTEXT)));
                oItemGuide.setsTag(cursor.getString(cursor.getColumnIndex(TAG)));
                oItemGuide.setsIsDefault(cursor.getString(cursor.getColumnIndex(ISDEFAULT)));
                oItemsGuides.add(oItemGuide);
            } while (cursor.moveToNext());
        }

       // cursor.close();

        return oItemsGuides;
    }


    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param iTAG Номер справочника который необходимо загрузить
     * @return
     */
    public static List<ItemGuides> getLabelMedicalGuideForRadioButton(DataBaseHelper dbHelper, int iTAG){

        List<ItemGuides> oItemsGuides = new ArrayList<ItemGuides>();

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + TAG + "=" + iTAG;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemGuides oItemGuide = new ItemGuides();
                oItemGuide.setsIdGuides(cursor.getString(cursor.getColumnIndex(IDGUIDES)));
                oItemGuide.setsCode(cursor.getString(cursor.getColumnIndex(CODE)));
                oItemGuide.setsText(cursor.getString(cursor.getColumnIndex(TEXT)));
                oItemGuide.setsShortText(cursor.getString(cursor.getColumnIndex(SHORTTEXT)));
                oItemGuide.setsTag(cursor.getString(cursor.getColumnIndex(TAG)));
                oItemGuide.setsIsDefault(cursor.getString(cursor.getColumnIndex(ISDEFAULT)));
                oItemsGuides.add(oItemGuide);
            } while (cursor.moveToNext());
        }

        return oItemsGuides;
    }

    public static Cursor getLabelMedicalGuideViaCursor(DataBaseHelper dbHelper, String iTAG){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + TAG + "=" + iTAG;

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }


    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    public static boolean checkGuides(DataBaseHelper dbHelper, int iTAG){
        return true;
    }

}