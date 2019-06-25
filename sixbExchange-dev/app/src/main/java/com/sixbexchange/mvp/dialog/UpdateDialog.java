package com.sixbexchange.mvp.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.dialog.BaseDialog;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.AppVersion;

/**
 * Created by 郭青枫 on 2017/11/28 0028.
 */

public class UpdateDialog extends BaseDialog implements DialogInterface.OnCancelListener {


    private TextView tv_title;
    private TextView tv_content;
    private TextView tv_cannel;
    private TextView tv_confirm;
    private TextView tv_http;
    AppVersion appVersion;
    DefaultClickLinsener defaultClickLinsener;

    public UpdateDialog setAppVersion(AppVersion appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public UpdateDialog setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
        return this;
    }

    public UpdateDialog(Context context) {
        super(context);
    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_updata;
    }

    public UpdateDialog setCancelListener(@Nullable OnCancelListener listener) {
        setOnCancelListener(listener);
        return this;
    }

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.CENTER);
        setWindowNoPadding();
        setCancelable(true);
        setCanceledOnTouchOutside(false);
        tv_cannel = findViewById(R.id.tv_cannel);

        tv_confirm = findViewById(R.id.tv_confirm);
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_http = findViewById(R.id.tv_http);

        tv_cannel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消
                click(0);
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //确认
                click(1);
                dismiss();
            }
        });
        tv_http.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //官网下载
                click(2);
                dismiss();
                UiHeplUtils.startWeb(mContext, appVersion.getDownloadAddr());
            }
        });
        this.setOnCancelListener(this);

    }

    public void showDialog() {
        tv_title.setText(appVersion.getTitle());
        tv_content.setText(Html.fromHtml(appVersion.getContent()));
        if (appVersion.isMustUpdate()) {
            tv_cannel.setText(CommonUtils.getString(R.string.str_cancel));
        } else {
            tv_cannel.setText(CommonUtils.getString(R.string.str_no_again_updata));
        }
        this.show();
    }

    private void click(int posotion) {
        if (defaultClickLinsener != null) {
            defaultClickLinsener.onClick(null, posotion, appVersion);
        }
    }

    @Override
    public void onCancel(DialogInterface dialogInterface) {
        //取消
        click(0);
    }
}
