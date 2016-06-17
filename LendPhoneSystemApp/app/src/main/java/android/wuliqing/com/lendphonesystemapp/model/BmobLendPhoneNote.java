package android.wuliqing.com.lendphonesystemapp.model;


import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class BmobLendPhoneNote extends BmobObject{
    private Long phone_id;
    private String lend_phone_name;
    private Integer lend_phone_number;
    private BmobFile bmobFile;

    public Long getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(Long phone_id) {
        this.phone_id = phone_id;
    }

    public String getLend_phone_name() {
        return lend_phone_name;
    }

    public void setLend_phone_name(String lend_phone_name) {
        this.lend_phone_name = lend_phone_name;
    }

    public Integer getLend_phone_number() {
        return lend_phone_number;
    }

    public void setLend_phone_number(Integer lend_phone_number) {
        this.lend_phone_number = lend_phone_number;
    }

    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }

}
