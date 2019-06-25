package com.tablayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

import skin.support.widget.SkinCompatBackgroundHelper;
import skin.support.widget.SkinCompatSupportable;

/**
 * Created by 郭青枫 on 2018/1/14 0014.
 */

public class SkinHorizontalScrollView extends HorizontalScrollView implements SkinCompatSupportable {
    private SkinCompatBackgroundHelper mBackgroundTintHelper;


    public SkinHorizontalScrollView(Context context) {
        this(context, null);
    }

    public SkinHorizontalScrollView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SkinHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mBackgroundTintHelper = new SkinCompatBackgroundHelper(this);
        mBackgroundTintHelper.loadFromAttributes(attrs, defStyleAttr);

    }


    @Override
    public void applySkin() {
        if (mBackgroundTintHelper != null) {
            mBackgroundTintHelper.applySkin();
        }
    }
}
