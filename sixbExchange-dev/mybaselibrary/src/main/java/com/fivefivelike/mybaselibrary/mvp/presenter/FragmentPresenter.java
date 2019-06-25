package com.fivefivelike.mybaselibrary.mvp.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.mvp.view.IDelegate;

import me.yokeyword.fragmentation.SupportFragment;


/**
 * Created by 郭青枫 on 2017/7/3.
 */

public abstract class FragmentPresenter<T extends IDelegate> extends SupportFragment {
    public T viewDelegate;
    private IntentHelper intentHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intentHelper = new IntentHelper(getActivity());
        try {
            viewDelegate = getDelegateClass().newInstance();
        } catch (java.lang.InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public IntentHelper gotoActivity(Class activity) {
        intentHelper.gotoActivity(activity);
        return intentHelper;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        viewDelegate.create(inflater, container, savedInstanceState);
        viewDelegate.initView();
        bindEvenListenerBuyState(savedInstanceState);
        bindEvenListener();
        return viewDelegate.getRootView();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (viewDelegate == null) {
            try {
                viewDelegate = getDelegateClass().newInstance();
            } catch (java.lang.InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        intentHelper.clean();
        super.onDestroy();
        viewDelegate = null;

    }
    protected void bindEvenListenerBuyState(Bundle savedInstanceState) {
    }
    protected void bindEvenListener() {
    }

    protected abstract Class<T> getDelegateClass();
}
