package android.wuliqing.com.lendphonesystemapp;

import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.test.AndroidTestCase;
import android.wuliqing.com.lendphonesystemapp.DataBase.DBHelper;
import android.wuliqing.com.lendphonesystemapp.Utils.FormatTools;

import java.util.Date;
import java.util.List;

import zte.phone.greendao.LendPhoneNote;
import zte.phone.greendao.LendPhoneNoteDao;
import zte.phone.greendao.PhoneNote;
import zte.phone.greendao.PhoneNoteDao;

/**
 * Created by 10172915 on 2016/5/25.
 */
public class PhoneDatabaseTest extends AndroidTestCase {//AbstractDaoSessionTest<DaoMaster, DaoSession>

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        assertNotNull(mContext);
        DBHelper.init(mContext);
    }

    public void testPhoneTableAdd() {
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        assertNotNull(phoneNoteDao);
        phoneNoteDao.deleteAll();
        insertPhoneNote(phoneNoteDao);
        List<PhoneNote> list = phoneNoteDao.queryBuilder().build().list();

        assertEquals(1, phoneNoteDao.queryBuilder().buildCount().count());
        assertEquals(1, list.size());
        assertNotNull(list.get(0));
        assertEquals("P635A50", list.get(0).getPhone_name());
        assertEquals(10, list.get(0).getPhone_number().intValue());
        assertEquals("MT6735P", list.get(0).getProject_name());
//        assertEquals("2016-5-25", list.get(0).getPhone_time());
//        assertEquals("1.png", list.get(0).getPhone_photo());

        phoneNoteDao.insert(new PhoneNote());
        phoneNoteDao.insert(new PhoneNote());

        assertEquals(3, phoneNoteDao.queryBuilder().buildCount().count());
        DBHelper.getInstance().PhoneTableAdd("T60",5,"8939",
                BitmapFactory.decodeResource(mContext.getResources(), R.mipmap.ic_launcher));
        assertEquals(4, phoneNoteDao.queryBuilder().buildCount().count());
        phoneNoteDao.deleteAll();
    }

    public void testPhoneTableRemove() {
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();

        PhoneNote phoneNote = new PhoneNote();
        phoneNoteDao.insert(phoneNote);
        assertEquals(1, phoneNoteDao.count());
        assertEquals(1, phoneNoteDao.queryBuilder().buildCount().count());
        phoneNoteDao.delete(phoneNote);
        assertEquals(0, phoneNoteDao.count());
        assertEquals(0, phoneNoteDao.queryBuilder().buildCount().count());

        insertPhoneNote(phoneNoteDao);
        assertEquals(1, phoneNoteDao.count());
        DBHelper.getInstance().PhoneTableRemove(1);
        assertEquals(0, phoneNoteDao.count());

        phoneNoteDao.deleteAll();
    }

    public void testPhoneTableUpdate() {
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();

        PhoneNote phoneNote = new PhoneNote();
        phoneNote.setPhone_number(10);
        phoneNoteDao.insert(phoneNote);
        Cursor cursor = phoneNoteDao.getDatabase().query(PhoneNoteDao.TABLENAME,
                new String[]{PhoneNoteDao.Properties.Phone_number.columnName}, null, null, null, null, null);
        assertNotNull(cursor);
        assertEquals(1,cursor.getCount());
        cursor.moveToFirst();
        assertEquals(10, cursor.getInt(0));
        cursor.close();
        phoneNote.setId(1L);
        phoneNote.setPhone_number(5);
        phoneNoteDao.update(phoneNote);
        Cursor cursor1 = phoneNoteDao.getDatabase().query(PhoneNoteDao.TABLENAME,
                new String[]{PhoneNoteDao.Properties.Phone_number.columnName}, null, null, null, null, null);
        assertNotNull(cursor1);
        cursor1.moveToFirst();
        assertEquals(5, cursor1.getInt(0));
        cursor1.close();

        List<PhoneNote> list = phoneNoteDao.queryBuilder().list();
        assertNotNull(list);
        assertEquals(1,list.size());
        assertNotNull(list.get(0));
        list.get(0).setPhone_name("V55");
        DBHelper.getInstance().PhoneTableUpdate(list.get(0));
        List<PhoneNote> list1 = phoneNoteDao.queryBuilder().list();
        assertNotNull(list1);
        assertEquals(1,list1.size());
        assertNotNull(list1.get(0));
        assertEquals("V55",list1.get(0).getPhone_name());

        phoneNoteDao.deleteAll();
    }

    public void testPhoneTableQuery() {
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();
        insertPhoneNote(phoneNoteDao);

        insertPhoneNote1(phoneNoteDao);

        List<PhoneNote> list = DBHelper.getInstance().PhoneTableQuery();
        assertEquals(2, list.size());
        assertNotNull(list.get(0));
        assertEquals(1, list.get(0).getId().intValue());
        assertEquals("P635A50", list.get(0).getPhone_name());
        assertEquals(10, list.get(0).getPhone_number().intValue());
        assertEquals("MT6735P", list.get(0).getProject_name());
//        assertEquals("2016-5-25", list.get(0).getPhone_time());
//        assertEquals("1.png", list.get(0).getPhone_photo());
        assertNotNull(list.get(1));
        assertEquals(2, list.get(1).getId().intValue());
        assertEquals("P635A10", list.get(1).getPhone_name());
        assertEquals(8, list.get(1).getPhone_number().intValue());
        assertEquals("MT6735M", list.get(1).getProject_name());
//        assertEquals("2016-5-26", list.get(1).getPhone_time());
//        assertEquals("2.png", list.get(1).getPhone_photo());
        phoneNoteDao.deleteAll();
    }

    public void testLendPhoneTableAdd() {
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();

        insertPhoneNote(phoneNoteDao);//添加10台
        assertEquals(1,phoneNoteDao.count());

        LendPhoneNoteDao lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        lendPhoneNoteDao.deleteAll();

        insertLendPhoneNote(lendPhoneNoteDao, 1);//借2台
        assertEquals(1,lendPhoneNoteDao.count());

        DBHelper.getInstance().LendPhoneTableAdd("wuliqing", 2, 1);//借2台
        assertEquals(2, lendPhoneNoteDao.count());

        lendPhoneNoteDao.deleteAll();
        phoneNoteDao.deleteAll();
    }

    public void testLendPhoneTableRemove() {
        LendPhoneNoteDao lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        lendPhoneNoteDao.deleteAll();

        insertLendPhoneNote(lendPhoneNoteDao, 1);
        insertLendPhoneNote(lendPhoneNoteDao, 2);
        assertEquals(2,lendPhoneNoteDao.count());

        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder().list();
        assertEquals(2,list.size());
        assertNotNull(list.get(0));
        lendPhoneNoteDao.delete(list.get(0));
        assertEquals(1,lendPhoneNoteDao.count());

        assertNotNull(list.get(1));
        DBHelper.getInstance().LendPhoneTableRemove(list.get(1).getId());
        assertEquals(0, lendPhoneNoteDao.count());

        lendPhoneNoteDao.deleteAll();
    }

    public void testLendPhoneTableUpdate() {
        LendPhoneNoteDao lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        lendPhoneNoteDao.deleteAll();

        insertLendPhoneNote(lendPhoneNoteDao, 1);
        assertEquals(1,lendPhoneNoteDao.count());
        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder().build().list();
        assertEquals(1, list.size());
        assertNotNull(list.get(0));
        list.get(0).setLend_phone_number(2);
        lendPhoneNoteDao.update(list.get(0));
        Cursor cursor = lendPhoneNoteDao.getDatabase().query(LendPhoneNoteDao.TABLENAME,
                new String[]{LendPhoneNoteDao.Properties.Lend_phone_number.columnName},null,null,null,null,null);
        assertNotNull(cursor);
        assertEquals(1,cursor.getCount());
        cursor.moveToFirst();
        assertEquals(2,cursor.getInt(0));
        cursor.close();

        list.get(0).setLend_phone_name("xiaoli");
        DBHelper.getInstance().LendPhoneTableUpdate(list.get(0));
        List<LendPhoneNote> list1 = lendPhoneNoteDao.queryBuilder().list();
        assertNotNull(list1);
        assertEquals(1, list1.size());
        assertNotNull(list1.get(0));
        assertEquals("xiaoli", list1.get(0).getLend_phone_name());

        lendPhoneNoteDao.deleteAll();
    }

    public void testLendPhoneTableQuery() {
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();
        LendPhoneNoteDao lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        lendPhoneNoteDao.deleteAll();

        insertPhoneNote(phoneNoteDao);
        assertEquals(1,phoneNoteDao.count());

        insertLendPhoneNote(lendPhoneNoteDao, 1);
        assertEquals(1,lendPhoneNoteDao.count());

        List<LendPhoneNote> list = DBHelper.getInstance().LendPhoneTableQuery(1);
        assertEquals(1,list.size());
        assertNotNull(list.get(0));
        assertNotNull(list.get(0).getPhoneNote());
        assertEquals("P635A50", list.get(0).getPhoneNote().getPhone_name());
        assertEquals(10, list.get(0).getPhoneNote().getPhone_number().intValue());
        assertEquals("MT6735P", list.get(0).getPhoneNote().getProject_name());
//        assertEquals("2016-5-25", list.get(0).getPhoneNote().getPhone_time());
//        assertEquals("1.png", list.get(0).getPhoneNote().getPhone_photo());

        lendPhoneNoteDao.deleteAll();
        phoneNoteDao.deleteAll();
    }

    public void testLendPhoneNumber() {//测试借手机的数量
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();
        LendPhoneNoteDao lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        lendPhoneNoteDao.deleteAll();

        insertPhoneNote(phoneNoteDao);//存入十台手机
        insertLendPhoneNote(lendPhoneNoteDao, 1);//借了一台手机
        insertLendPhoneNote(lendPhoneNoteDao, 1);//借了一台手机
        long lend_number = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Phone_id.eq(1)).buildCount().count();
        assertEquals(2,lend_number);

        assertEquals(4, DBHelper.getInstance().LendPhoneNumber(1));

        phoneNoteDao.deleteAll();
        lendPhoneNoteDao.deleteAll();
    }

    public void testLeftPhoneNumber() {//测试剩余手机数量
        PhoneNoteDao phoneNoteDao = DBHelper.getInstance().getPhoneNoteDao();
        phoneNoteDao.deleteAll();
        LendPhoneNoteDao lendPhoneNoteDao = DBHelper.getInstance().getLendPhoneNoteDao();
        lendPhoneNoteDao.deleteAll();

        insertPhoneNote(phoneNoteDao);//存入十台手机
        insertPhoneNote1(phoneNoteDao);//存入八台手机

        insertLendPhoneNote(lendPhoneNoteDao, 2);//借了2台手机
        insertLendPhoneNote(lendPhoneNoteDao, 2);//借了2台手机
        insertLendPhoneNote(lendPhoneNoteDao, 2);//借了2台手机

        List<LendPhoneNote> list = lendPhoneNoteDao.queryBuilder()
                .where(LendPhoneNoteDao.Properties.Phone_id.eq(2)).build().list();
        List<PhoneNote> list1 = phoneNoteDao.queryBuilder().list();

        assertNotNull(list);
        assertEquals(3, list.size());
        assertNotNull(list1);
        assertEquals(2, list1.size());
        assertNotNull(list.get(0));
        assertNotNull(list.get(1));
        assertNotNull(list.get(2));
        assertNotNull(list1.get(0));
        assertNotNull(list1.get(1));
        long num = list1.get(1).getPhone_number() -
                (list.get(0).getLend_phone_number() + list.get(1).getLend_phone_number()
                        + list.get(2).getLend_phone_number());
        assertEquals(2, num);
        insertLendPhoneNote(lendPhoneNoteDao, 2);//借了2台手机
        insertLendPhoneNote(lendPhoneNoteDao, 2);//借了2台手机
        assertEquals(0, DBHelper.getInstance().LeftPhoneNumber(2));

        phoneNoteDao.deleteAll();
        lendPhoneNoteDao.deleteAll();
    }

    private void insertPhoneNote(PhoneNoteDao phoneNoteDao) {
        PhoneNote phoneNote = new PhoneNote();
        phoneNote.setPhone_name("P635A50");
        phoneNote.setPhone_number(10);
        phoneNote.setProject_name("MT6735P");
        phoneNote.setPhone_time(new Date());
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
        phoneNote.setPhone_photo(FormatTools.getInstance().Drawable2Bytes(drawable));
        phoneNoteDao.insert(phoneNote);
    }

    private void insertPhoneNote1(PhoneNoteDao phoneNoteDao) {
        PhoneNote phoneNote1 = new PhoneNote();
        phoneNote1.setPhone_name("P635A10");
        phoneNote1.setPhone_number(8);
        phoneNote1.setProject_name("MT6735M");
        phoneNote1.setPhone_time(new Date());
        Drawable drawable = mContext.getResources().getDrawable(R.mipmap.ic_launcher);
        phoneNote1.setPhone_photo(FormatTools.getInstance().Drawable2Bytes(drawable));
        phoneNoteDao.insert(phoneNote1);
    }

    private void insertLendPhoneNote(LendPhoneNoteDao lendPhoneNoteDao, int phone_id) {
        LendPhoneNote lendPhoneNote = new LendPhoneNote();
        lendPhoneNote.setLend_phone_name("wuliqing");
        lendPhoneNote.setLend_phone_time(new Date());
        lendPhoneNote.setLend_phone_number(2);
        lendPhoneNote.setPhone_id(phone_id);
        lendPhoneNoteDao.insert(lendPhoneNote);
    }
}
