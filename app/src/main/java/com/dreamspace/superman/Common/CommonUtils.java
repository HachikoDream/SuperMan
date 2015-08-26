package com.dreamspace.superman.Common;

import com.dreamspace.superman.model.api.ErrorRes;

import retrofit.RetrofitError;

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

    public static ErrorRes getErrorInfo(RetrofitError error){
        return (ErrorRes) error.getBodyAs(ErrorRes.class);
    }

}
