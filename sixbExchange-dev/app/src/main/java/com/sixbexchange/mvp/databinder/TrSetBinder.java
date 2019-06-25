package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.TrSetDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class TrSetBinder extends BaseDataBind<TrSetDelegate> {

    public TrSetBinder(TrSetDelegate viewDelegate) {
        super(viewDelegate);
    }


}