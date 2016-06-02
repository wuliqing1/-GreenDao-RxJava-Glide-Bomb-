package android.wuliqing.com.lendphonesystemapp.dataBase;

import android.content.Context;
import android.graphics.Bitmap;
import android.wuliqing.com.lendphonesystemapp.utils.FormatTools;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import zte.phone.greendao.DaoMaster;
import zte.phone.greendao.DaoSession;
import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.LendPhoneNoteDao;
import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/25.
 */
public class DBHelper {
    private static DBHelper instance;
    private static Context mContext;

    private static DaoMaster daoMaster;
    private static DaoSession daoSession;
    private PhoneNoteDao phoneNoteDao;
    private LendPhoneNoteDao lendPhoneNoteDao;

    /**
     * 取得DaoMaster
     *
     * @param context
     * @return
     */
    public static DaoMaster getDaoMaster(Context context) {
        if (daoMaster == null) {
            DaoMaster.OpenHelper helper = new DaoMaster.DevOpenHelper(context,
                    "lend_phone.db", null);
            daoMaster = new DaoMaster(helper.getWritableDatabase());
        }
        return daoMaster;
    }

    /**
     * 取得DaoSession
     *
     * @param context
     * @return
     */
    public static DaoSession getDaoSession(Context context) {
        if (daoSession == null) {
            if (daoMaster == null) {
                daoMaster = getDaoMaster(context);
            }
            daoSession = daoMaster.newSession();
        }
        return daoSession;
    }

    private DBHelper() {
    }

    public static void init(Context context) {
        mContext = context;
        instance = new DBHelper();
        // 数据库对象
        DaoSession daoSession = getDaoSession(mContext);
        instance.setPhoneNoteDao(daoSession.getPhoneNoteDao());
        instance.setLendPhoneNoteDao(daoSession.getLendPhoneNoteDao());
    }

    private void setPhoneNoteDao(PhoneNoteDao phoneNoteDao) {
        this.phoneNoteDao = phoneNoteDao;
    }

    private void setLendPhoneNoteDao(LendPhoneNoteDao lendPhoneNoteDao) {
        this.lendPhoneNoteDao = lendPhoneNoteDao;
    }

    public PhoneNoteDao getPhoneNoteDao() {
        return phoneNoteDao;
    }

    public static DBHelper getInstance() {
        if (instance == null) {
            synchronized (DBHelper.class) {
                if (instance == null) {
                    instance = new DBHelper();
                }
            }
        }
        return instance;
    }

    public LendPhoneNoteDao getLendPhoneNoteDao() {
        return lendPhoneNoteDao;
    }

    public void PhoneTableAdd(String phone_name, int phone_number, String project_name, Bitmap bitmap) {
        PhoneNote phoneNote = new PhoneNote();
        phoneNote.setPhone_name(phone_name);
        phoneNote.setPhone_number(phone_number);
        phoneNote.setProject_name(project_name);
        phoneNote.setPhone_time(new Date());
        phoneNote.setPhone_photo(FormatTools.getInstance().Bitmap2Bytes(bitmap));
        new PhoneTableAction().add(phoneNote);
    }

    public void PhoneTableRemove(long phone_id) {
        new PhoneTableAction().remove(phone_id);
    }

    public void PhoneTableUpdate(PhoneNote phoneNote) {
        new PhoneTableAction().update(phoneNote);
    }

    public List<PhoneNote> PhoneTableQuery() {
        return new PhoneTableAction().query();
    }

    public void LendPhoneTableAdd(String lend_phone_name, int lend_phone_number, long phone_id) {
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        LendPhoneNote lendPhoneNote = new LendPhoneNote();
        lendPhoneNote.setLend_phone_name(lend_phone_name);
        lendPhoneNote.setLend_phone_number(lend_phone_number);
        lendPhoneNote.setLend_phone_time(new Date());
        lendPhoneNote.setPhone_id(phone_id);
        new LendPhoneTableAction(phone_id).add(lendPhoneNote);
    }

    public void LendPhoneTableRemove(long id) {
        new LendPhoneTableAction(0).remove(id);
    }

    public void LendPhoneTableUpdate(LendPhoneNote lendPhoneNote) {
        new LendPhoneTableAction(0).update(lendPhoneNote);
    }

    public List<LendPhoneNote> LendPhoneTableQuery(long phone_id) {
        return new LendPhoneTableAction(phone_id).query();
    }

    public String lendPhoneNames(long phone_id) {
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        List<String> names = new ArrayList<>();
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Phone_id.eq(phone_id)).build().list();
        for (LendPhoneNote lendPhoneNote : list) {
            if (!names.contains(lendPhoneNote.getLend_phone_name())) {
                names.add(lendPhoneNote.getLend_phone_name());
            }
        }
        list = null;
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < names.size(); i++) {
            String name = names.get(i);
            if (i != names.size() - 1) {
                stringBuilder.append(name).append(',');
            } else {
                stringBuilder.append(name);
            }
        }
        names = null;
        return stringBuilder.toString();
    }

    public int LendPhoneNumber(long phone_id) {//借手机数量
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        int number = 0;
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Phone_id.eq(phone_id)).build().list();
        for (LendPhoneNote lendPhoneNote : list) {
            number += lendPhoneNote.getLend_phone_number();
        }
        list = null;
        return number;
    }

    public int LeftPhoneNumber(long phone_id) {//剩余手机数量
        if (phone_id <= 0) {
            throw new IllegalArgumentException();
        }
        int lend_number = 0;
        List<PhoneNote> phoneNotes = phoneNoteDao.queryBuilder()
                .where(PhoneNoteDao.Properties.Id.eq(phone_id)).build().list();
        int total_number = 0;
        for (PhoneNote phoneNote : phoneNotes) {
            total_number += phoneNote.getPhone_number();
        }
        List<LendPhoneNote> lendPhoneNotes = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Phone_id.eq(phone_id)).build().list();
        for (LendPhoneNote lendPhoneNote : lendPhoneNotes) {
            lend_number += lendPhoneNote.getLend_phone_number();
        }
//        if (lend_number > total_number){
//            throw new IndexOutOfBoundsException();
//        }
        phoneNotes = null;
        lendPhoneNotes = null;
        int left_num = total_number - lend_number;
        return left_num < 0 ? 0 : left_num;
    }
}
