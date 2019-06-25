package com.sixbexchange.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.StringUtil;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.HuobiOrListBean;
import com.sixbexchange.entity.bean.selectExchhuobiBeans;
import com.sixbexchange.utils.DateUtils;

import java.util.List;

public class HuobiorderhistAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<HuobiOrListBean> huobiOrListBean;
    String symbol1,symbol2;
    int PricePrecision;
    Context context;
    public HuobiorderhistAdapter(Context context,List<HuobiOrListBean> huobiOrListBean,String symbol1,String symbol2,int PricePrecision) {
        super();
        this.context=context;
        mInflater = LayoutInflater.from(context);
        this.huobiOrListBean=huobiOrListBean;
        this.symbol1=symbol1;
        this.symbol2=symbol2;
        this.PricePrecision=PricePrecision;
    }

    @Override
    public int getCount() {
        return huobiOrListBean.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
       ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.adapter_huobi_orderlist, null);
            viewHolder.createdAt =  convertView.findViewById(R.id.createdAt);
            viewHolder.fieldAmount =  convertView.findViewById(R.id.fieldAmount);
            viewHolder.fieldCashAmount =  convertView.findViewById(R.id.fieldCashAmount);
            viewHolder.symbol =  convertView.findViewById(R.id.symbol);
            viewHolder.tv_type =  convertView.findViewById(R.id.tv_type);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.createdAt.setText(DateUtils.format_yyyy_MM_dd_HH_mm_ss(huobiOrListBean.get(i).getCreatedAt()));
        viewHolder.fieldAmount.setText(huobiOrListBean.get(i).getFieldAmount()+" "+symbol1);
        double price=huobiOrListBean.get(i).getFieldCashAmount()/huobiOrListBean.get(i).getFieldAmount();
        viewHolder.fieldCashAmount.setText(StringUtil.getDecimal(PricePrecision,price)+"");
        viewHolder.symbol.setText(symbol1+"/"+symbol2);
        if (huobiOrListBean.get(i).getType().equals("1"))
        {
            viewHolder.tv_type.setText("买入");
            viewHolder.tv_type.setBackgroundColor(ContextCompat.getColor(context,R.color.decreasing_color));
        }else
        {
            viewHolder.tv_type.setText("卖出");
            viewHolder.tv_type.setBackgroundColor(ContextCompat.getColor(context,R.color.color_E7625D));
        }
        return convertView;
    }

    public  class ViewHolder
    {
        public TextView createdAt,fieldAmount,fieldCashAmount,symbol,tv_type;
    }
}
