package com.fivefivelike.mybaselibrary.base;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.R;
import com.yanzhenjie.album.widget.photoview.FixViewPager;

/**
 * Created by 郭青枫 on 2017/10/16.
 */

public class BigImageveiwDelegate extends BaseDelegate {

    public  ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder=new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.album_activity_preview;
    }

    public static class ViewHolder {
        public View rootView;
        public FixViewPager view_pager;
        public Toolbar toolbar;
        public TextView tv_duration;
        public AppCompatCheckBox cb_album_check;
        public RelativeLayout bottom_root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.view_pager = (FixViewPager) rootView.findViewById(R.id.view_pager);
            this.toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
            this.tv_duration = (TextView) rootView.findViewById(R.id.tv_duration);
            this.cb_album_check = (AppCompatCheckBox) rootView.findViewById(R.id.cb_album_check);
            this.bottom_root = (RelativeLayout) rootView.findViewById(R.id.bottom_root);
        }

    }
}
