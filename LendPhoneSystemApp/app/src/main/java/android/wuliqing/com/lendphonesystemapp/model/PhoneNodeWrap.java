package android.wuliqing.com.lendphonesystemapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/7/8.
 */
public class PhoneNodeWrap implements Parcelable {
    private long id;
    private String bmob_phone_id;
    private String phone_name;
    private long phone_number;
    private String project_name;
    private String phone_time;
    private String phone_photo_url;

    public PhoneNodeWrap(PhoneNote phoneNote) {
        id = phoneNote.getId();
        bmob_phone_id = phoneNote.getBmob_phone_id();
        phone_name = phoneNote.getPhone_name();
        phone_number = phoneNote.getPhone_number();
        project_name = phoneNote.getProject_name();
        phone_time = phoneNote.getPhone_time();
        phone_photo_url = phoneNote.getPhone_photo_url();
    }

    protected PhoneNodeWrap(Parcel in) {
        id = in.readLong();
        bmob_phone_id = in.readString();
        phone_name = in.readString();
        phone_number = in.readLong();
        project_name = in.readString();
        phone_time = in.readString();
        phone_photo_url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(bmob_phone_id);
        dest.writeString(phone_name);
        dest.writeLong(phone_number);
        dest.writeString(project_name);
        dest.writeString(phone_time);
        dest.writeString(phone_photo_url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PhoneNodeWrap> CREATOR = new Creator<PhoneNodeWrap>() {
        @Override
        public PhoneNodeWrap createFromParcel(Parcel in) {
            return new PhoneNodeWrap(in);
        }

        @Override
        public PhoneNodeWrap[] newArray(int size) {
            return new PhoneNodeWrap[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getBmob_phone_id() {
        return bmob_phone_id;
    }

    public void setBmob_phone_id(String bmob_phone_id) {
        this.bmob_phone_id = bmob_phone_id;
    }

    public String getPhone_name() {
        return phone_name;
    }

    public void setPhone_name(String phone_name) {
        this.phone_name = phone_name;
    }

    public long getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(long phone_number) {
        this.phone_number = phone_number;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public String getPhone_time() {
        return phone_time;
    }

    public void setPhone_time(String phone_time) {
        this.phone_time = phone_time;
    }

    public String getPhone_photo_url() {
        return phone_photo_url;
    }

    public void setPhone_photo_url(String phone_photo_url) {
        this.phone_photo_url = phone_photo_url;
    }
}
