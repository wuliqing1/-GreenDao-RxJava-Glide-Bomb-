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
        phoneNote.setBmob_phone_id(Long.parseLong(bmobPhoneNote.getObjectId(), 16));
        return phoneNote;
    }

    public static void updatePhoneNoteTable(final List<BmobPhoneNote> bmobPhoneNotes,
                                            final UpdateDataListener updateDataListener) {
        final PhoneTableAction mPhoneTableAction = new PhoneTableAction();

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                List<PhoneNote> phoneNotes = mPhoneTableAction.query();
                List<Long> phoneIds = new ArrayList<Long>();
                for (PhoneNote phoneNote :
                        phoneNotes) {
                    phoneIds.add(phoneNote.getBmob_phone_id());
                }
                for (BmobPhoneNote bmobPhoneNote :
                        bmobPhoneNotes) {
                    Long id = Long.parseLong(bmobPhoneNote.getObjectId(), 16);
                    if (!phoneIds.contains(id)) {//如果数据不存在 则添加本地数据库
                        mPhoneTableAction.add(convertBmobPhoneNoteToPhoneNote(bmobPhoneNote));
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
        phoneNoteModel.setPic_path(phoneNote.getPhone_photo_url());
        phoneNoteModel.setPhone_id(phoneNote.getBmob_phone_id());
        return phoneNoteModel;
    }
}
