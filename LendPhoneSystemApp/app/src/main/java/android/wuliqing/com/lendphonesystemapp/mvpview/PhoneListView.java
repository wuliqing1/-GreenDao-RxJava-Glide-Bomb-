package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/26.
 */
public interface PhoneListView extends MvpView{
    void onFetchedPhones(List<PhoneNote> phoneNotes);

    //    void onRemoveResult(boolean result);
//
//    void onQueryResult(List<PhoneNoteModel> phoneNotes);
//    void onUpdateOnePhoneCompleted();
}
