package com.sixbexchange.mvp.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.SoftKeyboardUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.dialog.BaseDialog;
import com.sixbexchange.R;
import com.tablayout.CommonTabLayout;
import com.tablayout.TabEntity;
import com.tablayout.listener.CustomTabEntity;
import com.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

/**
 * Created by 郭青枫 on 2019/1/15 0015.
 */

public class ChangeMArginDialog extends BaseDialog {


    DefaultClickLinsener defaultClickLinsener;
    private CommonTabLayout tl_2;
    private TextView tv_unit;
    private EditText et_num;
    private TextView tv_toast;
    private RoundButton tv_commit;
    boolean isAdd = true;

    public boolean isAdd() {
        return isAdd;
    }

    public ChangeMArginDialog(Activity context, DefaultClickLinsener defaultClickLinsener) {
        super(context);
        this.defaultClickLinsener = defaultClickLinsener;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_change_margin;
    }

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    String[] stringArray;

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.BOTTOM);
        setWindowNoPadding();
        tl_2 = (CommonTabLayout) findViewById(R.id.tl_2);
        et_num = (EditText) findViewById(R.id.et_num);
        tv_unit = (TextView) findViewById(R.id.tv_unit);
        tv_toast = (TextView) findViewById(R.id.tv_toast);
        tv_commit = (RoundButton) findViewById(R.id.tv_commit);


        stringArray = CommonUtils.getStringArray(R.array.sa_select_change_margin_title);
        mTabEntities = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            mTabEntities.add(new TabEntity(stringArray[i], 0, 0));
        }
        tv_commit.setText(stringArray[0]);
        tl_2.setTabData(mTabEntities);
        tl_2.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                tv_commit.setText(stringArray[position]);
                isAdd = position == 0;
            }

            @Override
            public void onTabReselect(int position) {

            }
        });

        tv_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, 0, et_num.getText().toString());
                et_num.setText("");
                dismiss();
            }
        });

    }

    public void showDialog(String unit) {
        show();
        tv_unit.setText("保证金" + "(" + unit + ")");
        et_num.postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftKeyboardUtil.showSoftInputFromWindow(et_num);
            }
        }, 50);
    }

    @Override
    public void cancel() {
        super.cancel();
        defaultClickLinsener.onClick(null, 2, null);
        et_num.postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftKeyboardUtil.hideSoftKeyboard(mContext, et_num);
            }
        }, 50);
    }
}
