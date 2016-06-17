package android.wuliqing.com.lendphonesystemapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class PhoneListAdapter extends BasePullListAdapter<PhoneNoteModel> {

    public PhoneListAdapter(Context context, int layoutId, List<PhoneNoteModel> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, PhoneNoteModel phoneNoteModel) {
        holder.setText(R.id.phone_name_view, phoneNoteModel.getPhone_name());
        holder.setText(R.id.phone_number_left_view, String.valueOf(phoneNoteModel.getLeft_number()));
        holder.setText(R.id.phone_number_lend_view, String.valueOf(phoneNoteModel.getLend_number()));
        String names = phoneNoteModel.getLend_names();
        if (TextUtils.isEmpty(names)) {
            names = mContext.getResources().getString(R.string.none);
        }
        holder.setText(R.id.phone_names_lend_view, names);
        if (phoneNoteModel.getPic_path() != null) {
            Glide.with(mContext)
                    .load(phoneNoteModel.getPic_path())
                    .centerCrop()
                    .placeholder(R.drawable.ic_phone_iphone_48pt_2x)
                    .error(R.drawable.ic_phone_iphone_48pt_2x)
                    .into((ImageView) holder.getView(R.id.phone_icon_view));
        }
        holder.setText(R.id.phone_record_time_view, phoneNoteModel.getDate().toString());
    }

}
