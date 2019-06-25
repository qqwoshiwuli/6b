package com.sixbexchange.mvp.delegate;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.SingleLineZoomTextView;
import com.fivefivelike.mybaselibrary.view.SwipeRefreshLayout;
import com.sixbexchange.R;

public class TrTransactionDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        viewHolder.tv_order_price_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tv_order_price.setFocusable(true);
                viewHolder.tv_order_price.setFocusableInTouchMode(true);
                viewHolder.tv_order_price.requestFocus();
            }
        });
        viewHolder.tv_order_num_unit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.tv_order_num.setFocusable(true);
                viewHolder.tv_order_num.setFocusableInTouchMode(true);
                viewHolder.tv_order_num.requestFocus();
            }
        });
        viewHolder.tv_order_price.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    viewHolder.tv_order_price.setCursorVisible(false);
                    viewHolder.tv_order_price.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.tv_order_price.setCursorVisible(true);
                            viewHolder.lin_order_price.setBackground(
                                    CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                            viewHolder.tv_order_price.setSelection(
                                    viewHolder.tv_order_price.getText().toString().length());
                        }
                    }, 50);
                } else {
                    viewHolder.lin_order_price.setBackground(
                            CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                }
            }
        });
        viewHolder.tv_order_num.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    viewHolder.tv_order_num.setCursorVisible(false);
                    viewHolder.tv_order_num.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.tv_order_num.setCursorVisible(true);
                            viewHolder.lin_order_num.setBackground(
                                    CommonUtils.getDrawable(R.drawable.shape_mark_border_r5));
                            viewHolder.tv_order_num.setSelection(
                                    viewHolder.tv_order_num.getText().toString().length());
                        }
                    }, 50);
                } else {
                    viewHolder.lin_order_num.setBackground(
                            CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                }
            }
        });
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_tr_transaction;
    }


    public static class ViewHolder {
        public View rootView;
        public IconFontTextview tv_order_type;
        public LinearLayout lin_order_type;
        public IconFontTextview tv_lever;
        public LinearLayout lin_lever;
        public TextView tv_now_price;
        public ImageView iv_to_kline;
        public LinearLayout lin_to_kline;
        public TextView tv_open;
        public TextView tv_close;
        public TextView tv_opponent_price;
        public FrameLayout fl_opponent_price;
        public TextView tv_limit_price;
        public FrameLayout fl_limit_price;
        public LinearLayout lin_order_select_price;
        public EditText tv_order_price;
        public TextView tv_order_price_unit;
        public LinearLayout lin_order_price;
        public EditText tv_order_num;
        public TextView tv_order_num_unit;
        public LinearLayout lin_order_num;
        public RoundButton tv_buy;
        public TextView tv_buy_left;
        public TextView tv_buy_available;
        public LinearLayout lin_buy_left;
        public TextView tv_buy_type;
        public TextView tv_buy_type_num;
        public RoundButton tv_sell;
        public TextView tv_sell_left;
        public TextView tv_sell_available;
        public LinearLayout lin_sell_left;
        public TextView tv_sell_type;
        public TextView tv_sell_type_num;
        public TextView tv_show_price_type;
        public TextView tv_coin_num;
        public RecyclerView rv_buy;
        public RecyclerView rv_sell;
        public TextView tv_latest_index;
        public TextView tv_fund_rate;
        public RecyclerView recycler_view;
        public LinearLayout lin_no_order;
        public LinearLayout lin_close_all;
        public SwipeRefreshLayout swipeRefreshLayout;
        public FrameLayout fl_left;
        public DrawerLayout main_drawer_layout;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_order_type = (IconFontTextview) rootView.findViewById(R.id.tv_order_type);
            this.lin_order_type = (LinearLayout) rootView.findViewById(R.id.lin_order_type);
            this.tv_lever = (IconFontTextview) rootView.findViewById(R.id.tv_lever);
            this.lin_lever = (LinearLayout) rootView.findViewById(R.id.lin_lever);
            this.tv_now_price = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_now_price);
            this.iv_to_kline = (ImageView) rootView.findViewById(R.id.iv_to_kline);
            this.lin_to_kline = (LinearLayout) rootView.findViewById(R.id.lin_to_kline);
            this.tv_open = (TextView) rootView.findViewById(R.id.tv_open);
            this.tv_close = (TextView) rootView.findViewById(R.id.tv_close);
            this.tv_opponent_price = (TextView) rootView.findViewById(R.id.tv_opponent_price);
            this.fl_opponent_price = (FrameLayout) rootView.findViewById(R.id.fl_opponent_price);
            this.tv_limit_price = (TextView) rootView.findViewById(R.id.tv_limit_price);
            this.fl_limit_price = (FrameLayout) rootView.findViewById(R.id.fl_limit_price);
            this.lin_order_select_price = (LinearLayout) rootView.findViewById(R.id.lin_order_select_price);
            this.tv_order_price = (EditText) rootView.findViewById(R.id.tv_order_price);
            this.tv_order_price_unit = (TextView) rootView.findViewById(R.id.tv_order_price_unit);
            this.lin_order_price = (LinearLayout) rootView.findViewById(R.id.lin_order_price);
            this.tv_order_num = (EditText) rootView.findViewById(R.id.tv_order_num);
            this.tv_order_num_unit = (TextView) rootView.findViewById(R.id.tv_order_num_unit);
            this.lin_order_num = (LinearLayout) rootView.findViewById(R.id.lin_order_num);
            this.tv_buy = (RoundButton) rootView.findViewById(R.id.tv_buy);
            this.tv_buy_left = (TextView) rootView.findViewById(R.id.tv_buy_left);
            this.tv_buy_available = (TextView) rootView.findViewById(R.id.tv_buy_available);
            this.lin_buy_left = (LinearLayout) rootView.findViewById(R.id.lin_buy_left);
            this.tv_buy_type = (TextView) rootView.findViewById(R.id.tv_buy_type);
            this.tv_buy_type_num = (TextView) rootView.findViewById(R.id.tv_buy_type_num);
            this.tv_sell = (RoundButton) rootView.findViewById(R.id.tv_sell);
            this.tv_sell_left = (TextView) rootView.findViewById(R.id.tv_sell_left);
            this.tv_sell_available = (TextView) rootView.findViewById(R.id.tv_sell_available);
            this.lin_sell_left = (LinearLayout) rootView.findViewById(R.id.lin_sell_left);
            this.tv_sell_type = (TextView) rootView.findViewById(R.id.tv_sell_type);
            this.tv_sell_type_num = (TextView) rootView.findViewById(R.id.tv_sell_type_num);
            this.tv_show_price_type = (TextView) rootView.findViewById(R.id.tv_show_price_type);
            this.tv_coin_num = (TextView) rootView.findViewById(R.id.tv_coin_num);
            this.rv_buy = (RecyclerView) rootView.findViewById(R.id.rv_buy);
            this.rv_sell = (RecyclerView) rootView.findViewById(R.id.rv_sell);
            this.tv_latest_index = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_latest_index);
            this.tv_fund_rate = (SingleLineZoomTextView) rootView.findViewById(R.id.tv_fund_rate);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            this.lin_no_order = (LinearLayout) rootView.findViewById(R.id.lin_no_order);
            this.lin_close_all = (LinearLayout) rootView.findViewById(R.id.lin_close_all);
            this.swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefreshLayout);
            this.fl_left = (FrameLayout) rootView.findViewById(R.id.fl_left);
            this.main_drawer_layout = (DrawerLayout) rootView.findViewById(R.id.main_drawer_layout);
        }

    }
}