package android.wuliqing.com.lendphonesystemapp.presenter;

import android.text.TextUtils;
import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;

import java.util.ArrayList;
import java.util.List;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class AddPhonePresenter extends BasePresenter<AddPhoneView> {
    private DataBaseAction mPhoneTableAction = new PhoneTableAction();

    public void addPhone(final PhoneNote phoneNote) {
        if (phoneNote == null) {
            throw new IllegalArgumentException();
        }
        if (addPhoneTable(phoneNote)) {
            mView.onResult(true);
        }
    }

    public boolean addPhoneTable(final PhoneNote phoneNote) {
        mPhoneTableAction.add(phoneNote);
        return true;
    }

    public void setDataBaseAction(DataBaseAction mDataBaseAction) {
        this.mPhoneTableAction = mDataBaseAction;
    }

    public void queryPhoneNameAndProjectName() {
        List<String> phoneNames = new ArrayList<>();
        List<String> projectNames = new ArrayList<>();
        List<PhoneNote> phoneNotes = mPhoneTableAction.query();
        for (PhoneNote phoneNote : phoneNotes) {
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
