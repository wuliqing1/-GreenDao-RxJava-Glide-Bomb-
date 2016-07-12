package android.wuliqing.com.lendphonesystemapp.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.widgets.PullRecycler;
import android.wuliqing.com.lendphonesystemapp.widgets.layoutmanager.DividerItemDecoration;
import android.wuliqing.com.lendphonesystemapp.widgets.layoutmanager.ILayoutManager;
import android.wuliqing.com.lendphonesystemapp.widgets.layoutmanager.MyLinearLayoutManager;

/**
 * Created by Stay on 8/3/16.
 * Powered by www.stay4it.com
 */
public abstract class BaseListFragment<T> extends Fragment implements PullRecycler.OnRecyclerRefreshListener {
    protected BasePullListAdapter adapter;
    protected PullRecycler recycler;
    protected int page = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recycler = (PullRecycler) view.findViewById(R.id.pullRecycler);
        adapter = createAdapter();
        recycler.setOnRefreshListener(this);
//        recycler.enablePullToRefresh(false);
//        recycler.enableLoadMore(true);
        recycler.setLayoutManager(getLayoutManager());
        recycler.addItemDecoration(getItemDecoration());
        recycler.addItemAnimator(getItemAnimator());
        recycler.setAdapter(adapter);
    }

    protected RecyclerView.ItemAnimator getItemAnimator() {
        return new DefaultItemAnimator();
    }

    protected abstract BasePullListAdapter createAdapter();


    protected ILayoutManager getLayoutManager() {
        return new MyLinearLayoutManager(getActivity());
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration(getActivity(),R.drawable.list_divider);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initParamData();
        createPresenter();
    }

    protected abstract void initParamData();

    @Override
    public void onDestroy() {
        super.onDestroy();
        detachPresenter();
    }

    protected abstract void detachPresenter();

    protected abstract void createPresenter();

}
