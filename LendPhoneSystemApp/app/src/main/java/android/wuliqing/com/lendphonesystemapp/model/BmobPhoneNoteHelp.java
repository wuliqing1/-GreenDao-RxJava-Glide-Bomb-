package android.wuliqing.com.lendphonesystemapp.model;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
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

    public static void updatePhoneNoteTable(final List<BmobPhoneNote> bmobPhoneNotes,
                                            final UpdateDataListener updateDataListener) {
        final PhoneTableAction mPhoneTableAction = new PhoneTableAction();

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                List<PhoneNote> phoneNotes = mPhoneTableAction.query();
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

                List<PhoneNote> phoneNotes1 = mPhoneTableAction.query();
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

    public static List<PhoneNoteModel> getConvertPhoneModelData(List<PhoneNote> list) {
        List<PhoneNoteModel> phoneNoteModels = new ArrayList<>();
        if (list != null && list.size() > 0) {
            for (PhoneNote phoneNote :
                    list) {
                PhoneNoteModel phoneNoteModel = convertPhoneNoteToPhoneNoteModel(phoneNote);
                phoneNoteModels.add(phoneNoteModel);
            }
        }
        return phoneNoteModels;
    }

    public static PhoneNoteModel convertPhoneNoteToPhoneNoteModel(PhoneNote phoneNote) {
        PhoneNoteModel phoneNoteModel = new PhoneNoteModel();
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
        phoneNote.setPhone_time(bmobPhoneNote.getUpdatedAt());
    }
}
