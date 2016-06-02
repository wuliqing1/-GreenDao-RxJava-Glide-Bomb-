package android.wuliqing.com.lendphonesystemapp.adapter;

import android.content.Context;
import android.wuliqing.com.lendphonesystemapp.adapter.recyclerview.CommonAdapter;

import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class PhoneListAdapter extends CommonAdapter {

    public PhoneListAdapter(Context context, int layoutId, List<PhoneNote> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, Object o) {

    }

    public void setData(List<PhoneNote> phoneNotes) {
        mDatas.clear();
        mDatas.addAll(phoneNotes);
        notifyDataSetChanged();
    }
}
