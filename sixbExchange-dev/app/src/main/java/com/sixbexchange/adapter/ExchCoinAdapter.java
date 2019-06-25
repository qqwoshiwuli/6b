package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/5/6 0006.
 */

public class ExchCoinAdapter extends BaseAdapter<TradeDetailBean> {


    private TextView tv_name;
    String type;
    DefaultClickLinsener defaultClickLinsener;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ExchCoinAdapter(Context context, List<TradeDetailBean> datas) {
        super(context, R.layout.adapter_exch_coin, datas);

    }


    @Override
    protected void convert(ViewHolder holder, TradeDetailBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);

        tv_name.setText(s.getCurrencyPairName());
        if (ObjectUtils.equals(type, s.getCurrencyPairName())) {
            tv_name.setTextColor(CommonUtils.getColor(R.color.white));
            tv_name.setBackground(new RadiuBg(CommonUtils.getColor(R.color.mark_color),
                    (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                    (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                    (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                    (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px)));
        } else {
            tv_name.setTextColor(CommonUtils.getColor(R.color.color_font3));
            tv_name.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
        }
        tv_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v instanceof TextView) {
                    ((TextView)v).setTextColor(CommonUtils.getColor(R.color.white));
                    v.setBackground(new RadiuBg(CommonUtils.getColor(R.color.mark_color),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px)));
                }
                defaultClickLinsener.onClick(v, position, null);
            }
        });
    }

}
