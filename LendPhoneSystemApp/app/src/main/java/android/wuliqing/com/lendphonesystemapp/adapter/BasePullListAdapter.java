package android.wuliqing.com.lendphonesystemapp.adapter;

import android.content.Context;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.CommonAdapter;

import java.util.List;

/**
 * Created by Stay on 7/3/16.
 * Powered by www.stay4it.com
 */
public abstract class BasePullListAdapter<T> extends CommonAdapter<T> {

    protected static final int VIEW_TYPE_LOAD_MORE_FOOTER = 100;
    protected boolean isLoadMoreFooterShown;

    public BasePullListAdapter(Context context, int layoutId, List<T> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LOAD_MORE_FOOTER) {
            return onCreateLoadMoreFooterViewHolder(parent, viewType);
        }
        return super.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams params = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
                params.setFullSpan(true);
            }
            return;
        }
        super.onBindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + (isLoadMoreFooterShown ? 1 : 0);
    }

    @Override
    public int getItemViewType(int position) {
        if (isLoadMoreFooterShown && position == getItemCount() - 1) {
            return VIEW_TYPE_LOAD_MORE_FOOTER;
        }
        return super.getItemViewType(position);
    }

    protected ViewHolder onCreateLoadMoreFooterViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = ViewHolder.get(mContext, null, parent, R.layout.widget_pull_to_refresh_footer, -1);
        setListener(parent, holder, viewType);
        return holder;
    }

    public void onLoadMoreStateChanged(boolean isShown) {
        this.isLoadMoreFooterShown = isShown;
        if (isShown) {
            notifyItemInserted(getItemCount());
        } else {
            notifyItemRemoved(getItemCount());
        }
    }

    public boolean isLoadMoreFooter(int position) {
        return isLoadMoreFooterShown && position == getItemCount() - 1;
    }

    public boolean isSectionHeader(int position) {
        return false;
    }

    public void setData(List<T> phoneNotes) {
        if(phoneNotes != null) {
            mDatas.clear();
            mDatas.addAll(phoneNotes);
            notifyDataSetChanged();
        }
    }
}
