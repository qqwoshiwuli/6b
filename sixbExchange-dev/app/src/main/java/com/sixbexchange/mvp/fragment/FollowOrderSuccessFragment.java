package com.sixbexchange.mvp.fragment;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.FollowOrderSuccessBinder;
import com.sixbexchange.mvp.delegate.FollowOrderSuccessDelegate;
/*
*跟投成功页面
* @author gqf
* @Description
* @Date  2019/4/3 0003 13:53
* @Param
* @return
**/
public class FollowOrderSuccessFragment extends BaseDataBindFragment<FollowOrderSuccessDelegate, FollowOrderSuccessBinder> {

    @Override
    protected Class<FollowOrderSuccessDelegate> getDelegateClass() {
        return FollowOrderSuccessDelegate.class;
    }

    @Override
    public FollowOrderSuccessBinder getDataBinder(FollowOrderSuccessDelegate viewDelegate) {
        return new FollowOrderSuccessBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle("投资确认"));

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
