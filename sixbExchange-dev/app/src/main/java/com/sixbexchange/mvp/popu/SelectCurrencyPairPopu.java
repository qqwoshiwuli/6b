package com.sixbexchange.mvp.popu;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.ObjectUtils;
import com.circledialog.res.drawable.RadiuBg;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.popupWindow.BasePopupWindow;
import com.sixbexchange.R;
import com.sixbexchange.adapter.CurrencyPairAdapter;
import com.sixbexchange.entity.bean.ExchCurrencyPairBean;
import com.sixbexchange.entity.bean.TradeDetailBean;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2017/8/17.
 */

public class SelectCurrencyPairPopu extends BasePopupWindow {


    private TagFlowLayout tagFlowLayout;
    private RecyclerView recycler_view_coins;

    List<ExchCurrencyPairBean> data;
    TradeDetailBean select;

    CurrencyPairAdapter adapter;

    public SelectCurrencyPairPopu(Context context) {
        super(context);

    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_select_coins;
    }

    @Override
    public void initView() {
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        recycler_view_coins = findViewById(R.id.recycler_view_coins);
        tagFlowLayout = findViewById(R.id.tagFlowLayout);
        ArrayList list = new ArrayList<>();
        adapter = new CurrencyPairAdapter(context, list);
        recycler_view_coins.setLayoutManager(new LinearLayoutManager(context));
        recycler_view_coins.setAdapter(adapter);

    }

    int index = 0;
    List<String> name;

    public void showList(
            List<ExchCurrencyPairBean> data, TradeDetailBean select,
            View view,
            DefaultClickLinsener defaultClickLinsener) {
        this.data = data;
        this.select = select;
        adapter.setDefaultClickLinsener(defaultClickLinsener);
        adapter.setType(select.getCurrencyPair());
        name = new ArrayList<>();
        index = 0;
        for (int i = 0; i < data.size(); i++) {
            if (ObjectUtils.equals(select.getSecondType(), data.get(i).getName())) {
                index = i;
            }
            name.add(data.get(i).getName());
        }
        selectTag();
        selectList();
        showAsDropDown(view);
    }

    private void selectList() {
        adapter.setSelectIndex(index);
        adapter.setData(data.get(index).getList());
    }

    private void selectTag() {
        tagFlowLayout.setAdapter(new TagAdapter<String>(name) {
            @Override
            public View getView(FlowLayout parent, int position, String s) {
                TextView tv =
                        (TextView) ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                                .inflate(R.layout.layout_flowtext, null);
                if (index == position) {
                    tv.setTextColor(CommonUtils.getColor(R.color.white));
                    tv.setBackground(new RadiuBg(CommonUtils.getColor(R.color.mark_color),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px),
                            (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_5px)));
                } else {
                    tv.setTextColor(CommonUtils.getColor(R.color.color_font3));
                    tv.setBackground(CommonUtils.getDrawable(R.drawable.shape_gray_border_r5));
                }
                tv.setText(s);
                return tv;
            }

            @Override
            public void onSelected(int position, View view) {
                super.onSelected(position, view);
                index = position;
                selectList();
                selectTag();
            }
        });
    }

}
