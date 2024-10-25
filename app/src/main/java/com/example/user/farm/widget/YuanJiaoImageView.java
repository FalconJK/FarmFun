package com.example.user.farm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.util.TypedValue;

import com.example.user.farm.R;

/**
 * Created by Administrator on 2016/10/10.
 */

public class YuanJiaoImageView extends AppCompatImageView {
    /**
     * 圖片的類型，圓形or圓角
     */
    private int type;
    private static final int TYPE_CIRCLE = 0;
    public static final int TYPE_ROUND = 1;

    /**
     * 圓角大小的默認值
     */
    private static final int BODER_RADIUS_DEFAULT = 10;
    /**
     * 圓角的大小
     */
    private int mBorderRadius;

    /**
     * 繪圖的Paint
     */
    private Paint mBitmapPaint;
    /**
     * 圓角的半徑
     */
    protected int mRadius;
    /**
     * 3x3 矩陣，主要用於縮小放大
     */
    private Matrix mMatrix;
    /**
     * 渲染圖像，使用圖像為繪製圖形著色
     */
    private BitmapShader mBitmapShader;
    /**
     * view的寬度
     */
    private int mWidth;

    private RectF mRoundRect;
    private float width_round;
    private float height_round;


    public YuanJiaoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMatrix = new Matrix();
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);

//        mBorderRadius = a.getDimensionPixelSize(
//                R.styleable.RoundImageView_borderRadius, (int) TypedValue
//                        .applyDimension(TypedValue.COMPLEX_UNIT_DIP,
//                                BODER_RADIUS_DEFAULT, getResources()
//                                        .getDisplayMetrics()));// 默認為10dp
        type = a.getInteger(R.styleable.RoundImageView_type,0);// 默認為Circle
        mBorderRadius = (int) a.getDimension(R.styleable.RoundImageView_radius_imageview,10);
        width_round = a.getDimension(R.styleable.RoundImageView_width_imageview,10);
        height_round = a.getDimension(R.styleable.RoundImageView_height_imageview,10);
        a.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        /**
         * 如果類型是圓形，則強制改變view的寬高一致，以小值為準
         */
        if (type == TYPE_CIRCLE) {
            mWidth = Math.min(getMeasuredWidth(), getMeasuredHeight());
            mRadius = mWidth / 2;
            setMeasuredDimension(mWidth, mWidth);
        }

    }

    /**
     * 初始化BitmapShader
     */
    private void setUpShader() {
        Drawable drawable = getDrawable();
        if (drawable == null) {
            return;
        }

        Bitmap bmp = drawableToBitamp(drawable);
        // 將bmp作為著色器，就是在指定區域內繪製bmp
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR);
        float scale = 1.0f;
        if (type == TYPE_CIRCLE) {
            // 拿到bitmap寬或高的小值
            int bSize = Math.min(bmp.getWidth(), bmp.getHeight());
            scale = mWidth * 1.0f / bSize;
            mMatrix.setScale(scale,scale);
        } else if (type == TYPE_ROUND) {
            // 如果圖片的寬或者高與view的寬高不匹配，計算出需要縮放的比例；縮放後的圖片的寬高，一定要大於我們view的寬高；所以我們這裡取大值；但是圖片會不在中間，所以天才李靜就想了一種方法
            //scale = Math.max(width_round * 1.0f / bmp.getWidth(), height_round
            //       * 1.0f / bmp.getHeight());
            // shader的變換矩陣，我們這裡主要用於放大或者縮小
            mMatrix.setScale(width_round * 1.0f / bmp.getWidth(), height_round
                    * 1.0f / bmp.getHeight());
        }

        // 設置變換矩陣
        mBitmapShader.setLocalMatrix(mMatrix);
        // 設置shader
        mBitmapPaint.setShader(mBitmapShader);
    }

    /**
     * drawable轉bitmap
     *
     * @param drawable
     * @return
     */
    private Bitmap drawableToBitamp(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            return bd.getBitmap();
        }
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null) {
            return;
        }
        setUpShader();

        if (type == TYPE_ROUND) {
            canvas.drawRoundRect(mRoundRect, mBorderRadius, mBorderRadius,
                    mBitmapPaint);
        } else {
            canvas.drawCircle(mRadius, mRadius, mRadius, mBitmapPaint);


        }
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 圓角圖片的範圍
        if (type == TYPE_ROUND)
            mRoundRect = new RectF(0, 0, width_round, height_round);
    }

    private static final String STATE_INSTANCE = "state_instance";
    private static final String STATE_TYPE = "state_type";
    private static final String STATE_BORDER_RADIUS = "state_border_radius";

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(STATE_INSTANCE, super.onSaveInstanceState());
        bundle.putInt(STATE_TYPE, type);
        bundle.putInt(STATE_BORDER_RADIUS, mBorderRadius);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            super.onRestoreInstanceState(((Bundle) state)
                    .getParcelable(STATE_INSTANCE));
            this.type = bundle.getInt(STATE_TYPE);
            this.mBorderRadius = bundle.getInt(STATE_BORDER_RADIUS);
        } else {
            super.onRestoreInstanceState(state);
        }

    }

    public void setBorderRadius(int borderRadius) {
        int pxVal = dp2px(borderRadius);
        if (this.mBorderRadius != pxVal) {
            this.mBorderRadius = pxVal;
            invalidate();
        }
    }

    public void setType(int type) {
        if (this.type != type) {
            this.type = type;
            if (this.type != TYPE_ROUND && this.type != TYPE_CIRCLE) {
                this.type = TYPE_CIRCLE;
            }
            requestLayout();
        }

    }

    public int dp2px(int dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
