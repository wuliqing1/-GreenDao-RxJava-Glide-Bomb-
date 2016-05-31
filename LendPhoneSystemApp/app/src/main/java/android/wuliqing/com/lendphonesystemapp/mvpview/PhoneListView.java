package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public interface PhoneListView extends MvpView{
    public void onFetchedPhones(List<PhoneNote> phoneNotes);

    public void onRemoveResult(boolean result);

    public void onQueryResult(List<PhoneNote> phoneNotes);
}
