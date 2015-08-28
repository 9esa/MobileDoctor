package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 * Структура заполненых протоколов
 * Created by kirichenko on 17.05.2015.
 */
public class StructureFullFieldProtocols {

    final public static String TABLE_NAME = "structure_full_field_protocols";
    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;
    final public static String ID = "id";
    final public static String PROTOCOLID = "formResultId";
    final public static String EXTRAFIELD = "extraField";
    final public static String VISITID = "visitid";
    final public static String FORMITEMID = "formItemId";
    final public static String TYP = "typ";
    final public static String VALUETYPE = "valueType";
    final public static String DATATYPE = "dataType";
    final public static String ISMULTI = "isMulti";
    final public static String ISLISTCHKR = "isListChkr";
    final public static String COLOR = "color";
    final public static String TEXT = "text";
    final public static String STATUS = "status";
    final public static String MANDATORY = "mandatory";
    final public static String ISBLOCKINPUT = "isBlockInput";
    final public static String DIAGTYPE = "diagType";
    final public static String DEFAULTVALUEID = "defaultValueId";
    final public static String DEFAULTVALUE = "defaultValue";
    final public static String FORMRESULTVALUEID = "formResultValueId";
    final public static String CTEXT = "cText";
    final public static String FILTERID = "filterId";



    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PROTOCOLID + " VARCHAR(512),"
            + EXTRAFIELD + " VARCHAR(512),"
            + VISITID + " VARCHAR(512),"
            + FORMITEMID + " VARCHAR(512),"
            + TYP + " VARCHAR(512),"
            + VALUETYPE + " VARCHAR(512),"
            + DATATYPE + " VARCHAR(512),"
            + ISMULTI + " VARCHAR(512),"
            + ISLISTCHKR + " VARCHAR(512),"
            + COLOR + " VARCHAR(512),"
            + TEXT + " VARCHAR(1024),"
            + STATUS + " VARCHAR(512),"
            + MANDATORY + " VARCHAR(512),"
            + ISBLOCKINPUT + " VARCHAR(512),"
            + DIAGTYPE + " VARCHAR(512),"
            + DEFAULTVALUEID + " VARCHAR(512),"
            + DEFAULTVALUE + " VARCHAR(512),"
            + FORMRESULTVALUEID + " VARCHAR(512),"
            + FILTERID + " VARCHAR(512),"
            + CTEXT + " VARCHAR(512));";

    /**
     * @param dbHelper      Класс для работы с базой данных
     * @param sFormResultId Номер протокола для получения информации
     * @return Курсор с полученными данными
     */
    public static Cursor getInformationAboutFullFieldProtocol(DataBaseHelper dbHelper, String sFormResultId) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + PROTOCOLID + " = " + sFormResultId +
                    " AND " + EXTRAFIELD + " = " + "'-'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }

    /**
     * @param dbHelper      Класс для работы с базой данных
     * @param sFormItemId Номер протокола для получения информации
     * @return Курсор с полученными данными
     */
    public static Cursor getInformationAboutEmptyProtocol(DataBaseHelper dbHelper, String sFormItemId) {

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + PROTOCOLID + " = " + sFormItemId +
                " AND " + EXTRAFIELD + " = " + "'-'";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return cursor;
    }
    public static void removeAllInfornationAboutTables(DataBaseHelper dbHelper) {

        String selectQuery = "DELETE FROM " + TABLE_NAME;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }

    public static void updateExtraFieldStattalon(DataBaseHelper dbHelper, ItemProtocols oItemProtocols) {

        if (oItemProtocols != null) {

            if (oItemProtocols.getsCtext() != null) {
                if (!oItemProtocols.getsCtext().isEmpty()) {
                    String selectQuery = "UPDATE " + TABLE_NAME + " SET " + CTEXT + " = " + "'" + oItemProtocols.getsCtext() + "'" +
                            " WHERE " + EXTRAFIELD + " = " + "'EXTRAFIELD'" + " AND " + VISITID + " = " + oItemProtocols.getsVisitId()
                            + " AND " + FORMITEMID + " = " + oItemProtocols.getsFormitemId() + " AND " + ID + " = " + oItemProtocols.getsId();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL(selectQuery);
                }
            }

        }
    }

    public static void updateFieldProtocol(DataBaseHelper dbHelper, ItemProtocols oItemProtocols) {

        if (oItemProtocols != null) {

            if (oItemProtocols.getsCtext() != null) {
                if (!oItemProtocols.getsCtext().isEmpty()) {
                    String selectQuery = "UPDATE " + TABLE_NAME + " SET " + CTEXT + " = " + "'" + oItemProtocols.getsCtext() + "'" +
                            " WHERE " + EXTRAFIELD + " = " + "'-'"
                            + " AND " + FORMITEMID + " = " + oItemProtocols.getsFormitemId() + " AND " + ID + " = " + oItemProtocols.getsId();
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    db.execSQL(selectQuery);
                }
            }

        }
    }

