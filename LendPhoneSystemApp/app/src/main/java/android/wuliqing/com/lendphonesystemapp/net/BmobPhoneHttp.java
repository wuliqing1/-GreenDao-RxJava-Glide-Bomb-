package android.wuliqing.com.lendphonesystemapp.net;

import android.wuliqing.com.lendphonesystemapp.LendPhoneApplication;
import android.wuliqing.com.lendphonesystemapp.listeners.DeleteDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.LoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.SendDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpLoadDataListener;
import android.wuliqing.com.lendphonesystemapp.listeners.UpdateDataListener;
import android.wuliqing.com.lendphonesystemapp.model.BmobPhoneNote;
import android.wuliqing.com.lendphonesystemapp.utils.ToastUtils;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.GetListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/6/21.
 */
public class BmobPhoneHttp extends BaseHttp {
    @Override
    public void send(String method, String url, final Object phoneNote, final SendDataListener sendDataListener) {
        final BmobPhoneNote bmobPhoneNote = BmobPhoneNote.transformPhoneNote((PhoneNote) phoneNote);
        bmobPhoneNote.save(LendPhoneApplication.getAppContext(), new SaveListener() {
            @Override
            public void onSuccess() {
                sendDataListener.onSuccess(bmobPhoneNote);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "send:" + s);
                sendDataListener.onFailure(i, s);
            }
        });
    }

    @Override
    public void load(String method, String url, final LoadDataListener loadDataListener) {
        BmobQuery<BmobPhoneNote> bmobPhoneNoteBmobQuery = new BmobQuery<>();
        bmobPhoneNoteBmobQuery.findObjects(LendPhoneApplication.getAppContext(), new FindListener<BmobPhoneNote>() {
            @Override
            public void onSuccess(List<BmobPhoneNote> list) {
                loadDataListener.onComplete(list);
            }

            @Override
            public void onError(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "load:" + s);
                loadDataListener.onError();
            }
        });
    }

    @Override
    public void upLoad(File file, String url, final UpLoadDataListener upLoadDataListener) {
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadblock(LendPhoneApplication.getAppContext(), new UploadFileListener() {

            @Override
            public void onSuccess() {
                //bmobFile.getFileUrl(context)--返回的上传文件的完整地址
                upLoadDataListener.onComplete(bmobFile.getFileUrl(LendPhoneApplication.getAppContext()));
            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
                upLoadDataListener.onProgress(value);
            }

            @Override
            public void onFailure(int code, String msg) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "upLoad:" + msg);
                upLoadDataListener.onError();
            }
        });
    }

    @Override
    public void queryOnePhoneWithId(String method, String url, String phone_id, final LoadDataListener loadDataListener) {
        BmobQuery<BmobPhoneNote> query = new BmobQuery<BmobPhoneNote>();
        query.getObject(LendPhoneApplication.getAppContext(), phone_id, new GetListener<BmobPhoneNote>() {
            @Override
            public void onSuccess(BmobPhoneNote bmobPhoneNote) {
                loadDataListener.onComplete(bmobPhoneNote);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastUtils.show(LendPhoneApplication.getAppContext(), "queryOnePhoneWithId:" + s);
                loadDataListener.onError();
            }
        });
    }

//    BmobQuery<GameScore> query = new BmobQuery<GameScore>();
////查询playerName叫“比目”的数据
//    query.addWhereEqualTo("playerName", "比目");
////返回50条数据，如果不加上这条语句，默认返回10条数据
//    query.setLimit(50);
////执行查询方法
//    query.findObjects(new FindListener<GameScore>() {
//        @Override
//        public void done(List<GameScore> object, BmobException e) {
//            if(e==null){
//                toast("查询成功：共"+object.size()+"条数据。");
//                for (GameScore gameScore : object) {
//                    //获得playerName的信息
//                    gameScore.getPlayerName();
//                    //获得数据的objectId信息
//                    gameScore.getObjectId();
//                    //获得createdAt数据创建时间（注意是：createdAt，不是createAt）
//                    gameScore.getCreatedAt();
//                }
//            }else{
//                Log.i("bmob","失败："+e.getMessage()+","+e.getErrorCode());
//            }
//        }
//    });

    @Override
    public void update(String method, String url, Object obj, final UpdateDataListener updateDataListener) {
        PhoneNote phoneNote = (PhoneNote)obj;
        final BmobPhoneNote bmobPhoneNote = BmobPhoneNote.transformPhoneNote(phoneNote);
        bmobPhoneNote.update(LendPhoneApplication.getAppContext(),
                phoneNote.getBmob_phone_id(), new UpdateListener() {
                    @Override
                    public void onSuccess() {
                        updateDataListener.onResult(bmobPhoneNote);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        ToastUtils.show(LendPhoneApplication.getAppContext(), "update:" + s);
                    }
                });
    }

    @Override
    public void delete(String method, String url, String objId, final DeleteDataListener updateDataListener) {
        BmobPhoneNote bmobPhoneNote = new BmobPhoneNote();
        bmobPhoneNote.setObjectId(objId);
        bmobPhoneNote.delete(LendPhoneApplication.getAppContext(), new DeleteListener() {
            @Override
            public void onSuccess() {
                updateDataListener.onDeleteResult(true);
            }

            @Override
            public void onFailure(int i, String s) {
                updateDataListener.onDeleteResult(false);
                ToastUtils.show(LendPhoneApplication.getAppContext(), "delete:" + s);
            }
        });
    }

}
