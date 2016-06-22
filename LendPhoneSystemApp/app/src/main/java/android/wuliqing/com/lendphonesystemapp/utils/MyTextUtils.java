package android.wuliqing.com.lendphonesystemapp.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 10172915 on 2016/5/31.
 */
public class MyTextUtils {
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String getDateStringForName() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String str = format.format(new Date());
        return str;
    }

}
