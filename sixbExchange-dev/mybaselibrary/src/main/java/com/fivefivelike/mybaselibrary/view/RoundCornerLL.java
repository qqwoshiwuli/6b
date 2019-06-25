package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.fivefivelike.mybaselibrary.R;


/**
 * @ author      Qsy
 * @ date        17/4/19 下午3:22
 * @ description
 */

public class RoundCornerLL extends LinearLayout {
    private float roundLayoutRadius = 14f;
    private Path  roundPath;
    private RectF rectF;

    public RoundCornerLL(Context context) {
        this(context, null);
    }

    public RoundCornerLL(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundCornerLL);
        roundLayoutRadius = typedArray.getDimensionPixelSize(R.styleable.RoundCornerLL_RoundCornerLL_roundLayoutRadius, (int) roundLayoutRadius);
        typedArray.recycle();

        init();
    }

    private void init() {
        setWillNotDraw(false);//如果你继承的是ViewGroup,注意此行,否则draw方法是不会回调的;
        roundPath = new Path();
        rectF = new RectF();
    }
    private void setRoundPath() {
        //添加一个圆角矩形到path中, 如果要实现任意形状的View, 只需要手动添加path就行
        roundPath.addRoundRect(rectF, roundLayoutRadius, roundLayoutRadius, Path.Direction.CW);
    }


    public void setRoundLayoutRadius(float roundLayoutRadius) {
        this.roundLayoutRadius = roundLayoutRadius;
        setRoundPath();
        postInvalidate();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        rectF.set(0f, 0f, getMeasuredWidth(), getMeasuredHeight());
        setRoundPath();
    }

    @Override
    public void draw(Canvas canvas) {
        if (roundLayoutRadius > 0f) {
            canvas.clipPath(roundPath);
        }
        super.draw(canvas);
    }
}
