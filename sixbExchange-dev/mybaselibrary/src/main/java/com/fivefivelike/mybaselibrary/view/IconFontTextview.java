package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import skin.support.widget.SkinCompatTextView;

/**
 * Created by 郭青枫 on 2017/10/14.
 */

public class IconFontTextview extends SkinCompatTextView {
    public IconFontTextview(Context context) {
        super(context);
        init(context);
    }

    public IconFontTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconFontTextview(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        Typeface typeface = IconFontTypeFace.getTypeface(context);
        this.setTypeface(typeface);
        setIncludeFontPadding(false);

    }

    public static class IconFontTypeFace {

        //用static,整个app共用整个typeface就够了
        private static Typeface ttfTypeface = null;

        public static synchronized Typeface getTypeface(Context context) {
            if (ttfTypeface == null) {
                try {
                    ttfTypeface = Typeface.createFromAsset(context.getAssets(), "iconfont/iconfont.ttf");
                } catch (Exception e) {

                }
            }
            return ttfTypeface;
        }

        public static synchronized void clearTypeface() {
            ttfTypeface = null;
        }
    }
}
