package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadFileListener;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/21.
 */
public class BmobHttp extends BaseHttp {
    @Override
    public void send(String method, String url, final PhoneNote phoneNote, final SendDataListener sendDataListener) {
        BmobPhoneNote.transformPhoneNote(phoneNote).save(LendPhoneApplication.getAppContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                sendDataListener.onSuccess();
            }

            @Override
            public void onFailure(int i, String s) {
                sendDataListener.onFailure(i, s);
            }
        });
    }

    @Override
    public void load(String method, String url, final LoadDataListener loadDataListener) {
        BmobQuery<BmobPhoneNote> bmobPhoneNoteBmobQuery = new BmobQuery<>();
        bmobPhoneNoteBmobQuery.findObjects(LendPhoneApplication.getAppContext(), new FindListener<BmobPhoneNote>() {
            @Override
            public void onSuccess(List<BmobPhoneNote> list) {
                loadDataListener.onComplete(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), s);
                loadDataListener.onError();
            }
        });
    }

    @Override
    public void upLoad(File file, String url, final UpLoadDataListener upLoadDataListener) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(LendPhoneApplication.getAppContext(), new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                upLoadDataListener.onComplete(bmobFile.getFileUrl(LendPhoneApplication.getAppContext()));
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                upLoadDataListener.onProgress(value);
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), msg);
                upLoadDataListener.onError();
            }
        });
    }
}
