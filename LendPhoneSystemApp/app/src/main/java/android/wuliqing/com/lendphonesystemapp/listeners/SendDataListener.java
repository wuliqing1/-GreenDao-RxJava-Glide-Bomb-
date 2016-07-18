package android.wuliqing.com.lendphonesystemapp.listeners;

/**
 * Created by 10172915 on 2016/6/21.
 */
public interface SendDataListener<T> {
    void onSuccess(T obj);
    void onFailure(int i, String s);
}
