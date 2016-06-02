package android.wuliqing.com.lendphonesystemapp.utils;

/**
 * Created by 10172915 on 2016/5/31.
 */
public class MyTextUtils {
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++){
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}
