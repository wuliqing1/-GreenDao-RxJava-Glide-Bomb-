package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.adapter.BasePullListAdapter;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenter extends BasePresenter<PhoneListView> {
    private static final String TAG = "PhoneListPresenter";
    private DataBaseAction mDataBaseAction = new PhoneTableAction();

    public void loadAllPhoneData() {
        queryListInDataBase();
    }

    private void queryListInDataBase() {//查询本地数据库
        Observable.create(new Observable.OnSubscribe<List<PhoneNote>>() {
            @Override
            public void call(Subscriber<? super List<PhoneNote>> subscriber) {
                List<PhoneNote> phoneNotes = mDataBaseAction.queryAll();
//                List<PhoneNoteModel> phoneNoteModels = phoneNotes == null ? null
//                        : BmobPhoneNoteHelp.getConvertPhoneModelData(phoneNotes);//数据转换
                subscriber.onNext(phoneNotes);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<PhoneNote>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), "queryListInDataBase " + e.toString());
                        if (mView != null) {
                            mView.onFetchedPhones(null);
                        }
                    }

                    @Override
                    public void onNext(List<PhoneNote> phoneNoteModels) {
                        if (mView != null) {
                            mView.onFetchedPhones(phoneNoteModels);
                        }
                    }
                });
    }

    public void updatePhoneOneData(final BasePullListAdapter basePullListAdapter, final String phone_id) {
        Observable.create(new Observable.OnSubscribe<PhoneNote>() {
            @Override
            public void call(Subscriber<? super PhoneNote> subscriber) {
                PhoneNote phoneNote = (PhoneNote)mDataBaseAction.queryOneDataWithID(phone_id);
//                PhoneNoteModel phoneNoteModel = phoneNote == null ? null
//                        : BmobPhoneNoteHelp.convertPhoneNoteToPhoneNoteModel(phoneNote);//数据转换
                basePullListAdapter.updateOneData(phoneNote, phone_id);
                subscriber.onNext(phoneNote);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PhoneNote>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), "queryListInDataBase " + e.toString());
                    }

                    @Override
                    public void onNext(PhoneNote phoneNoteModel) {
//                        if (mView != null) {
//                            mView.onUpdateOnePhoneCompleted();
//                        }
                    }
                });
    }

    public void removeData(String id) {
        mDataBaseAction.remove(id);
    }

    public void queryPhoneWithPhoneName(String key) {
        mDataBaseAction.queryWithKey(PhoneNoteDao.Properties.Phone_name.columnName, key);
    }

    public void setDataBaseAction(DataBaseAction dataBaseAction) {
        mDataBaseAction = dataBaseAction;
    }
}
