package com.fivefivelike.mybaselibrary.view.popupWindow;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fivefivelike.mybaselibrary.R;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by 郭青枫 on 2017/8/17.
 */

public class ChoosePopupWindow extends BasePopupWindow {


    private RecyclerView recycler_view;
    private List<String> list;
    private PopupAdapter adapter;

    public ChoosePopupWindow(Context context) {
        super(context);
    }

    @Override
    public int getLayoutId() {
        return R.layout.popup_input;
    }

    @Override
    public void initView() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        recycler_view = findViewById(R.id.recyclerView);
        list = new ArrayList<>();
        adapter = new PopupAdapter(context, list);
        recycler_view.setLayoutManager(new LinearLayoutManager(context));
        recycler_view.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {


            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });
    }

    public void setAdapter(PopupAdapter adapter) {
        this.adapter = adapter;
        if (recycler_view != null) {
            recycler_view.setAdapter(adapter);
        }
    }


    public ChoosePopupWindow setSelectItem(String[] items) {
        list.clear();
        List<String> itemList = Arrays.asList(items);
        if (itemList != null && itemList.size() > 0) {
            list.addAll(itemList);
        }
        adapter.notifyDataSetChanged();
        return this;
    }


    class PopupAdapter extends CommonAdapter<String> {

        public PopupAdapter(Context context, List<String> datas) {
            super(context, 0, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, int position) {
            //            holder.setText(R.id.tv_name, s);
            //            holder.setVisible(R.id.view_line, position != mDatas.size() - 1);
        }
    }

}
