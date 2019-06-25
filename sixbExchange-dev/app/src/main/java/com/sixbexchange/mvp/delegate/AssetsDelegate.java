package com.sixbexchange.mvp.delegate;

import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.JudgeNestedScrollView;
import com.sixbexchange.R;
import com.tablayout.SlidingTabLayout;

public class AssetsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_assets;
    }


    public static class ViewHolder {
        public View rootView;

        public TextView tv_total_assets;
        public TextView tv_total_assets_unit;
        public TextView tv_available_assets;
        public TextView tv_available_assets_unit;
        public RecyclerView recycler_view;
        public SlidingTabLayout tl_2;
        public ViewPager vp_sliding;
        public JudgeNestedScrollView judgeNestedScrollView;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tv_total_assets = (TextView) rootView.findViewById(R.id.tv_total_assets);
            this.tv_total_assets_unit = (TextView) rootView.findViewById(R.id.tv_total_assets_unit);
            this.tv_available_assets = (TextView) rootView.findViewById(R.id.tv_available_assets);
            this.tv_available_assets_unit = (TextView) rootView.findViewById(R.id.tv_available_assets_unit);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.tl_2 = (SlidingTabLayout) rootView.findViewById(R.id.tl_2);
            this.vp_sliding = (ViewPager) rootView.findViewById(R.id.vp_sliding);
            this.judgeNestedScrollView = (JudgeNestedScrollView) rootView.findViewById(R.id.judgeNestedScrollView);
        }

    }
}