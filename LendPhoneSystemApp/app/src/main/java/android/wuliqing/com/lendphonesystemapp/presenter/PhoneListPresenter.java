package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.DataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.DataListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenter extends BasePresenter<PhoneListView> {
    private static final String TAG = "PhoneListPresenter";

    public void onFetchedPhoneList() {
        mView.onShowLoading();
        loadData();
    }

    public void loadData() {
        dataSyncTools.loadData(new DataListener<List<PhoneNote>>() {
            @Override
            public void onComplete(List<PhoneNote> result) {
                mView.onHideLoading();
                mView.onFetchedPhones(result);
            }
        }, new PhoneTableAction());
    }
}
