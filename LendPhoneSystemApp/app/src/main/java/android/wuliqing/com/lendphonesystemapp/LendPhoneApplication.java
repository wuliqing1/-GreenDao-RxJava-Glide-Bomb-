package android.wuliqing.com.lendphonesystemapp;

import android.app.Application;
import android.wuliqing.com.lendphonesystemapp.DataBase.DBHelper;

/**
 * Created by 10172915 on 2016/5/25.
 */
public class LendPhoneApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.init(this);
    }
}
