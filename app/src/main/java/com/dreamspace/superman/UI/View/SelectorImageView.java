package com.dreamspace.superman.UI.View;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.dreamspace.superman.R;


/**
 * Created by Administrator on 2015/8/11 0011.
 */
public class SelectorImageView extends ImageView implements View.OnClickListener {
    private Bitmap selectedBitmap;
    //    private Bitmap sourceBitmap;
    private int width;
    private int height;
    private boolean isSelected = false;
    private Rect mRect;
    private float alpha = 0.8f;
    private static final String INFO = "INFO";
    private String name;

    public SelectorImageView(Context context) {
        this(context, null, 0);
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
        validateView();
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    public SelectorImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SelectorImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
//        BitmapDrawable sourceBitmapDrawable= (BitmapDrawable) getDrawable();
//        sourceBitmap=sourceBitmapDrawable.getBitmap();
        BitmapDrawable bitmapDrawable = (BitmapDrawable) getResources().getDrawable(R.drawable.choose_class_rc);
        selectedBitmap = bitmapDrawable.getBitmap();
        width = selectedBitmap.getWidth();
        height = selectedBitmap.getHeight();
        setOnClickListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int sourceh = getMeasuredHeight();
        int sourcew = getMeasuredWidth();
        Log.i(INFO, "w: " + sourcew + " h:" + sourceh);
        Log.i(INFO, "rcw: " + width + " rch:" + height);
        mRect = new Rect(sourcew - width - 20, sourceh - height - 20, sourcew - 20, sourceh - 20);

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    private void reverseSelected() {
        isSelected = !isSelected;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(INFO, "ondraw in");
        super.onDraw(canvas);
        setAlpha(1.0f);
        if (isSelected) {
            Log.i(INFO, "on draw is selected in");
            setAlpha(alpha);
            canvas.drawBitmap(selectedBitmap, null, mRect, null);
        }
    }

    private void validateView() {
        if (Looper.getMainLooper() == Looper.myLooper()) {
            invalidate();
        } else {
            postInvalidate();
        }
    }

    @Override
    public void onClick(View v) {
        reverseSelected();
        setIsSelected(isSelected);
    }
}
