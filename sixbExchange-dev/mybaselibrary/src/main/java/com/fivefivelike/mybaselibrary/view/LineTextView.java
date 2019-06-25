package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.fivefivelike.mybaselibrary.R;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;

/**
 * Created by 郭青枫 on 2019/1/7 0007.
 */

public class LineTextView extends FontTextview {
    private int mY;
    private float mContentWidth;
    private String mEndText;
    private int mCNNum = 0;
    private float mEndWidth = 0;

    public LineTextView(Context context) {
        this(context, null);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        final TypedArray custom = context.obtainStyledAttributes(
                attrs, R.styleable.LableView, defStyleAttr, 0);
        mCNNum = custom.getInt(R.styleable.LableView_cnNum, 0);
        mEndText = custom.getString(R.styleable.LableView_endText);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (!TextUtils.isEmpty(mEndText)) {
            mEndWidth = getPaint().measureText(mEndText);
        }
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getMode(widthMeasureSpec);
        float width = 0;
        if (mCNNum != 0 && isChinese(getText().toString())) {
            width = getPaint().measureText("测") * mCNNum;
            width += mEndWidth;
            setMeasuredDimension((int) width, getMeasuredHeight());
        } else if (mode == MeasureSpec.AT_MOST) {
            width = (int) getPaint().measureText(getText() + mEndText);
            width += mEndWidth;
            size = (int) Math.max(width, size);
            setMeasuredDimension(size, getMeasuredHeight());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        TextPaint paint = getPaint();
        paint.setColor(getCurrentTextColor());
        paint.drawableState = getDrawableState();
        mContentWidth = getMeasuredWidth() - mEndWidth;
        String text = getText().toString();
        mY = 0;
        mY += getTextSize();
        Layout layout = getLayout();

        // layout.getLayout()在4.4.3出现NullPointerException
        if (layout == null) {
            return;
        }

        Paint.FontMetrics fm = paint.getFontMetrics();

        int textHeight = (int) (Math.ceil(fm.descent - fm.ascent));
        textHeight = (int) (textHeight * layout.getSpacingMultiplier() + layout
                .getSpacingAdd());
        drawScaledText(canvas, 0, getWords(getText().toString()), mContentWidth);
        mY += textHeight;
        mY -= textHeight;
        if (!TextUtils.isEmpty(mEndText)) {
            canvas.drawText(mEndText, getMeasuredWidth() - paint.measureText(mEndText), mY, paint);
        }
    }

    private void drawScaledText(Canvas canvas, int start, List<String> words,
                                float width) {
        float used = 0, space = 0;
        float[] wordWidths = new float[words.size()];
        for (int i = 0; i < words.size(); i++) {
            wordWidths[i] = StaticLayout.getDesiredWidth(words.get(i), getPaint());
            used += wordWidths[i];
        }
        space = (width - used) / (words.size() - 1);
        float x = 0;
        for (int i = 0; i < words.size(); i++) {
            canvas.drawText(words.get(i), x, mY, getPaint());
            x += space + wordWidths[i];
        }
    }
    public  List<String> getWords(@NonNull String source) {
        char[] dst = new char[source.length()];
        List<String> words = new ArrayList<>();
        //中文、空白字符当做分界符
        source.getChars(0, source.length(), dst, 0);
        int word_begin = 0;
        char preChar = ' ';
        for (int i = 0; i < dst.length; i++) {
            char item = dst[i];
            if (isChinese(item)) {
                if (!isChinese(preChar) && !Character.isSpaceChar(preChar)) {
                    words.add(source.substring(word_begin, i));
                }
                words.add(new String(new char[]{item}));
                word_begin = i + 1;
            } else if (Character.isSpaceChar(item)) {
                if ((!Character.isSpaceChar(preChar) && !isChinese(preChar))) {
                    words.add(source.substring(word_begin, i));
                }
                word_begin = i + 1;
            } else if (!Character.isSpaceChar(item) && i == dst.length - 1) {
                words.add(source.substring(word_begin, i + 1));
            }
            preChar = item;
        }
        return words;
    }

    public  boolean isChinese(String string) {
        if (string.replaceAll("[\u4e00-\u9fa5]*", "").length() == 0) {
            return true;
        } else {
            return false;
        }
    }

    private  boolean isChinese(char item) {
        return (item >= 0x4e00) && (item <= 0x9fbb);
    }
}
