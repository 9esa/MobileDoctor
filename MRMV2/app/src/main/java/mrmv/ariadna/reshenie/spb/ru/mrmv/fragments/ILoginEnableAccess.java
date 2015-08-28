package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments;

import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;

/**
 * Created by kirichenko on 04.05.2015.
 * Интерфейс который помогает определить, что загрузка закончена
 */
public interface ILoginEnableAccess {

    void enableAccessLoadFinished(LoginAccount loginAccount);

}
