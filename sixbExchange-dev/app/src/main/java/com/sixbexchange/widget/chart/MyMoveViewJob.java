package com.sixbexchange.widget.chart;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;

import com.fivefivelike.mybaselibrary.view.dialog.NetWorkDialog;
import com.github.mikephil.charting.jobs.AnimatedViewPortJob;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by 郭青枫 on 2018/7/22 0022.
 */

public class MyMoveViewJob extends AnimatedViewPortJob {
    NetWorkDialog netWorkDialog;
    float endIndex;

    public void setEndIndex(float endIndex) {
        this.endIndex = endIndex;
    }

    public void setNetWorkDialog(NetWorkDialog netWorkDialog) {
        this.netWorkDialog = netWorkDialog;
    }

    public MyMoveViewJob(ViewPortHandler viewPortHandler, float xValue, float yValue, Transformer trans, View v, float xOrigin, float yOrigin, long duration) {
        super(viewPortHandler, xValue, yValue, trans, v, xOrigin, yOrigin, duration);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        pts[0] = xOrigin + (xValue - xOrigin) * phase;
        pts[1] = yOrigin + (yValue - yOrigin) * phase;
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (netWorkDialog != null) {
                    netWorkDialog.dimessDialog(true);
                }
            }
        });
        if (endIndex == pts[0]) {

        }

        mTrans.pointValuesToPixel(pts);
        mViewPortHandler.centerViewPort(pts, view);


    }
}
