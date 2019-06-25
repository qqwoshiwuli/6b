package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.FontTextview;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.sixbexchange.R;

public class FollowOrderDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_follow_order;
    }


    public static class ViewHolder {
        public View rootView;

        public TextView tv_progress;
        public View view_start;
        public View view_end;
        public FontTextview tv_min;
        public FontTextview tv_lass;
        public FontTextview tv_have;
        public TextView tv_content;
        public ImageView iv_reduce;
        public EditText tv_num;
        public ImageView iv_add;
        public TextView tv_follow_num;
        public ImageView iv_check;
        public TextView tv_protocol;
        public RoundButton tv_commit;
        public TextView tv_subtract_wallet;
        public TextView tv_toast;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tv_progress = (TextView) rootView.findViewById(R.id.tv_progress);
            this.view_start = (View) rootView.findViewById(R.id.view_start);
            this.view_end = (View) rootView.findViewById(R.id.view_end);
            this.tv_min = (FontTextview) rootView.findViewById(R.id.tv_min);
            this.tv_lass = (FontTextview) rootView.findViewById(R.id.tv_lass);
            this.tv_have = (FontTextview) rootView.findViewById(R.id.tv_have);
            this.tv_content = (TextView) rootView.findViewById(R.id.tv_content);
            this.iv_reduce = (ImageView) rootView.findViewById(R.id.iv_reduce);
            this.tv_num = (EditText) rootView.findViewById(R.id.tv_num);
            this.iv_add = (ImageView) rootView.findViewById(R.id.iv_add);
            this.tv_follow_num = (TextView) rootView.findViewById(R.id.tv_follow_num);
            this.iv_check = (ImageView) rootView.findViewById(R.id.iv_check);
            this.tv_protocol = (TextView) rootView.findViewById(R.id.tv_protocol);
            this.tv_commit = (RoundButton) rootView.findViewById(R.id.tv_commit);
            this.tv_subtract_wallet = (TextView) rootView.findViewById(R.id.tv_subtract_wallet);
            this.tv_toast = (TextView) rootView.findViewById(R.id.tv_toast);
        }

    }
}