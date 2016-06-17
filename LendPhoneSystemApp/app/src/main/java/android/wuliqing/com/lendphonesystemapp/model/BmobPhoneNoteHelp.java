package android.wuliqing.com.lendphonesystemapp.model;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

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
 * Created by 10172915 on 2016/6/16.
 */
public class BmobPhoneNoteHelp {
    public static void queryBmobPhoneNote(final LoadDataListener loadDataListener) {
        BmobQuery<BmobPhoneNote> bmobPhoneNoteBmobQuery = new BmobQuery<>();
        bmobPhoneNoteBmobQuery.findObjects(LendPhoneApplication.getAppContext(), new FindListener<BmobPhoneNote>() {
            @Override
            public void onSuccess(List<BmobPhoneNote> list) {
                loadDataListener.onComplete(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), s);
                loadDataListener.onError();
            }
        });
    }

    public static PhoneNote convertBmobPhoneNoteToPhoneNote(BmobPhoneNote bmobPhoneNote) {
        PhoneNote phoneNote = new PhoneNote();
        phoneNote.setPhone_number(bmobPhoneNote.getPhone_number());
        phoneNote.setPhone_time(bmobPhoneNote.getUpdatedAt());
        phoneNote.setProject_name(bmobPhoneNote.getProject_name());
        phoneNote.setPhone_name(bmobPhoneNote.getPhone_name());
        phoneNote.setPhone_photo_url(bmobPhoneNote.getPic_url());
        return phoneNote;
    }

    public static void updatePhoneNoteTable(final List<BmobPhoneNote> bmobPhoneNotes,
                                            final UpdateDataListener updateDataListener) {
        final PhoneTableAction mPhoneTableAction = new PhoneTableAction();

        Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                mPhoneTableAction.clearData();
                List<PhoneNote> phoneNotes = new ArrayList<PhoneNote>();
                for (BmobPhoneNote bmobPhoneNote :
                        bmobPhoneNotes) {
                    phoneNotes.add(convertBmobPhoneNoteToPhoneNote(bmobPhoneNote));
                }
                mPhoneTableAction.addCollection(phoneNotes);
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
        int left_number = DBHelper.getInstance().LeftPhoneNumber(phoneNote.getId());
        phoneNoteModel.setLeft_number(left_number);
        int lend_number = DBHelper.getInstance().LendPhoneNumber(phoneNote.getId());
        phoneNoteModel.setLend_number(lend_number);
        phoneNoteModel.setLend_names(DBHelper.getInstance().lendPhoneNames(phoneNote.getId()));
        phoneNoteModel.setDate(phoneNote.getPhone_time());
        phoneNoteModel.setPic_path(phoneNote.getPhone_photo_url());
        return phoneNoteModel;
    }
}
