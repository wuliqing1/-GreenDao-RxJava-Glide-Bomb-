package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNoteHelp;
import android.wuliqing.com.lendphonesystemapp.model.PhoneDetailNote;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneDetailView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobLendPhoneHttp;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class PhoneDetailPresenter extends BasePresenter<PhoneDetailView> {

//    private DataBaseAction mLendPhoneTableAction;
    private BaseHttp mBaseHttp = new BmobLendPhoneHttp();
//    private BmobRealTimeData rtd;

    @Override
    public void attach(PhoneDetailView view) {
        super.attach(view);
//        rtd = new BmobRealTimeData();
//        rtd.start(LendPhoneApplication.getAppContext(), new ValueEventListener() {
//            @Override
//            public void onConnectCompleted() {
//                if (rtd.isConnected()) {
//                    rtd.subTableUpdate(BmobLendPhoneNote.TABLE_NAME);
//                }
//            }
//
//            @Override
//            public void onDataChange(JSONObject jsonObject) {
//                if (BmobLendPhoneNote.TABLE_NAME.equals(jsonObject.optString("tableName"))) {
//                    JSONObject data = jsonObject.optJSONObject("data");
//                    JsonElement element = new JsonParser().parse(data.toString());
//                    Gson gson = new Gson();
//                    BmobLendPhoneNote bmobLendPhoneNote = gson.fromJson(element, BmobLendPhoneNote.class);
//                    insertLocalLendDataBase(bmobLendPhoneNote);
//                }
//            }
//        });

    }

    @Override
    public void detach() {
        super.detach();
//        if (rtd.isConnected()) {
//            rtd.unsubTableUpdate(BmobLendPhoneNote.TABLE_NAME);
//        }
    }

    public PhoneDetailPresenter() {
//        mLendPhoneTableAction = new LendPhoneTableAction();
    }

    public void doPhoneDetailHeadData(final String phone_id) {
        Observable.create(new Observable.OnSubscribe<PhoneDetailNote>() {
            @Override
            public void call(Subscriber<? super PhoneDetailNote> subscriber) {
                PhoneTableAction phoneTableAction = new PhoneTableAction();
                PhoneNote phoneNode = phoneTableAction.queryOneDataWithID(phone_id);
                PhoneDetailNote phoneDetailNote = BmobPhoneNoteHelp.convertPhoneNoteToPhoneNoteModel(phoneNode);
                subscriber.onNext(phoneDetailNote);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PhoneDetailNote>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), "queryListInDataBase " + e.toString());
                    }

                    @Override
                    public void onNext(PhoneDetailNote phoneDetailNote) {
                        if (mView != null) {
                            mView.onQueryPhoneResult(phoneDetailNote);
                        }
                    }
                });
    }

    public void lendPhone(BmobLendPhoneNote bmobLendPhoneNote) {//借手机
        mBaseHttp.send("", "", bmobLendPhoneNote, new SendDataListener<BmobLendPhoneNote>() {
            @Override
            public void onSuccess(BmobLendPhoneNote obj) {
//                insertLocalLendDataBase(obj);
                if (mView != null) {
                    mView.onLendPhoneResult(obj);
                }
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "lendPhone " + s);
                if (mView != null) {
                    mView.onLendPhoneResult(null);
                }
            }
        });
    }

//    private void insertLocalLendDataBase(final BmobLendPhoneNote obj) {
//        Observable.create(new Observable.OnSubscribe<LendPhoneNote>() {
//            @Override
//            public void call(Subscriber<? super LendPhoneNote> subscriber) {
//                LendPhoneNote lendPhoneNote = (LendPhoneNote) mLendPhoneTableAction.queryOneDataWithID(obj.getObjectId());
//                if (lendPhoneNote == null) {//本地数据库不存在
//                    if (!TextUtils.isEmpty(obj.getCreatedAt())
//                            && !TextUtils.isEmpty(obj.getLend_phone_name())) {
//                        lendPhoneNote = BmobPhoneNoteHelp.convertBmobLendPhoneNoteToLendPhoneNote(obj);
//                        mLendPhoneTableAction.add(lendPhoneNote);
//                    }
//                } else {//更新本地数据库
//                    if (TextUtils.isEmpty(obj.getCreatedAt())
//                            && TextUtils.isEmpty(obj.getLend_phone_name())) {
//                        mLendPhoneTableAction.remove(obj.getObjectId());
//                    } else {
//                        BmobPhoneNoteHelp.bmobLendPhoneNoteCopyToLendPhoneNote(obj, lendPhoneNote);
//                        mLendPhoneTableAction.update(lendPhoneNote);
//                    }
//                }
//
//                subscriber.onNext(lendPhoneNote);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<LendPhoneNote>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.show(LendPhoneApplication.getAppContext(), "insertLocalLendDataBase " + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(LendPhoneNote lendPhoneNote) {
//                        if (mView != null) {
//                            mView.onLendPhoneResult(lendPhoneNote);
//                        }
//                    }
//                });
//    }
}
