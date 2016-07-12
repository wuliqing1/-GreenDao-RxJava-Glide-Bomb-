package android.wuliqing.com.lendphonesystemapp.model;

import android.text.TextUtils;
import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.dataBase.LendPhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/16.
 */
public class BmobPhoneNoteHelp {

    public static PhoneNote convertBmobPhoneNoteToPhoneNote(BmobPhoneNote bmobPhoneNote) {
        PhoneNote phoneNote = new PhoneNote();
        phoneNote.setPhone_number(bmobPhoneNote.getPhone_number());
        phoneNote.setPhone_time(bmobPhoneNote.getUpdatedAt());
        phoneNote.setProject_name(bmobPhoneNote.getProject_name());
        phoneNote.setPhone_name(bmobPhoneNote.getPhone_name());
        phoneNote.setPhone_photo_url(bmobPhoneNote.getPic_url());
        phoneNote.setBmob_phone_id(bmobPhoneNote.getObjectId());
        return phoneNote;
    }

    public static LendPhoneNote convertBmobLendPhoneNoteToLendPhoneNote(BmobLendPhoneNote bmobLendPhoneNote) {
        LendPhoneNote lendPhoneNote = new LendPhoneNote();
        lendPhoneNote.setLend_phone_name(bmobLendPhoneNote.getLend_phone_name());
        lendPhoneNote.setLend_phone_time(bmobLendPhoneNote.getCreatedAt());
        lendPhoneNote.setAttach_bmob_phone_id(bmobLendPhoneNote.getPhone_id());
        lendPhoneNote.setBmob_lend_phone_id(bmobLendPhoneNote.getObjectId());
        lendPhoneNote.setLend_phone_number(bmobLendPhoneNote.getLend_phone_number());
        lendPhoneNote.setLend_phone_photo_url(bmobLendPhoneNote.getPhoto_url());
        return lendPhoneNote;
    }

