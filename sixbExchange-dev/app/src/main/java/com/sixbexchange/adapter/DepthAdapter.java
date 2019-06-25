package com.sixbexchange.adapter;

import android.content.Context;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.DepthBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/7/16 0016.
 */

public class DepthAdapter extends BaseAdapter<DepthBean> {


    private TextView tv_price;
    private TextView tv_num;
    boolean isBuy;

    int getRiseColor;
    int getDropColor;

    //    private View view_left;
    //    private View view_right;

    private int priceSize = 2;
    private int numSize = 2;
    String symbol;
    boolean isOkex = false;

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }


    public void setOkex(boolean okex) {
        isOkex = okex;
    }

    public void setPriceSize(int priceSize) {
        this.priceSize = priceSize;
    }

    public void setNumSize(int numSize) {
        this.numSize = numSize;
    }

    public int getNumSize() {
        return numSize;
    }

    boolean isHaveData = false;

    public boolean isHaveData() {
        return isHaveData;
    }

    public DepthAdapter(Context context, List<DepthBean> datas, boolean isBuy) {
        super(context, R.layout.adapter_depth, datas);
        this.isBuy = isBuy;
        getRiseColor = CommonUtils.getColor(UserSet.getinstance().getRiseColor());
        getDropColor = CommonUtils.getColor(UserSet.getinstance().getDropColor());
    }


    @Override
    protected void convert(ViewHolder holder, DepthBean s, final int position) {
        tv_price = holder.getView(R.id.tv_price);
        tv_num = holder.getView(R.id.tv_num);
        //        view_left = holder.getView(R.id.view_left);
        //        view_right = holder.getView(R.id.view_right);
        if (isBuy) {
            tv_price.setTextColor(getRiseColor);
            //view_right.setBackgroundColor(getRiseColor);
        } else {
            tv_price.setTextColor(getDropColor);
            //view_right.setBackgroundColor(getDropColor);
        }
        //view_right.setAlpha(0.15f);


        if (ObjectUtils.equals("--", s.getPrice())) {
            tv_price.setText(s.getPrice());
        } else {
            tv_price.setText(new BigDecimal(s.getPrice()).setScale(priceSize,
                    RoundingMode.DOWN).toPlainString());
        }
        if (ObjectUtils.equals("--", s.getVolume())) {
            tv_num.setText(s.getVolume());
            //view_right.setVisibility(View.GONE);
            isHaveData = false;
        } else {
            //view_right.setVisibility(View.VISIBLE);
            isHaveData = true;
            BigDecimal bigDecimal = new BigDecimal(
                    isOkex ?
                            BigUIUtil.getinstance().bigLeafToCoin(s.getVolume(), s.getPrice(), symbol) :
                            s.getVolume());
            if (new BigDecimal(s.getVolume()).doubleValue() > 1000) {
                tv_num.setText(BigUIUtil.getinstance().bigEnglishNum(
                        bigDecimal
                                .divide(new BigDecimal("1000"),
                                        8, RoundingMode.DOWN).toPlainString(), 2) + "K");
            } else {
                tv_num.setText(BigUIUtil.getinstance().bigEnglishNum(s.getVolume(), numSize));
            }

            //            LinearLayout.LayoutParams layoutLeft = (LinearLayout.LayoutParams)
            //                    view_left.getLayoutParams();
            //            layoutLeft.weight = new BigDecimal(max).floatValue() - bigDecimal.floatValue();
            //            view_left.setLayoutParams(layoutLeft);
            //
            //            LinearLayout.LayoutParams layoutRight = (LinearLayout.LayoutParams)
            //                    view_right.getLayoutParams();
            //            layoutRight.weight = bigDecimal.floatValue();
            //            view_right.setLayoutParams(layoutRight);


        }

    }
}