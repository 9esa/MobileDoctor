package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.diagnoseAction;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.ILoadedComleted;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.AsyncLoadingInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation.ICallbackForLoadingMainInformation;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.CommonMainLoading;

/**
 * Created by kirichenko on 09.05.2015.
 */
public class LoadDiagnose extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    private ILoadedComleted iLoadedComleteCallBack;

    public LoadDiagnose(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationAboutDiagnoses(String sVisitId, Context mContext, ILoadedComleted iLoadedComleteCallBack) {

        this.mContext = mContext;

        this.iLoadedComleteCallBack = iLoadedComleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/" + sVisitId + "/diagnoslist";


        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, sVisitId);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }

    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {
        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);

        /**
         * Сообщаем карточки доктора, что мы загузили данные с сервера и можно получить информацию из базы данных
         */
        if (iLoadedComleteCallBack != null) {
            iLoadedComleteCallBack.loadedDiagnosesComleted();
        }
    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sId = null, sVisitId = null, sDiagnosType = null, sDiagnosId = null, sDiagnosCode = null,
                sDiagnosText = null, sIllTypeId = null, sIllTypeText = null, sWorseId = null,
                sWorseText = null, sCrimeId = null, sCrimeText = null, sStageId = null,
                sStageText = null, sDispId = null, sDispText = null, sDispOffId = null, sDispOffText = null,
                sTraumaId = null, sTraumaText = null;


        Diagnose.removeAllInfornationAboutTables(sqlHelper);

        //*Сперва удаляем все записи по этому справочнику
//        String cleanRequest = "DELETE FROM " + Diagnose.TABLE_NAME;
//        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject oItemGuides = dataAboutLoadedObjects.get(iCount);
                try {

                    sId = String.valueOf(oItemGuides.get(Diagnose.DIAGNOSEID));
                    sVisitId = String.valueOf(identificatieNummer);

                    sDiagnosType = String.valueOf(oItemGuides.get(Diagnose.DIAGNOSTYPE));
                    sDiagnosId = String.valueOf(oItemGuides.get(Diagnose.DIAGNOSID));

                    sDiagnosCode = String.valueOf(oItemGuides.get(Diagnose.DIAGNOSCODE));
                    sDiagnosText = String.valueOf(oItemGuides.get(Diagnose.DIAGNOSTEXT));

                    sIllTypeId = String.valueOf(oItemGuides.get(Diagnose.ILLTYPEID));
                    sIllTypeText = String.valueOf(oItemGuides.get(Diagnose.ILLTYPETEXT));

                    sWorseId = String.valueOf(oItemGuides.get(Diagnose.WORSEID));
                    sWorseText = String.valueOf(oItemGuides.get(Diagnose.WORSETEXT));
                    sCrimeId = String.valueOf(oItemGuides.get(Diagnose.CRIMEID));
                    sCrimeText = String.valueOf(oItemGuides.get(Diagnose.CRIMETEXT));
                    sStageId = String.valueOf(oItemGuides.get(Diagnose.STAGEID));

                    sStageText = String.valueOf(oItemGuides.get(Diagnose.STAGETEXT));
                    sDispId = String.valueOf(oItemGuides.get(Diagnose.DISPID));
                    sDispText = String.valueOf(oItemGuides.get(Diagnose.DISPTEXT));
                    sDispOffId = String.valueOf(oItemGuides.get(Diagnose.DISPOFFID));
                    sDispOffText = String.valueOf(oItemGuides.get(Diagnose.DISPOFFTEXT));

                    sTraumaId = String.valueOf(oItemGuides.get(Diagnose.TRAVMAID));
                    sTraumaText = String.valueOf(oItemGuides.get(Diagnose.TRAVMATEXT));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ContentValues values = new ContentValues();

                values.put(Diagnose.DIAGNOSEID, sId);
                values.put(Diagnose.VISITID, sVisitId);
                values.put(Diagnose.DIAGNOSTYPE, sDiagnosType);

                values.put(Diagnose.DIAGNOSID, sDiagnosId);
                values.put(Diagnose.DIAGNOSCODE, sDiagnosCode);
                values.put(Diagnose.DIAGNOSTEXT, sDiagnosText);
                values.put(Diagnose.ILLTYPEID, sIllTypeId);
                values.put(Diagnose.ILLTYPETEXT, sIllTypeText);

                values.put(Diagnose.WORSEID, sWorseId);
                values.put(Diagnose.WORSETEXT, sWorseText);
                values.put(Diagnose.CRIMEID, sCrimeId);
                values.put(Diagnose.CRIMETEXT, sCrimeText);
                values.put(Diagnose.STAGEID, sStageId);

                values.put(Diagnose.STAGETEXT, sStageText);
                values.put(Diagnose.DISPID, sDispId);
                values.put(Diagnose.DISPTEXT, sDispText);
                values.put(Diagnose.DISPOFFID, sDispOffId);
                values.put(Diagnose.DISPOFFTEXT, sDispOffText);

                values.put(Diagnose.TRAVMAID, sTraumaId);
                values.put(Diagnose.TRAVMATEXT, sTraumaText);
                values.put(Diagnose.STATING, Diagnose.BASE_DIAGNOS);

                sqlHelper.getWritableDatabase().insertWithOnConflict(Diagnose.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);


            }
        }

    }
}
