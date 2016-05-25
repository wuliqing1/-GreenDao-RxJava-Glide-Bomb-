package android.wuliqing.com.lendphonesystemapp.Utils;

import android.content.Context;

import de.greenrobot.dao.query.QueryBuilder;
import zte.phone.greendao.DaoMaster;
import zte.phone.greendao.DaoSession;
import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.LendPhoneNoteDao;
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
        return instance;
    }

    public LendPhoneNoteDao getLendPhoneNoteDao() {
        return lendPhoneNoteDao;
    }

    public int getAllLendPhoneNumByPhoneID(Long id) {
        QueryBuilder<LendPhoneNote> qb = lendPhoneNoteDao.queryBuilder();
        qb.where(LendPhoneNoteDao.Properties.Phone_id
                .eq(id));
        return (int) qb.buildCount().count();
    }

}
