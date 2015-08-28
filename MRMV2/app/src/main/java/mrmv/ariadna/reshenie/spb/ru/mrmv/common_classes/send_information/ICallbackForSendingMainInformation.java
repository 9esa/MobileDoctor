package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.send_information;

import org.json.JSONObject;

/**
 * Created by kirichenko on 07.05.2015.
 * Позволяет получать информацию об отправке информации на сервер
 */
public interface ICallbackForSendingMainInformation {

    //Колбэк о полученной информации
    void getLoadedInformation(JSONObject jsonObject, Object oIdentifyObject);

}
