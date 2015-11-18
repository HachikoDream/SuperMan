package com.dreamspace.superman.Common;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Wells on 2015/11/18.
 */
public class InputUtils {
    /**
     * @throws
     * @MethodName:closeInputMethod
     * @Description:关闭系统软键盘
     */

    public static void closeInputMethod(Activity activity) {

        try {

            ((InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE))

                    .hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),

                            InputMethodManager.HIDE_NOT_ALWAYS);

        } catch (Exception e) {
        } finally {
        }

    }

    /**
     * @throws
     * @MethodName:openInputMethod
     * @Description:打开系统软键盘
     */

    public static void openInputMethod(final EditText editText) {


        InputMethodManager inputManager = (InputMethodManager) editText

                .getContext().getSystemService(

                        Context.INPUT_METHOD_SERVICE);

        inputManager.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

    }
    public static void reverse(Context context){
        InputMethodManager m=(InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
}