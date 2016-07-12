package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;

/**
 * Created by 10172915 on 2016/6/21.
 */
public interface ISender {
    void send(String method, String url, Object phoneNote, SendDataListener sendDataListener);
}