    public static List<ItemProtocols> getListEnableProtocols(DataBaseHelper dbHelper, String sVisitId) {

        List<ItemProtocols> listOfItemsGuides = new ArrayList<ItemProtocols>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + EXTRAFIELD + " = " + "'EXTRAFIELD'" + " AND " + VISITID + " = " + sVisitId;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemProtocols oItemProtocols = new ItemProtocols();
                oItemProtocols.setsId(cursor.getString(cursor.getColumnIndex(ID)));
                oItemProtocols.setsProtocolId(cursor.getString(cursor.getColumnIndex(PROTOCOLID)));
                oItemProtocols.setsExtraField(cursor.getString(cursor.getColumnIndex(EXTRAFIELD)));
                oItemProtocols.setsVisitId(cursor.getString(cursor.getColumnIndex(VISITID)));
                oItemProtocols.setsFormitemId(cursor.getString(cursor.getColumnIndex(FORMITEMID)));
                oItemProtocols.setsTyp(cursor.getString(cursor.getColumnIndex(TYP)));
                oItemProtocols.setsValueType(cursor.getString(cursor.getColumnIndex(VALUETYPE)));
                oItemProtocols.setsDataType(cursor.getString(cursor.getColumnIndex(DATATYPE)));
                oItemProtocols.setsIsmulti(cursor.getString(cursor.getColumnIndex(ISMULTI)));
                oItemProtocols.setsIslistchkr(cursor.getString(cursor.getColumnIndex(ISLISTCHKR)));
                oItemProtocols.setsColor(cursor.getString(cursor.getColumnIndex(COLOR)));
                oItemProtocols.setsText(cursor.getString(cursor.getColumnIndex(TEXT)));
                oItemProtocols.setsStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                oItemProtocols.setsMandatory(cursor.getString(cursor.getColumnIndex(MANDATORY)));
                oItemProtocols.setsIsBlockinput(cursor.getString(cursor.getColumnIndex(ISBLOCKINPUT)));
                oItemProtocols.setsDiagType(cursor.getString(cursor.getColumnIndex(DIAGTYPE)));
                oItemProtocols.setsDefaultValueId(cursor.getString(cursor.getColumnIndex(DEFAULTVALUEID)));
                oItemProtocols.setsDefaultValue(cursor.getString(cursor.getColumnIndex(DEFAULTVALUE)));
                oItemProtocols.setsFormResultValueId(cursor.getString(cursor.getColumnIndex(FORMRESULTVALUEID)));
                oItemProtocols.setsCtext(cursor.getString(cursor.getColumnIndex(CTEXT)));
                oItemProtocols.setsFilterId(cursor.getString(cursor.getColumnIndex(FILTERID)));
                listOfItemsGuides.add(oItemProtocols);

            } while (cursor.moveToNext());
        }

        return listOfItemsGuides;

    }

    public static List<ItemProtocols> getListEnableProtocolsToSend (DataBaseHelper dbHelper, String sFormId) {

        List<ItemProtocols> listOfItemsGuides = new ArrayList<ItemProtocols>();

        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE " + EXTRAFIELD + " = " + "'-'" + " AND " + PROTOCOLID + " = " + sFormId;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemProtocols oItemProtocols = new ItemProtocols();
                oItemProtocols.setsId(cursor.getString(cursor.getColumnIndex(ID)));
                oItemProtocols.setsProtocolId(cursor.getString(cursor.getColumnIndex(PROTOCOLID)));
                oItemProtocols.setsExtraField(cursor.getString(cursor.getColumnIndex(EXTRAFIELD)));
                oItemProtocols.setsVisitId(cursor.getString(cursor.getColumnIndex(VISITID)));
                oItemProtocols.setsFormitemId(cursor.getString(cursor.getColumnIndex(FORMITEMID)));
                oItemProtocols.setsTyp(cursor.getString(cursor.getColumnIndex(TYP)));
                oItemProtocols.setsValueType(cursor.getString(cursor.getColumnIndex(VALUETYPE)));
                oItemProtocols.setsDataType(cursor.getString(cursor.getColumnIndex(DATATYPE)));
                oItemProtocols.setsIsmulti(cursor.getString(cursor.getColumnIndex(ISMULTI)));
                oItemProtocols.setsIslistchkr(cursor.getString(cursor.getColumnIndex(ISLISTCHKR)));
                oItemProtocols.setsColor(cursor.getString(cursor.getColumnIndex(COLOR)));
                oItemProtocols.setsText(cursor.getString(cursor.getColumnIndex(TEXT)));
                oItemProtocols.setsStatus(cursor.getString(cursor.getColumnIndex(STATUS)));
                oItemProtocols.setsMandatory(cursor.getString(cursor.getColumnIndex(MANDATORY)));
                oItemProtocols.setsIsBlockinput(cursor.getString(cursor.getColumnIndex(ISBLOCKINPUT)));
                oItemProtocols.setsDiagType(cursor.getString(cursor.getColumnIndex(DIAGTYPE)));
                oItemProtocols.setsDefaultValueId(cursor.getString(cursor.getColumnIndex(DEFAULTVALUEID)));
                oItemProtocols.setsDefaultValue(cursor.getString(cursor.getColumnIndex(DEFAULTVALUE)));
                oItemProtocols.setsFormResultValueId(cursor.getString(cursor.getColumnIndex(FORMRESULTVALUEID)));
                oItemProtocols.setsCtext(cursor.getString(cursor.getColumnIndex(CTEXT)));
                oItemProtocols.setsFilterId(cursor.getString(cursor.getColumnIndex(FILTERID)));
                listOfItemsGuides.add(oItemProtocols);

            } while (cursor.moveToNext());
        }

        return listOfItemsGuides;

    }

    public static void removeAllInfornationAboutTables(DataBaseHelper sqlHelper, String sProtocolId) {
        String selectQuery = "DELETE FROM " + TABLE_NAME + " WHERE " + PROTOCOLID + " = " + sProtocolId;
        SQLiteDatabase db = sqlHelper.getWritableDatabase();
        db.execSQL(selectQuery);
    }
}
