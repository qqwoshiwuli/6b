package com.fivefivelike.mybaselibrary.base;

import android.view.View;
import android.widget.FrameLayout;

import com.fivefivelike.mybaselibrary.R;

/**
 * Created by 郭青枫 on 2017/10/13.
 */

public class BaseWebViewDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_webview;
    }


    public static class ViewHolder {
        public View rootView;
        public FrameLayout root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.root = (FrameLayout) rootView.findViewById(R.id.root);
        }

    }
}
