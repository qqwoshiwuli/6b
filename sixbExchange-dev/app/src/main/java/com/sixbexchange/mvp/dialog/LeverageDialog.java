package com.sixbexchange.mvp.dialog;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.dialog.BaseDialog;
import com.sixbexchange.R;
import com.wheelview.OnWheelChangedListener;
import com.wheelview.StringWheelAdapter;
import com.wheelview.Wheel3DView;
import com.wheelview.WheelView;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/1/15 0015.
 */

public class LeverageDialog extends BaseDialog implements OnWheelChangedListener {


    private TextView tv_cancel;
    private TextView tv_ok;
    private Wheel3DView month;
    DefaultClickLinsener defaultClickLinsener;

    public LeverageDialog(Activity context, DefaultClickLinsener defaultClickLinsener) {
        super(context);
        this.defaultClickLinsener = defaultClickLinsener;
    }

    @Override
    protected int getLayout() {
        return R.layout.layout_modify_leverage;
    }


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
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, leverage);
                dismiss();
            }
        });
    }


    StringWheelAdapter stringWheelAdapter;
    int size=6;

    public void setSize(int size) {
        this.size = size;
    }

    public void showDilaog(String coin, List<String> showPosition) {
        leverage = coin;
        position = 0;
        for (int i = 0; i < showPosition.size(); i++) {
            if (ObjectUtils.equals(showPosition.get(i), coin)) {
                position = i;
            }
        }
        stringWheelAdapter = new StringWheelAdapter(showPosition);
        stringWheelAdapter.setSize(size);
        month.setAdapter(stringWheelAdapter);
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
