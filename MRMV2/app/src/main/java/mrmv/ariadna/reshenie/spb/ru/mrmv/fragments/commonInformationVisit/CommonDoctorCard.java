package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.appointment.ChooseDoctorFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.doctorCards.DoctorCardFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.historyIllFragments.HistoryFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.mesFragments.MesFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.commonInformationVisit.questionListFragment.QuestionListFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.my_calls_item.AdapterMenuMyCalls;

/**
 * Created by kirichenko on 14.05.2015.
 * Класс держащий набор всех табов.
 */
public class CommonDoctorCard extends Fragment{

    /**
     * Выбранная информация для элемента
     */
    private AdapterMenuMyCalls.MenuHolder menuHolder;
    private LoginAccount oLoginAccount;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /**
         * Вынужденная мера передачи параметров для сохранения данных при переключении между вкладками
         */
        DoctorCardFragment.setoLoginAccount(oLoginAccount);
        DoctorCardFragment.setMenuHolder(menuHolder);

        HistoryFragment.setoLoginAccount(oLoginAccount);
        HistoryFragment.setMenuHolder(menuHolder);

        MesFragment.setoLoginAccount(oLoginAccount);
        MesFragment.setMenuHolder(menuHolder);

        QuestionListFragment.setoLoginAccount(oLoginAccount);
        QuestionListFragment.setMenuHolder(menuHolder);

        ChooseDoctorFragment.setoLoginAccount(oLoginAccount);
        ChooseDoctorFragment.setMenuHolder(menuHolder);
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View oView = inflater.inflate(R.layout.common_doctor_card_fragment, null);

        FragmentTabHost mTabHost = (FragmentTabHost)oView.findViewById(android.R.id.tabhost);

        mTabHost.setup(getActivity(), getChildFragmentManager(), android.R.id.tabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("stat_talon").setIndicator("Статистический талон"),
                DoctorCardFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("question_list").setIndicator("Протокол"),
                QuestionListFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("mes").setIndicator("МЭС"),
                MesFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("history_helth").setIndicator("История лечения"),
                HistoryFragment.class, null);

        mTabHost.addTab(mTabHost.newTabSpec("book_visit").setIndicator("Запись на прием"),
                ChooseDoctorFragment.class, null);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String s) {
                Log.d("TEst", "Test");
            }
        });

        return oView;

    }

    public void setMenuHolder(AdapterMenuMyCalls.MenuHolder menuHolder) {
        this.menuHolder = menuHolder;
    }

    public void setLoginAccount(LoginAccount oLoginAccount) {
        this.oLoginAccount = oLoginAccount;
    }

    public void setCurrentDay(String currentDay) {
        DoctorCardFragment.setCurrentDay(currentDay);
    }
}
