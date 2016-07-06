package android.wuliqing.com.lendphonesystemapp.dataBase;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/26.
 */
public interface DataBaseAction<T> {
    public List<T> query();
    public void update(T note);
    public void remove(String id);

    public void remove(long id);
    public void add(T note);

    public List<T> queryWithKey(String column, String key);

    public void clearData();

    public T queryOneDataWithID(String id);
}
