package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.LendPhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.model.AdminPhoneDetailNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.mvpview.AdminApplyPhoneListView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobLendPhoneHttp;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.LendPhoneNoteDao;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class AdminApplyPhoneListPresenter extends BasePresenter<AdminApplyPhoneListView> {
    private final DataBaseAction mLendPhoneTableAction;
    private final DataBaseAction mPhoneTableAction;
    private final BaseHttp mBaseHttp = new BmobLendPhoneHttp();


    public AdminApplyPhoneListPresenter() {
        mLendPhoneTableAction = new LendPhoneTableAction();
        mPhoneTableAction = new PhoneTableAction();
    }

    public void queryApplyDataBaseAll() {
        Observable.create(new Observable.OnSubscribe<List<AdminPhoneDetailNote>>() {
            @Override
            public void call(Subscriber<? super List<AdminPhoneDetailNote>> subscriber) {
                List<LendPhoneNote> list = mLendPhoneTableAction
                        .queryWithColumn(LendPhoneNoteDao.Properties.Lend_phone_status, BmobLendPhoneNote.APPLY_ING_STATUS);
                List<AdminPhoneDetailNote> adminPhoneDetailNotes = new ArrayList<>();
                for (int i = 0; i < list.size(); i++) {
                    LendPhoneNote lendPhoneNote = list.get(i);
                    AdminPhoneDetailNote adminPhoneDetailNote = new AdminPhoneDetailNote();
                    adminPhoneDetailNote.setLendPhoneNote(lendPhoneNote);
                    PhoneNote phoneNote = (PhoneNote) mPhoneTableAction.queryOneDataWithID(lendPhoneNote.getAttach_bmob_phone_id());
                    adminPhoneDetailNote.setPhone_url(phoneNote.getPhone_photo_url());
                    adminPhoneDetailNote.setPhone_name(phoneNote.getPhone_name());
                    adminPhoneDetailNotes.add(adminPhoneDetailNote);
                }
                subscriber.onNext(adminPhoneDetailNotes);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<AdminPhoneDetailNote>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), "queryApplyDataBaseAll " + e.toString());
                        if (mView != null) {
                            mView.onFetchedLendPhones(null);
                        }
                    }

                    @Override
                    public void onNext(List<AdminPhoneDetailNote> phoneNoteModels) {
                        if (mView != null) {
                            mView.onFetchedLendPhones(phoneNoteModels);
                        }
                    }
                });

    }

    public void agreePhoneApply(final AdminPhoneDetailNote adminPhoneDetailNote) {
        LendPhoneNote lendPhoneNote = adminPhoneDetailNote.getLendPhoneNote();
        lendPhoneNote.setLend_phone_status(BmobLendPhoneNote.APPLY_SUCCESS_STATUS);
        mBaseHttp.update("", "", lendPhoneNote, new UpdateDataListener<Boolean>() {
            @Override
            public void onResult(Boolean result) {
                mView.onAgreeResult(result);
            }
        });
    }

}
