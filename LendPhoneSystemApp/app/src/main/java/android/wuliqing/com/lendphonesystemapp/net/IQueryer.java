package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;

/**
 * Created by 10172915 on 2016/6/30.
 */
public interface IQueryer {
    void queryOnePhoneWithId(String method, String url, String id, LoadDataListener loadDataListener);

    void queryWithColumn(String method, String url, String[] columns, String[] values, LoadDataListener loadDataListener);
}
