package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class MainPresenter extends BasePresenter<MainView> {
    public void syncLocalDataBaseAndNetWork() {
        mView.onSyncResult(null);
    }

    public void clearDataBase() {
        DBHelper.getInstance().getPhoneNoteDao().deleteAll();
        DBHelper.getInstance().getLendPhoneNoteDao().deleteAll();
    }
}
