package android.wuliqing.com.lendphonesystemapp.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;

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
        if (phoneNoteModel.getPic_url() != null) {
            Glide.with(mContext)
                    .load(phoneNoteModel.getPic_url())
                    .placeholder(R.drawable.ic_phone_iphone_48pt_2x)
                    .error(R.drawable.ic_phone_iphone_48pt_2x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into((ImageView) holder.getView(R.id.phone_icon_view));
        } else {
            ((ImageView) holder.getView(R.id.phone_icon_view)).setImageResource(R.drawable.ic_phone_iphone_48pt_2x);
        }
        if (!TextUtils.isEmpty(phoneNoteModel.getDate())) {
            holder.setText(R.id.phone_record_time_view, phoneNoteModel.getDate().toString());
        }
    }

    @Override
    public void updateOneData(PhoneNoteModel phone, String phone_id) {//异步
        List<PhoneNoteModel> phoneNoteModels = mDatas;
        if (phoneNoteModels != null && phoneNoteModels.size() > 0) {
            if (phone == null) {
                for (int i = 0; i < phoneNoteModels.size(); i++) {//删除操作：删除数据中多余的
                    if (phoneNoteModels.get(i).getPhone_id().equals(phone_id)) {
                        phoneNoteModels.remove(i);
                        break;
                    }
                }
            } else {
                if (!phoneNoteModels.contains(phone)) {//添加操作：数据中不存在
                    phoneNoteModels.add(0, phone);
                } else {//更新操作：数据中已存在
                    phoneNoteModels.remove(phone);
                    phoneNoteModels.add(0, phone);
                }
            }
        } else {
            phoneNoteModels.add(phone);
        }
    }
}
