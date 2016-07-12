package android.wuliqing.com.lendphonesystemapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.wuliqing.com.lendphonesystemapp.fragment.InputDialogFragment;
import android.wuliqing.com.lendphonesystemapp.fragment.LendPhoneListFragment;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.model.PhoneDetailNote;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNodeWrap;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneDetailView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneDetailPresenter;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.bumptech.glide.Glide;

import cn.bmob.v3.BmobUser;
import zte.phone.greendao.LendPhoneNote;

public class PhoneDetailActivity extends BaseToolBarActivity implements PhoneDetailView {
    public static final String PHONE_DETAIL_DATA = "phone_detail_data";
    public static final int PHONE_DETAIL_REQUEST_CODE = 98;
    private PhoneNodeWrap mPhoneNodeWrap;
    private PhoneDetailPresenter mPhoneDetailPresenter;
    private FragmentManager mFragmentManager;
    private LendPhoneListFragment mLendPhoneListFragment;
    private ImageView mPhonePhoto;
    private TextView phone_detail_name_view;
    private TextView phone_detail_number_left_view;
    private TextView phone_detail_number_lend_view;
    private TextView phone_detail_names_lend_view;
    private TextView phone_detail_record_time_view;
    private MyUser myUser;

    @Override
    protected void initIntentData(Bundle savedInstanceState) {
        mPhoneNodeWrap = getIntent().getParcelableExtra(PHONE_DETAIL_DATA);
    }

    @Override
    protected void detachPresenter() {
        mPhoneDetailPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mPhoneDetailPresenter = new PhoneDetailPresenter();
        mPhoneDetailPresenter.attach(this);
        mPhoneDetailPresenter.queryLocalDataBase(mPhoneNodeWrap);
        myUser = BmobUser.getCurrentUser(this, MyUser.class);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected int getContentViewResId() {
        return R.layout.activity_phone_detail;
    }

    @Override
    protected void setupToolbar() {
        super.setupToolbar();
        if (mToolbar != null) {
            mToolbar.setTitle(getString(R.string.lend_phone_title, mPhoneNodeWrap.getPhone_name()));
        }
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
    }

    private void initListView() {
        final String PHONE_LIST_TAG = "lend_phone_list_tag";
        mFragmentManager = getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        mLendPhoneListFragment = (LendPhoneListFragment) mFragmentManager.findFragmentByTag(PHONE_LIST_TAG);
        Bundle bundle = new Bundle();
        bundle.putString(LendPhoneListFragment.LEND_PHONE_ID_PARAM, mPhoneNodeWrap.getBmob_phone_id());

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
                    bmobLendPhoneNote.setPhone_id(mPhoneNodeWrap.getBmob_phone_id());
                    bmobLendPhoneNote.setPhoto_url(myUser.getPhoto_url());
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
        if (mPhoneNodeWrap != null) {
            phone_detail_name_view.setText(mPhoneNodeWrap.getPhone_name());
            phone_detail_record_time_view.setText(mPhoneNodeWrap.getPhone_time());
            updatePhoto(mPhoneNodeWrap.getPhone_photo_url());
        }
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
                    .load(mPhoneNodeWrap.getPhone_photo_url())
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
            updatePhoto(phoneDetailNote.getPic_url());
        }
    }

    @Override
    public void onLendPhoneResult(LendPhoneNote result) {
        mPhoneDetailPresenter.queryLocalDataBase(mPhoneNodeWrap);
        ToastUtils.show(this, R.string.lend_phone_success_msg);
    }

    @Override
    public void onQueryPhoneResult(PhoneDetailNote result) {
        updateHeadView(result);
        mLendPhoneListFragment.updateData();
    }

    @Override
    public void onSyncPhoneResult(boolean result) {
        if (result) {

        }
    }
}
