package com.fivefivelike.mybaselibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fivefivelike.mybaselibrary.R;
import com.fivefivelike.mybaselibrary.utils.ToastUtil;

import java.util.HashMap;
import java.util.Map;


public abstract class BaseDialog extends Dialog implements View.OnClickListener {
    protected Context mContext;
    protected Map<String, String> baseMap;

    public BaseDialog(Context context) {
        this(context, R.style.baseDialog);
    }

    public BaseDialog(Context context, int style) {
        super(context, style);
        this.mContext = context;
        setContentView(getLayout());
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        startInit();
    }

    protected abstract int getLayout();

    protected abstract void startInit();

    @SuppressWarnings("unchecked")
    protected <E extends View> E getView(int viewId) {
        return (E) (findViewById(viewId));
    }

    /**
     * 去除边距
     */
    protected void setWindowNoPadding() {
        setWindowPadding(0, 0, 0, 0);
    }

    public void setWindowPadding(int left, int top, int right, int bottom) {
        Window win = getWindow();
        win.getDecorView().setPadding(left, top, right, bottom);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        win.setAttributes(lp);
    }

    /**
     * 弹出的toast
     */
    public void toast(String content) {
        ToastUtil.show( content);
    }

    /**
     * 获取map
     *
     * @return
     */
    protected Map getBaseMap() {
        baseMap = new HashMap<>();
        return baseMap;
    }

    @Override
    public void onClick(View v) {

    }
}
