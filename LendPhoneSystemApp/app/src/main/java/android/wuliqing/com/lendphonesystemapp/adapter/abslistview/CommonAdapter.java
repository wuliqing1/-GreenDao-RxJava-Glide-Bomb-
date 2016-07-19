package android.wuliqing.com.lendphonesystemapp.adapter.abslistview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.wuliqing.com.lendphonesystemapp.adapter.ViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseAdapter {
    final Context mContext;
    final List<T> mDatas;
//    private final LayoutInflater mInflater;
    private final int layoutId;

    CommonAdapter(Context context, int layoutId, List<T> datas) {
        this.mContext = context;
//        mInflater = LayoutInflater.from(context);
        this.mDatas = datas;
        this.layoutId = layoutId;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.get(mContext, convertView, parent,
                layoutId, position);
        convert(holder, getItem(position));
        return holder.getConvertView();
    }

    public abstract void convert(ViewHolder holder, T t);

}
