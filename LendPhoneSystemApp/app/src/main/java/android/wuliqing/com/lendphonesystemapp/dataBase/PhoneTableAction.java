package android.wuliqing.com.lendphonesystemapp.dataBase;

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
    public List<PhoneNote> queryWithKey(String column, String key) {
        String[] columns = phoneNoteDao.getAllColumns();
        boolean isColumnExist = false;
        for (String column_temp :
                columns) {
            if (column_temp.equalsIgnoreCase(column)) {
                isColumnExist = true;
            }
        }
        if(!isColumnExist || key == null){
            throw new IllegalArgumentException();
        }
        //需要优化，目前只支持phone_name字段查询
        List<PhoneNote> list = phoneNoteDao.queryBuilder().where(PhoneNoteDao.Properties.Phone_name.like(key))
                .orderAsc(PhoneNoteDao.Properties.Phone_time).build().list();
        return list;
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
