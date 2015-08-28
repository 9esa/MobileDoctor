package mrmv.ariadna.reshenie.spb.ru.mrmv.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;


import mrmv.ariadna.reshenie.spb.ru.mrmv.activites.LauncherActivity;

//@RunWith(AndroidJUnit4.class)
public class FirstLoadTest
        extends ActivityInstrumentationTestCase2<LauncherActivity> {

    private Activity mActivity;

    public FirstLoadTest(Class<LauncherActivity> activityClass) {
        super(activityClass);
    }

//    @Before
//    public void setUp() throws Exception {
//        super.setUp();
//
//        // Injecting the Instrumentation instance is required
//        // for your test to run with AndroidJUnitRunner.
//        injectInstrumentation(InstrumentationRegistry.getInstrumentation());
//        mActivity = getActivity();
//    }
//
//    @Test
//    public void typeOperandsAndPerformAddOperation() {
//        // Call the CalculatorActivity add() method and pass in some operand values, then
//        // check that the expected value is returned.
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        super.tearDown();
//    }
}