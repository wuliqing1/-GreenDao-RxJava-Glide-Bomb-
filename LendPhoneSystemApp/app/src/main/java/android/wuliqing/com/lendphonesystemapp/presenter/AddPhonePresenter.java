package android.wuliqing.com.lendphonesystemapp.presenter;

import android.wuliqing.com.lendphonesystemapp.dataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.dataBase.PhoneTableAction;
import android.wuliqing.com.lendphonesystemapp.mvpview.AddPhoneView;

import zte.phone.greendao.PhoneNote;

/**
 * Created by 10172915 on 2016/5/27.
 */
public class AddPhonePresenter extends BasePresenter<AddPhoneView> {
    private DataBaseAction mPhoneTableAction = new PhoneTableAction();

    public void addPhone(final PhoneNote phoneNote) {
        addPhoneTable(phoneNote);
    }

    public boolean addPhoneTable(final PhoneNote phoneNote) {
        mPhoneTableAction.add(phoneNote);
        return true;
    }

    public void setDataBaseAction(DataBaseAction mDataBaseAction) {
        this.mPhoneTableAction = mDataBaseAction;
    }
}
