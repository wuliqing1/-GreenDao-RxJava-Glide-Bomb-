package android.wuliqing.com.lendphonesystemapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.wuliqing.com.lendphonesystemapp.fragment.PhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.presenter.MainPresenter;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

public class LendPhoneMainActivity extends BaseToolBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {
    private static final String TAG = "LendPhoneMainActivity";
    private MainPresenter mainPresenter = new MainPresenter();
    private FragmentManager mFragmentManager;
    private PhoneListFragment mPhoneListFragment = new PhoneListFragment();
    private ImageView sync_iv;
    @Override
    protected void detachPresenter() {
        mainPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mainPresenter.attach(this);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_lend_phone_main;
    }

    @Override
    protected void initWidgets() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        mFragmentManager = getFragmentManager();
        mFragmentManager.beginTransaction().add(R.id.fragment_layout_content, mPhoneListFragment).commitAllowingStateLoss();
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        View view = getLayoutInflater().inflate(R.layout.toolbar_custom_view, null);
        sync_iv = (ImageView) view.findViewById(R.id.action_sync_iv);
        sync_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RotateAnimation animation = new RotateAnimation(0, 360);
//                animation.setDuration(100000);//设定转一圈的时间
                animation.setRepeatCount(Animation.INFINITE);//设定无限循环
                animation.setRepeatMode(Animation.RESTART);
                sync_iv.startAnimation(animation);
                mainPresenter.syncLocalDataBaseAndNetWork();
            }
        });
        mToolbar.addView(view);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lend_phone_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_phone) {
            Intent intent = new Intent(this, EditPhoneActivity.class);
            startActivityForResult(intent, EditPhoneActivity.ADD_PHONE_REQUEST_CODE);
            return true;
        }
//        else if (id == R.id.action_sync_phone) {
//            mainPresenter.syncLocalDataBaseAndNetWork();
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditPhoneActivity.ADD_PHONE_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean is_update = data.getBooleanExtra(EditPhoneActivity.ADD_PHONE_RESULT_KEY, false);
            if (is_update) {
                mainPresenter.syncLocalDataBaseAndNetWork();
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mFragmentManager.beginTransaction().replace(R.id.fragment_layout_content, mPhoneListFragment).commit();
        } else if (id == R.id.nav_clear) {
//            mainPresenter.clearDataBase();
//            ToastUtils.show(this, R.string.clear_database_success);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSyncResult(boolean result) {
        if (result) {
            mPhoneListFragment.updateData();
        } else {
            ToastUtils.show(this, getString(R.string.sync_error_msg));
        }
    }
}
