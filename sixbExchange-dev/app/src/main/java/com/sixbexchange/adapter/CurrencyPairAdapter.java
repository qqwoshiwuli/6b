package com.sixbexchange.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.utils.glide.GlideUtils;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/10 0010.
 */

public class CurrencyPairAdapter extends BaseAdapter<TradeDetailBean> {


    private ImageView iv_piv;
    private TextView tv_name;
    private TextView tv_price;
    private ImageView iv_select;
    private LinearLayout lin_root;

    String type;

    public void setType(String type) {
        this.type = type;
    }

    int selectIndex=0;

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }

    DefaultClickLinsener defaultClickLinsener;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public CurrencyPairAdapter(Context context, List<TradeDetailBean> datas) {
        super(context, R.layout.adapter_currency_pair, datas);

    }


    @Override
    protected void convert(ViewHolder holder, TradeDetailBean s, final int position) {
        tv_name = holder.getView(R.id.tv_name);
        iv_piv = holder.getView(R.id.iv_piv);
        tv_price = holder.getView(R.id.tv_price);
        iv_select = holder.getView(R.id.iv_select);
        lin_root = holder.getView(R.id.lin_root);

        tv_name.setText(s.getCurrencyPairName());

        GlideUtils.loadImage(s.getCurrencyIcon(), iv_piv);

        iv_select.setVisibility(ObjectUtils.equals(type, s.getCurrencyPair()) ? View.VISIBLE : View.INVISIBLE);
        lin_root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                defaultClickLinsener.onClick(v, position, selectIndex);
            }
        });

    }

}