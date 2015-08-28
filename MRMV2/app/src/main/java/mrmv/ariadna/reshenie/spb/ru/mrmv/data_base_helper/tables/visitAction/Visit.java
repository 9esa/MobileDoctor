package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;

/**
 * Created by kirichenko on 05.05.2015.
 */
public class Visit {

    final public static String TABLE_NAME  = "visit_item";

    final public static String ID = "id";
    final public static String PATIENTID = "patientId";
    final public static String LASTNAME = "lastName";
    final public static String FIRSTNAME = "firstName";
    final public static String SECONDNAME = "secondName";
    final public static String SEX = "sex";
    final public static String BIRTHDATE = "birthDate";
    final public static String AGE = "age";

    final public static String REGADDRESS = "regAddress";
    final public static String LOCADDRESS = "locAddress";
    final public static String PHONE = "phone";
    final public static String CELLULAR = "cellular";
    final public static String VISITID = "visitId";
    final public static String VISITTYPEID = "visitTypeId";
    final public static String VISITTYPETEXT = "visitTypeText";
    final public static String VISITDATE = "visitDate";
    final public static String VISITPLACEID = "visitPlaceId";
    final public static String VISITPLACETEXT = "visitPlaceText";
    final public static String VISTYPEID = "visTypeId";
    final public static String VISTYPETEXT = "visTypeText";
    final public static String VISITPROFID = "visitProfId";
    final public static String VISITPROFTEXT = "visitProfText";
    final public static String CASEID = "caseId";
    final public static String CASETEXT = "caseText";
    final public static String CASEFINALITYID = "caseFinalityId";
    final public static String CASEFINALITYTEXT = "caseFinalityText";
    final public static String CASEOUTCOMEID = "caseOutcomeId";
    final public static String CASEOUTCOMETEXT = "caseOutcomeText";
    final public static String CASERESULTID = "caseResultId";
    final public static String CASERESULTTEXT = "caseResultText";
    final public static String MESID = "mesId";
    final public static String MESCODE = "mesCode";
    final public static String MESTEXT = "mesText";
    final public static String MESOPENDATE = "mesOpenDate";

    public static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TABLE_NAME + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + PATIENTID + " VARCHAR(255),"
            + LASTNAME + " VARCHAR(255),"
            + FIRSTNAME + " VARCHAR(255),"
            + SECONDNAME + " VARCHAR(255),"
            + SEX + " VARCHAR(255),"
            + BIRTHDATE + " VARCHAR(255),"
            + AGE + " VARCHAR(255),"
            + REGADDRESS + " VARCHAR(255),"
            + LOCADDRESS + " VARCHAR(255),"
            + PHONE + " VARCHAR(255),"
            + CELLULAR + " VARCHAR(255),"
            + VISITID + " VARCHAR(255),"
            + VISITTYPEID + " VARCHAR(255),"
            + VISITTYPETEXT + " VARCHAR(255),"
            + VISITDATE + " VARCHAR(255),"
            + VISITPLACEID + " VARCHAR(255),"
            + VISITPLACETEXT + " VARCHAR(255),"
            + VISTYPEID + " VARCHAR(255),"
            + VISTYPETEXT + " VARCHAR(255),"
            + VISITPROFID + " VARCHAR(255),"
            + VISITPROFTEXT + " VARCHAR(255),"
            + CASEID + " VARCHAR(255),"
            + CASETEXT + " VARCHAR(255),"
            + CASEFINALITYID + " VARCHAR(255),"
            + CASEFINALITYTEXT + " VARCHAR(255),"
            + CASEOUTCOMEID + " VARCHAR(255),"
            + CASEOUTCOMETEXT + " VARCHAR(255),"
            + CASERESULTID + " VARCHAR(255),"
            + CASERESULTTEXT + " VARCHAR(255),"
            + MESID + " VARCHAR(255),"
            + MESCODE + " VARCHAR(255),"
            + MESTEXT + " VARCHAR(255),"
            + MESOPENDATE + " VARCHAR(255));";

    public static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TABLE_NAME;


    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param oItemVisit Объект содержащий инфо
     * @return Информация об успешности записи
     */
    public static boolean saveInfoByVisit(DataBaseHelper dbHelper, ItemVisit oItemVisit){


        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(VISITID, oItemVisit.getsVisitId());
        values.put(VISITTYPEID, oItemVisit.getsVisitTypeId());
        values.put(VISITPLACEID, oItemVisit.getsVisitPlaceId());
        values.put(VISTYPEID, oItemVisit.getsVisTypeId());
        values.put(VISITPROFID, oItemVisit.getsVisitProfId());
        values.put(CASEID, oItemVisit.getsCaseId());
        values.put(CASEFINALITYID,oItemVisit.getsCaseFinalityId());
        values.put(CASEOUTCOMEID,oItemVisit.getsCaseOutcomeId());
        values.put(CASERESULTID, oItemVisit.getsCaseResultId());

        db.insertWithOnConflict(Visit.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

        return true;
    }

    /**
     *
     * @param dbHelper Класс для работы с базой данных
     * @param sIdVisit Номер визита для получения информации
     * @return Курсор с полученными данными
     */
    public static Cursor getInformationAboutVisit(DataBaseHelper dbHelper, String sIdVisit){

        String selectQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE " + VISITID + "=" + sIdVisit;

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
