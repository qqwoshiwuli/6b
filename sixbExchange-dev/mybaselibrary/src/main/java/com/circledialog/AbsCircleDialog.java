package com.circledialog;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.circledialog.params.CircleParams;
import com.circledialog.params.DialogParams;

/**
 * Created by hupei on 2017/3/29.
 */

public final class AbsCircleDialog extends BaseCircleDialog {
    private static final String SAVED_PARAMS = "circle:params";
    private CircleParams mParams;
    private Controller mController;
    CircleDialog.CircleDialogLinsener circleDialogLinsener;

    public void setCircleDialogLinsener(CircleDialog.CircleDialogLinsener circleDialogLinsener) {
        this.circleDialogLinsener = circleDialogLinsener;
    }

    public static AbsCircleDialog newAbsCircleDialog(CircleParams params) {
        AbsCircleDialog circleDialog = new AbsCircleDialog();
        circleDialog.mParams = params;
        Bundle bundle = new Bundle();
        bundle.putParcelable(SAVED_PARAMS, params);
        circleDialog.setArguments(bundle);
        return circleDialog;
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        if (circleDialogLinsener != null) {
            circleDialogLinsener.onCancel(dialog);
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        mParams = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mParams = savedInstanceState.getParcelable(SAVED_PARAMS);
        }
        if (mParams != null) {
            DialogParams dialogParams = mParams.dialogParams;
            setGravity(dialogParams.gravity);
            setCanceledOnTouchOutside(dialogParams.canceledOnTouchOutside);
            setCanceledBack(dialogParams.cancelable);
            setWidth(dialogParams.width);
            int[] mPadding = dialogParams.mPadding;
            if (mPadding != null)
                setPadding(mPadding[0], mPadding[1], mPadding[2], mPadding[3]);
            setAnimations(dialogParams.animStyle);
            setDimEnabled(dialogParams.isDimEnabled);
            setBackgroundColor(dialogParams.backgroundColor);
            setRadius(dialogParams.radius);
            setAlpha(dialogParams.alpha);
            setX(dialogParams.xOff);
            setY(dialogParams.yOff);
        } else {
            dismiss();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(SAVED_PARAMS, mParams);
    }

    @Override
    public View createView(Context context, LayoutInflater inflater, ViewGroup container) {
        mController = new Controller(getContext(), mParams);
        mParams.dialogFragment = this;
        return mController.createView();
    }

    public void refreshView() {
        mController.refreshView();
    }
}
