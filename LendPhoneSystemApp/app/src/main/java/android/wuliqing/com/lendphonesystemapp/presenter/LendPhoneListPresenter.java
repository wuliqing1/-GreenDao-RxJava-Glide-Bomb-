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

    public LendPhoneListPresenter(long phone_id) {
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        mLendPhoneTableAction = new LendPhoneTableAction(phone_id);
    }

    public void loadData(final long phone_id) {
        queryDataBaseAll(phone_id);
    }

    public void queryDataBaseAll(final long phone_id) {
        List<LendPhoneNote> list = mLendPhoneTableAction.query();//从数据库获取
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
