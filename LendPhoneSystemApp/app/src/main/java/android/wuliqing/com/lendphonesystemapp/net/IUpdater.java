package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/7/1.
 */
public interface IUpdater {
    public void update(String method, String url, PhoneNote phoneNote, UpdateDataListener updateDataListener);
}
