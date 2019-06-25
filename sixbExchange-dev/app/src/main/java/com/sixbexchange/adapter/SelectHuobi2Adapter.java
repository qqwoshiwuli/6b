package com.sixbexchange.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sixbexchange.R;
import com.sixbexchange.entity.bean.selectExchhuobiBeans;

import java.util.List;

public class SelectHuobi2Adapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<selectExchhuobiBeans> selectExchhuobi;
    public SelectHuobi2Adapter(Context context,List<selectExchhuobiBeans> selectExchhuobi) {
        super();
        mInflater = LayoutInflater.from(context);
        this.selectExchhuobi=selectExchhuobi;
    }

    @Override
    public int getCount() {
        return selectExchhuobi.get(temp).getData().size();
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
            convertView = mInflater.inflate(R.layout.adapter_select_huobi2, null);
            viewHolder.textView =  convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(selectExchhuobi.get(temp).getData().get(i).getBaseCurrency()+"/"+selectExchhuobi.get(temp).getData().get(i).getQuoteCurrency());
        if (currentItem == i) {
            //如果被点击，设置当前TextView被选中
            viewHolder.textView.setSelected(true);
        } else {
            //如果没有被点击，设置当前TextView未被选中
            viewHolder.textView.setSelected(false);
        }
        return convertView;
    }
    int temp=0;
    public void setlist(int temp)
    {
        this.temp=temp;
    }
    //当前Item被点击的位置
    private int currentItem = -1;

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }
    public  class ViewHolder
    {
        public TextView textView;
    }
}
