package android.wuliqing.com.lendphonesystemapp.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.PhoneDetailActivity;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.MyPhoneListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.OnItemClickListener;
import android.wuliqing.com.lendphonesystemapp.model.AdminPhoneDetailNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.MyPhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.MyPhoneListPresenter;
import android.wuliqing.com.lendphonesystemapp.utils.ProgressDialogHelper;
import android.wuliqing.com.lendphonesystemapp.widgets.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class MyPhoneListFragment extends BaseListFragment<LendPhoneNote> implements MyPhoneListView {
    private MyPhoneListPresenter mMyPhoneListPresenter;
    private List<AdminPhoneDetailNote> myPhoneDetailNotes = new ArrayList<>();
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (PhoneDetailActivity.LEND_PHONE_NOTE_CHANGE_ACTION.equals(intent.getAction())) {
                updateData();
            }
        }
    };
    private MyUser myUser;
    private ProgressDialog mProgressDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mProgressDialog = ProgressDialogHelper.initProgressDialog(ProgressDialog.STYLE_SPINNER, getActivity(),
                getString(R.string.backup_message));
        recycler.setRefreshing();
    }

    public void updateData() {
        if (recycler != null) {
            recycler.setRefreshing();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    @Override
    protected void detachPresenter() {
        mMyPhoneListPresenter.detach();
    }

    @Override
    protected void initParamData() {
    }

    @Override
    protected void createPresenter() {
        mMyPhoneListPresenter = new MyPhoneListPresenter();
        mMyPhoneListPresenter.attach(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(PhoneDetailActivity.LEND_PHONE_NOTE_CHANGE_ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, filter);
    }


    @Override
    protected BasePullListAdapter createAdapter() {
        BasePullListAdapter basePullListAdapter = new MyPhoneListAdapter(getActivity(),
                R.layout.my_phone_list_item_view, myPhoneDetailNotes);
        basePullListAdapter.setOnItemClickListener(new OnItemClickListener<AdminPhoneDetailNote>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, AdminPhoneDetailNote adminPhoneDetailNote, int position) {
                if (adminPhoneDetailNote.getLendPhoneNote().getLend_phone_status() == BmobLendPhoneNote.APPLY_SUCCESS_STATUS) {
                    showBackUpApplyPhoneDialog(adminPhoneDetailNote);
                }
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, AdminPhoneDetailNote adminPhoneDetailNote, int position) {
                return true;
            }
        });
        return basePullListAdapter;
    }

    private void showBackUpApplyPhoneDialog(final AdminPhoneDetailNote adminPhoneDetailNote) {
        MyDialogFragment.newInstance("", getString(R.string.apply_backup_dialog_msg, adminPhoneDetailNote.getPhone_name()),
                new MyDialogFragment.DialogListener() {
                    @Override
                    public void onClickDialogOk() {
                        mProgressDialog.show();
                        mMyPhoneListPresenter.backUpPhoneApply(adminPhoneDetailNote);
                    }

                    @Override
                    public void onClickDialogCancel() {

                    }
                }).show(getActivity().getFragmentManager(), "");
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        if (myUser != null) {
            mMyPhoneListPresenter.queryMyPhoneDataBaseAll(myUser.getUsername());
        }

    }

    @Override
    public void onFetchedLendPhones(List<AdminPhoneDetailNote> lendPhoneNotes) {
        adapter.setData(lendPhoneNotes);
        recycler.onRefreshCompleted();
    }

    @Override
    public void onAgreeResult(boolean result) {
        if (mProgressDialog.isShowing() && !getActivity().isFinishing()) {
            mProgressDialog.dismiss();
        }
    }

}
