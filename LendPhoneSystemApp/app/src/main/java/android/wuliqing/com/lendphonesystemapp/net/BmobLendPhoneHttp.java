package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 10172915 on 2016/7/11.
 */
public class BmobLendPhoneHttp extends BaseHttp {
    @Override
    public void send(String method, String url, Object obj, final SendDataListener sendDataListener) {
        final BmobLendPhoneNote bmobLendPhoneNote = (BmobLendPhoneNote) obj;
        bmobLendPhoneNote.save(LendPhoneApplication.getAppContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                sendDataListener.onSuccess(bmobLendPhoneNote);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "BmobLendPhoneHttp.send " + s);
            }
        });
    }

    @Override
    public void load(String method, String url, final LoadDataListener loadDataListener) {
        BmobQuery<BmobLendPhoneNote> bmobPhoneNoteBmobQuery = new BmobQuery<>();
        bmobPhoneNoteBmobQuery.findObjects(LendPhoneApplication.getAppContext(), new FindListener<BmobLendPhoneNote>() {
            @Override
            public void onSuccess(List<BmobLendPhoneNote> list) {
                loadDataListener.onComplete(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "load:" + s);
                loadDataListener.onError();
            }
        });
    }

    @Override
    public void queryWithColumn(String method, String url, String[] columns, String[] values, final LoadDataListener loadDataListener) {
        if (columns == null || values == null || columns.length != values.length) {
            throw new IllegalArgumentException();
        }
        BmobQuery<BmobLendPhoneNote> query = new BmobQuery<>();
        for (int i = 0; i < columns.length; i++) {
            query.addWhereEqualTo(columns[i], values[i]);
        }

        query.findObjects(LendPhoneApplication.getAppContext(), new FindListener<BmobLendPhoneNote>() {
            @Override
            public void onSuccess(List<BmobLendPhoneNote> list) {
                loadDataListener.onComplete(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "queryWithColumn:" + s);
            }
        });
    }
}
