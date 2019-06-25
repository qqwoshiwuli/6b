package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.fivefivelike.mybaselibrary.R;

import io.reactivex.internal.util.OpenHashSet;
import skin.support.widget.SkinCompatTextView;

/**
 * Created by 郭青枫 on 2017/10/14.
 */

public class FontTextview extends SkinCompatTextView {
    OpenHashSet<Typeface> resources;

    int type = 0;

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.setTextColor(Color.parseColor(color));
    }

    public FontTextview(Context context) {
        super(context);
        init(context, null);
    }

    public FontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        if (attributeSet == null)
            return;

        TypedArray attributes = context.obtainStyledAttributes(
                attributeSet, R.styleable.FontTextview);

        type = attributes.getInt(
                R.styleable.FontTextview_TypeFace, type);
        if (type != 0) {
            Typeface typeface = getTypeface(context, type);
            if (typeface != null) {
                this.setTypeface(typeface);
            }
        }
        setIncludeFontPadding(false);
    }

    public synchronized Typeface getTypeface(Context context, int type) {
        String name = "";
        if (type == 1) {
            name = "Bold";
        } else if (type == 2) {
            name = "Regular";
        } else if (type == 3) {
            name = "Semibold";
        } else if (type == 4) {
            name = "Medium";
        }



        try {
            Typeface ttfTypeface = Typeface.createFromAsset(context.getAssets(),
                    "font/DINPro-" + name + ".otf");
            return ttfTypeface;
        } catch (Exception e) {
            return null;
        }
    }

}
