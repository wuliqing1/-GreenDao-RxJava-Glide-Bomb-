package android.wuliqing.com.lendphonesystemapp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobHttp;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class AddPhonePresenter extends BasePresenter<AddPhoneView> {
    private DataBaseAction mPhoneTableAction = new PhoneTableAction();
    private File file = null;
    private BaseHttp mBaseHttp = new BmobHttp();

    public void setHttp(BaseHttp mBaseHttp) {//可以定制网络框架
        this.mBaseHttp = mBaseHttp;
    }

    public void addPhone(final PhoneNote phoneNote) {
        if (phoneNote == null) {
            throw new IllegalArgumentException();
        }
        mBaseHttp.send(null, null, phoneNote, new SendDataListener() {
            @Override
            public void onSuccess() {
                addPhoneTable(phoneNote);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), s);
                if (mView != null)
                    mView.onResult(false);
            }
        });
    }

    public void addPicToNetWork(final Context context, final UpLoadDataListener<String> upLoadDataListener) {
        mBaseHttp.upLoad(file, null, upLoadDataListener);
    }

    public void addPhoneTable(final PhoneNote phoneNote) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                mPhoneTableAction.add(phoneNote);
                subscriber.onNext(true);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), e.toString());
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (mView != null) {
                            mView.onResult(aBoolean);
                        }
                    }
                });
    }

    public void setDataBaseAction(DataBaseAction mDataBaseAction) {
        this.mPhoneTableAction = mDataBaseAction;
    }

    public void queryPhoneNameAndProjectName() {
        BmobQuery<BmobPhoneNote> bmobPhoneNoteBmobQuery = new BmobQuery<>();
        bmobPhoneNoteBmobQuery.findObjects(LendPhoneApplication.getAppContext(), new FindListener<BmobPhoneNote>() {
            @Override
            public void onSuccess(List<BmobPhoneNote> list) {
                queryResultHandler(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), s);
                queryNameInDataBase();
            }
        });

    }

    private void queryNameInDataBase() {
        Observable.create(new Observable.OnSubscribe<List<PhoneNote>>() {
            @Override
            public void call(Subscriber<? super List<PhoneNote>> subscriber) {
                List<PhoneNote> phoneNotes = mPhoneTableAction.query();
                subscriber.onNext(phoneNotes);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PhoneNote>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<PhoneNote> phoneNotes) {
                        if (mView != null) {
                            List<String> phoneNames = new ArrayList<>();
                            List<String> projectNames = new ArrayList<>();
                            for (PhoneNote phoneNote : phoneNotes) {
                                if (!TextUtils.isEmpty(phoneNote.getPhone_name())) {
                                    phoneNames.add(phoneNote.getPhone_name());
                                }
                                if (!TextUtils.isEmpty(phoneNote.getProject_name())) {
                                    projectNames.add(phoneNote.getProject_name());
                                }
                            }
                            mView.onQueryPhoneNameResult(phoneNames);
                            mView.onQueryProjectNameResult(projectNames);
                        }
                    }
                });
    }

    private void queryResultHandler(List<BmobPhoneNote> list) {
        if (mView != null) {
            List<String> phoneNames = new ArrayList<>();
            List<String> projectNames = new ArrayList<>();
            for (BmobPhoneNote phoneNote : list) {
                if (!TextUtils.isEmpty(phoneNote.getPhone_name())) {
                    phoneNames.add(phoneNote.getPhone_name());
                }
                if (!TextUtils.isEmpty(phoneNote.getProject_name())) {
                    projectNames.add(phoneNote.getProject_name());
                }
            }
            mView.onQueryPhoneNameResult(phoneNames);
            mView.onQueryProjectNameResult(projectNames);
        }
    }

    public void setBmobFile(File file) {
        this.file = file;
    }

    public File getBmobFile() {
        return file;
    }
}
