package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class RechargeAddressDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_recharge_address;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView tv_select_coins;
        public LinearLayout lin_select_coin;
        public ImageView iv_address;
        public IconFontTextview tv_address_help;
        public TextView tv_address;
        public RoundButton tv_copy_address;
        public IconFontTextview tv_memo_help;
        public TextView tv_memo;
        public RoundButton tv_copy_memo;
        public LinearLayout lin_memo;
        public TextView tv_content;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_select_coins = (TextView) rootView.findViewById(R.id.tv_select_coins);
            this.lin_select_coin = (LinearLayout) rootView.findViewById(R.id.lin_select_coin);
            this.iv_address = (ImageView) rootView.findViewById(R.id.iv_address);
            this.tv_address_help = (IconFontTextview) rootView.findViewById(R.id.tv_address_help);
            this.tv_address = (TextView) rootView.findViewById(R.id.tv_address);
            this.tv_copy_address = (RoundButton) rootView.findViewById(R.id.tv_copy_address);
            this.tv_memo_help = (IconFontTextview) rootView.findViewById(R.id.tv_memo_help);
            this.tv_memo = (TextView) rootView.findViewById(R.id.tv_memo);
            this.tv_copy_memo = (RoundButton) rootView.findViewById(R.id.tv_copy_memo);
            this.lin_memo = (LinearLayout) rootView.findViewById(R.id.lin_memo);
            this.tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        }

    }
}