package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.FundPasswordBinder;
import com.sixbexchange.mvp.delegate.FundPasswordDelegate;

/*
*设置资金密码
* @author gqf
* @Description
* @Date  2019/4/3 0003 14:01
* @Param
* @return
**/
public class FundPasswordFragment extends BaseDataBindFragment<FundPasswordDelegate, FundPasswordBinder> {

    @Override
    protected Class<FundPasswordDelegate> getDelegateClass() {
        return FundPasswordDelegate.class;
    }

    @Override
    public FundPasswordBinder getDataBinder(FundPasswordDelegate viewDelegate) {
        return new FundPasswordBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("设置资金密码"));

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
