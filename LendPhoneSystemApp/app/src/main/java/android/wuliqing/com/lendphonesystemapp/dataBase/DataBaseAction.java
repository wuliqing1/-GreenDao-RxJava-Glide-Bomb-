package android.wuliqing.com.lendphonesystemapp.dataBase;

import java.util.List;

import de.greenrobot.dao.Property;

/**
 * Created by 10172915 on 2016/5/26.
 */
public interface DataBaseAction<T> {
    List<T> queryAll();
    List<T> query(String id);

    List<T> queryWithColumn(Property property, Object column);
    void update(T note);
    void remove(String id);

    void remove(long id);
    void add(T note);

    void addCollection(List<T> list);

    List<T> queryWithKey(String column, String key);

    void clearData();

    T queryOneDataWithID(String id);
}
