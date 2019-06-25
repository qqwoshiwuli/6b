package com.fivefivelike.mybaselibrary.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.fivefivelike.mybaselibrary.mvp.view.IDelegate;

import me.yokeyword.fragmentation.SupportActivity;
import skin.support.annotation.Skinable;


/**
 * Created by 郭青枫 on 2017/7/3.
 */
@Skinable
public abstract class ActivityPresenter<T extends IDelegate> extends SupportActivity {
    protected T viewDelegate;
    private static final String TAG = "BaseActivity";

    public ActivityPresenter() {
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("create IDelegate error");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("create IDelegate error");
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDelegate.create(getLayoutInflater(), null, savedInstanceState);
        setContentView(viewDelegate.getRootView());
        viewDelegate.initView();
        intentHelper = new IntentHelper(this);
        bindEvenListenerBuyState(savedInstanceState);
        bindEvenListener();
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (InstantiationException e) {
                throw new RuntimeException("create IDelegate error");
            } catch (IllegalAccessException e) {
                throw new RuntimeException("create IDelegate error");
            }
        }
    }

    private IntentHelper intentHelper;

    public IntentHelper gotoActivity(Class activity) {
        intentHelper.gotoActivity(activity);
        return intentHelper;
    }


    @Override
    protected void onDestroy() {
        intentHelper.clean();
        super.onDestroy();
        viewDelegate = null;
    }

    protected void bindEvenListener() {
    }

    protected void bindEvenListenerBuyState(Bundle savedInstanceState) {
    }

    protected abstract Class<T> getDelegateClass();

}
