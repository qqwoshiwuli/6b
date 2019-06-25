package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.R;

/**
 * Created by 郭青枫 on 2017/10/12.
 */

public class PopupBackView extends View {
    private Paint mPaint;
    private Bitmap bufferBitmap;
    private Canvas bufferCanvas;
    private float arrowWidth;
    private int arrowGravity;
    private float arrowMarginLeft;
    private float arrowMarginRight;
    private int backageColor;
    private float roundConner;
    private float arrowHeight;
    private DisplayMetrics displayMetrics;
    public static final int ARROW_ALIGN_LEFT = 0;
    public static final int ARROW_ALIGN_CENTER = 1;
    public static final int ARROW_ALIGN_RIGHT = 2;

    public PopupBackView(Context context) {
        this(context, null);
    }

    public PopupBackView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PopupBackView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.PopupBackView);
        arrowWidth = typedArray.getDimension(R.styleable.PopupBackView_arrowWidth, dp2px(10));
        arrowMarginLeft = typedArray.getDimension(R.styleable.PopupBackView_arrow_margin_left, 0);
        arrowMarginRight = typedArray.getDimension(R.styleable.PopupBackView_arrow_margin_right, 0);
        roundConner = typedArray.getDimension(R.styleable.PopupBackView_roundConner, dp2px(5));
        arrowGravity = typedArray.getInt(R.styleable.PopupBackView_arrow_gravity, ARROW_ALIGN_RIGHT);
        backageColor = typedArray.getColor(R.styleable.PopupBackView_backageColor, Color.parseColor("#77000000"));
        arrowHeight = (float) Math.sqrt(Math.pow(arrowWidth, 2) - Math.pow(arrowWidth / 2, 2));
        typedArray.recycle();

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        init();

    }

    private void init() {
        bufferBitmap = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        bufferCanvas = new Canvas(bufferBitmap);
    }

    private void drawBuffer() {
        initPaint();
        drawTriangle();
        drawRound();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(backageColor);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBuffer();
        canvas.drawBitmap(bufferBitmap, 0, 0, null);
    }

    /**
     * 绘制三角形
     */
    private void drawTriangle() {
        int measuredWidth = getMeasuredWidth();
        float startYpoint = measuredWidth - roundConner;
        switch (arrowGravity) {
            case ARROW_ALIGN_LEFT://左边
                startYpoint = roundConner + arrowWidth;
                break;
            case ARROW_ALIGN_CENTER://中间
                startYpoint = measuredWidth / 2 + arrowWidth / 2;
                break;
            case ARROW_ALIGN_RIGHT://右边
                startYpoint = measuredWidth - roundConner;
                break;
        }
        if (arrowMarginLeft != 0) {//左边距
            if (startYpoint + arrowMarginLeft >= measuredWidth - roundConner) {
                startYpoint = measuredWidth - roundConner;
            } else {
                startYpoint += arrowMarginLeft;
            }
        } else {//右边距
            if (arrowMarginRight != 0) {
                if (startYpoint - arrowMarginLeft <= roundConner + arrowWidth) {
                    startYpoint = roundConner + arrowWidth;
                } else {
                    startYpoint -= arrowMarginLeft;
                }
            }
        }
        Path path = new Path();
        path.moveTo(startYpoint, arrowHeight);
        path.lineTo(startYpoint - arrowWidth / 2, 0);
        path.lineTo(startYpoint - arrowWidth, arrowHeight);
        path.lineTo(startYpoint, arrowHeight);
        path.close();
        bufferCanvas.drawPath(path, mPaint);
    }

    /**
     * 绘制矩形
     */
    private void drawRound() {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        RectF rectf = new RectF();
        rectf.left = 0;
        rectf.top = arrowHeight;
        rectf.right = measuredWidth;
        rectf.bottom = measuredHeight;
        bufferCanvas.drawRoundRect(rectf, roundConner, roundConner, mPaint);
    }

    private float dp2px(int dp) {
        displayMetrics = getContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

}
