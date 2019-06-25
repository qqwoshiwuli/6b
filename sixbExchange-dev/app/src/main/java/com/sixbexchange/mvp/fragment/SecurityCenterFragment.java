package com.sixbexchange.mvp.fragment;

import android.view.View;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.SecurityCenterBinder;
import com.sixbexchange.mvp.delegate.SecurityCenterDelegate;

/*
*安全中心
* @author gqf
* @Description
* @Date  2019/4/3 0003 13:36
* @Param
* @return
**/
public class SecurityCenterFragment extends BaseDataBindFragment<SecurityCenterDelegate, SecurityCenterBinder> {

    @Override
    protected Class<SecurityCenterDelegate> getDelegateClass() {
        return SecurityCenterDelegate.class;
    }

    @Override
    public SecurityCenterBinder getDataBinder(SecurityCenterDelegate viewDelegate) {
        return new SecurityCenterBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("安全中心"));
        viewDelegate.viewHolder.lin1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start(new FundPasswordFragment());
            }
        });
    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
