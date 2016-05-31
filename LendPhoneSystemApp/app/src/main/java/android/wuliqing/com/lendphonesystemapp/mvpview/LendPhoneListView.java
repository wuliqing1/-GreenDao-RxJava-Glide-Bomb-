package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface LendPhoneListView extends MvpView {
    public void onFetchedLendPhones(List<LendPhoneNote> lendPhoneNotes);

    public void onUpdateResult(boolean result);
}
