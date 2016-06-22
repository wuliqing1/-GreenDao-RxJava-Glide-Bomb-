package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/21.
 */
public interface ISender {
    void send(String method, String url, PhoneNote phoneNote, SendDataListener sendDataListener);
}
