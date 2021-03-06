package android.wuliqing.com.lendphonesystemapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.fragment.MyDialogFragment;
import android.wuliqing.com.lendphonesystemapp.fragment.MyPhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.fragment.PhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.presenter.MainPresenter;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;
import android.wuliqing.com.lendphonesystemapp.utils.PreferenceUtil;
import android.wuliqing.com.lendphonesystemapp.utils.ProgressDialogHelper;
import android.wuliqing.com.lendphonesystemapp.utils.RxDrawer;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.bumptech.glide.Glide;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.update.BmobUpdateAgent;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

public class LendPhoneMainActivity extends BaseToolBarActivity
        implements NavigationView.OnNavigationItemSelectedListener, MainView {
    private static final String TAG = "LendPhoneMainActivity";
    public static final String PHONE_NOTE_CHANGE_ACTION = "com.phone.note.change.action";
    public static final String CUR_USER_CHANGE_ACTION = "com.user.note.change.action";
    private MainPresenter mainPresenter = new MainPresenter();
    private FragmentManager mFragmentManager;
    private PhoneListFragment mPhoneListFragment;
    private MyPhoneListFragment mMyPhoneListFragment;
    private ProgressDialog mProgressDialog;
    private ImageView user_photo_iv;
    private TextView user_name_tv;
    private TextView user_department;
    private TextView user_position;
    private NavigationView navigationView;
    private DrawerLayout drawer;
//    private UpdateResponse ur;

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PHONE_NOTE_CHANGE_ACTION.equals(intent.getAction())) {
                mPhoneListFragment.updateData();
            } else if (CUR_USER_CHANGE_ACTION.equals(intent.getAction())) {
                invalidateOptionsMenu();
            }
        }
    };

    @Override
    protected void detachPresenter() {
        mainPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mainPresenter.attach(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PHONE_NOTE_CHANGE_ACTION);
        filter.addAction(CUR_USER_CHANGE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
//        BmobUpdateAgent.initAppVersion(this);
        BmobUpdateAgent.setUpdateOnlyWifi(false);
//        BmobUpdateAgent.setUpdateListener(new BmobUpdateListener() {
//
//            @Override
//            public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
//                BmobException e = updateInfo.getException();
//                if (e != null) {
//                    Toast.makeText(LendPhoneMainActivity.this, "检测更新返回：" + e.getMessage() + "(" + e.getErrorCode() + ")", Toast.LENGTH_SHORT).show();
//                } else {
//                    ur = updateInfo;
//                }
//            }
//        });
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_lend_phone_main;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!PreferenceUtil.readBoolean(LendPhoneApplication.getAppContext(),
                PreferenceUtil.FIRST_RUN_KEY)) {
            syncNetWorkDataDialog();
            PreferenceUtil.write(LendPhoneApplication.getAppContext(),
                    PreferenceUtil.FIRST_RUN_KEY, true);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void syncNetWorkDataDialog() {
        MyDialogFragment.newInstance("", getString(R.string.sync_dialog_msg), new MyDialogFragment.DialogListener() {
            @Override
            public void onClickDialogOk() {
                doSyncAction();
            }

            @Override
            public void onClickDialogCancel() {

            }
        }).show(getFragmentManager(), "");
    }

    @Override
    protected void initWidgets() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        initFragments();

        mProgressDialog = ProgressDialogHelper.initProgressDialog(ProgressDialog.STYLE_SPINNER, this,
                getString(R.string.sync_in_msg));
        initLogin(navigationView);
    }

    private void initFragments() {
        final String PHONE_LIST_TAG = "phone_list_tag";
        final String MY_PHONE_LIST_TAG = "my_phone_list_tag";
        mFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mPhoneListFragment = (PhoneListFragment) mFragmentManager.findFragmentByTag(PHONE_LIST_TAG);
        mMyPhoneListFragment = (MyPhoneListFragment) mFragmentManager.findFragmentByTag(MY_PHONE_LIST_TAG);
        if (mPhoneListFragment == null) {
            mPhoneListFragment = new PhoneListFragment();
            fragmentTransaction.add(R.id.fragment_layout_content, mPhoneListFragment, PHONE_LIST_TAG);
        }
        if (mMyPhoneListFragment == null) {
            mMyPhoneListFragment = new MyPhoneListFragment();
        }
        fragmentTransaction.commitAllowingStateLoss();
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
                        .placeholder(R.drawable.ic_account_circle_60pt_2x)
                        .error(R.drawable.ic_account_circle_60pt_2x)
                        .crossFade()
                        .centerCrop()
                        .bitmapTransform(new CropCircleTransformation(this))
                        .into(user_photo_iv);
            } else {
                user_photo_iv.setImageResource(R.drawable.ic_account_circle_60pt_2x);
            }
        } else {
            user_name_tv.setText(null);
            user_department.setText(null);
            user_position.setText(null);
            user_photo_iv.setImageResource(R.drawable.ic_account_circle_60pt_2x);
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitDialog();
//            super.onBackPressed();
        }
    }

    private void showExitDialog() {
        MyDialogFragment.newInstance("", getString(R.string.exit_app_dialog_msg), new MyDialogFragment.DialogListener() {
            @Override
            public void onClickDialogOk() {
                finishActivity();
            }

            @Override
            public void onClickDialogCancel() {

            }
        }).show(getFragmentManager(), "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.lend_phone_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem adminMenu = navigationView.getMenu().findItem(R.id.nav_admin);
        MenuItem addMenu = menu.findItem(R.id.action_add_phone);
        MenuItem userMenu = navigationView.getMenu().findItem(R.id.nav_user);
        if (adminMenu != null) {
            MyUser myUser = BmobUser.getCurrentUser(this, MyUser.class);
            if (myUser != null) {
                if (myUser.isAdmin()) {
                    adminMenu.setVisible(true);
                    addMenu.setVisible(true);
                } else {
                    adminMenu.setVisible(false);
                    addMenu.setVisible(false);
                }
                userMenu.setVisible(true);
            } else {
                adminMenu.setVisible(false);
                addMenu.setVisible(false);
                userMenu.setVisible(false);
            }
        }
        return super.onPrepareOptionsMenu(menu);
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
            syncNetWorkDataDialog();
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
//            boolean is_update = data.getBooleanExtra(EditPhoneActivity.UPDATE_PHONE_RESULT_KEY, false);
//            if (is_update) {
//                doSyncAction();
//                mPhoneListFragment.setPhoneId(data.getStringExtra(EditPhoneActivity.EDIT_PHONE_ID_RESULT_KEY));
//                mPhoneListFragment.setPhoneActionAndUpdate(PhoneListFragment.PHONE_ADD_ACTION);
//                mPhoneListFragment.updateData();
//            }
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
        } else if (requestCode == EditPhoneActivity.EDIT_PHONE_REQUEST_CODE && resultCode == RESULT_OK) {
//            boolean update_flag = data.getBooleanExtra(EditPhoneActivity.UPDATE_PHONE_RESULT_KEY, false);
//            boolean delete_flag = data.getBooleanExtra(EditPhoneActivity.DELETE_PHONE_RESULT_KEY, false);
//            mPhoneListFragment.setPhoneId(data.getStringExtra(EditPhoneActivity.EDIT_PHONE_ID_RESULT_KEY));
//            if (update_flag) {
//                mPhoneListFragment.setPhoneActionAndUpdate(PhoneListFragment.PHONE_UPDATE_ACTION);
//            } else if (delete_flag) {
//                mPhoneListFragment.setPhoneActionAndUpdate(PhoneListFragment.PHONE_DELETE_ACTION);
//            }
        } else if (requestCode == PhoneDetailActivity.PHONE_DETAIL_REQUEST_CODE && resultCode == RESULT_OK) {

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(final MenuItem item) {
        // Handle navigation view item clicks here.
        RxDrawer.close(drawer).subscribeOn(AndroidSchedulers.mainThread())
                .unsubscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<Void>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), e.toString());
            }

            @Override
            public void onNext(Void aVoid) {
                switchNaveMenu(item);
            }
        });

        return true;
    }

    private void switchNaveMenu(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            mToolbar.setTitle(R.string.app_name);
            mFragmentManager.beginTransaction().replace(R.id.fragment_layout_content, mPhoneListFragment).commit();
        }
