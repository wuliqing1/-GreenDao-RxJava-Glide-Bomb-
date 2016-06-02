package android.wuliqing.com.lendphonesystemapp;

import android.app.Application;
import android.content.Context;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by 10172915 on 2016/5/25.
 */
public class LendPhoneApplication extends Application {
    private static Context mContext;
    private static RefWatcher mRefWatcher;
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.init(this);
        mContext = this;
        mRefWatcher = LeakCanary.install(this);
    }

    public static Context getAppContext() {
        return mContext;
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
}
