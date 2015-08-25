package com.dreamspace.superman.Common;

/**
 * Created by Administrator on 2015/8/25 0025.
 */
public class CommonUtils {
    public static boolean isEmpty(String str) {
        if (str == null || str.length() == 0 || str.equalsIgnoreCase("null") || str.isEmpty() || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }
}
