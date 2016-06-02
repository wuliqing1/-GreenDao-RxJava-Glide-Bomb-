package android.wuliqing.com.lendphonesystemapp.widgets.layoutmanager;

import android.support.v7.widget.RecyclerView;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;


/**
 * Created by Stay on 5/3/16.
 * Powered by www.stay4it.com
 */
public interface ILayoutManager {
    RecyclerView.LayoutManager getLayoutManager();
    int findLastVisiblePosition();
    void setUpAdapter(BasePullListAdapter adapter);
}
