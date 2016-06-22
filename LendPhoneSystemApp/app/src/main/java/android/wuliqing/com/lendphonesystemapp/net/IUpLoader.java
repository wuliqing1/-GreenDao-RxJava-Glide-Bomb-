package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;

import java.io.File;

/**
 * Created by 10172915 on 2016/6/21.
 */
public interface IUpLoader {

    void upLoad(File file, String url, UpLoadDataListener upLoadDataListener);
}
