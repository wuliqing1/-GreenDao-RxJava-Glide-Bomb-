package android.wuliqing.com.lendphonesystemapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.EditPhoneActivity;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.PhoneListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.OnItemClickListener;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneListPresenter;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;
import android.wuliqing.com.lendphonesystemapp.widgets.PullRecycler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class PhoneListFragment extends BaseListFragment<PhoneNoteModel> implements PhoneListView {
    private PhoneListPresenter mPhoneListPresenter = new PhoneListPresenter();
    private List<PhoneNoteModel> phoneNoteModels = new ArrayList<>();
    public static final int PHONE_ADD_ACTION = 1;
    public static final int PHONE_UPDATE_ACTION = 2;
    public static final int PHONE_DELETE_ACTION = 3;
    public static final int PHONE_ALL_ACTION = 0;
    private int current_action = PHONE_ALL_ACTION;
    private String phone_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoneListPresenter.attach(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recycler.setRefreshing();
    }

    public void setPhoneId(String id) {
        this.phone_id = id;
    }

    public void setPhoneActionAndUpdate(int action) {
        current_action = action;
        updateData();
    }

    private boolean isPhoneUpAction() {
        if (current_action == PHONE_ADD_ACTION
                || current_action == PHONE_UPDATE_ACTION
                || current_action == PHONE_DELETE_ACTION) {
            return true;
        }
        return false;
    }

    public void updateData() {
        if (recycler != null) {
            recycler.setRefreshing();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPhoneListPresenter.detach();
    }

    @Override
    public void onFetchedPhones(List<PhoneNoteModel> phoneNotes) {
        adapter.setData(phoneNotes);
        recycler.onRefreshCompleted();
    }

    @Override
    public void onUpdateOnePhoneCompleted() {
        adapter.notifyDataSetChanged();
        recycler.onRefreshCompleted();
        current_action = PHONE_ALL_ACTION;
        phone_id = null;
    }

    @Override
    protected BasePullListAdapter createAdapter() {
        BasePullListAdapter basePullListAdapter = new PhoneListAdapter(getActivity(),
                R.layout.phone_list_item_view, phoneNoteModels);
        basePullListAdapter.setOnItemClickListener(new OnItemClickListener<PhoneNoteModel>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, PhoneNoteModel phoneNoteModel, int position) {
                ToastUtils.show(getActivity(), "onItemClick " + position);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, PhoneNoteModel phoneNoteModel, int position) {
                showEditPhoneDialog(phoneNoteModel);
                return true;
            }
        });
        return basePullListAdapter;
    }

    private void showEditPhoneDialog(final PhoneNoteModel phoneNoteModel) {
        MyDialogFragment.newInstance("", getString(R.string.phone_edit_dialog_msg, phoneNoteModel.getPhone_name()),
                new MyDialogFragment.DialogListener() {
                    @Override
                    public void onClickDialogOk() {
                        Intent intent = new Intent(getActivity(), EditPhoneActivity.class);
                        intent.putExtra(EditPhoneActivity.EDIT_PHONE_DATA, phoneNoteModel);
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
        if (isPhoneUpAction()) {
            mPhoneListPresenter.updatePhoneOneData(adapter, phone_id);
        } else {
            mPhoneListPresenter.loadAllPhoneData();
        }
    }
}
