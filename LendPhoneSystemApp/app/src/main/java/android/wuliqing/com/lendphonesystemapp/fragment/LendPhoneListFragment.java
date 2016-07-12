package android.wuliqing.com.lendphonesystemapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.EditPhoneActivity;
import android.wuliqing.com.lendphonesystemapp.PhoneDetailActivity;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.LendPhoneListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.OnItemClickListener;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNodeWrap;
import android.wuliqing.com.lendphonesystemapp.mvpview.LendPhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.LendPhoneListPresenter;
import android.wuliqing.com.lendphonesystemapp.widgets.PullRecycler;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class LendPhoneListFragment extends BaseListFragment<PhoneNote> implements LendPhoneListView {
    private LendPhoneListPresenter mLendPhoneListPresenter;
    private List<LendPhoneNote> lendPhoneNotes = new ArrayList<>();
    public static final String LEND_PHONE_ID_PARAM = "lend_phone_id_param";
    private String phone_id;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        recycler.setRefreshing();
    }

    public void updateData() {
        if (recycler != null) {
            recycler.setRefreshing();
        }
    }

    @Override
    protected void detachPresenter() {
        mLendPhoneListPresenter.detach();
    }

    @Override
    protected void initParamData() {
        phone_id = getArguments().getString(LEND_PHONE_ID_PARAM);
    }

    @Override
    protected void createPresenter() {
        mLendPhoneListPresenter = new LendPhoneListPresenter(phone_id);
        mLendPhoneListPresenter.attach(this);
    }


    @Override
    protected BasePullListAdapter createAdapter() {
        BasePullListAdapter basePullListAdapter = new LendPhoneListAdapter(getActivity(),
                R.layout.lend_phone_list_item_view, lendPhoneNotes);
        basePullListAdapter.setOnItemClickListener(new OnItemClickListener<PhoneNote>() {
            @Override
            public void onItemClick(ViewGroup parent, View view, PhoneNote phoneNoteModel, int position) {
                Intent intent = new Intent(getActivity(), PhoneDetailActivity.class);
                PhoneNodeWrap mPhoneNodeWrap = new PhoneNodeWrap(phoneNoteModel);
                intent.putExtra(PhoneDetailActivity.PHONE_DETAIL_DATA, mPhoneNodeWrap);
                startActivityForResult(intent, PhoneDetailActivity.PHONE_DETAIL_REQUEST_CODE);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, PhoneNote phoneNoteModel, int position) {
                showEditPhoneDialog(phoneNoteModel);
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
                        PhoneNodeWrap mPhoneNodeWrap = new PhoneNodeWrap(phoneNoteModel);
                        intent.putExtra(EditPhoneActivity.EDIT_PHONE_DATA, mPhoneNodeWrap);
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
        mLendPhoneListPresenter.queryDataBaseAll(phone_id);
    }

    @Override
    public void onFetchedLendPhones(List<LendPhoneNote> lendPhoneNotes) {
        adapter.setData(lendPhoneNotes);
        recycler.onRefreshCompleted();
    }

    @Override
    public void onUpdateResult(boolean result) {

    }
}
