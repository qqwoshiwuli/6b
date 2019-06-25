package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;

public class ExchangeSetDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_exchange_set;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView et_overload_time;
        public ImageView iv_select2;
        public LinearLayout lin_select2;
        public ImageView iv_select3;
        public LinearLayout lin_select3;
        public TextView et_price;
        public LinearLayout lin_bitmex;
        public ImageView iv_leaf;
        public LinearLayout lin_leaf;
        public ImageView iv_coin;
        public LinearLayout lin_coin;
        public LinearLayout lin_okex;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_overload_time = (TextView) rootView.findViewById(R.id.et_overload_time);
            this.iv_select2 = (ImageView) rootView.findViewById(R.id.iv_select2);
            this.lin_select2 = (LinearLayout) rootView.findViewById(R.id.lin_select2);
            this.iv_select3 = (ImageView) rootView.findViewById(R.id.iv_select3);
            this.lin_select3 = (LinearLayout) rootView.findViewById(R.id.lin_select3);
            this.et_price = (TextView) rootView.findViewById(R.id.et_price);
            this.lin_bitmex = (LinearLayout) rootView.findViewById(R.id.lin_bitmex);
            this.iv_leaf = (ImageView) rootView.findViewById(R.id.iv_leaf);
            this.lin_leaf = (LinearLayout) rootView.findViewById(R.id.lin_leaf);
            this.iv_coin = (ImageView) rootView.findViewById(R.id.iv_coin);
            this.lin_coin = (LinearLayout) rootView.findViewById(R.id.lin_coin);
            this.lin_okex = (LinearLayout) rootView.findViewById(R.id.lin_okex);
        }

    }
}