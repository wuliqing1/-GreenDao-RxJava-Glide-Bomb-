package android.wuliqing.com.lendphonesystemapp.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.transformations.CropCircleTransformation;

import com.bumptech.glide.Glide;

import java.util.List;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class LendPhoneListAdapter extends BasePullListAdapter<LendPhoneNote> {

    public LendPhoneListAdapter(Context context, int layoutId, List<LendPhoneNote> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(ViewHolder holder, LendPhoneNote lendPhoneNote) {
        holder.setText(R.id.lend_phone_name_view, lendPhoneNote.getLend_phone_name());
        holder.setText(R.id.lend_phone_number_view, String.valueOf(lendPhoneNote.getLend_phone_number()));
        if (lendPhoneNote.getLend_phone_photo_url() != null) {
            Glide.with(mContext)
                    .load(lendPhoneNote.getLend_phone_photo_url())
                    .placeholder(R.drawable.ic_account_circle_60pt_2x)
                    .error(R.drawable.ic_account_circle_60pt_2x)
                    .crossFade()
                    .centerCrop()
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into((ImageView) holder.getView(R.id.lend_phone_icon_view));
        } else {
            ((ImageView) holder.getView(R.id.lend_phone_icon_view)).setImageResource(R.drawable.ic_account_circle_60pt_2x);
        }
        holder.setText(R.id.lend_phone_time_view, lendPhoneNote.getLend_phone_time());
        if (lendPhoneNote.getLend_phone_status() == BmobLendPhoneNote.APPLY_ING_STATUS) {
            holder.setText(R.id.lend_status_view, mContext.getString(R.string.apply_ing_status));
        } else if (lendPhoneNote.getLend_phone_status() == BmobLendPhoneNote.APPLY_SUCCESS_STATUS) {
            holder.setText(R.id.lend_status_view, mContext.getString(R.string.apply_suc_status));
        }
    }

    @Override
    public void updateOneData(LendPhoneNote phone, String phone_id) {//异步
//        List<LendPhoneNote> phoneNoteModels = mDatas;
//        if (phoneNoteModels != null && phoneNoteModels.size() > 0) {
//            if (phone == null) {
//                for (int i = 0; i < phoneNoteModels.size(); i++) {//删除操作：删除数据中多余的
//                    if (phoneNoteModels.get(i).getBmob_phone_id().equals(phone_id)) {
//                        phoneNoteModels.remove(i);
//                        break;
//                    }
//                }
//            } else {
//                if (!phoneNoteModels.contains(phone)) {//添加操作：数据中不存在
//                    phoneNoteModels.add(0, phone);
//                } else {//更新操作：数据中已存在
//                    phoneNoteModels.remove(phone);
//                    phoneNoteModels.add(0, phone);
//                }
//            }
//        } else {
//            phoneNoteModels.add(phone);
//        }
    }
}