    public static void updatePhoneNoteTable(final BmobPhoneNote bmobPhoneNote, final UpdateDataListener updateDataListener) {
        final PhoneTableAction mPhoneTableAction = new PhoneTableAction();

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                PhoneNote phoneNote = mPhoneTableAction.queryOneDataWithID(bmobPhoneNote.getObjectId());
                if (phoneNote == null) {//添加本地数据库
                    if (!TextUtils.isEmpty(bmobPhoneNote.getCreatedAt())
                            && !TextUtils.isEmpty(bmobPhoneNote.getPhone_name())) {
                        phoneNote = convertBmobPhoneNoteToPhoneNote(bmobPhoneNote);
                        mPhoneTableAction.add(phoneNote);
                    }
                } else {//更新本地数据库
                    if (TextUtils.isEmpty(bmobPhoneNote.getCreatedAt())
                            && TextUtils.isEmpty(bmobPhoneNote.getPhone_name())) {
                        mPhoneTableAction.remove(bmobPhoneNote.getObjectId());
                    } else {
                        bmobPhoneNoteCopyToPhoneNote(bmobPhoneNote, phoneNote);
                        mPhoneTableAction.update(phoneNote);
                    }
                }

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
                        updateDataListener.onResult(false);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        updateDataListener.onResult(aBoolean);
                    }
                });
    }

    public static void updateLendPhoneNoteTable(final List<BmobLendPhoneNote> bmobPhoneNotes,
                                                final UpdateDataListener updateDataListener) {
        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                syncLendPhoneNoteTable(bmobPhoneNotes);

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
                        updateDataListener.onResult(false);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        updateDataListener.onResult(aBoolean);
                    }
                });

    }

    private static void syncLendPhoneNoteTable(List<BmobLendPhoneNote> bmobPhoneNotes) {
        LendPhoneTableAction mPhoneTableAction = new LendPhoneTableAction();
        mPhoneTableAction.clearData();
        List<LendPhoneNote> list = new ArrayList<>();
        for (int i = 0; i < bmobPhoneNotes.size(); i++) {
            list.add(convertBmobLendPhoneNoteToLendPhoneNote(bmobPhoneNotes.get(i)));
        }
        mPhoneTableAction.addCollection(list);
//        List<LendPhoneNote> phoneNotes = mPhoneTableAction.queryAll();
//        List<String> localPhoneIds = new ArrayList<String>();
//        for (LendPhoneNote phoneNote :
//                phoneNotes) {
//            localPhoneIds.add(phoneNote.getBmob_lend_phone_id());
//        }
//        List<String> bmobPhoneIds = new ArrayList<String>();
//        for (BmobLendPhoneNote bmobPhoneNote :
//                bmobPhoneNotes) {//本地数据库不存在的 则添加到本地数据库
//            String id = bmobPhoneNote.getObjectId();
//            bmobPhoneIds.add(id);
//            if (!localPhoneIds.contains(id)) {//如果数据不存在 则添加本地数据库
//                mPhoneTableAction.add(convertBmobLendPhoneNoteToLendPhoneNote(bmobPhoneNote));
//                localPhoneIds.add(id);
//            }
//        }
//        for (int i = 0; i < localPhoneIds.size(); i++) {//本地数据库多余的 则删除本地数据多余的数据
//            if (!bmobPhoneIds.contains(localPhoneIds.get(i))) {
//                try {
//                    mPhoneTableAction.remove(localPhoneIds.get(i));
//                } catch (IllegalArgumentException e) {
//                    mPhoneTableAction.remove(phoneNotes.get(i).getId());
//                }
//            }
//        }

//        List<LendPhoneNote> phoneNotes1 = mPhoneTableAction.query();
//        for (int i = 0; i < bmobPhoneNotes.size(); i++) {//更新本地数据库
//            BmobLendPhoneNote bmobPhoneNote = bmobPhoneNotes.get(i);
//            for (int j = 0; j < phoneNotes1.size(); j++) {
//                LendPhoneNote phoneNote = phoneNotes1.get(j);
//                if (bmobPhoneNote.getObjectId().equals(phoneNote.getBmob_lend_phone_id())) {
//                    bmobLendPhoneNoteCopyToLendPhoneNote(bmobPhoneNote, phoneNote);
//                    mPhoneTableAction.update(phoneNote);
//                    break;
//                }
//            }
//        }
    }

    public static void updatePhoneNoteTable(final List<BmobPhoneNote> bmobPhoneNotes,
                                            final UpdateDataListener updateDataListener) {

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                syncPhoneNoteTable(bmobPhoneNotes);

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
                        updateDataListener.onResult(false);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        updateDataListener.onResult(aBoolean);
                    }
                });
    }

    private static void syncPhoneNoteTable(List<BmobPhoneNote> bmobPhoneNotes) {
        PhoneTableAction mPhoneTableAction = new PhoneTableAction();
        List<PhoneNote> phoneNotes = mPhoneTableAction.queryAll();
        List<String> localPhoneIds = new ArrayList<String>();
        for (PhoneNote phoneNote :
                phoneNotes) {
            localPhoneIds.add(phoneNote.getBmob_phone_id());
        }
        List<String> bmobPhoneIds = new ArrayList<String>();
        for (BmobPhoneNote bmobPhoneNote :
                bmobPhoneNotes) {//本地数据库不存在的 则添加到本地数据库
            String id = bmobPhoneNote.getObjectId();
            bmobPhoneIds.add(id);
            if (!localPhoneIds.contains(id)) {//如果数据不存在 则添加本地数据库
                mPhoneTableAction.add(convertBmobPhoneNoteToPhoneNote(bmobPhoneNote));
                localPhoneIds.add(id);
            }
        }
        for (int i = 0; i < localPhoneIds.size(); i++) {//本地数据库多余的 则删除本地数据多余的数据
            if (!bmobPhoneIds.contains(localPhoneIds.get(i))) {
                try {
                    mPhoneTableAction.remove(localPhoneIds.get(i));
                } catch (IllegalArgumentException e) {
                    mPhoneTableAction.remove(phoneNotes.get(i).getId());
                }
            }
        }

        List<PhoneNote> phoneNotes1 = mPhoneTableAction.queryAll();
        for (int i = 0; i < bmobPhoneNotes.size(); i++) {//更新本地数据库
            BmobPhoneNote bmobPhoneNote = bmobPhoneNotes.get(i);
            for (int j = 0; j < phoneNotes1.size(); j++) {
                PhoneNote phoneNote = phoneNotes1.get(j);
                if (bmobPhoneNote.getObjectId().equals(phoneNote.getBmob_phone_id())) {
                    bmobPhoneNoteCopyToPhoneNote(bmobPhoneNote, phoneNote);
                    mPhoneTableAction.update(phoneNote);
                    break;
                }
            }
        }
    }

    public static PhoneDetailNote convertPhoneNoteToPhoneNoteModel(PhoneNodeWrap phoneNote) {
        PhoneDetailNote phoneNoteModel = new PhoneDetailNote();
        phoneNoteModel.setPhone_name(phoneNote.getPhone_name());
        int left_number = DBHelper.getInstance().LeftPhoneNumber(phoneNote.getBmob_phone_id());
        phoneNoteModel.setLeft_number(left_number);
        int lend_number = DBHelper.getInstance().LendPhoneNumber(phoneNote.getBmob_phone_id());
        phoneNoteModel.setLend_number(lend_number);
        phoneNoteModel.setLend_names(DBHelper.getInstance().lendPhoneNames(phoneNote.getBmob_phone_id()));
        phoneNoteModel.setDate(phoneNote.getPhone_time());
        phoneNoteModel.setPic_url(phoneNote.getPhone_photo_url());
        phoneNoteModel.setPhone_id(phoneNote.getBmob_phone_id());
        phoneNoteModel.setProject_name(phoneNote.getProject_name());
        phoneNoteModel.setPhone_number(phoneNote.getPhone_number());
        return phoneNoteModel;
    }

    public static void bmobPhoneNoteCopyToPhoneNote(BmobPhoneNote bmobPhoneNote, PhoneNote phoneNote) {
        phoneNote.setPhone_number(bmobPhoneNote.getPhone_number());
        phoneNote.setPhone_photo_url(bmobPhoneNote.getPic_url());
        phoneNote.setProject_name(bmobPhoneNote.getProject_name());
        phoneNote.setPhone_name(bmobPhoneNote.getPhone_name());
        if (TextUtils.isEmpty(bmobPhoneNote.getUpdatedAt())) {
            phoneNote.setPhone_time(bmobPhoneNote.getCreatedAt());
        } else {
            phoneNote.setPhone_time(bmobPhoneNote.getUpdatedAt());
        }
    }

    public static void bmobLendPhoneNoteCopyToLendPhoneNote(BmobLendPhoneNote bmobPhoneNote, LendPhoneNote phoneNote) {
        phoneNote.setLend_phone_number(bmobPhoneNote.getLend_phone_number());
        phoneNote.setLend_phone_photo_url(bmobPhoneNote.getPhoto_url());
        phoneNote.setLend_phone_name(bmobPhoneNote.getLend_phone_name());
        phoneNote.setBmob_lend_phone_id(bmobPhoneNote.getObjectId());
        if (TextUtils.isEmpty(bmobPhoneNote.getUpdatedAt())) {
            phoneNote.setLend_phone_time(bmobPhoneNote.getCreatedAt());
        } else {
            phoneNote.setLend_phone_time(bmobPhoneNote.getUpdatedAt());
        }
        phoneNote.setAttach_bmob_phone_id(bmobPhoneNote.getPhone_id());
    }
}
