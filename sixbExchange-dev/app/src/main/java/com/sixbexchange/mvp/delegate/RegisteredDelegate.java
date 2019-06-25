package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class RegisteredDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_registered;
    }


    public static class ViewHolder {
        public View rootView;
        public EditText et_phone;
        public EditText et_code;
        public TextView tv_get_code;
        public EditText et_pass;
        public EditText et_invite;
        public RoundButton tv_registered;
        public TextView tv_help;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.et_phone = (EditText) rootView.findViewById(R.id.et_phone);
            this.et_code = (EditText) rootView.findViewById(R.id.et_code);
            this.tv_get_code = (TextView) rootView.findViewById(R.id.tv_get_code);
            this.et_pass = (EditText) rootView.findViewById(R.id.et_pass);
            this.et_invite = (EditText) rootView.findViewById(R.id.et_invite);
            this.tv_registered = (RoundButton) rootView.findViewById(R.id.tv_registered);
            this.tv_help = (TextView) rootView.findViewById(R.id.tv_help);
        }

    }
}