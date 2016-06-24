package android.wuliqing.com.lendphonesystemapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 10172915 on 2016/6/2. 主界面显示数据modle
 */
public class PhoneNoteModel implements Parcelable {
    private long phone_id;
    private String pic_url;
    private String phone_name;
    private int left_number;
    private int lend_number;
    private String lend_names;
    private String date;

    public PhoneNoteModel() {

    }

    protected PhoneNoteModel(Parcel in) {
        pic_url = in.readString();
        phone_name = in.readString();
        left_number = in.readInt();
        lend_number = in.readInt();
        lend_names = in.readString();
        date = in.readString();
        phone_id = in.readLong();
    }

    public static final Creator<PhoneNoteModel> CREATOR = new Creator<PhoneNoteModel>() {
        @Override
        public PhoneNoteModel createFromParcel(Parcel in) {
            return new PhoneNoteModel(in);
        }

        @Override
        public PhoneNoteModel[] newArray(int size) {
            return new PhoneNoteModel[size];
        }
    };

    public long getPhone_id() {
        return phone_id;
    }

    public void setPhone_id(long phone_id) {
        this.phone_id = phone_id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPic_path() {
        return pic_url;
    }

    public void setPic_path(String pic_path) {
        this.pic_url = pic_path;
    }

    public String getPhone_name() {
        return phone_name;
    }

    public void setPhone_name(String phone_name) {
        this.phone_name = phone_name;
    }

    public int getLeft_number() {
        return left_number;
    }

    public void setLeft_number(int left_number) {
        this.left_number = left_number;
    }

    public int getLend_number() {
        return lend_number;
    }

    public void setLend_number(int lend_number) {
        this.lend_number = lend_number;
    }

    public String getLend_names() {
        return lend_names;
    }

    public void setLend_names(String lend_names) {
        this.lend_names = lend_names;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(pic_url);
        dest.writeString(phone_name);
        dest.writeInt(left_number);
        dest.writeInt(lend_number);
        dest.writeString(lend_names);
        dest.writeString(date);
        dest.writeLong(phone_id);
    }
}
