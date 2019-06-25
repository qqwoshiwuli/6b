package com.sixbexchange.mvp.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.dialog.BaseDialog;
import com.sixbexchange.R;

/**
 * Created by 郭青枫 on 2019/1/15 0015.
 */

public class OpenBitmexDialog extends BaseDialog {


    DefaultClickLinsener defaultClickLinsener;
    private TextView tv_title;
    private TextView tv_close;
    private TextView tv_open;

    public OpenBitmexDialog(Activity context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_open_bitmex;
    }

    String title = "为避免无效的账号申请造成浪费，当前开通BitMEX需要消费0.001 BTC。 \n" +
            "开通说明 \n" +
            "1 通过6b交易Bitmex可以享受手续费，具体比例以个人中心说明为准。 \n" +
            "2 开通账号所消费的0.001 BTC，在您正常交易一周后会退回您的账户。 \n" +
            "3 BitMEX账号为独立账号，站内转账也是充提，不能瞬间到账。 所有转账将在10:00-24:00 处理，到账时间约20-120分钟。";

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.CENTER);
        setWindowNoPadding();
        tv_title = findViewById(R.id.tv_title);
        tv_close = findViewById(R.id.tv_close);
        tv_open = findViewById(R.id.tv_open);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (defaultClickLinsener != null) {
                    defaultClickLinsener.onClick(v, 0, null);
                }
                dismiss();
            }
        });
    }

    public void showDialog(DefaultClickLinsener d) {
        this.defaultClickLinsener = d;
        show();
        tv_title.setText(title);
    }

    @Override
    public void cancel() {
        super.cancel();
    }
}
