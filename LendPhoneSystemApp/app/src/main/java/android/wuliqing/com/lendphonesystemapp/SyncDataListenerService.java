package android.wuliqing.com.lendphonesystemapp;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNoteHelp;
import android.wuliqing.com.lendphonesystemapp.model.MyUser;
import android.wuliqing.com.lendphonesystemapp.utils.LogHelper;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.ValueEventListener;

public class SyncDataListenerService extends Service {
    private static final String TAG = "SyncDataListenerService";
    private BmobRealTimeData rtd;

    public SyncDataListenerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (rtd.isConnected()) {
            rtd.unsubTableUpdate(BmobPhoneNote.TABLE_NAME);
            rtd.unsubTableUpdate(BmobLendPhoneNote.TABLE_NAME);
            rtd.unsubTableUpdate(MyUser.TABLE_NAME);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        rtd = new BmobRealTimeData();
        rtd.start(LendPhoneApplication.getAppContext(), new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                if (rtd.isConnected()) {
                    rtd.subTableUpdate(BmobPhoneNote.TABLE_NAME);
                    rtd.subTableUpdate(BmobLendPhoneNote.TABLE_NAME);
                    rtd.subTableUpdate(MyUser.TABLE_NAME);
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if (BmobPhoneNote.TABLE_NAME.equals(jsonObject.optString("tableName"))) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    JsonElement element = new JsonParser().parse(data.toString());
                    Gson gson = new Gson();
                    BmobPhoneNote bmobPhoneNote = gson.fromJson(element, BmobPhoneNote.class);
                    LogHelper.logD(TAG, "onDataChange BmobPhoneNote");
                    BmobPhoneNoteHelp.updatePhoneNoteTable(bmobPhoneNote, new UpdateDataListener<Boolean>() {
                        @Override
                        public void onResult(Boolean result) {
                            if (result) {
                                //发送广播
                                LogHelper.logD(TAG, "onDataChange send broadcast 1");
                                Intent intent = new Intent(LendPhoneMainActivity.PHONE_NOTE_CHANGE_ACTION);
                                LocalBroadcastManager.getInstance(SyncDataListenerService.this).sendBroadcast(intent);
                            }
                        }
                    });
                } else if (BmobLendPhoneNote.TABLE_NAME.equals(jsonObject.optString("tableName"))) {
                    LogHelper.logD(TAG, "onDataChange BmobLendPhoneNote");
                    JSONObject data = jsonObject.optJSONObject("data");
                    JsonElement element = new JsonParser().parse(data.toString());
                    Gson gson = new Gson();
                    BmobLendPhoneNote bmobLendPhoneNote = gson.fromJson(element, BmobLendPhoneNote.class);
                    BmobPhoneNoteHelp.updateLendPhoneNoteTable(bmobLendPhoneNote, new UpdateDataListener() {
                        @Override
                        public void onResult(Object result) {
                            if (result != null) {
                                //发送广播
                                LogHelper.logD(TAG, "onDataChange send broadcast 2");
                                Intent intent = new Intent(PhoneDetailActivity.LEND_PHONE_NOTE_CHANGE_ACTION);
                                LocalBroadcastManager.getInstance(SyncDataListenerService.this).sendBroadcast(intent);
                            }
                        }
                    });
                } else if (MyUser.TABLE_NAME.equals(jsonObject.optString("tableName"))) {
                    LogHelper.logD(TAG, "onDataChange MyUser");
                    JSONObject data = jsonObject.optJSONObject("data");
                    JsonElement element = new JsonParser().parse(data.toString());
                    Gson gson = new Gson();
                    MyUser myUser = gson.fromJson(element, MyUser.class);
                    if (!TextUtils.isEmpty(myUser.getPhoto_url())) {
                        notifyCurUserData(myUser);
                        BmobPhoneNoteHelp.updateLendPhoneNoteTableWithPhoto(myUser, new UpdateDataListener() {
                            @Override
                            public void onResult(Object result) {
                                if (result != null) {
                                    //发送广播
                                    LogHelper.logD(TAG, "onDataChange send broadcast 3");
                                    Intent intent = new Intent(PhoneDetailActivity.LEND_PHONE_NOTE_CHANGE_ACTION);
                                    LocalBroadcastManager.getInstance(SyncDataListenerService.this).sendBroadcast(intent);
                                }
                            }
                        });
                    }
                }
            }
        });
        return super.onStartCommand(intent, flags, startId);
    }

    private void notifyCurUserData(MyUser myUser) {
        MyUser myCurUser = BmobUser.getCurrentUser(LendPhoneApplication.getAppContext(), MyUser.class);
        if (myCurUser != null
                && myCurUser.getUsername().equals(myUser.getUsername())
                && myCurUser.isAdmin() != myUser.isAdmin()) {
//            myCurUser.setPhoto_url(myUser.getPhoto_url());
//            myCurUser.setDepartment(myUser.getDepartment());
//            myCurUser.setPosition(myUser.getPosition());
            myCurUser.setAdmin(myUser.isAdmin());
            myCurUser.update(LendPhoneApplication.getAppContext(), myCurUser.getObjectId(), new UpdateListener() {

                @Override
                public void onSuccess() {
                    //发送广播
                    Intent intent = new Intent(LendPhoneMainActivity.CUR_USER_CHANGE_ACTION);
                    LocalBroadcastManager.getInstance(SyncDataListenerService.this).sendBroadcast(intent);
                }

                @Override
                public void onFailure(int code, String msg) {
                    ToastUtils.show(LendPhoneApplication.getAppContext(), "更新用户信息失败:" + msg);
                }
            });
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
