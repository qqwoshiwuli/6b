package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.TrOrderDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class TrOrderBinder extends BaseDataBind<TrOrderDelegate> {

    public TrOrderBinder(TrOrderDelegate viewDelegate) {
        super(viewDelegate);
    }


}