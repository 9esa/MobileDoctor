package mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.visitAction;

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
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.extraFieldsStatTalon.LoadExtraField;

/**
 * Created by kirichenko on 05.05.2015.
 * Класс загружающий информацию о визите
 */
public class LoadVisit extends CommonMainLoading implements ICallbackForLoadingMainInformation {

    private LoginAccount oLoginAccount;

    private DataBaseHelper sqlHelper;

    private Context mContext;

    private String sAddress;

    private String sVisit;
    private ILoadedComleted iLoadedComleteCallBack;

    public LoadVisit(LoginAccount oLoginAccount, DataBaseHelper sqlHelper, String sAddress) {
        this.oLoginAccount = oLoginAccount;
        this.sqlHelper = sqlHelper;
        this.sAddress = sAddress;
    }

    public void startLoadInformationAboutVisit(String sVisitId, Context mContext, ILoadedComleted iLoadedComleteCallBack) {

        this.mContext = mContext;
        this.sVisit = sVisitId;
        this.iLoadedComleteCallBack = iLoadedComleteCallBack;

        String sValueAddressForRequest = "http://" + sAddress + "/doctor-web/api/housevisit/" + sVisitId + "/info";

        AsyncLoadingInformation loadingInfromation = new AsyncLoadingInformation("", this, 0);
        loadingInfromation.setsCurrentToken(oLoginAccount.getsToken());
        loadingInfromation.setsRequest(sValueAddressForRequest);
        loadingInfromation.setDaemon(true);
        loadingInfromation.start();

    }


