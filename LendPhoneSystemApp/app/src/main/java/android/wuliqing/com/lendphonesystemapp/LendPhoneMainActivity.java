package android.wuliqing.com.lendphonesystemapp;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.fragment.PhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.presenter.MainPresenter;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.bumptech.glide.Glide;

import cn.bmob.v3.BmobUser;

public class LendPhoneMainActivity extends BaseToolBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {
    private static final String TAG = "LendPhoneMainActivity";
    private MainPresenter mainPresenter = new MainPresenter();
    private FragmentManager mFragmentManager;
    private PhoneListFragment mPhoneListFragment = new PhoneListFragment();
    private ProgressDialog mProgressDialog;
    private ImageView user_photo_iv;
    private TextView user_name_tv;
    private TextView user_department;
    private TextView user_position;

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
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage(getString(R.string.sync_in_msg));
        initLogin(navigationView);
    }

    private void initLogin(NavigationView navigationView) {
        View view = navigationView.getHeaderView(0);
        user_photo_iv = (ImageView) view.findViewById(R.id.user_imageView);
        user_photo_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyUser myUser = BmobUser.getCurrentUser(LendPhoneMainActivity.this, MyUser.class);
                if (myUser != null) {
                    Intent intent = new Intent(LendPhoneMainActivity.this, UserActivity.class);
                    startActivityForResult(intent, UserActivity.USER_REQUEST_CODE);
                } else {
                    Intent intent = new Intent(LendPhoneMainActivity.this, LoginActivity.class);
                    startActivityForResult(intent, LoginActivity.LOGIN_REQUEST_CODE);
                }
            }
        });
        user_name_tv = (TextView) view.findViewById(R.id.user_name);
        user_department = (TextView) view.findViewById(R.id.user_department);
        user_position = (TextView) view.findViewById(R.id.user_position);
        updateUserData();
    }

    private void updateUserData() {
        MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
        if (myUser != null) {
            user_name_tv.setText(myUser.getUsername());
            user_department.setText(myUser.getDepartment());
            user_position.setText(myUser.getPosition());
            String photo_url = myUser.getPhoto_url();

            if (!TextUtils.isEmpty(photo_url)) {
                Glide.with(this)
                        .load(photo_url)
                        .centerCrop()
                        .placeholder(R.drawable.ic_account_circle_60pt_2x)
                        .error(R.drawable.ic_account_circle_60pt_2x)
                        .into(user_photo_iv);
            }
        } else {
            user_name_tv.setText(null);
            user_department.setText(null);
            user_position.setText(null);
            user_photo_iv.setImageResource(R.drawable.ic_account_circle_60pt_2x);
        }
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
        } else if (id == R.id.action_sync_phone) {
            doSyncAction();
        }

        return super.onOptionsItemSelected(item);
    }

    private void doSyncAction() {
        mProgressDialog.show();
        mainPresenter.syncLocalDataBaseAndNetWork();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditPhoneActivity.ADD_PHONE_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean is_update = data.getBooleanExtra(EditPhoneActivity.ADD_PHONE_RESULT_KEY, false);
            if (is_update) {
                doSyncAction();
            }
        } else if (requestCode == LoginActivity.LOGIN_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean flag = data.getBooleanExtra(LoginActivity.LOGIN_FLAG_KEY, false);
            if (flag) {
                updateUserData();
            }
        } else if (requestCode == UserActivity.USER_REQUEST_CODE && resultCode == RESULT_OK) {
            boolean flag = data.getBooleanExtra(UserActivity.USER_FLAG_KEY, false);
            if (flag) {
                updateUserData();
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
        mProgressDialog.dismiss();
    }
}
