package android.wuliqing.com.lendphonesystemapp.mvpview;

import android.wuliqing.com.lendphonesystemapp.model.PhoneDetailNote;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface PhoneDetailView extends MvpView {
    public void onLendPhoneResult(LendPhoneNote result);

    public void onQueryPhoneResult(PhoneDetailNote result);
//    public void onSyncPhoneResult(boolean result);
}
