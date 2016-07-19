package android.wuliqing.com.lendphonesystemapp.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.R;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.listeners.DeleteDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobPhoneHttp;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class EditPhonePresenter extends BasePresenter<AddPhoneView> {
//    private DataBaseAction mPhoneTableAction = new PhoneTableAction();
    private File file = null;
    private BaseHttp mBaseHttp = new BmobPhoneHttp();

    public void setHttp(BaseHttp mBaseHttp) {//可以定制网络框架
        this.mBaseHttp = mBaseHttp;
    }

    public void addPhone(final PhoneNote phoneNote) {
        if (phoneNote == null) {
            throw new IllegalArgumentException();
        }
        mBaseHttp.send(null, null, phoneNote, new SendDataListener<BmobPhoneNote>() {
            @Override
            public void onSuccess(BmobPhoneNote bmobPhoneNote) {
//                phoneNote.setBmob_phone_id(bmobPhoneNote.getObjectId());
//                phoneNote.setPhone_time(bmobPhoneNote.getCreatedAt());
//                addPhoneTable(phoneNote);
                if (mView != null) {
                    mView.onResult(true, bmobPhoneNote.getObjectId());
                }
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), s);
                if (mView != null)
                    mView.onResult(false, phoneNote.getBmob_phone_id());
            }
        });
    }

    public void updatePhone(final PhoneNote phoneNote, final String old_url) {
        mBaseHttp.update(null, null, phoneNote, new UpdateDataListener<BmobPhoneNote>() {
            @Override
            public void onResult(BmobPhoneNote bmobPhoneNote) {
                if (bmobPhoneNote != null) {
                    if (!TextUtils.isEmpty(old_url)) {
                        deleteNetWorkPic(old_url);
                    }
//                    updateLocalDataBase(bmobPhoneNote);
                }
                assert bmobPhoneNote != null;
                mView.onResult(true, bmobPhoneNote.getObjectId());
            }
        });
    }

    public void deletePhone(final PhoneNote phoneNoteModel) {
        if (TextUtils.isEmpty(phoneNoteModel.getBmob_phone_id())) {
            throw new IllegalArgumentException();
        }
        mBaseHttp.delete("", "", phoneNoteModel.getBmob_phone_id(), new DeleteDataListener() {
            @Override
            public void onDeleteResult(boolean result) {
                if (result) {
                    if (!TextUtils.isEmpty(phoneNoteModel.getPhone_photo_url())) {
                        deleteNetWorkPic(phoneNoteModel.getPhone_photo_url());
                    }
//                    deleteLocalDataBase(phoneNoteModel.getBmob_phone_id());
                    mView.onDeleteResult(true, phoneNoteModel.getBmob_phone_id());
                }
            }
        });
    }

    private void deleteNetWorkPic(String url) {
        BmobFile file = new BmobFile();
        file.setUrl(url);
        file.delete(LendPhoneApplication.getAppContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                ToastUtils.show(LendPhoneApplication.getAppContext(), R.string.delete_network_pic_success);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "deleteNetWorkPic " + s);
            }
        });
    }

//    private void deleteLocalDataBase(final String phone_id) {
//        Observable.create(new Observable.OnSubscribe<String>() {
//            @Override
//            public void call(Subscriber<? super String> subscriber) {
//                mPhoneTableAction.remove(phone_id);
//                subscriber.onNext(phone_id);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<String>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.show(LendPhoneApplication.getAppContext(), "updateLocalDataBase" + e.toString());
//                        mView.onDeleteResult(false, phone_id);
//                    }
//
//                    @Override
//                    public void onNext(String phoneNote) {
//                        mView.onDeleteResult(true, phone_id);
//                    }
//                });
//    }
//
//    private void updateLocalDataBase(final BmobPhoneNote bmobPhoneNote) {
//        Observable.create(new Observable.OnSubscribe<PhoneNote>() {
//            @Override
//            public void call(Subscriber<? super PhoneNote> subscriber) {
//                PhoneNote phoneNote = (PhoneNote) mPhoneTableAction.queryOneDataWithID(bmobPhoneNote.getObjectId());
//                BmobPhoneNoteHelp.bmobPhoneNoteCopyToPhoneNote(bmobPhoneNote, phoneNote);
//                mPhoneTableAction.update(phoneNote);
//                subscriber.onNext(phoneNote);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<PhoneNote>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.show(LendPhoneApplication.getAppContext(), "updateLocalDataBase " + e.toString());
//                        mView.onResult(false, bmobPhoneNote.getObjectId());
//                    }
//
//                    @Override
//                    public void onNext(PhoneNote phoneNote) {
//                        mView.onResult(true, bmobPhoneNote.getObjectId());
//                    }
//                });
//    }

    public void addPicToNetWork(final Context context, final UpLoadDataListener<String> upLoadDataListener) {
        mBaseHttp.upLoad(file, null, upLoadDataListener);
    }

//    public void addPhoneTable(final PhoneNote phoneNote) {
//        Observable.create(new Observable.OnSubscribe<Boolean>() {
//            @Override
//            public void call(Subscriber<? super Boolean> subscriber) {
//                mPhoneTableAction.add(phoneNote);
//                subscriber.onNext(true);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<Boolean>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        ToastUtils.show(LendPhoneApplication.getAppContext(), "addPhoneTable " + e.toString());
//                    }
//
//                    @Override
//                    public void onNext(Boolean aBoolean) {
//                        if (mView != null) {
//                            mView.onResult(aBoolean, phoneNote.getBmob_phone_id());
//                        }
//                    }
//                });
//    }

//    public void setDataBaseAction(DataBaseAction mDataBaseAction) {
//        this.mPhoneTableAction = mDataBaseAction;
//    }

    public void queryPhoneWithID(final String phone_id) {
        Observable.create(new Observable.OnSubscribe<PhoneNote>() {
            @Override
            public void call(Subscriber<? super PhoneNote> subscriber) {
                PhoneNote mPhoneNote = new PhoneTableAction().queryOneDataWithID(phone_id);
                subscriber.onNext(mPhoneNote);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<PhoneNote>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), e.toString());
                    }

                    @Override
                    public void onNext(PhoneNote phoneNote) {
                        mView.onQueryPhone(phoneNote);
                    }
                });

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
//                queryNameInDataBase();
            }
        });

    }

//    private void queryNameInDataBase() {
//        Observable.create(new Observable.OnSubscribe<List<PhoneNote>>() {
//            @Override
//            public void call(Subscriber<? super List<PhoneNote>> subscriber) {
//                List<PhoneNote> phoneNotes = (List<PhoneNote>)mPhoneTableAction.queryAll();
//                subscriber.onNext(phoneNotes);
//            }
//        }).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<List<PhoneNote>>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(List<PhoneNote> phoneNotes) {
//                        if (mView != null) {
//                            List<String> phoneNames = new ArrayList<>();
//                            List<String> projectNames = new ArrayList<>();
//                            for (PhoneNote phoneNote : phoneNotes) {
//                                if (!TextUtils.isEmpty(phoneNote.getPhone_name())) {
//                                    phoneNames.add(phoneNote.getPhone_name());
//                                }
//                                if (!TextUtils.isEmpty(phoneNote.getProject_name())) {
//                                    projectNames.add(phoneNote.getProject_name());
//                                }
//                            }
//                            mView.onQueryPhoneNameResult(phoneNames);
//                            mView.onQueryProjectNameResult(projectNames);
//                        }
//                    }
//                });
//    }

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
