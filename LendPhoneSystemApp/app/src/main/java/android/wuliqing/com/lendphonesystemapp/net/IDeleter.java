package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.DeleteDataListener;

/**
 * Created by 10172915 on 2016/7/5.
 */
public interface IDeleter {
    void delete(String method, String url, String objId, DeleteDataListener updateDataListener);
}
