package android.wuliqing.com.lendphonesystemapp.model;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;
import zte.phone.greendao.PhoneNote;

public class BmobPhoneNote extends BmobObject{
    private String phone_name;
    private Integer phone_number;
    private String project_name;
    private BmobFile bmobFile;

    public String getPhone_name() {
        return phone_name;
    }

    public void setPhone_name(String phone_name) {
        this.phone_name = phone_name;
    }

    public Integer getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(Integer phone_number) {
        this.phone_number = phone_number;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public BmobFile getBmobFile() {
        return bmobFile;
    }

    public void setBmobFile(BmobFile bmobFile) {
        this.bmobFile = bmobFile;
    }

    public static BmobPhoneNote transformPhoneNote(PhoneNote phoneNote) {
        BmobPhoneNote bmobPhoneNote = new BmobPhoneNote();
        bmobPhoneNote.setPhone_name(phoneNote.getPhone_name());
        bmobPhoneNote.setPhone_number(phoneNote.getPhone_number());
        bmobPhoneNote.setProject_name(phoneNote.getProject_name());
        bmobPhoneNote.setBmobFile(BmobFile.createEmptyFile());
        return bmobPhoneNote;
    }
}
