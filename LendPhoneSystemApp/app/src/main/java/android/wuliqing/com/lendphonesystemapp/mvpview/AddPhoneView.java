package android.wuliqing.com.lendphonesystemapp.mvpview;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/27.
 */
public interface AddPhoneView extends MvpView {
    public void onResult(boolean result);

    public void onQueryPhoneNameResult(List<String> list);

    public void onQueryProjectNameResult(List<String> list);
}
