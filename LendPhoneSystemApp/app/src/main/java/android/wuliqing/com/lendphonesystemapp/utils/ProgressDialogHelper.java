package android.wuliqing.com.lendphonesystemapp.utils;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by 10172915 on 2016/7/15.
 */
public class ProgressDialogHelper {
    public static ProgressDialog initProgressDialog(int style, Context context, String msg) {
        ProgressDialog mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setProgressStyle(style);// ProgressDialog.STYLE_HORIZONTAL 设置水平进度条
        mProgressDialog.setCancelable(false);// 设置是否可以通过点击Back键取消
        mProgressDialog.setCanceledOnTouchOutside(false);// 设置在点击Dialog外是否取消Dialog进度条
//        mProgressDialog.setIcon(R.drawable.ic_launcher);// 设置提示的title的图标，默认是没有的
//        mProgressDialog.setTitle("提示");
        mProgressDialog.setMax(100);
        mProgressDialog.setMessage(msg);
        return mProgressDialog;
    }
}
