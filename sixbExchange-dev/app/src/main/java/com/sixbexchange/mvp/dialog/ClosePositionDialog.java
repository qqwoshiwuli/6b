package com.sixbexchange.mvp.dialog;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.SoftKeyboardUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.RoundButton;
import com.fivefivelike.mybaselibrary.view.dialog.BaseDialog;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.HoldPositionBean;

import java.math.BigDecimal;

/**
 * Created by 郭青枫 on 2019/1/15 0015.
 */

public class ClosePositionDialog extends BaseDialog {

    private EditText et_price;
    private EditText et_num;
    private RoundButton tv_close_all;
    private RoundButton tv_close;

    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_price_unit;
    private TextView tv_num_unit;

    public ClosePositionDialog(Activity context, DefaultClickLinsener defaultClickLinsener) {
        super(context);
        this.defaultClickLinsener = defaultClickLinsener;
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_close_position;
    }

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.BOTTOM);
        setWindowNoPadding();
        et_price = (EditText) findViewById(R.id.et_price);
        et_num = (EditText) findViewById(R.id.et_num);
        tv_close_all = (RoundButton) findViewById(R.id.tv_close_all);
        tv_close = (RoundButton) findViewById(R.id.tv_close);
        tv_price_unit = (TextView) findViewById(R.id.tv_price_unit);
        tv_num_unit = (TextView) findViewById(R.id.tv_num_unit);

        tv_close_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, 0, null);
                dismiss();
            }
        });
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(et_price.getText().toString())) {
                    ToastUtil.show("请输入价格");
                    return;
                }
                if (TextUtils.isEmpty(et_num.getText().toString())) {
                    ToastUtil.show("请输入数量");
                    return;
                }
                defaultClickLinsener.onClick(v, 1,
                        et_price.getText().toString() + "/" +
                                et_num.getText().toString()
                );
                dismiss();
            }
        });

    }

    HoldPositionBean t;

    public void showDialog(HoldPositionBean trPositionBean) {
        t = trPositionBean;
        show();
        tv_price_unit.setText(t.getDetail().getPriceUnit());
        tv_num_unit.setText(t.getDetail().getAmountUnit());
        et_price.setText(t.getCurrentPrice());
        et_num.setText(t.getAvailable().replace("-",""));
        et_price.postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftKeyboardUtil.showSoftInputFromWindow(et_price);
                et_price.setText(new BigDecimal(t.getCurrentPrice()).toPlainString());
                et_price.setSelection(
                        et_price.getText().toString().length());
            }
        }, 50);
    }

    @Override
    public void cancel() {
        super.cancel();
        defaultClickLinsener.onClick(null, 2, null);
        et_price.postDelayed(new Runnable() {
            @Override
            public void run() {
                SoftKeyboardUtil.hideSoftKeyboard(mContext, et_price, et_num);
                et_price.setText("");
                et_num.setText("");
            }
        }, 50);
    }
}
