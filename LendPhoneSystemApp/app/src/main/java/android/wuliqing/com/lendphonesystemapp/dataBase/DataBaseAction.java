package android.wuliqing.com.lendphonesystemapp.dataBase;

import java.util.List;

import de.greenrobot.dao.Property;

/**
 * Created by 10172915 on 2016/5/26.
 */
public interface DataBaseAction<T> {
    public List<T> queryAll();
    public List<T> query(String id);

    public List<T> queryWithColumn(Property property, Object column);
    public void update(T note);
    public void remove(String id);

    public void remove(long id);
    public void add(T note);

    public void addCollection(List<T> list);

    public List<T> queryWithKey(String column, String key);

    public void clearData();

    public T queryOneDataWithID(String id);
}
