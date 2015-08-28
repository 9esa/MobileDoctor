package mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;

import mrmv.ariadna.reshenie.spb.ru.mrmv.R;
import mrmv.ariadna.reshenie.spb.ru.mrmv.common_classes.LoginAccount;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.fragmentsInterface.IClickTabAction;

/**
 * Created by kirichenko on 23.04.2015.
 */                                                     //**FragmentPagerAdapter
public class MyCallsPageAdapterFragment extends FragmentStatePagerAdapter implements IClickTabAction {
    final int PAGE_COUNT = 3;

    private String tabTitles[];
    private Context context;
    private int iActionIndex;
    private LoginAccount loginAccount;

    private ArrayList<MyCallsFragment> listOfMyCallsFragments;

    public static final String ARG_PAGE = "ARG_PAGE";

    public MyCallsPageAdapterFragment(FragmentManager fragmentManager, Context context, LoginAccount loginAccount ) {
        super(fragmentManager);
        this.context = context;
        this.loginAccount = loginAccount;

                tabTitles = new String[] {
                context.getResources().getString(R.string.my_calls_all_calls_title),
                context.getResources().getString(R.string.my_calls_new_calls_title),
                context.getResources().getString(R.string.my_calls_old_calls_title)
        };

        listOfMyCallsFragments = new ArrayList<MyCallsFragment>();

        for (int iCount = 0; iCount < PAGE_COUNT; iCount++ ){
            listOfMyCallsFragments.add(MyCallsFragment.newInstance(iCount, loginAccount));
        }

    }

    @Override
    public Parcelable saveState() {
        return null;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        return listOfMyCallsFragments.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void onClickTab(int iIndex) {
        this.iActionIndex = iIndex;
    }

}
