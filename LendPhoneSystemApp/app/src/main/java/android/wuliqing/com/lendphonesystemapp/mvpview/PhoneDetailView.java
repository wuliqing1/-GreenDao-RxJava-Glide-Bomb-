package android.wuliqing.com.lendphonesystemapp.mvpview;

import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.PhoneDetailNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface PhoneDetailView extends MvpView {
    public void onLendPhoneResult(BmobLendPhoneNote result);

    public void onQueryPhoneResult(PhoneDetailNote result);
//    public void onSyncPhoneResult(boolean result);
}
