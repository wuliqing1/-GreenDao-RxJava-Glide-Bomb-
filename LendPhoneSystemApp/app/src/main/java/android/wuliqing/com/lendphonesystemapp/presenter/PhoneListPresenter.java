package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.utils.FormatTools;
import android.wuliqing.com.lendphonesystemapp.utils.LogHelper;
import android.wuliqing.com.lendphonesystemapp.utils.NetUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenter extends BasePresenter<PhoneListView> {
    private static final String TAG = "PhoneListPresenter";
    private DataBaseAction mDataBaseAction = new PhoneTableAction();

    public void loadData() {
//        Observable dataBase_observable = Observable.create(new Observable.OnSubscribe<List<PhoneNoteModel>>() {
//            @Override
//            public void call(Subscriber<? super List<PhoneNoteModel>> subscriber) {
//                if (!NetUtil.isNetworkConnected(LendPhoneApplication.getAppContext())) {
//                    List<PhoneNote> list = mDataBaseAction.query();//从数据库获取
//                    List<PhoneNoteModel> phoneNoteModels = getConvertPhoneData(list);
//                    subscriber.onNext(phoneNoteModels);
//                } else {
//                    subscriber.onCompleted();
//                }
//            }
//        });
        Observable network_observable = Observable.just("network")
                .map(new Func1<String, List<PhoneNote>>() {
                    @Override
                    public List<PhoneNote> call(String s) {
                        List<PhoneNote> phoneNotes = null;//从网络获取
                        if (NetUtil.isNetworkConnected(LendPhoneApplication.getAppContext())) {
                            phoneNotes = null;
                            if (phoneNotes == null) {
                                phoneNotes = mDataBaseAction.query();//从数据库获取
                            }
                        } else {
                            phoneNotes = mDataBaseAction.query();
                        }

                        return phoneNotes;
                    }
                }).map(new Func1<List<PhoneNote>, List<PhoneNoteModel>>() {
                    @Override
                    public List<PhoneNoteModel> call(List<PhoneNote> phoneNotes) {
                        return phoneNotes == null ? null : getConvertPhoneData(phoneNotes);
                    }
                });

        Subscriber subscriber = new Subscriber<List<PhoneNoteModel>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                LogHelper.logD(TAG, e.toString());
            }

            @Override
            public void onNext(List<PhoneNoteModel> phoneNoteModels) {
                if (mView != null)
                    mView.onFetchedPhones(phoneNoteModels);
            }
        };
//        Action1<List<PhoneNoteModel>> action_complete = new Action1<List<PhoneNoteModel>>() {
//            @Override
//            public void call(List<PhoneNoteModel> phoneNoteModels) {
//                if (mView != null)
//                    mView.onFetchedPhones(phoneNoteModels);
//            }
//        };

//        Observable.concat(dataBase_observable, network_observable).first()
        network_observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    private List<PhoneNoteModel> getConvertPhoneData(List<PhoneNote> list) {
        List<PhoneNoteModel> phoneNoteModels = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (PhoneNote phoneNote :
                    list) {
                PhoneNoteModel phoneNoteModel = convertPhoneNote(phoneNote);
                phoneNoteModels.add(phoneNoteModel);
            }
        }
        return phoneNoteModels;
    }

    private PhoneNoteModel convertPhoneNote(PhoneNote phoneNote) {
        PhoneNoteModel phoneNoteModel = new PhoneNoteModel();
        phoneNoteModel.setPhone_name(phoneNote.getPhone_name());
        int left_number = DBHelper.getInstance().LeftPhoneNumber(phoneNote.getId());
        phoneNoteModel.setLeft_number(left_number);
        int lend_number = DBHelper.getInstance().LendPhoneNumber(phoneNote.getId());
        phoneNoteModel.setLend_number(lend_number);
        phoneNoteModel.setLend_names(DBHelper.getInstance().lendPhoneNames(phoneNote.getId()));
        phoneNoteModel.setDate(phoneNote.getPhone_time());
        phoneNoteModel.setBitmap(FormatTools.getInstance().Bytes2Bitmap(phoneNote.getPhone_photo()));
        return phoneNoteModel;
    }

    public void removeData(long id) {
        mDataBaseAction.remove(id);
    }

    public void queryPhoneWithPhoneName(String key) {
        mDataBaseAction.queryWithKey(PhoneNoteDao.Properties.Phone_name.columnName, key);
    }

    public void setDataBaseAction(DataBaseAction dataBaseAction) {
        mDataBaseAction = dataBaseAction;
    }
}
