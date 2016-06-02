package android.wuliqing.com.lendphonesystemapp.fragment;

import android.os.Bundle;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.PhoneListAdapter;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.presenter.PhoneListPresenter;
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
    public void onDestroy() {
        super.onDestroy();
        mPhoneListPresenter.detach();
    }

    @Override
    public void onResume() {
        super.onResume();
        recycler.setRefreshing();
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
        return new PhoneListAdapter(getActivity(), R.layout.phone_list_item_view, phoneNoteModels);
    }

    @Override
    public void onRefresh(int action) {
        if (action == PullRecycler.ACTION_PULL_TO_REFRESH) {
            page = 1;
        }
        mPhoneListPresenter.loadData();
    }
}
