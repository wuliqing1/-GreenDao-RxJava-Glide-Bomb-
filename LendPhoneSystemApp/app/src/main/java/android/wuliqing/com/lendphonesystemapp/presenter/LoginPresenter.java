package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.LoginView;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class LoginPresenter extends BasePresenter<LoginView> {

    public void login(String name, String password) {
        final MyUser myUser = new MyUser();
        myUser.setUsername(name);
        myUser.setPassword(password);
        myUser.setAdmin(false);
        myUser.login(LendPhoneApplication.getAppContext(), new SaveListener() {

            @Override
            public void onSuccess() {
                mView.onLoginResult(true);
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "登陆失败:" + code + "," + msg);
                mView.onLoginResult(false);
            }
        });
    }
}
