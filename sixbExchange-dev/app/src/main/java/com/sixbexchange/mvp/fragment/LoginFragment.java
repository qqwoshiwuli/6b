package com.sixbexchange.mvp.fragment;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.utils.GsonUtil;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;
import com.sixbexchange.entity.bean.UserLoginInfo;
import com.sixbexchange.mvp.activity.FindPassWordActivity;
import com.sixbexchange.mvp.activity.HomeActivity;
import com.sixbexchange.mvp.databinder.LoginBinder;
import com.sixbexchange.mvp.delegate.LoginDelegate;
import com.sixbexchange.server.HttpUrl;

public class LoginFragment extends BaseDataBindFragment<LoginDelegate, LoginBinder> {

    @Override
    protected Class<LoginDelegate> getDelegateClass() {
        return LoginDelegate.class;
    }

    @Override
    public LoginBinder getDataBinder(LoginDelegate viewDelegate) {
        return new LoginBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.viewHolder.tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_phone.getText().toString())) {
                    ToastUtil.show("请输入手机号");
                    return;
                }
                if (TextUtils.isEmpty(viewDelegate.viewHolder.et_pass.getText().toString())) {
                    ToastUtil.show("请输入密码");
                    return;
                }
                addRequest(binder.login(
                        viewDelegate.viewHolder.et_phone.getText().toString(),
                        viewDelegate.viewHolder.et_pass.getText().toString(),
                        LoginFragment.this
                ));
            }
        });
        viewDelegate.viewHolder.tv_help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(viewDelegate.getActivity(), FindPassWordActivity.class));
            }
        });
//        if (BuildConfig.isLog) {
//            viewDelegate.viewHolder.et_phone.setText("18936133001");
//            viewDelegate.viewHolder.et_pass.setText("1995629zrz");
//        }
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
            case 0x123:
                HttpUrl.getIntance().setToken(data);
                addRequest(binder.userinfo(this));
                break;
            case 0x124:
                UserLoginInfo.addNewLoginInfo(GsonUtil.getInstance().toObj(data, UserLoginInfo.class));
                startActivity(new Intent(getActivity(),HomeActivity.class));
                getActivity().finish();
                break;
        }
    }

}
