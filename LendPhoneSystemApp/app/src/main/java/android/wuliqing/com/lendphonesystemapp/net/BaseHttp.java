package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;

import java.io.File;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/21.
 */
public abstract class BaseHttp implements ISender, ILoader, IUpLoader {
    @Override
    public void load(String method, String url, LoadDataListener loadDataListener) {

    }

    @Override
    public void send(String method, String url, PhoneNote phoneNote, SendDataListener sendDataListener) {

    }

    @Override
    public void upLoad(File file, String url, UpLoadDataListener upLoadDataListener) {

    }
}
