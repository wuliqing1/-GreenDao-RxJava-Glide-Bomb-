package android.wuliqing.com.lendphonesystemapp.model;

import cn.bmob.v3.BmobUser;

public class MyUser extends BmobUser {
    public static final String TABLE_NAME = "_User";
    private static final long serialVersionUID = 1L;
    private String photo_url;
    private String department;
    private String position;
    private boolean isAdmin;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getPhoto_url() {
        return photo_url;
    }

    public void setPhoto_url(String photo_url) {
        this.photo_url = photo_url;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
