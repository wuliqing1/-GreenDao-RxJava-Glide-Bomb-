package android.wuliqing.com.lendphonesystemapp;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.wuliqing.com.lendphonesystemapp.fragment.PhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.presenter.MainPresenter;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.util.List;

import zte.phone.greendao.PhoneNote;

public class LendPhoneMainActivity extends BaseToolBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView{
    private static final String TAG = "LendPhoneMainActivity";
    private MainPresenter mainPresenter = new MainPresenter();
    private FragmentManager mFragmentManager;
    private PhoneListFragment mPhoneListFragment = new PhoneListFragment();

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
            Intent intent = new Intent(this, AddPhoneActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_sync_phone) {
            mainPresenter.syncLocalDataBaseAndNetWork();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_home) {
            mFragmentManager.beginTransaction().replace(R.id.fragment_layout_content, mPhoneListFragment).commit();
        } else if (id == R.id.nav_clear) {
            mainPresenter.clearDataBase();
            ToastUtils.show(this, R.string.clear_database_success);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onSyncResult(List<PhoneNote> phoneNotes) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LendPhoneApplication.getRefWatcher().watch(this);
    }
}
