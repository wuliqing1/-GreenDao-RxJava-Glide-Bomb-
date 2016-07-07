package android.wuliqing.com.lendphonesystemapp;

import android.app.Application;
import android.content.Context;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;

import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobConfig;

/**
 * Created by 10172915 on 2016/5/25.
 */
public class LendPhoneApplication extends Application {
    private static Context mContext;
    private static RefWatcher mRefWatcher;
    /**
     * SDK初始化也可以放到Application中
     */
    public static String APPID = "6c41a1b4c6d84bc87434ae20eee6002e";
    @Override
    public void onCreate() {
        super.onCreate();
        DBHelper.init(this);
        mContext = this;
        mRefWatcher = LeakCanary.install(this);

        //提供以下两种方式进行初始化操作：
//		//第一：设置BmobConfig，允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)
		BmobConfig config =new BmobConfig.Builder(this)
//		//设置appkey
		.setApplicationId(APPID)
//		//请求超时时间（单位为秒）：默认15s
		.setConnectTimeout(30)
//		//文件分片上传时每片的大小（单位字节），默认512*1024
		.setUploadBlockSize(100 * 1024)
//		//文件的过期时间(单位为秒)：默认1800s
		.setFileExpiration(5500)
		.build();
		Bmob.initialize(config);
        //第二：默认初始化
//        Bmob.initialize(this, APPID);

    }

    public static Context getAppContext() {
        return mContext;
    }

    public static RefWatcher getRefWatcher() {
        return mRefWatcher;
    }
}