//        else if (id == R.id.nav_clear) {
//            mainPresenter.clearDataBase();
//            ToastUtils.show(LendPhoneMainActivity.this, R.string.clear_database_success);
//        }
        else if (id == R.id.nav_admin) {
            Intent intent = new Intent(LendPhoneMainActivity.this, AdminWorkActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_user) {
            mToolbar.setTitle(R.string.menu_my_phone_title);
            mFragmentManager.beginTransaction().replace(R.id.fragment_layout_content, mMyPhoneListFragment).commit();
        }
//        else if (id == R.id.nav_update) {
//            BmobUpdateAgent.update(LendPhoneMainActivity.this);
//        }
        else if (id == R.id.nav_set) {
            Intent intentSetting = new Intent(LendPhoneMainActivity.this, SettingActivity.class);
            startActivity(intentSetting);
        } else if (id == R.id.nav_about) {
            startActivity(new Intent(LendPhoneMainActivity.this, AboutActivity.class));
        }
    }

    @Override
    public void onSyncResult(boolean result) {
        if (result) {
            ToastUtils.show(LendPhoneApplication.getAppContext(), R.string.sync_success_msg);
            mPhoneListFragment.updateData();
        } else {
            ToastUtils.show(LendPhoneApplication.getAppContext(), R.string.sync_error_msg);
        }
        mProgressDialog.dismiss();
    }
}
