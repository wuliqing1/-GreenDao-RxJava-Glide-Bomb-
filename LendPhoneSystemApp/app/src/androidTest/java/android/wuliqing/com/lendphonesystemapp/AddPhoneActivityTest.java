package android.wuliqing.com.lendphonesystemapp;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * Created by 10172915 on 2016/5/31.
 */
public class AddPhoneActivityTest extends ActivityInstrumentationTestCase2<AddPhoneActivity> {
    AddPhoneActivity mAddPhoneActivity;
    private ImageView mAdd_phone_photo_view;
    private AutoCompleteTextView mAdd_phone_name_view;
    private EditText mAdd_phone_number_view;
    private AutoCompleteTextView mAdd_phone_project_view;
    public AddPhoneActivityTest() {
        super(AddPhoneActivity.class);
    }

    public AddPhoneActivityTest(Class<AddPhoneActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mAddPhoneActivity = getActivity();
        checkWidgets();
    }

    private void checkWidgets() {
        mAdd_phone_photo_view = findViewById(R.id.add_phone_photo_view);
        assertNotNull(mAdd_phone_photo_view);
        mAdd_phone_name_view = findViewById(R.id.add_phone_name_view);
        assertNotNull(mAdd_phone_name_view);
        assertEquals("", mAdd_phone_name_view.getText().toString());
        mAdd_phone_number_view = findViewById(R.id.add_phone_number_view);
        assertNotNull(mAdd_phone_number_view);
        assertEquals("", mAdd_phone_number_view.getText().toString());
        mAdd_phone_project_view = findViewById(R.id.add_phone_project_view);
        assertNotNull(mAdd_phone_project_view);
        assertEquals("", mAdd_phone_project_view.getText().toString());
    }

    public void testAddPhone() {
        mAddPhoneActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAdd_phone_photo_view.setImageResource(R.drawable.ic_arrow_back_white_2x);
                mAdd_phone_name_view.setText("P635A50");
                mAdd_phone_number_view.setText("10");
                mAdd_phone_project_view.setText("P6735M");
            }
        });
        getInstrumentation().waitForIdleSync();
        assertEquals("P635A50", mAdd_phone_name_view.getText().toString());
        assertEquals("10", mAdd_phone_number_view.getText().toString());
        assertEquals("P6735M",mAdd_phone_project_view.getText().toString());
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    private <T extends View> T findViewById(int id) {
        return (T) mAddPhoneActivity.findViewById(id);
    }
}
