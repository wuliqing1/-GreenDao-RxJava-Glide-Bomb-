package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.UserView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobPhoneHttp;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class UserPresenter extends BasePresenter<UserView> {
    private File file = null;
    private BaseHttp mBaseHttp = new BmobPhoneHttp();

    public void setHttp(BaseHttp mBaseHttp) {//可以定制网络框架
        this.mBaseHttp = mBaseHttp;
    }

    public void addPicToNetWork(final UpLoadDataListener<String> upLoadDataListener) {
        mBaseHttp.upLoad(file, null, upLoadDataListener);
    }

    public void setBmobFile(File file) {
        this.file = file;
    }

    public File getBmobFile() {
        return file;
    }

    public void update(final String photoUrl,
                       final String name, final String department, final String position) {
        final MyUser bmobUser = BmobUser.getCurrentUser(LendPhoneApplication.getAppContext(), MyUser.class);
        if (bmobUser != null) {
            final MyUser myUser = new MyUser();
            myUser.setUsername(name);
            myUser.setPhoto_url(photoUrl);
            myUser.setDepartment(department);
            myUser.setPosition(position);
            myUser.setAdmin(bmobUser.isAdmin());
            myUser.update(LendPhoneApplication.getAppContext(), bmobUser.getObjectId(), new UpdateListener() {

                @Override
                public void onSuccess() {
                    mView.onUpdateResult(true);
                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtils.show(LendPhoneApplication.getAppContext(), "更新用户信息失败:" + msg);
                    mView.onUpdateResult(false);
                }
            });
        }

    }
}
