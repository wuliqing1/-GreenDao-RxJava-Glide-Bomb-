package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;

/**
 * Created by 10172915 on 2016/6/21.
 */
public interface ILoader {
    void load(String method, String url, LoadDataListener loadDataListener);
}
