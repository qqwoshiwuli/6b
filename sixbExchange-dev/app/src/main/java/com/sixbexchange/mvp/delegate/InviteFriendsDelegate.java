package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class InviteFriendsDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_invite_friends;
    }


    public static class ViewHolder {
        public View rootView;
        public ImageView iv_invite;
        public TextView tv_input;
        public TextView tv_code;
        public RoundButton tv_Copy;
        public TextView tv_input_again;
        public ImageView iv_piv;
        public ImageView iv_bottom;
        public TextView tv_invite_code;
        public ImageView iv_zxing;
        public ImageView iv_activity_rules;
        public TextView tv_share;
        public TextView tv_switch;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.iv_invite = (ImageView) rootView.findViewById(R.id.iv_invite);
            this.tv_input = (TextView) rootView.findViewById(R.id.tv_input);
            this.tv_code = (TextView) rootView.findViewById(R.id.tv_code);
            this.tv_Copy = (RoundButton) rootView.findViewById(R.id.tv_Copy);
            this.tv_input_again = (TextView) rootView.findViewById(R.id.tv_input_again);
            this.iv_piv = (ImageView) rootView.findViewById(R.id.iv_piv);
            this.iv_bottom = (ImageView) rootView.findViewById(R.id.iv_bottom);
            this.tv_invite_code = (TextView) rootView.findViewById(R.id.tv_invite_code);
            this.iv_zxing = (ImageView) rootView.findViewById(R.id.iv_zxing);
            this.iv_activity_rules = (ImageView) rootView.findViewById(R.id.iv_activity_rules);
            this.tv_share = (TextView) rootView.findViewById(R.id.tv_share);
            this.tv_switch = (TextView) rootView.findViewById(R.id.tv_switch);
        }

    }
}