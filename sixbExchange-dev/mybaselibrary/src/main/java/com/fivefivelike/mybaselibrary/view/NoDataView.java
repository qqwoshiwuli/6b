package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.NetworkUtils;
import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;

/**
 * Created by 郭青枫 on 2018/7/26 0026.
 */

public class NoDataView extends FrameLayout {
    public ImageView ic_nodata;
    public TextView tv_nodata;

    int imgId = 0;
    String text = "";

    public NoDataView(@NonNull Context context) {
        super(context);
        initView(context);
    }

    public NoDataView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public NoDataView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        LayoutInflater.from(context).inflate(R.layout.merge_layout_no_data, this);
        this.ic_nodata = (ImageView) findViewById(R.id.ic_nodata);
        this.tv_nodata = (TextView) findViewById(R.id.tv_nodata);
        this.setVisibility(GONE);
        if (imgId != 0) {
            ic_nodata.setImageResource(imgId);
        }
        if (!TextUtils.isEmpty(text)) {
            tv_nodata.setText(text);
        }
    }

    public void setShowImg(int imgId) {
        this.imgId = imgId;
        if (ic_nodata != null) {
            ic_nodata.setImageResource(imgId);
        }
    }


    public void setToastText(String text) {
        if (!TextUtils.isEmpty(text)) {
            this.text = text;
            if (tv_nodata != null) {
                tv_nodata.setText(Html.fromHtml(text));
            }
        }
    }

    public void setIsShow(boolean isShow) {
        this.setVisibility(isShow ? VISIBLE : GONE);
    }

    public void show() {
        show(CommonUtils.getString(R.string.str_no_data));
    }

    public void show(String noDataText) {
        this.setVisibility(VISIBLE);
        boolean connected = NetworkUtils.isConnected();
        if (!connected) {
            setToastText(CommonUtils.getString(R.string.str_no_network));
            setShowImg(R.drawable.ic_nonetwork);
        } else {
            setToastText(noDataText);
            setShowImg(R.drawable.ic_nodata);
        }
    }

    public void hide() {
        this.setVisibility(GONE);
    }

}
