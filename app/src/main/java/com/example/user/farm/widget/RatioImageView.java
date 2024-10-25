package com.example.user.farm.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;

import com.example.user.farm.R;


/**
 * Created by libo on 2017/6/16.
 *
 *  寬高設置比例imageview
 */

public class RatioImageView extends AppCompatImageView {

    private float mDrawableSizeRatio = -1f; // src圖片(前景圖)的寬高比例
    // 根據前景圖寬高比例測量View,防止圖片縮放變形
    private boolean mIsWidthFitDrawableSizeRatio; // 寬度是否根據src圖片(前景圖)的比例來測量（高度已知）
    private boolean mIsHeightFitDrawableSizeRatio; // 高度是否根據src圖片(前景圖)的比例來測量（寬度已知）
    // 寬高比例
    private float mWidthRatio = -1; // 寬度 = 高度*mWidthRatio
    private float mHeightRatio = -1; // 高度 = 寬度*mHeightRatio

    public RatioImageView(Context context) {
        this(context, null);
    }

    public RatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr); // 雖然此處會調用setImageDrawable，但此時成員變量還未被正確初始化
        init(attrs);

        if (getDrawable() != null) {
            mDrawableSizeRatio = 1f * getDrawable().getIntrinsicWidth()
                    / getDrawable().getIntrinsicHeight();
        }
    }

    /**
     * 初始化變量
     */
    private void init(AttributeSet attrs) {

        TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.RatioImageView);
        mIsWidthFitDrawableSizeRatio = a.getBoolean(R.styleable.RatioImageView_is_width_fix_drawable_size_ratio,
                mIsWidthFitDrawableSizeRatio);
        mIsHeightFitDrawableSizeRatio = a.getBoolean(R.styleable.RatioImageView_is_height_fix_drawable_size_ratio,
                mIsHeightFitDrawableSizeRatio);
        mHeightRatio = a.getFloat(
                R.styleable.RatioImageView_height_to_width_ratio, mHeightRatio);
        mWidthRatio = a.getFloat(
                R.styleable.RatioImageView_width_to_height_ratio, mWidthRatio);
        a.recycle();
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (getDrawable() != null) {
            mDrawableSizeRatio = 1f * getDrawable().getIntrinsicWidth()
                    / getDrawable().getIntrinsicHeight();
            if (mDrawableSizeRatio > 0
                    && (mIsWidthFitDrawableSizeRatio || mIsHeightFitDrawableSizeRatio)) {
                requestLayout();
            }
        }
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (getDrawable() != null) {
            mDrawableSizeRatio = 1f * getDrawable().getIntrinsicWidth()
                    / getDrawable().getIntrinsicHeight();
            if (mDrawableSizeRatio > 0
                    && (mIsWidthFitDrawableSizeRatio || mIsHeightFitDrawableSizeRatio)) {
                requestLayout();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 優先級從大到小：
        // mIsWidthFitDrawableSizeRatio mIsHeightFitDrawableSizeRatio
        // mWidthRatio mHeightRatio
        if (mDrawableSizeRatio > 0) {
            // 根據前景圖寬高比例來測量view的大小
            if (mIsWidthFitDrawableSizeRatio) {
                mWidthRatio = mDrawableSizeRatio;
            } else if (mIsHeightFitDrawableSizeRatio) {
                mHeightRatio = 1 / mDrawableSizeRatio;
            }
        }

        if (mHeightRatio > 0 && mWidthRatio > 0) {
            throw new RuntimeException("高度和寬度不能同時設置百分比！！");
        }

        if (mWidthRatio > 0) { // 高度已知，根據比例，設置寬度
            int height = View.MeasureSpec.getSize(heightMeasureSpec);
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(
                    (int) (height * mWidthRatio), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(height, View.MeasureSpec.EXACTLY));
        } else if (mHeightRatio > 0) { // 寬度已知，根據比例，設置高度
            int width = View.MeasureSpec.getSize(widthMeasureSpec);
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(width,
                    View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(
                    (int) (width * mHeightRatio), View.MeasureSpec.EXACTLY));
        } else { // 系統默認測量
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

}
