package android.wuliqing.com.lendphonesystemapp.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.model.AdminPhoneDetailNote;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class MyPhoneListAdapter extends BasePullListAdapter<AdminPhoneDetailNote> {

    public MyPhoneListAdapter(Context context, int layoutId, List<AdminPhoneDetailNote> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, AdminPhoneDetailNote adminPhoneDetailNote) {
        holder.setText(R.id.admin_phone_name_view, adminPhoneDetailNote.getPhone_name());
        holder.setText(R.id.admin_lend_phone_name_view, adminPhoneDetailNote.getLendPhoneNote().getLend_phone_name());
        holder.setText(R.id.admin_phone_number_view, String.valueOf(adminPhoneDetailNote.getLendPhoneNote().getLend_phone_number()));
        if (adminPhoneDetailNote.getPhone_url() != null) {
            Glide.with(mContext)
                    .load(adminPhoneDetailNote.getPhone_url())
                    .placeholder(R.drawable.ic_phone_iphone_48pt_2x)
                    .error(R.drawable.ic_phone_iphone_48pt_2x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into((ImageView) holder.getView(R.id.admin_phone_icon_view));
        } else {
            ((ImageView) holder.getView(R.id.admin_phone_icon_view)).setImageResource(R.drawable.ic_phone_iphone_48pt_2x);
        }
        holder.setText(R.id.admin_phone_time_view, adminPhoneDetailNote.getLendPhoneNote().getLend_phone_time());
//        if (lendPhoneNote.getLend_phone_status() == BmobLendPhoneNote.APPLY_ING_STATUS) {
//            holder.setText(R.id.admin_status_view, mContext.getString(R.string.apply_ing_status));
//        } else if (lendPhoneNote.getLend_phone_status() == BmobLendPhoneNote.APPLY_SUCCESS_STATUS) {
//            holder.setText(R.id.admin_status_view, mContext.getString(R.string.apply_suc_status));
//        }
    }


}
