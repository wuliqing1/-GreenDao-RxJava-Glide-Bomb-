package android.wuliqing.com.lendphonesystemapp.widgets.layoutmanager;

import android.support.v7.widget.GridLayoutManager;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;

/**
 * Created by Stay on 6/3/16.
 * Powered by www.stay4it.com
 */
public class FooterSpanSizeLookup extends GridLayoutManager.SpanSizeLookup {
    private BasePullListAdapter adapter;
    private int spanCount;

    public FooterSpanSizeLookup(BasePullListAdapter adapter, int spanCount) {
        this.adapter = adapter;
        this.spanCount = spanCount;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter.isLoadMoreFooter(position) || adapter.isSectionHeader(position)) {
            return spanCount;
        }
        return 1;
    }
}
