package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.circledialog.view.NoSlideViewPager;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;
import com.tablayout.CommonTabLayout;

public class TransactionDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transaction;
    }


    public static class ViewHolder {
        public View rootView;
        public ImageView iv_exch;
        public TextView tv_name;
        public LinearLayout lin_exch;
        public TextView tv_level;
        public CommonTabLayout tl_2;
        public NoSlideViewPager vp_root;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_exch = (ImageView) rootView.findViewById(R.id.iv_exch);
            this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
            this.lin_exch = (LinearLayout) rootView.findViewById(R.id.lin_exch);
            this.tv_level = (TextView) rootView.findViewById(R.id.tv_level);
            this.tl_2 = (CommonTabLayout) rootView.findViewById(R.id.tl_2);
            this.vp_root = (NoSlideViewPager) rootView.findViewById(R.id.vp_root);
        }

    }
}