package com.dreamspace.superman.Common;

import com.dreamspace.superman.model.api.ErrorRes;

import java.text.NumberFormat;
import java.text.ParseException;

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
    public static int getPriceFromString(String price_content){
        int price=-1;
        NumberFormat numberFormat=NumberFormat.getNumberInstance();
        numberFormat.setMaximumFractionDigits(2);
        try {
            Number number=numberFormat.parse(price_content);
            price= (int) (number.floatValue()*100);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return price;
    }
    public static String getStringFromPrice(int price){
        String dot=".";
        StringBuilder sb=new StringBuilder();
        String priceContent=String.valueOf(price);
        if(priceContent.length()>=3){
            String tail=priceContent.substring(priceContent.length()-2,priceContent.length());
            String start=priceContent.substring(0,priceContent.length()-2);
            if(tail.equals("00")){
                return start;
            }else {
                return sb.append(start).append(dot).append(tail).toString();
            }
        }else {
            float priceF=(float)price/100;
            return String.valueOf(priceF);
        }

    }

}
