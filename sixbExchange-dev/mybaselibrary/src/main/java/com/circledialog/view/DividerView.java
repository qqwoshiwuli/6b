package com.circledialog.view;

import android.content.Context;
import android.widget.LinearLayout;

import com.circledialog.res.values.CircleColor;

import skin.support.widget.SkinCompatView;

/**
 * 分隔线，默认垂直
 * Created by hupei on 2017/3/30.
 */
class DividerView extends SkinCompatView {
    public DividerView(Context context) {
        super(context);
        init();
    }

    private void init() {
        setLayoutParams(new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
        setBackgroundColor(CircleColor.getDivider());
    }

    /**
     * 将分隔线设置为水平分隔
     */
    public void setVertical() {
        setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
    }
}
