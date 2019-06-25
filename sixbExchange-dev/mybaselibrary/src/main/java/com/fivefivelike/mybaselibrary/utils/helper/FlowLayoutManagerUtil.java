package com.fivefivelike.mybaselibrary.utils.helper;

import android.content.Context;
import android.support.annotation.IntRange;
import android.view.Gravity;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.gravity.IChildGravityResolver;
import com.beloo.widget.chipslayoutmanager.layouter.breaker.IRowBreaker;

/**
 * @ author      Qsy
 * @ date        17/2/23 上午11:10
 * @ description 流式布局工具
 * 使用需要 compile 'com.beloo.widget:ChipsLayoutManager:0.3.7@aar'
 */
public class FlowLayoutManagerUtil {

    public static ChipsLayoutManager getDefaultFlowLayoutManager(Context context) {
        return ChipsLayoutManager.newBuilder(context)
                //set vertical gravity for all items in a row. Default = Gravity.CENTER_VERTICAL
                .setChildGravity(Gravity.TOP)
                //whether RecyclerView can scroll. TRUE by default
                .setScrollingEnabled(false)
                //set maximum views count in a particular row
                .setMaxViewsInRow(10)
                //set gravity resolver where you can determine gravity for item in position.
                //This method have priority over previous one
                .setGravityResolver(new IChildGravityResolver() {
                    @Override
                    public int getItemGravity(int position) {
                        return Gravity.LEFT;
                    }
                })
                //you are able to break row due to your conditions. Row breaker should return true for that views
                .setRowBreaker(new IRowBreaker() {
                    @Override
                    public boolean isItemBreakRow(@IntRange(from = 0) int position) {
//                        return position == 6 || position == 11 || position == 2;
                        return false;
                    }
                })
                //a layoutOrientation of layout manager, could be VERTICAL OR HORIZONTAL. HORIZONTAL by default
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                // row strategy for views in completed row, could be STRATEGY_DEFAULT, STRATEGY_FILL_VIEW,
                //STRATEGY_FILL_SPACE or STRATEGY_CENTER
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                // whether strategy is applied to last row. FALSE by default
                .withLastRow(true)
                .build();
    }
}
