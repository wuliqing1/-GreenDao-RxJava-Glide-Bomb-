package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.mvpview.RegisterView;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class RegisterPresenter extends BasePresenter<RegisterView> {

    public void register(String name, String password) {
        final MyUser myUser = new MyUser();
        myUser.setUsername(name);
        myUser.setPassword(password);
        myUser.signUp(LendPhoneApplication.getAppContext(), new SaveListener() {

            @Override
            public void onSuccess() {
//                toast("注册成功:" + myUser.getUsername() + "-"
//                        + myUser.getObjectId() + "-" + myUser.getCreatedAt()
//                        + "-" + myUser.getSessionToken()+",是否验证："+myUser.getEmailVerified());
                mView.onRegisterResult(true);
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "注册失败:" + msg);
                mView.onRegisterResult(false);
            }
        });
    }
}
