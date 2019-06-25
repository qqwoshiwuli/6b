package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.UserOrderDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class UserOrderBinder extends BaseDataBind<UserOrderDelegate> {

    public UserOrderBinder(UserOrderDelegate viewDelegate) {
        super(viewDelegate);
    }


}