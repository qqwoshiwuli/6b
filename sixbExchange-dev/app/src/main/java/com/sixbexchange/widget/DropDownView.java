package com.sixbexchange.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.fivefivelike.mybaselibrary.base.BaseAdapter;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.fivefivelike.mybaselibrary.view.IconFontTextview;
import com.sixbexchange.R;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/1/24 0024.
 */

public class DropDownView extends LinearLayout {
    Context mContext;

    List<String> datas;
    int selectColorId = R.color.mark_color;
    int defaultColorId = R.color.color_font2;
    int textSizeId = R.dimen.text_trans_24px;
    int popuBgId = R.color.colorPrimary;
    private static final int DEFAULT_ELEVATION = 4;
    DropAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    int selectPosition = 0;
    boolean isPopu = false;
    String defaultStr = "";

    public void setDefaultStr(String defaultStr) {
        this.defaultStr = defaultStr;
    }

    DefaultClickLinsener defaultClickLinsener;

    public DropDownView setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
        return this;
    }

    public DropDownView setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
        return this;
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public DropDownView setSelectColorId(int selectColorId) {
        this.selectColorId = selectColorId;
        return this;
    }

    public DropDownView setDefaultColorId(int defaultColorId) {
        this.defaultColorId = defaultColorId;
        return this;
    }

    public DropDownView setTextSizeId(int textSizeId) {
        this.textSizeId = textSizeId;
        return this;
    }

    public void setTextColor(int color) {
        if (tv_title != null) {
            tv_title.setTextColor(color);
            tv_drop.setTextColor(color);
        }
    }

    public DropDownView setDatas(List<String> datas, RecyclerView.LayoutManager layoutManager) {
        this.datas = datas;
        tv_title.setText(selectPosition < 0 ? defaultStr : datas.get(selectPosition));
        adapter = new DropAdapter(mContext, this.datas);
        if (selectPosition >= 0) {
            adapter.setSelectPosition(selectPosition);
        }
        if (layoutManager == null) {
            layoutManager = new LinearLayoutManager(mContext) {
                @Override
                public boolean canScrollVertically() {
                    return false;
                }
            };
        }
        this.layoutManager = layoutManager;
        recyclerview.setLayoutManager(this.layoutManager);
        recyclerview.setAdapter(adapter);
        adapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                tv_title.setText(adapter.getDatas().get(position));
                selectPosition = position;
                adapter.setSelectPosition(position);
                popupWindow.dismiss();
                if (defaultClickLinsener != null) {
                    defaultClickLinsener.onClick(view, position, adapter.getDatas().get(position));
                }
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        return this;
    }

    public DropDownView(Context context) {
        super(context);
        initView(context);
    }

    public DropDownView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DropDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    public TextView tv_title;
    public IconFontTextview tv_drop;
    public LinearLayout lin_root;
    RecyclerView recyclerview;
    PopupWindow popupWindow;

    private void initView(Context context) {
        mContext = context;
        View rootView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_drop_down, null);
        this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
        this.tv_drop = (IconFontTextview) rootView.findViewById(R.id.tv_drop);
        this.lin_root = (LinearLayout) rootView.findViewById(R.id.lin_root);
        this.addView(rootView);
        tv_drop.setTextColor(CommonUtils.getColor(defaultColorId));
        tv_title.setTextColor(CommonUtils.getColor(defaultColorId));
        lin_root.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                //显示popuwindow
                if (popupWindow != null) {
                    if (isPopu) {
                        popupWindow.dismiss();
                    } else {
                        showPopu();
                        //                        tv_drop.setTextColor(CommonUtils.getColor(selectColorId));
                        //                        tv_title.setTextColor(CommonUtils.getColor(selectColorId));
                        isPopu = !isPopu;
                        lin_root.setBackgroundColor(CommonUtils.getColor(R.color.base_mask));
                    }
                }
            }
        });

        recyclerview = new RecyclerView(context);
        //recyclerview.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        // Set the spinner's id into the listview to make it pretend to be the right parent in
        // onItemClick
        recyclerview.setId(getId());
        //hide vertical and horizontal scrollbars
        recyclerview.setVerticalScrollBarEnabled(false);
        recyclerview.setHorizontalScrollBarEnabled(false);
        recyclerview.setPadding(0, 0, 0, 0);

        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(recyclerview);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isPopu = !isPopu;
                //tv_drop.setTextColor(CommonUtils.getColor(defaultColorId));
                //tv_title.setTextColor(CommonUtils.getColor(defaultColorId));
                tv_drop.setVisibility(VISIBLE);
                lin_root.setBackgroundColor(CommonUtils.getColor(R.color.transparent));
            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(DEFAULT_ELEVATION);
            // popupWindow.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.spinner_drawable));
            popupWindow.setBackgroundDrawable(CommonUtils.getDrawable(popuBgId));
        } else {
            popupWindow.setBackgroundDrawable(CommonUtils.getDrawable(popuBgId));
        }
        displayHeight = getContext().getResources().getDisplayMetrics().heightPixels;

    }

    public void setText(String txt) {
        if (tv_title != null) {
            tv_title.setText(txt);
        }
    }


    private void showPopu() {
        if (itemWidth == 0) {
            measurePopUpDimension();
        }
        tv_drop.setVisibility(INVISIBLE);
        popupWindow.showAsDropDown(this);
    }

    private int displayHeight;
    private int parentVerticalOffset;
    public static final int VERTICAL_OFFSET = 1;
    int itemWidth = 0;

    private void measurePopUpDimension() {
        int widthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(
                displayHeight - getParentVerticalOffset() - getMeasuredHeight(),
                MeasureSpec.AT_MOST);
        itemWidth = widthSpec;
        if (layoutManager instanceof GridLayoutManager) {
            widthSpec = MeasureSpec.makeMeasureSpec(ScreenUtils.getScreenWidth() - 2 * (int) CommonUtils.getDimensionPixelSize(R.dimen.trans_30px), MeasureSpec.EXACTLY);
            itemWidth = widthSpec / ((GridLayoutManager) layoutManager).getSpanCount();
        }
        recyclerview.measure(widthSpec, heightSpec);
        popupWindow.setWidth(recyclerview.getMeasuredWidth());
        popupWindow.setHeight(recyclerview.getMeasuredHeight());
    }

    private int getParentVerticalOffset() {
        if (parentVerticalOffset > 0) {
            return parentVerticalOffset;
        }
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        return parentVerticalOffset = locationOnScreen[VERTICAL_OFFSET];
    }


    private class DropAdapter extends BaseAdapter<String> {

        int adaSelectPosition = -1;

        public void setSelectPosition(int position) {
            int oldPosition = adaSelectPosition;
            this.adaSelectPosition = position;
            this.notifyItemChanged(oldPosition);
            this.notifyItemChanged(adaSelectPosition);
        }

        public int getSelectPosition() {
            return selectPosition;
        }

        public DropAdapter(Context context, List<String> datas) {
            super(context, R.layout.adapter_drop_down_item, datas);
        }

        @Override
        protected void convert(ViewHolder holder, String s, final int position) {
            TextView tv_item;
            tv_item = holder.getView(R.id.tv_item);
            int left = tv_item.getLeft();
            int right = tv_item.getRight();
            //tv_item.setLayoutParams(new ConstraintLayout.LayoutParams(itemWidth - left - right, ViewGroup.LayoutParams.WRAP_CONTENT));
            tv_item.setText(s);
            tv_item.setBackground(null);
            tv_item.setTextColor(CommonUtils.getColor(R.color.color_font2));
            if (adaSelectPosition == position) {
                tv_item.setBackgroundResource(R.color.mark_color);
                tv_item.setTextColor(CommonUtils.getColor(R.color.white));
            }
        }
    }
}
