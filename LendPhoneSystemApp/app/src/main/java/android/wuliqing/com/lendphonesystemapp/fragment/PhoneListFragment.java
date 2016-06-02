package android.wuliqing.com.lendphonesystemapp.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.PhoneListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.DividerItemDecoration;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.OnItemClickListener;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneListPresenter;
import android.wuliqing.com.lendphonesystemapp.widgets.AutoLoadRecyclerView;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class PhoneListFragment extends Fragment implements PhoneListView,
        SwipeRefreshLayout.OnRefreshListener {
    private PhoneListPresenter mPhoneListPresenter = new PhoneListPresenter();
    private SwipeRefreshLayout phone_list_swiperefreshlayout;
    private AutoLoadRecyclerView phone_auto_recyclerview;
    private PhoneListAdapter phoneListAdapter;
    private List<PhoneNote> phoneNotes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoneListPresenter.attach(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPhoneListPresenter.detach();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPhoneListPresenter.loadData();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_phone_list, null);
        initRefreshView(view);
        initAdapter();
        phone_list_swiperefreshlayout.setRefreshing(true);
        return view;
    }

    private void initAdapter() {
        phoneListAdapter = new PhoneListAdapter(getActivity(), R.layout.phone_list_item_view, phoneNotes);
        phoneListAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {

            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        phone_auto_recyclerview.setAdapter(phoneListAdapter);
    }

    protected void initRefreshView(View view) {
        phone_list_swiperefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.phone_list_swiperefreshlayout);
        phone_list_swiperefreshlayout.setOnRefreshListener(this);

        phone_auto_recyclerview = (AutoLoadRecyclerView) view.findViewById(R.id.phone_auto_recyclerview);
        phone_auto_recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()
                .getApplicationContext()));
        phone_auto_recyclerview.setHasFixedSize(true);
        phone_auto_recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        phone_auto_recyclerview.setVisibility(View.VISIBLE);
//        phone_auto_recyclerview.setOnLoadListener(this);
    }

    @Override
    public void onFetchedPhones(List<PhoneNote> phoneNotes) {
        phoneListAdapter.setData(phoneNotes);
    }

    @Override
    public void onRemoveResult(boolean result) {

    }

    @Override
    public void onQueryResult(List<PhoneNote> phoneNotes) {

    }

    @Override
    public void onShowLoading() {
        phone_list_swiperefreshlayout.setRefreshing(true);
    }

    @Override
    public void onHideLoading() {
        phone_list_swiperefreshlayout.setRefreshing(false);
    }

    @Override
    public void onRefresh() {
        mPhoneListPresenter.loadData();
    }
}
