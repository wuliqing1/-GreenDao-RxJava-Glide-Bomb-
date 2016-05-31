package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;

import java.util.List;

import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenter extends BasePresenter<PhoneListView> {
    private static final String TAG = "PhoneListPresenter";
    private DataBaseAction mDataBaseAction = new PhoneTableAction();

    public void loadData() {
        List<PhoneNote> list = mDataBaseAction.query();//从数据库获取
        if (mView != null) {
            mView.onFetchedPhones(list);
        }
    }

    public void removeData(long id) {
        mDataBaseAction.remove(id);
    }

    public void queryPhoneWithPhoneName(String key) {
        mDataBaseAction.queryWithKey(PhoneNoteDao.Properties.Phone_name.columnName, key);
    }

    public void setDataBaseAction(DataBaseAction dataBaseAction) {
        mDataBaseAction = dataBaseAction;
    }
}
