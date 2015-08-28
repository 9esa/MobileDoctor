package mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.select_value_via_request;

import android.database.Cursor;
import android.widget.AdapterView;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.DataBaseHelper;
import mrmv.ariadna.reshenie.spb.ru.mrmv.data_base_helper.tables.ICommonLoadComplete;
import mrmv.ariadna.reshenie.spb.ru.mrmv.services.ServiceLoading;

/**
 * Created by kirichenko on 19.06.2015.
 * Интерфейс необходимый для общения между диалогом и классом вызвавшим его
 */
public interface IRouteDialogWithRequest {

    /**
     * Запускаем получение информации по визиту и запись этой информации в базу данных
     * @param oServiceLoading Сервис для загрузк иданных
     * @param oLoginAccount Авторизованный пользователь
     * @param oObjectForRequest Объект для запроса данных у сервера
     * @param iCommonLoadComplete Колбэк, в который придет сообщение об успешной загрузке
     */
    void getInformationByObject(ServiceLoading oServiceLoading, LoginAccount oLoginAccount, Object oObjectForRequest, ICommonLoadComplete iCommonLoadComplete);

    /**
     * Вызываем данную функцию для загрузки информации из базы данных
     * @param oDataBaseHelper Помошник для рабоыт с базой данных
     * @param oRequest Объект по которому необходимо осуществить запрос
     * @return Курсор который вернет информацию
     */
    Cursor loadInformationFromDataBase (DataBaseHelper oDataBaseHelper, Object oRequest);

    /**
     * Позволяет получить выбранное значение
     * @param adapterView Адаптер с полученнымми значениями
     * @param iItemSelected Выбранный элемент
     */
    void returnSelectedValue(AdapterView<?> adapterView, int iItemSelected);

}
