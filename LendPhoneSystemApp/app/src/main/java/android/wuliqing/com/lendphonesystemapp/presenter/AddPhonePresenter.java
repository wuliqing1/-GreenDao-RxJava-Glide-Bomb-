package android.wuliqing.com.lendphonesystemapp.presenter;

import android.text.TextUtils;
import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

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

    public void addPhone(final PhoneNote phoneNote) {
        if (phoneNote == null) {
            throw new IllegalArgumentException();
        }

        Observable network_observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                if (NetUtil.isNetworkConnected(LendPhoneApplication.getAppContext())) {
                    if (addPhoneNetWork(phoneNote)) {
                        subscriber.onCompleted();
                    } else {
                        subscriber.onNext(false);
                    }
                } else {
                    subscriber.onNext(false);
                }
            }
        });
        Observable database_observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                addPhoneTable(phoneNote);
                subscriber.onNext(true);
            }
        });
        Observable.concat(network_observable, database_observable).first().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (mView != null) {
                            mView.onResult(aBoolean);
                        }
                    }
                });
    }

    private boolean addPhoneNetWork(final PhoneNote phoneNote) {

        return true;
    }

    public boolean addPhoneTable(final PhoneNote phoneNote) {
        mPhoneTableAction.add(phoneNote);
        return true;
    }

    public void setDataBaseAction(DataBaseAction mDataBaseAction) {
        this.mPhoneTableAction = mDataBaseAction;
    }

    public void queryPhoneNameAndProjectName() {
        Observable.create(new Observable.OnSubscribe<List<PhoneNote>>() {
            @Override
            public void call(Subscriber<? super List<PhoneNote>> subscriber) {
                List<PhoneNote> phoneNotes = null;
                if (NetUtil.isNetworkConnected(LendPhoneApplication.getAppContext())) {
                    phoneNotes = null;//从网络获取
                } else {
                    phoneNotes = mPhoneTableAction.query();
                }
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
}
