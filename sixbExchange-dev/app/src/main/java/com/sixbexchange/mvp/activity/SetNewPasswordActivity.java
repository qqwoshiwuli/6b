package com.sixbexchange.mvp.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindActivity;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.sixbexchange.mvp.databinder.SetNewPasswordBinder;
import com.sixbexchange.mvp.delegate.SetNewPasswordDelegate;

public class SetNewPasswordActivity extends BaseDataBindActivity<SetNewPasswordDelegate, SetNewPasswordBinder> {

    @Override
    protected Class<SetNewPasswordDelegate> getDelegateClass() {
        return SetNewPasswordDelegate.class;
    }

    @Override
    public SetNewPasswordBinder getDataBinder(SetNewPasswordDelegate viewDelegate) {
        return new SetNewPasswordBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        getIntentData();
        initToolbar(new ToolbarBuilder().setTitle("重置密码"));
        viewDelegate.viewHolder.tv_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_input1.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_input2.getText().toString())) {
                    ToastUtil.show("请确认密码");
                    return;
                }
                if (!viewDelegate.viewHolder.et_input1.getText().toString().equals(viewDelegate.viewHolder.et_input2.getText().toString())) {
                    ToastUtil.show("两次输入的密码不一致");
                    return;
                }
                addRequest(binder.setpassword(
                        phone,
                        selectCode,
                        viewDelegate.viewHolder.et_input1.getText().toString(),
                        viewDelegate.viewHolder.et_input2.getText().toString(),
                        SetNewPasswordActivity.this
                ));
            }
        });
    }

    public static void startAct(Activity activity,
                                String selectCode,
                                String phone,
                                int requestCode) {
        Intent intent = new Intent(activity, SetNewPasswordActivity.class);
        intent.putExtra("selectCode", selectCode);
        intent.putExtra("phone", phone);
        activity.startActivityForResult(intent, requestCode);
    }

    private String phone;
    private String selectCode;

    private void getIntentData() {
        Intent intent = getIntent();
        phone = intent.getStringExtra("phone");
        selectCode = intent.getStringExtra("selectCode");
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                setResult(RESULT_OK);
                finish();
                break;
        }
    }

}
