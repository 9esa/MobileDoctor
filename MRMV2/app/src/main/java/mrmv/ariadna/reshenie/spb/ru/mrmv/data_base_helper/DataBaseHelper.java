package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.MedicalCommonConstants;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuides.MedicalGuides;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.actionMedicalGuidesMkb10.MedicalGuidesMKB10;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.callsAction.Calls;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction.Diagnose;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.doctors.Doctor;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon.ExtraFieldStatTalon;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.historyAction.History;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.informationAboutPatient.Patients;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.listOfDoctorsAction.EnableDoctors;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.Mes;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.mesAction.MesForDiagnoses;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.numbers.Numbers;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.EnableProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.FullFieldProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocols.StructureFullFieldProtocols;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.protocolsFiltersAction.ProtocolsFilter;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.specials.Special;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.statusDataBase.StatusDatabase;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction.Visit;

/**
 * Created by kirichenko on 01.05.2015.
 * Класс помогающий работать с базой данных
 */
public class DataBaseHelper extends SQLiteOpenHelper implements BaseColumns {

    private SharedPreferences sharedPreferences;

    private boolean bNewDataBase = false;

    private static DataBaseHelper mInstance;

    public static DataBaseHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context.getApplicationContext());
        }
        return mInstance;
    }

    public DataBaseHelper(Context context) {
        super(context, MedicalCommonConstants.DATABASE_NAME, null, MedicalCommonConstants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(StatusDatabase.SQL_CREATE_ENTRIES);
        db.execSQL(Calls.SQL_CREATE_ENTRIES);
        db.execSQL(MedicalGuides.SQL_CREATE_ENTRIES);
        db.execSQL(MedicalGuidesMKB10.SQL_CREATE_ENTRIES);
        db.execSQL(Diagnose.SQL_CREATE_ENTRIES);
        db.execSQL(History.SQL_CREATE_ENTRIES);
        db.execSQL(Visit.SQL_CREATE_ENTRIES);
        db.execSQL(EnableProtocols.SQL_CREATE_ENTRIES);
        db.execSQL(FullFieldProtocols.SQL_CREATE_ENTRIES);
        db.execSQL(StructureFullFieldProtocols.SQL_CREATE_ENTRIES);
        db.execSQL(ExtraFieldStatTalon.SQL_CREATE_ENTRIES);
        db.execSQL(Mes.SQL_CREATE_ENTRIES);
        db.execSQL(MesForDiagnoses.SQL_CREATE_ENTRIES);
        db.execSQL(EnableDoctors.SQL_CREATE_ENTRIES);
        db.execSQL(ProtocolsFilter.SQL_CREATE_ENTRIES);
        db.execSQL(Patients.SQL_CREATE_ENTRIES);
        db.execSQL(Numbers.SQL_CREATE_ENTRIES);
        db.execSQL(Special.SQL_CREATE_ENTRIES);
        db.execSQL(Doctor.SQL_CREATE_ENTRIES);

        setbNewDataBase(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("LOG_TAG", "Обновление базы данных с версии " + oldVersion
                + " до версии " + newVersion + ", которое удалит все старые данные");

        db.execSQL(StatusDatabase.SQL_DELETE_ENTRIES);
        db.execSQL(Calls.SQL_DELETE_ENTRIES);
        db.execSQL(MedicalGuides.SQL_DELETE_ENTRIES);
        db.execSQL(MedicalGuidesMKB10.SQL_DELETE_ENTRIES);
        db.execSQL(Diagnose.SQL_DELETE_ENTRIES);
        db.execSQL(History.SQL_DELETE_ENTRIES);
        db.execSQL(Visit.SQL_DELETE_ENTRIES);
        db.execSQL(EnableProtocols.SQL_DELETE_ENTRIES);
        db.execSQL(FullFieldProtocols.SQL_DELETE_ENTRIES);
        db.execSQL(StructureFullFieldProtocols.SQL_DELETE_ENTRIES);
        db.execSQL(ExtraFieldStatTalon.SQL_DELETE_ENTRIES);
        db.execSQL(Mes.SQL_DELETE_ENTRIES);
        db.execSQL(MesForDiagnoses.SQL_DELETE_ENTRIES);
        db.execSQL(EnableDoctors.SQL_DELETE_ENTRIES);
        db.execSQL(ProtocolsFilter.SQL_DELETE_ENTRIES);
        db.execSQL(Patients.SQL_DELETE_ENTRIES);
        db.execSQL(Numbers.SQL_DELETE_ENTRIES);
        db.execSQL(Special.SQL_DELETE_ENTRIES);
        db.execSQL(Doctor.SQL_DELETE_ENTRIES);

        onCreate(db);
    }

    public void deleteAllDataBase(SQLiteDatabase db){

        // Удаляем предыдущую таблицу при апгрейде
        StatusDatabase.removeAllInfornationAboutTables(this);
        Calls.removeAllInfornationAboutTables(this);
        MedicalGuides.removeAllInfornationAboutTables(this);
        MedicalGuidesMKB10.removeAllInfornationAboutTables(this);
        Diagnose.removeAllInfornationAboutTables(this);
        History.removeAllInfornationAboutTables(this);
        Visit.removeAllInfornationAboutTables(this);
        EnableProtocols.removeAllInfornationAboutTables(this);
        FullFieldProtocols.removeAllInfornationAboutTables(this);
        StructureFullFieldProtocols.removeAllInfornationAboutTables(this);
        ExtraFieldStatTalon.removeAllInfornationAboutTables(this);
        Mes.removeAllInfornationAboutTables(this);
        MesForDiagnoses.removeAllInfornationAboutTables(this);
        EnableDoctors.removeAllInfornationAboutTables(this);
        ProtocolsFilter.removeAllInfornationAboutTables(this);
        Patients.removeAllInfornationAboutTables(this);
        Numbers.removeAllInfornationAboutTables(this);
        Special.removeAllInfornationAboutTables(this);
        Doctor.removeAllInfornationAboutTables(this);

    }

    public boolean isbNewDataBase() {
        return bNewDataBase;
    }

    public void setbNewDataBase(boolean bNewDataBase) {
        this.bNewDataBase = bNewDataBase;
    }
}
