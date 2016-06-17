package android.wuliqing.com.lendphonesystemapp.model;

import android.graphics.Bitmap;

/**
 * Created by 10172915 on 2016/6/2.
 */
public class PhoneNoteModel {
    private Bitmap bitmap;
    private String phone_name;
    private int left_number;
    private int lend_number;
    private String lend_names;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
}
