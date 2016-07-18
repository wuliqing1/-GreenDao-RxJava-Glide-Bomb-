package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface AddPhoneView extends MvpView {
    void onResult(boolean result, String id);

    void onDeleteResult(boolean result,String id);

    void onQueryPhoneNameResult(List<String> list);

    void onQueryProjectNameResult(List<String> list);

    void onQueryPhone(PhoneNote phoneNote);
}
