package com.fivefivelike.mybaselibrary.mvp.presenter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;

/**
 * Created by 郭青枫 on 2017/10/24.
 */

public class IntentHelper {

    Activity context;

    Intent intent;

    Class activity;

    boolean isFinish = false;

    protected IntentHelper(Activity context) {
        this.context = context;
    }

    protected void clean() {
        context = null;
        intent = null;
        activity = null;
    }

    protected IntentHelper gotoActivity(Class activity) {
        this.activity = activity;
        return this;
    }

    public IntentHelper setIntent(Intent i) {
        intent = i;

        intent.setClass(context, activity);

        return this;
    }

    public IntentHelper setIsFinish(boolean isFinish) {
        this.isFinish = isFinish;
        return this;
    }

    public void startAct() {
        if (intent == null) {
            intent = new Intent(context, activity);
        }
        context.startActivity(intent);
        if (isFinish) {
            context.finish();
        }
        intent = null;
    }

    public void startActResult(int code) {
        if (intent == null) {
            intent = new Intent(context, activity);
        }
        context.startActivityForResult(intent, code);
        intent = null;
    }

    public void fragStartActResult(Fragment fragment, int code) {
        if (intent == null) {
            intent = new Intent(context, activity);
        }
        fragment.startActivityForResult(intent, code);
        intent = null;
    }
}
