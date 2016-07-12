package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.LendPhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.LendPhoneListView;

import java.util.List;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class LendPhoneListPresenter extends BasePresenter<LendPhoneListView> {

    private DataBaseAction mLendPhoneTableAction;

    public LendPhoneListPresenter(String phone_id) {
        if (phone_id == null) {
            throw new IllegalArgumentException();
        }
        mLendPhoneTableAction = new LendPhoneTableAction();
    }

    public void queryDataBaseAll(final String phone_id) {
        List<LendPhoneNote> list = mLendPhoneTableAction.query(phone_id);//从数据库获取
        if (mView != null) {
            mView.onFetchedLendPhones(list);
        }
    }

    public void setDatabaseAction(DataBaseAction databaseAction) {
        if (databaseAction == null) {
            throw new IllegalArgumentException();
        }
        mLendPhoneTableAction = databaseAction;
    }
}
