package com.example.user.farm.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by user on 2018/3/10.
 */
public class RectangleHalfWidth extends android.support.v7.widget.AppCompatImageView {
    public RectangleHalfWidth(Context context) {
        super(context);
    }

    public RectangleHalfWidth(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RectangleHalfWidth(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //傳入參數widthMeasureSpec、heightMeasureSpec
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable drawable = getDrawable();
        if(null != drawable){
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) Math.ceil((float) width * (float) drawable.getIntrinsicHeight() / (float) drawable.getIntrinsicWidth());
            setMeasuredDimension(width,height);
            super.onMeasure(width,height);
        }else{
            super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        }
    }


}