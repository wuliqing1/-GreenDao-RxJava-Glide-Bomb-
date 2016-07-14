package android.wuliqing.com.lendphonesystemapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.wuliqing.com.lendphonesystemapp.adapter.AdminFragmentPagerAdapter;
import android.wuliqing.com.lendphonesystemapp.fragment.AdminApplyBackUpListFragment;
import android.wuliqing.com.lendphonesystemapp.fragment.AdminApplyPhoneListFragment;

public class AdminWorkActivity extends BaseToolBarActivity {
    private AdminFragmentPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void detachPresenter() {

    }

    @Override
    protected void createPresenter() {

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_admin_work;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (mToolbar != null) {
            mToolbar.setTitle(R.string.menu_admin_title);
        }
    }

    @Override
    protected void initWidgets() {
        mSectionsPagerAdapter = new AdminFragmentPagerAdapter(getSupportFragmentManager());
        mSectionsPagerAdapter.addFragment(new AdminApplyPhoneListFragment());
        mSectionsPagerAdapter.addFragment(new AdminApplyBackUpListFragment());
        mSectionsPagerAdapter.addTitle(getString(R.string.admin_apply_phone_tab));
        mSectionsPagerAdapter.addTitle(getString(R.string.admin_apply_backup_tab));
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

}
