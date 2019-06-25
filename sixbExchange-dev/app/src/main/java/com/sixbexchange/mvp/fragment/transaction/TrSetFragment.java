package com.sixbexchange.mvp.fragment.transaction;

import com.fivefivelike.mybaselibrary.base.BaseDataBindFragment;
import com.fivefivelike.mybaselibrary.entity.ToolbarBuilder;
import com.sixbexchange.mvp.databinder.TrSetBinder;
import com.sixbexchange.mvp.delegate.TrSetDelegate;

public class TrSetFragment extends BaseDataBindFragment<TrSetDelegate, TrSetBinder> {

    @Override
    protected Class<TrSetDelegate> getDelegateClass() {
        return TrSetDelegate.class;
    }

    @Override
    public TrSetBinder getDataBinder(TrSetDelegate viewDelegate) {
        return new TrSetBinder(viewDelegate);
    }


    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        initToolbar(new ToolbarBuilder().setTitle(""));

    }


    @Override
    protected void onServiceSuccess(String data, String info, int status, int requestCode) {
        switch (requestCode) {
        }
    }

}
