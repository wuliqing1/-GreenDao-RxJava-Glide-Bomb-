package android.wuliqing.com.lendphonesystemapp.model;


import cn.bmob.v3.BmobObject;

public class BmobLendPhoneNote extends BmobObject {
    public static final String TABLE_NAME = "BmobLendPhoneNote";
    private String phone_id;
    private String lend_phone_name;
    private long lend_phone_number;
    private String photo_url;

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
}
