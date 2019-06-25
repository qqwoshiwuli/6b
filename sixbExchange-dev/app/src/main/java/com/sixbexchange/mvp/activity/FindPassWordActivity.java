package com.sixbexchange.mvp.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.sixbexchange.mvp.databinder.FindPassWordBinder;
import com.sixbexchange.mvp.delegate.FindPassWordDelegate;

public class FindPassWordActivity extends BaseDataBindActivity<FindPassWordDelegate, FindPassWordBinder> {

    @Override
    protected Class<FindPassWordDelegate> getDelegateClass() {
        return FindPassWordDelegate.class;
    }

    @Override
    public FindPassWordBinder getDataBinder(FindPassWordDelegate viewDelegate) {
        return new FindPassWordBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("找回密码"));
        viewDelegate.viewHolder.tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_input1.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_input2.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                SetNewPasswordActivity.startAct(
                        viewDelegate.getActivity(),
                        viewDelegate.viewHolder.et_input1.getText().toString(),
                        viewDelegate.viewHolder.et_input2.getText().toString(),
                        0x123
                );
            }
        });
        viewDelegate.viewHolder.tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_input1.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                addRequest(binder.preReset(viewDelegate.viewHolder.et_input1.getText().toString(), FindPassWordActivity.this));
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            if (requestCode == 0x123) {
                finish();
            }
        }
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                addRequest(UiHeplUtils.getCode(viewDelegate.viewHolder.tv_get_code, 60));
                break;
        }
    }

}
