package android.wuliqing.com.lendphonesystemapp.Utils;

import android.os.AsyncTask;
import android.wuliqing.com.lendphonesystemapp.DataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.listeners.DataListener;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/26.
 */
public class DataSyncFactory {
    private static DataSyncFactory mDataSyncFactory;

    private DataSyncFactory() {

    }

    public static DataSyncFactory getInstance() {
        if (mDataSyncFactory == null) {
            synchronized (DataSyncFactory.class) {
                if (mDataSyncFactory == null) {
                    mDataSyncFactory = new DataSyncFactory();
                }
            }
        }
        return mDataSyncFactory;
    }

    public <T> void loadData(final DataListener<List<T>> listener, final DataBaseAction<T> action) {
        new AsyncTask<Void, Void, List<T>>() {
            protected List<T> doInBackground(Void... params) {
                List<T> list = action.query();//从数据库查询数据

                return list;
            }

            protected void onPostExecute(List<T> result) {
                if (listener != null) {
                    listener.onComplete(result);
                }
            }
        }.execute();
    }
}
