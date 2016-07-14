package android.wuliqing.com.lendphonesystemapp.model;

import zte.phone.greendao.LendPhoneNote;

/**
 * Created by 10172915 on 2016/7/14.
 */
public class AdminPhoneDetailNote {
    private String phone_url;
    private String phone_name;
    private LendPhoneNote lendPhoneNote;

    public String getPhone_url() {
        return phone_url;
    }

    public void setPhone_url(String phone_url) {
        this.phone_url = phone_url;
    }

    public String getPhone_name() {
        return phone_name;
    }

    public void setPhone_name(String phone_name) {
        this.phone_name = phone_name;
    }

    public LendPhoneNote getLendPhoneNote() {
        return lendPhoneNote;
    }

    public void setLendPhoneNote(LendPhoneNote lendPhoneNote) {
        this.lendPhoneNote = lendPhoneNote;
    }
}
