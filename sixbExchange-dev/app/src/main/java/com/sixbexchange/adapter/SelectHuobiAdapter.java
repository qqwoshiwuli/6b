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

public class SelectHuobiAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    List<selectExchhuobiBeans> selectExchhuobi;
    public SelectHuobiAdapter(Context context,List<selectExchhuobiBeans> selectExchhuobi) {
        super();
        mInflater = LayoutInflater.from(context);
        this.selectExchhuobi=selectExchhuobi;
    }

    @Override
    public int getCount() {
        return selectExchhuobi.size();
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
            convertView = mInflater.inflate(R.layout.adapter_select_huobi, null);
            viewHolder.textView =  convertView.findViewById(R.id.name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.textView.setText(""+selectExchhuobi.get(i).getType());
        if (currentItem == i) {
            //如果被点击，设置当前TextView被选中
            viewHolder.textView.setSelected(true);
        } else {
            //如果没有被点击，设置当前TextView未被选中
            viewHolder.textView.setSelected(false);
        }
        return convertView;
    }
    //当前Item被点击的位置
    private int currentItem = 0;

    public void setCurrentItem(int currentItem) {
        this.currentItem = currentItem;
    }
    public  class ViewHolder
    {
        public TextView textView;
    }
}
