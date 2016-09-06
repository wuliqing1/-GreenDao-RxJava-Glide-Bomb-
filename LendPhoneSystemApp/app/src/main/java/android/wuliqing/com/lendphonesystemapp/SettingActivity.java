package android.wuliqing.com.lendphonesystemapp;

import android.os.Bundle;
import android.wuliqing.com.lendphonesystemapp.fragment.SettingFragment;
import android.wuliqing.com.lendphonesystemapp.swipeBack.SwipeBackActivity;

/**
 * Created by hugo on 2016/2/19 0019.
 */
public class SettingActivity extends SwipeBackActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction().replace(R.id.framelayout, new SettingFragment()).commit();
    }

    @Override
    protected void detachPresenter() {

    }

    @Override
    protected void createPresenter() {

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initWidgets() {
        mToolbar.setTitle(R.string.settings_title);
    }

    @Override
    protected void initSwipeLayout() {

    }
}
