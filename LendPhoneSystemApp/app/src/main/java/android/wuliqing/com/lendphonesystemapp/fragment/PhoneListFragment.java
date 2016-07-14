package android.wuliqing.com.lendphonesystemapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.EditPhoneActivity;
import android.wuliqing.com.lendphonesystemapp.PhoneDetailActivity;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.PhoneListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.OnItemClickListener;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneListPresenter;
import android.wuliqing.com.lendphonesystemapp.widgets.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobUser;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class PhoneListFragment extends BaseListFragment<PhoneNote> implements PhoneListView {
    private PhoneListPresenter mPhoneListPresenter;
    private List<PhoneNote> phoneNoteModels = new ArrayList<>();
//    public static final int PHONE_ADD_ACTION = 1;
//    public static final int PHONE_UPDATE_ACTION = 2;
//    public static final int PHONE_DELETE_ACTION = 3;
//    public static final int PHONE_ALL_ACTION = 0;
//    private int current_action = PHONE_ALL_ACTION;
//    private String phone_id;

    @Override
    protected void initParamData() {

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recycler.setRefreshing();
    }

//    public void setPhoneId(String id) {
//        this.phone_id = id;
//    }

//    public void setPhoneActionAndUpdate(int action) {
//        current_action = action;
//        updateData();
//    }
//
//    private boolean isPhoneUpAction() {
//        if (current_action == PHONE_ADD_ACTION
//                || current_action == PHONE_UPDATE_ACTION
//                || current_action == PHONE_DELETE_ACTION) {
//            return true;
//        }
//        return false;
//    }

    public void updateData() {
        if (recycler != null) {
            recycler.setRefreshing();
        }
    }

    @Override
    protected void detachPresenter() {
        mPhoneListPresenter.detach();
    }

    @Override
    protected void createPresenter() {
        mPhoneListPresenter = new PhoneListPresenter();
        mPhoneListPresenter.attach(this);
    }

    @Override
    public void onFetchedPhones(List<PhoneNote> phoneNotes) {
        adapter.setData(phoneNotes);
        recycler.onRefreshCompleted();
    }

//    @Override
//    public void onUpdateOnePhoneCompleted() {
//        adapter.notifyDataSetChanged();
//        recycler.onRefreshCompleted();
//        current_action = PHONE_ALL_ACTION;
//        phone_id = null;
//    }

    @Override
    protected BasePullListAdapter createAdapter() {
        BasePullListAdapter basePullListAdapter = new PhoneListAdapter(getActivity(),
                R.layout.phone_list_item_view, phoneNoteModels);
        basePullListAdapter.setOnItemClickListener(new OnItemClickListener<PhoneNote>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, PhoneNote phoneNoteModel, int position) {
                Intent intent = new Intent(getActivity(), PhoneDetailActivity.class);
//                PhoneNodeWrap mPhoneNodeWrap = new PhoneNodeWrap(phoneNoteModel);
                intent.putExtra(PhoneDetailActivity.PHONE_DETAIL_ID_KEY, phoneNoteModel.getBmob_phone_id());
                startActivityForResult(intent, PhoneDetailActivity.PHONE_DETAIL_REQUEST_CODE);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, PhoneNote phoneNoteModel, int position) {
                MyUser myUser = BmobUser.getCurrentUser(getActivity(), MyUser.class);
                if (myUser != null && myUser.isAdmin()) {
                    showEditPhoneDialog(phoneNoteModel);
                }
                return true;
            }
        });
        return basePullListAdapter;
    }

    private void showEditPhoneDialog(final PhoneNote phoneNoteModel) {
        MyDialogFragment.newInstance("", getString(R.string.phone_edit_dialog_msg, phoneNoteModel.getPhone_name()),
                new MyDialogFragment.DialogListener() {
                    @Override
                    public void onClickDialogOk() {
                        Intent intent = new Intent(getActivity(), EditPhoneActivity.class);
//                        PhoneNodeWrap mPhoneNodeWrap = new PhoneNodeWrap(phoneNoteModel);
                        intent.putExtra(EditPhoneActivity.EDIT_PHONE_ID, phoneNoteModel.getBmob_phone_id());
                        getActivity().startActivityForResult(intent, EditPhoneActivity.EDIT_PHONE_REQUEST_CODE);
                    }

                    @Override
                    public void onClickDialogCancel() {

                    }
                }).show(getFragmentManager(), "");
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
//        if (isPhoneUpAction()) {
//            mPhoneListPresenter.updatePhoneOneData(adapter, phone_id);
//        } else {
        mPhoneListPresenter.loadAllPhoneData();
//        }
    }
}
