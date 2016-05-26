package android.wuliqing.com.lendphonesystemapp.DataBase;

import java.util.List;

import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class PhoneTableAction implements DataBaseAction<PhoneNote> {
    private PhoneNoteDao phoneNoteDao;

    public PhoneTableAction() {
        phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
    }

    @Override
    public List<PhoneNote> query() {
        List<PhoneNote> list = phoneNoteDao.queryBuilder().build().list();
        return list;
    }

    @Override
    public void add(PhoneNote phoneNote) {
        if (phoneNote == null){
            throw new IllegalArgumentException();
        }
        phoneNoteDao.insert(phoneNote);
    }

    @Override
    public void remove(long phone_id) {
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        List<PhoneNote> list = phoneNoteDao.queryBuilder()
                .where(PhoneNoteDao.Properties.Id.eq(phone_id)).build().list();
        if (list.size() <= 0) {
            return;
        }
        for (PhoneNote phoneNote : list) {
            phoneNoteDao.delete(phoneNote);
        }
    }

    @Override
    public void update(PhoneNote phoneNote) {
        if (phoneNote == null || phoneNote.getId() <= 0) {
            throw new IllegalArgumentException();
        }
        phoneNoteDao.update(phoneNote);
    }

}
