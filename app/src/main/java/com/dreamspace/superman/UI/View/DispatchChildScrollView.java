package com.dreamspace.superman.UI.View;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

/**
 * Created by Wells on 2016/2/15.
 */
public class DispatchChildScrollView extends ScrollView {
    private boolean enableScroll = true;

    public DispatchChildScrollView(Context context) {
        super(context);
    }

    public DispatchChildScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DispatchChildScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public DispatchChildScrollView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean result = true;
        if (!enableScroll) {
            result = false;
        } else {
        result = super.onInterceptTouchEvent(ev);
        }
        return result;
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        doOnBorderListener();
    }

    public void setEnableScroll(boolean enableScroll) {
        this.enableScroll = enableScroll;
    }

    private void doOnBorderListener() {
        View contentView = getChildAt(0);
        if (contentView != null && contentView.getMeasuredHeight() <= getScrollY() + getHeight()) {
            enableScroll = false;
        } else {
            enableScroll = true;
        }
    }
}
