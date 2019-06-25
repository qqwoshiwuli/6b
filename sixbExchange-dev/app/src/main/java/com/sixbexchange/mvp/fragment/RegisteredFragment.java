package com.sixbexchange.mvp.fragment;

import android.text.TextUtils;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.fivefivelike.mybaselibrary.utils.UiHeplUtils;
import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppInstallAdapter;
import com.fm.openinstall.model.AppData;
import com.sixbexchange.mvp.activity.LoginAndRegisteredActivity;
import com.sixbexchange.mvp.databinder.RegisteredBinder;
import com.sixbexchange.mvp.delegate.RegisteredDelegate;

public class RegisteredFragment extends BaseDataBindFragment<RegisteredDelegate, RegisteredBinder> {

    @Override
    protected Class<RegisteredDelegate> getDelegateClass() {
        return RegisteredDelegate.class;
    }

    @Override
    public RegisteredBinder getDataBinder(RegisteredDelegate viewDelegate) {
        return new RegisteredBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        OpenInstall.getInstall(new AppInstallAdapter() {
            @Override
            public void onInstall(AppData appData) {
                //获取渠道数据
                String channelCode = appData.getChannel();
                //获取自定义数据
                String bindData = appData.getData();
                //ToastUtil.show(bindData);
                viewDelegate.viewHolder.et_invite.setText(
                        GsonUtil.getInstance().getValue(bindData, "intiviteCode")
                );
            }
        });
        viewDelegate.viewHolder.tv_registered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_phone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_code.getText().toString())) {
                    ToastUtil.show("请输入验证码");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_pass.getText().toString())) {
                    ToastUtil.show("请设置密码");
                    return;
                }
                if (viewDelegate.viewHolder.et_pass.getText().toString().length() < 6) {
                    ToastUtil.show("密码长度不能小于6位");
                    return;
                }
                addRequest(binder.signup(
                        viewDelegate.viewHolder.et_phone.getText().toString(),
                        viewDelegate.viewHolder.et_pass.getText().toString(),
                        viewDelegate.viewHolder.et_code.getText().toString(),
                        viewDelegate.viewHolder.et_invite.getText().toString(),
                        RegisteredFragment.this
                ));
            }
        });
        viewDelegate.viewHolder.tv_get_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_phone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                getCode();
            }
        });
    }

    private void getCode() {
        addRequest(binder.vcode(
                viewDelegate.viewHolder.et_phone.getText().toString(),
                this
        ));
    }

    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                if (getActivity() instanceof LoginAndRegisteredActivity) {
                    ((LoginAndRegisteredActivity) getActivity()).setCurrentView(0);
                }
                break;
            case 0x124:
                addRequest(UiHeplUtils.getCode(viewDelegate.viewHolder.tv_get_code, 60));
                break;
        }
    }

}
