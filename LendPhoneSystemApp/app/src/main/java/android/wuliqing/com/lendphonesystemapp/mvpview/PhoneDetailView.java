package android.wuliqing.com.lendphonesystemapp.mvpview;

import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.PhoneDetailNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface PhoneDetailView extends MvpView {
    void onLendPhoneResult(BmobLendPhoneNote result);

    void onQueryPhoneResult(PhoneDetailNote result);
//    void onSyncPhoneResult(boolean result);
}
