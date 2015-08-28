package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuidesMkb10;

import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 06.05.2015.
 */
public class MedicalGuidesMKB10 {

    final public static String TABLE_NAME = "medical_guides_mkb10";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;

    final public static String ID = "_id";
    final public static String IDGUIDES = "id";
    final public static String LEVEL = "level";
    final public static String ROOTID = "rootId";
    final public static String CODE = "code";
    final public static String TEXT = "text";
    final public static String NOTE = "note";
    final public static String CHRONICSTATUS = "chronicStatus";
    final public static String SEXSTATUS = "sexStatus";
    final public static String NOTMAINSTATUS = "notMainStatus";
    final public static String ISGROUP = "isGroup";

    final public static int IS_GROUP = 1;
    final public static int NOT_GROUP = 0;

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + IDGUIDES + " VARCHAR(255),"
            + LEVEL + " VARCHAR(255),"
            + ROOTID + " VARCHAR(255),"
            + CODE + " VARCHAR(255),"
            + TEXT + " VARCHAR(512),"
            + NOTE + " VARCHAR(4000),"
            + CHRONICSTATUS + " VARCHAR(255),"
            + SEXSTATUS + " VARCHAR(255),"
            + NOTMAINSTATUS + " VARCHAR(255),"
            + ISGROUP + " VARCHAR(255));";

    public static Cursor getOnLevel(DataBaseHelper oDataBaseHelper, int iSourceLevel, int iRootId ) {
        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + LEVEL + "=" + iSourceLevel + " AND " + ROOTID + "=" + iRootId;

        SQLiteDatabase db = oDataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        /**
         * Смотрим нужно ли добавлять фейковое меню
         */
        if (iSourceLevel > 1) {

            MatrixCursor extras = new MatrixCursor(new String[]{MedicalGuidesMKB10.ID, MedicalGuidesMKB10.IDGUIDES,MedicalGuidesMKB10.CODE, MedicalGuidesMKB10.TEXT, MedicalGuidesMKB10.LEVEL, MedicalGuidesMKB10.ROOTID});
            extras.addRow(new String[]{"-1", "-1", "", "...", String.valueOf(iSourceLevel), String.valueOf(iRootId)});
            Cursor[] cursors = {extras, cursor};
            Cursor extendedCursor = new MergeCursor(cursors);

            return extendedCursor;

        } else {

            return cursor;
        }
    }

    public static Cursor getSearchValue(DataBaseHelper oDataBaseHelper, String sValueForSearch) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + TEXT + " LIKE " + "'%" + sValueForSearch + "%'"  + " OR " + CODE + " LIKE " + "'%" + sValueForSearch + "%'" + " AND " + ISGROUP + " = " + NOT_GROUP;

        SQLiteDatabase db = oDataBaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;

    }

    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper){

        String selectQuery = "DELETE FROM " + TABLE_NAME ;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }
}
