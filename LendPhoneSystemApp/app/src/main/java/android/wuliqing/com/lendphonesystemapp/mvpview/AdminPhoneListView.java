package android.wuliqing.com.lendphonesystemapp.mvpview;

import android.wuliqing.com.lendphonesystemapp.model.AdminPhoneDetailNote;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface AdminPhoneListView extends MvpView {
    public void onFetchedLendPhones(List<AdminPhoneDetailNote> adminPhoneDetailNotes);

    public void onAgreeResult(boolean result);
}
