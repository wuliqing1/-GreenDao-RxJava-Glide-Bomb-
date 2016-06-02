package android.wuliqing.com.lendphonesystemapp.presenter;

import android.os.AsyncTask;
import android.wuliqing.com.lendphonesystemapp.dataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.model.PhoneNoteModel;
import android.wuliqing.com.lendphonesystemapp.mvpview.PhoneListView;
import android.wuliqing.com.lendphonesystemapp.utils.FormatTools;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneListPresenter extends BasePresenter<PhoneListView> {
    private static final String TAG = "PhoneListPresenter";
    private DataBaseAction mDataBaseAction = new PhoneTableAction();

    public void loadData() {
        new AsyncTask<Void, Void, List<PhoneNoteModel>>() {
            @Override
            protected List<PhoneNoteModel> doInBackground(Void... params) {
                List<PhoneNote> list = mDataBaseAction.query();//从数据库获取
                if (mView != null) {
                    List<PhoneNoteModel> phoneNoteModels = getConvertPhoneData(list);
                    return phoneNoteModels;
                }
                return null;
            }

            @Override
            protected void onPostExecute(List<PhoneNoteModel> phoneNoteModels) {
                mView.onFetchedPhones(phoneNoteModels);
            }
        }.execute();

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
