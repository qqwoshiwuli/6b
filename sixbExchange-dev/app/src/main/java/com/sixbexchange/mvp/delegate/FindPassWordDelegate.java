package com.sixbexchange.mvp.delegate;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.fivefivelike.mybaselibrary.view.materialedittext.MaterialEditText;
import com.sixbexchange.R;

public class FindPassWordDelegate extends BaseDelegate {
    public ViewHolder viewHolder;

    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_pass_word;
    }


    public static class ViewHolder {
        public View rootView;

        public TextView tv_input_label1;
        public IconFontTextview tv_area_code;
        public MaterialEditText et_input1;
        public TextView tv_get_code;
        public LinearLayout lin_input1;
        public TextView tv_input_label2;
        public MaterialEditText et_input2;
        public LinearLayout lin_input2;
        public TextView tv_next;
        public TextView tv_no_code;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tv_input_label1 = (TextView) rootView.findViewById(R.id.tv_input_label1);
            this.tv_area_code = (IconFontTextview) rootView.findViewById(R.id.tv_area_code);
            this.et_input1 = (MaterialEditText) rootView.findViewById(R.id.et_input1);
            this.tv_get_code = (TextView) rootView.findViewById(R.id.tv_get_code);
            this.lin_input1 = (LinearLayout) rootView.findViewById(R.id.lin_input1);
            this.tv_input_label2 = (TextView) rootView.findViewById(R.id.tv_input_label2);
            this.et_input2 = (MaterialEditText) rootView.findViewById(R.id.et_input2);
            this.lin_input2 = (LinearLayout) rootView.findViewById(R.id.lin_input2);
            this.tv_next = (TextView) rootView.findViewById(R.id.tv_next);
            this.tv_no_code = (TextView) rootView.findViewById(R.id.tv_no_code);
        }

    }
}