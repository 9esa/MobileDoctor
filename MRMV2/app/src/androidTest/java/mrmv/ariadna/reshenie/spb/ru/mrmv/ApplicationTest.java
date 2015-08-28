package mrmv.ariadna.reshenie.spb.ru.mrmv;

import android.app.Activity;
import android.app.Fragment;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.test.suitebuilder.annotation.LargeTest;
import android.widget.Button;
import android.widget.EditText;

import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.HomeActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.LauncherActivity;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.LoginFragment;
import mrmv.ariadna.reshenie.spb.ru.mrmv.fragments.myCalls.MyCallsMenuFragmet;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
//public class ApplicationTest extends ApplicationTestCase<Application> {
public class ApplicationTest extends ActivityInstrumentationTestCase2<LauncherActivity> {
    private Activity mMainActivity;

    int TIMEOUT_IN_LOGIN = 10000;

    private String sValueForLogin = "5555";

    /**
     * Количество попыток залогинется
     */
    private int iTryLogin = 100;

    public ApplicationTest() {
        super(LauncherActivity.class);
    }

    protected void setUp() throws Exception
    {
        super.setUp();

    }

    @LargeTest
    public void testLoginAction()throws Exception {
        HomeActivity homeActivity = successfulLogin();

        assertNotNull(homeActivity);

        openStatTalon(homeActivity);


        successfulLogout(homeActivity);

    }

    private HomeActivity successfulLogin(){

        mMainActivity = getActivity();

        LauncherActivity oLauncherActivity = (LauncherActivity)getActivity();
        assertEquals(true,oLauncherActivity.isConnected());

        LoginFragment oLoginFragment = new LoginFragment();

        mMainActivity.getFragmentManager().beginTransaction()
                .replace(R.id.space_for_frame, oLoginFragment, "tag")
                .commit();

        getInstrumentation().waitForIdleSync();

        Fragment oCurrentFragment = mMainActivity.getFragmentManager().findFragmentByTag("tag");

        assertNotNull(oCurrentFragment.getView());

        EditText eLoginName  = (EditText)  oCurrentFragment.getView().findViewById(R.id.authentication_login);
        EditText ePassword  = (EditText)  oCurrentFragment.getView().findViewById(R.id.authentication_password);
        Button bButtonLogin =  (Button) oCurrentFragment.getView().findViewById(R.id.authentication_login_btn);

        assertNotNull(bButtonLogin);
        assertNotNull(eLoginName);
        assertNotNull(ePassword);

        ePassword.setText(sValueForLogin);

        Instrumentation.ActivityMonitor oActivityMonitor = getInstrumentation().addMonitor(HomeActivity.class.getName(), null, false);

        TouchUtils.clickView(this, bButtonLogin);

        HomeActivity receiverActivity = (HomeActivity)
                oActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_LOGIN);

        assertNotNull("HomeActivity is null", receiverActivity);
        assertEquals("Monitor for HomeActivity has not been called",
                1, oActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                HomeActivity.class, receiverActivity.getClass());

        getInstrumentation().removeMonitor(oActivityMonitor);

        return receiverActivity;
    }

    private boolean successfulLogout(HomeActivity homeActivity){

        getInstrumentation().waitForIdleSync();

        Button bButtonLogout =  (Button) homeActivity.findViewById(R.id.exite_button);

        Instrumentation.ActivityMonitor oActivityMonitor = getInstrumentation().addMonitor(LauncherActivity.class.getName(), null, false);

        // Validate that ReceiverActivity is started
        TouchUtils.clickView(this, bButtonLogout);

        LauncherActivity receiverActivity = (LauncherActivity)
                oActivityMonitor.waitForActivityWithTimeout(TIMEOUT_IN_LOGIN);

        assertNotNull("LauncherActivity is null", receiverActivity);
        assertEquals("Monitor for LauncherActivity has not been called",
                1, oActivityMonitor.getHits());
        assertEquals("Activity is of wrong type",
                LauncherActivity.class, receiverActivity.getClass());

        // Remove the ActivityMonitor
        getInstrumentation().removeMonitor(oActivityMonitor);

        return true;
    }

    private void openStatTalon(HomeActivity homeActivity){

        /*
         * Временный выбор мои вызовы
         */
        MyCallsMenuFragmet oMyCallsMenuFragmet = new MyCallsMenuFragmet();

        assertNotNull("Login account after authorization is null", homeActivity.getoLoginAccount());

        getInstrumentation().waitForIdleSync();

        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Fragment oCurrentFragment = homeActivity.getFragmentManager().findFragmentByTag("meCallsMenu");

        assertNotNull(oCurrentFragment.getView());

    }

}