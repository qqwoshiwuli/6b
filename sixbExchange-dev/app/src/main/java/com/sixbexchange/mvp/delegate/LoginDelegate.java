package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class LoginDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_login;
    }


    public static class ViewHolder {
        public View rootView;
        public EditText et_phone;
        public EditText et_pass;
        public RoundButton tv_login;
        public TextView tv_help;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_phone = (EditText) rootView.findViewById(R.id.et_phone);
            this.et_pass = (EditText) rootView.findViewById(R.id.et_pass);
            this.tv_login = (RoundButton) rootView.findViewById(R.id.tv_login);
            this.tv_help = (TextView) rootView.findViewById(R.id.tv_help);
        }

    }
}