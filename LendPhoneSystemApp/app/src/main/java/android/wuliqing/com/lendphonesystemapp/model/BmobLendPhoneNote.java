package android.wuliqing.com.lendphonesystemapp.model;


import android.text.TextUtils;

import cn.bmob.v3.BmobObject;
import zte.phone.greendao.LendPhoneNote;

public class BmobLendPhoneNote extends BmobObject {
    public static final String TABLE_NAME = "BmobLendPhoneNote";
    public static final long APPLY_ING_STATUS = 1;
    public static final long APPLY_SUCCESS_STATUS = 2;
    public static final long APPLY_BACK_ING_STATUS = 3;
    private String phone_id;
    private String lend_phone_name;
    private long lend_phone_number;
    private String photo_url;
    private long status;//1:申请中... 2:申请成功 3:申请归还

    public String getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(String phone_id) {
        this.phone_id = phone_id;
    }

    public String getLend_phone_name() {
        return lend_phone_name;
    }

    public void setLend_phone_name(String lend_phone_name) {
        this.lend_phone_name = lend_phone_name;
    }

    public long getLend_phone_number() {
        return lend_phone_number;
    }

    public void setLend_phone_number(long lend_phone_number) {
        this.lend_phone_number = lend_phone_number;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public long getStatus() {
        return status;
    }

    public void setStatus(long status) {
        this.status = status;
    }

    public static BmobLendPhoneNote transformLendPhoneNote(LendPhoneNote lendPhoneNote) {
        BmobLendPhoneNote bmobLendPhoneNote = new BmobLendPhoneNote();
        bmobLendPhoneNote.setStatus(lendPhoneNote.getLend_phone_status());
        bmobLendPhoneNote.setPhone_id(lendPhoneNote.getAttach_bmob_phone_id());
        bmobLendPhoneNote.setPhoto_url(lendPhoneNote.getLend_phone_photo_url());
        bmobLendPhoneNote.setLend_phone_number(lendPhoneNote.getLend_phone_number());
        bmobLendPhoneNote.setLend_phone_name(lendPhoneNote.getLend_phone_name());
        if (!TextUtils.isEmpty(lendPhoneNote.getBmob_lend_phone_id())) {
            bmobLendPhoneNote.setObjectId(lendPhoneNote.getBmob_lend_phone_id());
        }
        return bmobLendPhoneNote;
    }
}
