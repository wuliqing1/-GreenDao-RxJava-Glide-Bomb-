package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNoteHelp;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobHttp;

import java.util.List;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class MainPresenter extends BasePresenter<MainView> {
    private BaseHttp mBaseHttp = new BmobHttp();

    public void syncLocalDataBaseAndNetWork() {
        mBaseHttp.load(null, null, new LoadDataListener<List<BmobPhoneNote>>() {
            @Override
            public void onComplete(List<BmobPhoneNote> result) {
                BmobPhoneNoteHelp.updatePhoneNoteTable(result, new UpdateDataListener<Boolean>() {//更新本地数据库
                    @Override
                    public void onResult(Boolean result) {
                        if (result) {//更新本地数据库成功
                            mView.onSyncResult(true);
                        } else {//更新本地数据库失败
                            mView.onSyncResult(false);
                        }
                    }
                });
            }

            @Override
            public void onError() {
                mView.onSyncResult(false);
            }
        });
    }

    public void clearDataBase() {
        DBHelper.getInstance().getPhoneNoteDao().deleteAll();
        DBHelper.getInstance().getLendPhoneNoteDao().deleteAll();
    }
}
