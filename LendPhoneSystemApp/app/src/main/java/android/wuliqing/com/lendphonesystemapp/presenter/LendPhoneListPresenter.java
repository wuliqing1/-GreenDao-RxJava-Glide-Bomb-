package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.DataBase.LendPhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.DataListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.LendPhoneListView;

import java.util.List;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class LendPhoneListPresenter extends BasePresenter<LendPhoneListView> {

    public void onFetchedLendPhoneList(long phone_id) {
        mView.onShowLoading();
        loadData(phone_id);
    }

    public void loadData(final long phone_id) {
        dataSyncTools.loadData(new DataListener<List<LendPhoneNote>>() {
            @Override
            public void onComplete(List<LendPhoneNote> result) {
                mView.onHideLoading();
                mView.onFetchedLendPhones(result);
            }
        }, new LendPhoneTableAction(phone_id));
    }
}
