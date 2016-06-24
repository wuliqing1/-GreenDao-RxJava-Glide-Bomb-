package android.wuliqing.com.lendphonesystemapp.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
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
    public void onRemoveResult(boolean result) {

    }

    @Override
    public void onQueryResult(List<PhoneNoteModel> phoneNotes) {

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
                ToastUtils.show(getActivity(), "onItemLongClick " + position);
                return true;
            }
        });
        return basePullListAdapter;
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        mPhoneListPresenter.loadData();
    }
}
