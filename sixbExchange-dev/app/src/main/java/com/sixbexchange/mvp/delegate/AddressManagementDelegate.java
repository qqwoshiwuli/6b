package com.sixbexchange.mvp.delegate;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class AddressManagementDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_address_management;
    }


    public static class ViewHolder {
        public View rootView;

        public TextView tv_select_coins;
        public LinearLayout lin_select_coin;
        public EditText tv_addr;
        public LinearLayout lin_select_addr;
        public EditText tv_memo;
        public LinearLayout lin_memo;
        public EditText tv_num;
        public EditText tv_pw;
        public EditText tv_code;
        public TextView tv_get_code;
        public RoundButton tv_commit;
        public TextView tv_already;
        public RecyclerView recycler_view;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tv_select_coins = (TextView) rootView.findViewById(R.id.tv_select_coins);
            this.lin_select_coin = (LinearLayout) rootView.findViewById(R.id.lin_select_coin);
            this.tv_addr = (EditText) rootView.findViewById(R.id.tv_addr);
            this.lin_select_addr = (LinearLayout) rootView.findViewById(R.id.lin_select_addr);
            this.tv_memo = (EditText) rootView.findViewById(R.id.tv_memo);
            this.lin_memo = (LinearLayout) rootView.findViewById(R.id.lin_memo);
            this.tv_num = (EditText) rootView.findViewById(R.id.tv_num);
            this.tv_pw = (EditText) rootView.findViewById(R.id.tv_pw);
            this.tv_code = (EditText) rootView.findViewById(R.id.tv_code);
            this.tv_get_code = (TextView) rootView.findViewById(R.id.tv_get_code);
            this.tv_commit = (RoundButton) rootView.findViewById(R.id.tv_commit);
            this.tv_already = (TextView) rootView.findViewById(R.id.tv_already);
            this.recycler_view = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        }

    }
}