package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.DataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class AddPhonePresenter extends BasePresenter<AddPhoneView> {
    public void addPhone(final PhoneNote phoneNote) {
        mView.onShowLoading();
        dataSyncTools.addData(new UpdateDataListener() {
            @Override
            public void onResult(boolean result) {
                mView.onHideLoading();
                mView.onResult(result);
            }
        }, new PhoneTableAction(), phoneNote);
    }
}
