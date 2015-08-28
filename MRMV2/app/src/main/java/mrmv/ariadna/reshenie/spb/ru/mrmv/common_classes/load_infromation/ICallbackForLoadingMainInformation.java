package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.load_infromation;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by kirichenko on 29.04.2015.
 * Позволяет получить информацию загруженную с сервера
 */
public interface ICallbackForLoadingMainInformation {
    //Колбэк о полученной информации
    void getLoadedInformation(JSONObject jsonObject, Object identificatieNummer);

    //Обязательно должны сохранить информацию
    void saveLoadedItemToDB(ArrayList<JSONObject> dataAboutLoadedObjects, Object identificatieNummer);
}
