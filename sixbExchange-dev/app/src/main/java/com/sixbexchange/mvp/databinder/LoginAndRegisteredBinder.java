package com.sixbexchange.mvp.databinder;

import com.sixbexchange.mvp.delegate.LoginAndRegisteredDelegate;
import com.fivefivelike.mybaselibrary.base.BaseDataBind;
import com.fivefivelike.mybaselibrary.http.HttpRequest;
import com.fivefivelike.mybaselibrary.http.RequestCallback;

import io.reactivex.disposables.Disposable;

public class LoginAndRegisteredBinder extends BaseDataBind<LoginAndRegisteredDelegate> {

    public LoginAndRegisteredBinder(LoginAndRegisteredDelegate viewDelegate) {
        super(viewDelegate);
    }


}