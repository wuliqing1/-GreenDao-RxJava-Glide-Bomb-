package android.wuliqing.com.lendphonesystemapp.DataBase;

import java.util.List;

import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.LendPhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class LendPhoneTableAction implements DataBaseAction<LendPhoneNote> {
    private LendPhoneNoteDao lendPhoneNoteDao;
    private long phone_id;

    public LendPhoneTableAction(long phone_id) {
        lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        this.phone_id = phone_id;
    }

    @Override
    public List<LendPhoneNote> query() {
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Phone_id.eq(phone_id)).build().list();
        return list;
    }

    @Override
    public void update(LendPhoneNote note) {
        if (note == null || note.getId() <= 0) {
            throw new IllegalArgumentException();
        }
        lendPhoneNoteDao.update(note);
    }

    @Override
    public void remove(long id) {
        if (id <= 0) {
            throw new IllegalArgumentException();
        }
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Id.eq(id)).build().list();
        if (list.size() <= 0) {
            return;
        }
        for (LendPhoneNote lendPhoneNote : list) {
            lendPhoneNoteDao.delete(lendPhoneNote);
        }
    }

    @Override
    public void add(LendPhoneNote note) {
        if (note == null) {
            throw new IllegalArgumentException();
        }
        lendPhoneNoteDao.insert(note);
    }
}
