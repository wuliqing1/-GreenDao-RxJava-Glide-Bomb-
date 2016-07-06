package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface AddPhoneView extends MvpView {
    public void onResult(boolean result);

    public void onDeleteResult(boolean result);

    public void onQueryPhoneNameResult(List<String> list);

    public void onQueryProjectNameResult(List<String> list);

    public void onQueryPhone(PhoneNote phoneNote);
}
