package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public interface MainView extends MvpView{
    public void onSyncResult(List<PhoneNote> phoneNotes);
}
