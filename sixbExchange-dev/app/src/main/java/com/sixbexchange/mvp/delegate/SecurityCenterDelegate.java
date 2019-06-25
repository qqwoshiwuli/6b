package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.LinearLayout;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.sixbexchange.R;

public class SecurityCenterDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_security_center;
    }


    public static class ViewHolder {
        public View rootView;

        public LinearLayout lin1;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.lin1 = (LinearLayout) rootView.findViewById(R.id.lin1);
        }

    }
}