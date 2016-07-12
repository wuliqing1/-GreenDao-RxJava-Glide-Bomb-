package android.wuliqing.com.lendphonesystemapp.dataBase;

import android.text.TextUtils;

import java.util.List;

import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.LendPhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class LendPhoneTableAction implements DataBaseAction<LendPhoneNote> {
    private LendPhoneNoteDao lendPhoneNoteDao;

    public LendPhoneTableAction() {
        lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
    }

    @Override
    public List<LendPhoneNote> queryAll() {
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .orderDesc(LendPhoneNoteDao.Properties.Lend_phone_time).build().list();
        return list;
    }

    @Override
    public List<LendPhoneNote> query(String id) {
        if (TextUtils.isEmpty(id)) {
            throw new IllegalArgumentException();
        }
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Attach_bmob_phone_id.eq(id))
                .orderDesc(LendPhoneNoteDao.Properties.Lend_phone_time).build().list();
        return list;
    }

    @Override
    public void update(LendPhoneNote note) {
        if (note == null || note.getBmob_lend_phone_id() == null) {
            throw new IllegalArgumentException();
        }
        lendPhoneNoteDao.update(note);
    }

    @Override
    public void remove(String id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Bmob_lend_phone_id.eq(id)).build().list();
        if (list.size() <= 0) {
            return;
        }
        for (LendPhoneNote lendPhoneNote : list) {
            lendPhoneNoteDao.delete(lendPhoneNote);
        }
    }

    @Override
    public void remove(long id) {
        if (id == 0) {
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

    @Override
    public void addCollection(List<LendPhoneNote> list) {
        if (list == null) {
            throw new IllegalArgumentException();
        }
        lendPhoneNoteDao.insertInTx(list);
    }

    @Override
    public List<LendPhoneNote> queryWithKey(String column, String key) {
        String[] columns = lendPhoneNoteDao.getAllColumns();
        boolean isColumnExist = false;
        for (String column_temp :
                columns) {
            if (column_temp.equalsIgnoreCase(column)) {
                isColumnExist = true;
            }
        }
        if (!isColumnExist || key == null) {
            throw new IllegalArgumentException();
        }
        //需要优化，目前只支持lend_phone_name字段查询
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Lend_phone_name.like(key))
                .orderAsc(LendPhoneNoteDao.Properties.Lend_phone_time).build().list();
        return list;
    }

    @Override
    public void clearData() {
        lendPhoneNoteDao.deleteAll();
    }

    @Override
    public LendPhoneNote queryOneDataWithID(String id) {
        if (id == null) {
            throw new IllegalArgumentException();
        }
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Bmob_lend_phone_id.eq(id)).build().list();
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

}
