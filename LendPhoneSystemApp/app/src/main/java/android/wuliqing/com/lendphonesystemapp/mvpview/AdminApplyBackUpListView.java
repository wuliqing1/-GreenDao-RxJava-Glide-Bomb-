package android.wuliqing.com.lendphonesystemapp.mvpview;

import android.wuliqing.com.lendphonesystemapp.model.AdminPhoneDetailNote;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface AdminApplyBackUpListView extends MvpView {
    void onFetchedLendPhones(List<AdminPhoneDetailNote> adminPhoneDetailNotes);

    void onBackUpResult(boolean result);
}
