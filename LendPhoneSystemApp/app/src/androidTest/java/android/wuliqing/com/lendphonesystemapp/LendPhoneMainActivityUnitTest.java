package android.wuliqing.com.lendphonesystemapp;

import android.support.design.widget.FloatingActionButton;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;

/**
 * Created by 10172915 on 2016/5/24.
 */
public class LendPhoneMainActivityUnitTest extends ActivityInstrumentationTestCase2<LendPhoneMainActivity> {
    LendPhoneMainActivity lendPhoneMainActivity;
    FloatingActionButton floatingActionButton;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        lendPhoneMainActivity = getActivity();
        floatingActionButton = findViewById(R.id.fab);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testWidgetVisible() {
        assertEquals(true, floatingActionButton.getVisibility() == View.GONE);
    }

    public LendPhoneMainActivityUnitTest() {
        super(LendPhoneMainActivity.class);
    }

    public LendPhoneMainActivityUnitTest(Class<LendPhoneMainActivity> activityClass) {
        super(activityClass);
    }

    private <T extends View> T findViewById(int id) {
        return (T) lendPhoneMainActivity.findViewById(id);
    }
}
