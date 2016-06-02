package android.wuliqing.com.lendphonesystemapp.mvpview;

import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/26.
 */
public interface PhoneListView extends MvpView{
    public void onFetchedPhones(List<PhoneNoteModel> phoneNotes);

    public void onRemoveResult(boolean result);

    public void onQueryResult(List<PhoneNoteModel> phoneNotes);

}
