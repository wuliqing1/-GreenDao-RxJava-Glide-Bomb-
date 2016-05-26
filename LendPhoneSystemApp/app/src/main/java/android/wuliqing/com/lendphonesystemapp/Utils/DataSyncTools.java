package android.wuliqing.com.lendphonesystemapp.Utils;

import android.os.AsyncTask;
import android.wuliqing.com.lendphonesystemapp.DataBase.DataBaseAction;
import android.wuliqing.com.lendphonesystemapp.listeners.DataListener;

import java.util.List;

/**
 * Created by 10172915 on 2016/5/26.
 */
public final  class DataSyncTools {
    public static <T> void loadData(final DataListener<List<T>> listener, final DataBaseAction<T> action) {
        new AsyncTask<Void, Void, List<T>>() {
            protected List<T> doInBackground(Void... params) {

                return action.query();
            }

            protected void onPostExecute(List<T> result) {
                if (listener != null) {
                    listener.onComplete(result);
                }
            }
        }.execute();
    }
}
