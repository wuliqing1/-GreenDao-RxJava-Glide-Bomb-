package android.wuliqing.com.lendphonesystemapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {
    public static String LEND_PHONE_PRE = "lend_phone_pre";
    public static String FIRST_RUN_KEY = "first_run_key";

    public static void write(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(key, value).apply();
    }

    public static void write(Context context, String key, int value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static void write(Context context, String key, Boolean value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(key, value).apply();
    }

    public static String readString(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, "");
    }

    public static int readInt(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        return sharedPreferences.getInt(key, 0);
    }

    public static Boolean readBoolean(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        return sharedPreferences.getBoolean(key, false);
    }

    public static void remove(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                LEND_PHONE_PRE, Context.MODE_PRIVATE);
        sharedPreferences.edit().remove(key).apply();
    }

}
