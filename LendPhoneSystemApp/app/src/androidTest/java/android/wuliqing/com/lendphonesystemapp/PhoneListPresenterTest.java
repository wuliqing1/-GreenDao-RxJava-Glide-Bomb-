package android.wuliqing.com.lendphonesystemapp;

import android.test.AndroidTestCase;
import android.wuliqing.com.lendphonesystemapp.DataBase.DBHelper;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenterTest extends AndroidTestCase {
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertNotNull(mContext);
        DBHelper.init(mContext);
    }

    public void testOnFetchedPhoneList() {

    }
}
