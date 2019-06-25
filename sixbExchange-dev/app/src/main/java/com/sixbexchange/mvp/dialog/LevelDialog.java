package com.sixbexchange.mvp.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.dialog.BaseDialog;
import com.sixbexchange.R;
import com.wheelview.OnWheelChangedListener;
import com.wheelview.StringWheelAdapter;
import com.wheelview.Wheel3DView;
import com.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2019/1/15 0015.
 */

public class LevelDialog extends BaseDialog implements OnWheelChangedListener {


    private TextView tv_cancel;
    private TextView tv_ok;
    private Wheel3DView month;
    DefaultClickLinsener defaultClickLinsener;

    public LevelDialog(Activity context, DefaultClickLinsener defaultClickLinsener) {
        super(context);
        this.defaultClickLinsener = defaultClickLinsener;
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_modify_leverage;
    }


    String[] stringArray;

    @Override
    protected void startInit() {
        getWindow().setGravity(Gravity.BOTTOM);
        setWindowNoPadding();
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_ok = findViewById(R.id.tv_ok);
        month = findViewById(R.id.month);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        stringArray = CommonUtils.getStringArray(R.array.sa_select_level);
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, leverage);
                dismiss();
            }
        });
    }

    public void showDilaog(String leverage) {
        this.leverage = leverage;
        position = 0;
        List<String> showPosition = new ArrayList<>();
        for (int i = 0; i < stringArray.length; i++) {
            showPosition.add(stringArray[i]);
            if (ObjectUtils.equals(showPosition.get(i), leverage + "x")) {
                this.leverage = leverage + "x";
                position = i;
            }
            //            if (StringUtils.equalsIgnoreCase(leverMax + "X", stringArray[i])) {
            //                break;
            //            }
        }
        month.setAdapter(new StringWheelAdapter(showPosition));
        month.setOnWheelChangedListener(this);
        month.setCurrentIndex(position);
        show();
    }

    int position = 0;
    String leverage;

    public String getLeverage() {
        return leverage;
    }

    @Override
    public void onChanged(WheelView view, int oldIndex, int newIndex) {
        position = newIndex;
        leverage = month.getAdapter().getItem(newIndex);
    }
}
