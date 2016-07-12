package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobLendPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNoteHelp;
import android.wuliqing.com.lendphonesystemapp.mvpview.MainView;
import android.wuliqing.com.lendphonesystemapp.net.BaseHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobLendPhoneHttp;
import android.wuliqing.com.lendphonesystemapp.net.BmobPhoneHttp;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.List;

import cn.bmob.v3.BmobRealTimeData;
import cn.bmob.v3.listener.ValueEventListener;

/**
 * Created by 10172915 on 2016/6/1.
 */
public class MainPresenter extends BasePresenter<MainView> {
    private BaseHttp mBaseHttp = new BmobPhoneHttp();
    private BmobRealTimeData rtd;

    @Override
    public void attach(MainView view) {
        super.attach(view);
        rtd = new BmobRealTimeData();
        rtd.start(LendPhoneApplication.getAppContext(), new ValueEventListener() {
            @Override
            public void onConnectCompleted() {
                if (rtd.isConnected()) {
                    rtd.subTableUpdate(BmobPhoneNote.TABLE_NAME);
                }
            }

            @Override
            public void onDataChange(JSONObject jsonObject) {
                if (BmobRealTimeData.ACTION_UPDATETABLE.equals(jsonObject.optString("action"))) {
                    JSONObject data = jsonObject.optJSONObject("data");
                    JsonElement element = new JsonParser().parse(data.toString());
                    Gson gson = new Gson();
                    BmobPhoneNote bmobPhoneNote = gson.fromJson(element, BmobPhoneNote.class);
                    BmobPhoneNoteHelp.updatePhoneNoteTable(bmobPhoneNote, new UpdateDataListener() {
                        @Override
                        public void onResult(Object result) {
                            mView.onSyncResult(true);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void detach() {
        super.detach();
        if (rtd.isConnected()) {
            rtd.unsubTableUpdate(BmobPhoneNote.TABLE_NAME);
        }
    }

    public void syncLocalDataBaseAndNetWork() {
        mBaseHttp.load(null, null, new LoadDataListener<List<BmobPhoneNote>>() {
            @Override
            public void onComplete(List<BmobPhoneNote> result) {
                BmobPhoneNoteHelp.updatePhoneNoteTable(result, new UpdateDataListener<Boolean>() {//更新本地手机数据库
                    @Override
                    public void onResult(Boolean result) {
                        if (result) {
                            startSyncLendPhoneData();
                        }
                    }
                });
            }

            @Override
            public void onError() {
                mView.onSyncResult(false);
            }
        });
    }

    private void startSyncLendPhoneData() {
        BmobLendPhoneHttp bmobLendPhoneHttp = new BmobLendPhoneHttp();
        bmobLendPhoneHttp.load("", "", new LoadDataListener<List<BmobLendPhoneNote>>() {
            @Override
            public void onComplete(List<BmobLendPhoneNote> result) {
                BmobPhoneNoteHelp.updateLendPhoneNoteTable(result, new UpdateDataListener<Boolean>() {
                    @Override
                    public void onResult(Boolean result) {
                        if (result) {//更新本地数据库成功
                            mView.onSyncResult(true);
                        } else {//更新本地数据库失败
                            mView.onSyncResult(false);
                        }
                    }
                });
            }

            @Override
            public void onError() {
                mView.onSyncResult(false);
            }
        });
    }

    public void clearDataBase() {
        DBHelper.getInstance().getPhoneNoteDao().deleteAll();
        DBHelper.getInstance().getLendPhoneNoteDao().deleteAll();
    }
}
