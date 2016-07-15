package android.wuliqing.com.lendphonesystemapp;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.fragment.InputDialogFragment;
import android.wuliqing.com.lendphonesystemapp.fragment.LendPhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.model.PhoneDetailNote;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneDetailView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneDetailPresenter;
import android.wuliqing.com.lendphonesystemapp.swipeBack.SwipeBackActivity;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;
import android.wuliqing.com.lendphonesystemapp.utils.ProgressDialogHelper;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.bumptech.glide.Glide;

import cn.bmob.v3.BmobUser;

public class PhoneDetailActivity extends SwipeBackActivity implements PhoneDetailView {
    //    public static final String PHONE_DETAIL_DATA = "phone_detail_data";
    public static final String PHONE_DETAIL_ID_KEY = "phone_id_key";
    public static final int PHONE_DETAIL_REQUEST_CODE = 98;
    public static final String LEND_PHONE_NOTE_CHANGE_ACTION = "com.lend.phone.note.change.action";
    //    private PhoneNodeWrap mPhoneNodeWrap;
    private String phone_id;
    private PhoneDetailPresenter mPhoneDetailPresenter;
    private FragmentManager mFragmentManager;
    private LendPhoneListFragment mLendPhoneListFragment;
    private ImageView mPhonePhoto;
    private TextView phone_detail_name_view;
    private TextView phone_detail_number_left_view;
    private TextView phone_detail_number_lend_view;
    private TextView phone_detail_names_lend_view;
    private TextView phone_detail_record_time_view;
    private ProgressDialog mProgressDialog;
    private MyUser myUser;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (LEND_PHONE_NOTE_CHANGE_ACTION.equals(intent.getAction())) {
                mPhoneDetailPresenter.doPhoneDetailHeadData(phone_id);
                mLendPhoneListFragment.updateData();
            }
        }
    };

    @Override
    protected void initIntentData(Bundle savedInstanceState) {
//        mPhoneNodeWrap = getIntent().getParcelableExtra(PHONE_DETAIL_DATA);
        phone_id = getIntent().getStringExtra(PHONE_DETAIL_ID_KEY);
    }

    @Override
    protected void detachPresenter() {
        mPhoneDetailPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mPhoneDetailPresenter = new PhoneDetailPresenter();
        mPhoneDetailPresenter.attach(this);
        mPhoneDetailPresenter.doPhoneDetailHeadData(phone_id);

        myUser = BmobUser.getCurrentUser(this, MyUser.class);

        IntentFilter filter = new IntentFilter();
        filter.addAction(LEND_PHONE_NOTE_CHANGE_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_phone_detail;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
    }

    @Override
    protected void initWidgets() {
        initHeadView();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.lend_phone_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myUser != null) {
                    showInputDialog();
                } else {
                    ToastUtils.show(LendPhoneApplication.getAppContext(), R.string.login_prompt_msg);
                }
            }
        });
        initListView();
        mProgressDialog = ProgressDialogHelper.initProgressDialog(ProgressDialog.STYLE_SPINNER, this,
                getString(R.string.apply_message));
    }

    private void initListView() {
        final String PHONE_LIST_TAG = "lend_phone_list_tag";
        mFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mLendPhoneListFragment = (LendPhoneListFragment) mFragmentManager.findFragmentByTag(PHONE_LIST_TAG);
        Bundle bundle = new Bundle();
        bundle.putString(LendPhoneListFragment.LEND_PHONE_ID_PARAM, phone_id);

        if (mLendPhoneListFragment == null) {
            mLendPhoneListFragment = new LendPhoneListFragment();
            mLendPhoneListFragment.setArguments(bundle);
            fragmentTransaction.add(R.id.lend_phone_detail_content, mLendPhoneListFragment, PHONE_LIST_TAG);
        }
        fragmentTransaction.commitAllowingStateLoss();
    }

    private void showInputDialog() {
        String title = getString(R.string.input_lend_phone_number_title,
                phone_detail_number_left_view.getText().toString());
        InputDialogFragment inputDialogFragment = InputDialogFragment.newInstance(0, title, "1");
        inputDialogFragment.setListener(new InputDialogFragment.OnFragmentInteractionListener() {
            @Override
            public void onFragmentInteraction(String text, int id) {
                try {
                    long lend_num = Long.valueOf(text);
                    long left_num = Long.valueOf(phone_detail_number_left_view.getText().toString());
                    if (lend_num > left_num) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), R.string.input_number_big_msg);
                        return;
                    }
                    BmobLendPhoneNote bmobLendPhoneNote = new BmobLendPhoneNote();
                    bmobLendPhoneNote.setLend_phone_name(myUser.getUsername());
                    bmobLendPhoneNote.setLend_phone_number(lend_num);
                    bmobLendPhoneNote.setPhone_id(phone_id);
                    bmobLendPhoneNote.setPhoto_url(myUser.getPhoto_url());
                    bmobLendPhoneNote.setStatus(BmobLendPhoneNote.APPLY_ING_STATUS);
                    mProgressDialog.show();
                    mPhoneDetailPresenter.lendPhone(bmobLendPhoneNote);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    ToastUtils.show(LendPhoneApplication.getAppContext(), e.toString());
                }
            }
        });
        inputDialogFragment.show(getFragmentManager(), "");
    }

    private void initHeadView() {
        mPhonePhoto = (ImageView) findViewById(R.id.phone_detail_icon_view);
        phone_detail_name_view = (TextView) findViewById(R.id.phone_detail_name_view);
        phone_detail_number_left_view = (TextView) findViewById(R.id.phone_detail_number_left_view);
        phone_detail_number_lend_view = (TextView) findViewById(R.id.phone_detail_number_lend_view);
        phone_detail_names_lend_view = (TextView) findViewById(R.id.phone_detail_names_lend_view);
        phone_detail_record_time_view = (TextView) findViewById(R.id.phone_detail_record_time_view);
        mPhonePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(PhoneDetailActivity.this, EditPhoneActivity.class);
//                intent.putExtra(EditPhoneActivity.EDIT_PHONE_DATA, mPhoneNoteModel);
//                startActivityForResult(intent, EditPhoneActivity.EDIT_PHONE_REQUEST_CODE);
            }
        });
    }

    private void updatePhoto(String url) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                    .load(url)
                    .placeholder(R.drawable.ic_phone_iphone_48pt_2x)
                    .error(R.drawable.ic_phone_iphone_48pt_2x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(mPhonePhoto);
        }

    }

    private void updateHeadView(PhoneDetailNote phoneDetailNote) {
        if (phoneDetailNote != null) {
            phone_detail_name_view.setText(phoneDetailNote.getPhone_name());
            phone_detail_number_left_view.setText(String.valueOf(phoneDetailNote.getLeft_number()));
            phone_detail_number_lend_view.setText(String.valueOf(phoneDetailNote.getLend_number()));
            phone_detail_names_lend_view.setText(phoneDetailNote.getLend_names());
            phone_detail_record_time_view.setText(phoneDetailNote.getDate());
            if (mToolbar != null) {
                mToolbar.setTitle(getString(R.string.lend_phone_title, phoneDetailNote.getPhone_name()));
            }
            updatePhoto(phoneDetailNote.getPic_url());
        }
    }

    @Override
    public void onLendPhoneResult(BmobLendPhoneNote result) {
//        mPhoneDetailPresenter.doPhoneDetailHeadData(phone_id);
        ToastUtils.show(this, R.string.lend_phone_success_msg);
        if (mProgressDialog.isShowing() && !isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onQueryPhoneResult(PhoneDetailNote result) {
        updateHeadView(result);
        mLendPhoneListFragment.updateData();
    }

    @Override
    protected void initSwipeLayout() {
    }
}