    @Override
    public void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer) {

        saveLoadedItemToDB(converJsonToList(mContext, jsonObject), identificatieNummer);

    }

    @Override
    public void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer) {

        String sVisitId = null, sPatientId = null, sLastName = null, sFirstName = null, sSecondName = null, sSex = null,
                sBirthDate = null, sAge = null, sRegAddress = null, sLocAddress = null, sPhone = null, sCellular = null,
                sVisitTypeId = null, sVisitTypeText = null, sVisitDate = null, sVisitPlaceId = null, sVisitPlaceText = null,
                sVisTypeId = null, sVisTypeText = null, sVisitProfId = null, sVisitProfText = null, sCaseId = null, sCaseText = null,
                sCaseFinalityId = null, sCaseFinalityText = null, sCaseOutcomeId = null, sCaseOutcomeText = null,
                sCaseResultId = null, sCaseResultText = null, sMesId = null, sMesCode = null, sMesText = null, mesOpenDate = null;


        String cleanRequest = "DELETE FROM " + Visit.TABLE_NAME;
        sqlHelper.getWritableDatabase().execSQL(cleanRequest);

        if (dataAboutLoadedObjects != null) {

            for (int iCount = 0; iCount < dataAboutLoadedObjects.size(); iCount++) {

                JSONObject jsonObjectLoaderItem = dataAboutLoadedObjects.get(iCount);
                try {


                    sVisitId = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITID));
                    sPatientId = String.valueOf(jsonObjectLoaderItem.get(Visit.PATIENTID));
                    sLastName = String.valueOf(jsonObjectLoaderItem.get(Visit.LASTNAME));
                    sFirstName = String.valueOf(jsonObjectLoaderItem.get(Visit.FIRSTNAME));
                    sSecondName = String.valueOf(jsonObjectLoaderItem.get(Visit.SECONDNAME));
                    sSex = String.valueOf(jsonObjectLoaderItem.get(Visit.SEX));

                    sBirthDate = String.valueOf(jsonObjectLoaderItem.get(Visit.BIRTHDATE));
                    sAge = String.valueOf(jsonObjectLoaderItem.get(Visit.AGE));
                    sRegAddress = String.valueOf(jsonObjectLoaderItem.get(Visit.REGADDRESS));
                    sLocAddress = String.valueOf(jsonObjectLoaderItem.get(Visit.LOCADDRESS));
                    sPhone = String.valueOf(jsonObjectLoaderItem.get(Visit.PHONE));
                    sCellular = String.valueOf(jsonObjectLoaderItem.get(Visit.CELLULAR));


                    sVisitTypeId = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITTYPEID));
                    sVisitTypeText = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITTYPETEXT));
                    sVisitDate = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITDATE));
                    sVisitPlaceId = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITPLACEID));
                    sVisitPlaceText = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITPLACETEXT));

                    sVisTypeId = String.valueOf(jsonObjectLoaderItem.get(Visit.VISTYPEID));
                    sVisTypeText = String.valueOf(jsonObjectLoaderItem.get(Visit.VISTYPETEXT));
                    sVisitProfId = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITPROFID));
                    sVisitProfText = String.valueOf(jsonObjectLoaderItem.get(Visit.VISITPROFTEXT));
                    sCaseId = String.valueOf(jsonObjectLoaderItem.get(Visit.CASEID));
                    sCaseText = String.valueOf(jsonObjectLoaderItem.get(Visit.CASETEXT));

                    sCaseFinalityId = String.valueOf(jsonObjectLoaderItem.get(Visit.CASEFINALITYID));
                    sCaseFinalityText = String.valueOf(jsonObjectLoaderItem.get(Visit.CASEFINALITYTEXT));
                    sCaseOutcomeId = String.valueOf(jsonObjectLoaderItem.get(Visit.CASEOUTCOMEID));
                    sCaseOutcomeText = String.valueOf(jsonObjectLoaderItem.get(Visit.CASEOUTCOMETEXT));

                    sCaseResultId = String.valueOf(jsonObjectLoaderItem.get(Visit.CASERESULTID));
                    sCaseResultText = String.valueOf(jsonObjectLoaderItem.get(Visit.CASERESULTTEXT));
                    sMesId = String.valueOf(jsonObjectLoaderItem.get(Visit.MESID));
                    sMesCode = String.valueOf(jsonObjectLoaderItem.get(Visit.MESCODE));
                    sMesText = String.valueOf(jsonObjectLoaderItem.get(Visit.MESTEXT));
                    mesOpenDate = String.valueOf(jsonObjectLoaderItem.get(Visit.MESOPENDATE));

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                ContentValues values = new ContentValues();
                values.put(Visit.VISITID, sVisitId);
                values.put(Visit.PATIENTID, sPatientId);
                values.put(Visit.LASTNAME, sLastName);
                values.put(Visit.FIRSTNAME, sFirstName);
                values.put(Visit.SECONDNAME, sSecondName);
                values.put(Visit.SEX, sSex);

                values.put(Visit.BIRTHDATE, sBirthDate);
                values.put(Visit.AGE, sAge);
                values.put(Visit.REGADDRESS, sRegAddress);
                values.put(Visit.LOCADDRESS, sLocAddress);
                values.put(Visit.PHONE, sPhone);
                values.put(Visit.CELLULAR, sCellular);

                values.put(Visit.VISITTYPEID, sVisitTypeId);
                values.put(Visit.VISITTYPETEXT, sVisitTypeText);
                values.put(Visit.VISITDATE, sVisitDate);
                values.put(Visit.VISITPLACEID, sVisitPlaceId);
                values.put(Visit.VISITPLACETEXT, sVisitPlaceText);

                values.put(Visit.VISTYPEID, sVisTypeId);
                values.put(Visit.VISTYPETEXT, sVisTypeText);
                values.put(Visit.VISITPROFID, sVisitProfId);
                values.put(Visit.VISITPROFTEXT, sVisitProfText);
                values.put(Visit.CASEID, sCaseId);
                values.put(Visit.CASETEXT, sCaseText);

                values.put(Visit.CASEFINALITYID, sCaseFinalityId);
                values.put(Visit.CASEFINALITYTEXT, sCaseFinalityText);
                values.put(Visit.CASEOUTCOMEID, sCaseOutcomeId);
                values.put(Visit.CASEOUTCOMETEXT, sCaseOutcomeText);

                values.put(Visit.CASERESULTID, sCaseResultId);
                values.put(Visit.CASERESULTTEXT, sCaseResultText);
                values.put(Visit.MESID, sMesId);
                values.put(Visit.MESCODE, sMesCode);
                values.put(Visit.MESTEXT, sMesText);
                values.put(Visit.MESOPENDATE, mesOpenDate);

                sqlHelper.getWritableDatabase().insertWithOnConflict(Visit.TABLE_NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);

            }
        }

        new LoadExtraField(oLoginAccount, sqlHelper, sAddress, mContext).startLoadInformationExtraField(sVisit, iLoadedComleteCallBack);

    }
}
