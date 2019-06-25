package com.fivefivelike.mybaselibrary.view.dialog;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.circledialog.AbsCircleDialog;

/**
 * Created by 郭青枫 on 2017/11/24.
 */

public class NetWorkDialog {

    DialogFragment dialogFragment;
    FragmentManager fragmentManager;

    public NetWorkDialog(DialogFragment dialogFragment, FragmentManager fragmentManager) {
        this.dialogFragment = dialogFragment;
        this.fragmentManager = fragmentManager;
    }

    public void showDialog(boolean mIsShowDialog) {
        try {
            if (mIsShowDialog && dialogFragment != null) {
                dialogFragment.show(fragmentManager, "NetWorkDialog");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setTitle(String title){
        if(dialogFragment instanceof AbsCircleDialog){

        }
    }

    public void dimessDialog(boolean mIsShowDialog) {
        try {
            if (mIsShowDialog && dialogFragment != null && dialogFragment.getDialog().isShowing()) {
                dialogFragment.dismiss();
            }
        } catch (Exception e) {
        }
    }

}
