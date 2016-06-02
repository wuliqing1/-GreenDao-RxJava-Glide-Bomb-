package android.wuliqing.com.lendphonesystemapp.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

/**
 * Created by 10172915 on 2016/5/31.
 */
public class ToastUtils {
    public static void show(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void show(Context context, int resId) {
        Toast.makeText(context, context.getResources().getText(resId), Toast.LENGTH_LONG).show();
    }

    public static void showSnackBar(View view, int resId) {
        Snackbar.make(view, resId, Snackbar.LENGTH_LONG).setAction(android.R.string.ok, new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        }).show();
    }
}
