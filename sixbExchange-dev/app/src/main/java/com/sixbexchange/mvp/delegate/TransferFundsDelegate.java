package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class TransferFundsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_transfer_funds;
    }


    public static class ViewHolder {
        public View rootView;
        public TextView tv_select_coins;
        public LinearLayout lin_select_coin;
        public TextView tv_out;
        public TextView tv_in;
        public EditText tv_num;
        public RoundButton tv_commit;
        public TextView tv_content;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.tv_select_coins = (TextView) rootView.findViewById(R.id.tv_select_coins);
            this.lin_select_coin = (LinearLayout) rootView.findViewById(R.id.lin_select_coin);
            this.tv_out = (TextView) rootView.findViewById(R.id.tv_out);
            this.tv_in = (TextView) rootView.findViewById(R.id.tv_in);
            this.tv_num = (EditText) rootView.findViewById(R.id.tv_num);
            this.tv_commit = (RoundButton) rootView.findViewById(R.id.tv_commit);
            this.tv_content = (TextView) rootView.findViewById(R.id.tv_content);
        }

    }
}