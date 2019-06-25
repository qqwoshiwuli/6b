package com.circledialog.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.circledialog.params.CircleParams;
import com.circledialog.params.ItemsParams;
import com.circledialog.params.TitleParams;
import com.circledialog.res.drawable.SelectorBtn;
import com.circledialog.res.values.CircleColor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by hupei on 2017/3/30.
 */

class BodyItemsView extends ListView {
    private BaseAdapter mAdapter;

    public BodyItemsView(Context context, CircleParams params) {
        super(context);
        init(context, params);
    }

    private void init(Context context, CircleParams params) {
        final ItemsParams itemsParams = params.itemsParams;


        setSelector(new ColorDrawable(Color.TRANSPARENT));
        setDivider(new ColorDrawable(CircleColor.getDivider()));

        setDividerHeight(1);

        setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                itemsParams.dismiss();
                if (itemsParams.listener != null)
                    itemsParams.listener.onItemClick(parent, view, position, id);
            }
        });

        mAdapter = new ItemsAdapter(context, params);
        setAdapter(mAdapter);
        if (params.titleParams != null) {
            View view = new View(context);
            LinearLayout.LayoutParams viewLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            viewLayoutParams.height = 1;
            view.setLayoutParams(viewLayoutParams);
            view.setBackgroundColor(CircleColor.getDivider());
            addHeaderView(view);
        }
        if (mAdapter.getCount() >= 5) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams
                    .MATCH_PARENT, params.itemsParams.itemHeight * 5);
            setLayoutParams(layoutParams);
        } else {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams
                    .MATCH_PARENT, LayoutParams
                    .MATCH_PARENT);
            setLayoutParams(layoutParams);
        }
    }

    @Override
    public boolean canScrollVertically(int direction) {
        return false;
    }

    public void refreshItems() {
        post(new Runnable() {
            @Override
            public void run() {
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    static class ItemsAdapter<T> extends BaseAdapter {
        class ViewHolder {
            TextView item;
        }

        private Context mContext;
        private List<T> mItems;
        private int mRadius;
        private int mBackgroundColor;
        private ItemsParams mItemsParams;
        private TitleParams mTitleParams;

        public TitleParams getmTitleParams() {
            return mTitleParams;
        }

        public ItemsAdapter(Context context, CircleParams params) {
            this.mContext = context;
            this.mTitleParams = params.titleParams;
            this.mItemsParams = params.itemsParams;
            this.mRadius = params.dialogParams.radius;
            //如果没有背景色，则使用默认色
            this.mBackgroundColor = mItemsParams.backgroundColor != 0 ? mItemsParams
                    .backgroundColor : CircleColor
                    .getBgDialog();
            Object entity = this.mItemsParams.items;
            if (entity != null && entity instanceof Iterable) {
                this.mItems = (List<T>) entity;
            } else if (entity != null && entity.getClass().isArray()) {
                this.mItems = Arrays.asList((T[]) entity);
            } else {
                throw new IllegalArgumentException("entity must be an Array or an Iterable.");
            }
        }

        @Override
        public int getCount() {
            if (mItems != null)
                return mItems.size();
            return 0;
        }

        @Override
        public T getItem(int position) {
            if (mItems != null)
                return mItems.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                ScaleTextView textView = new ScaleTextView(mContext);
                textView.setLayoutParams(new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setTextSize(mItemsParams.textSize);
                textView.setTextColor(mItemsParams.textColor);
                textView.setHeight((int) (mItemsParams.itemHeight));
                viewHolder.item = textView;

                convertView = textView;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            //top
            if (position == 0 && mTitleParams == null) {
                if (getCount() == 1) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewHolder.item.setBackground(new SelectorBtn(mBackgroundColor, mRadius,
                                mRadius, mRadius, mRadius));
                    } else {
                        viewHolder.item.setBackgroundDrawable(new SelectorBtn(mBackgroundColor,
                                mRadius, mRadius, mRadius, mRadius));
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        viewHolder.item.setBackground(new SelectorBtn(mBackgroundColor,
                                mRadius, mRadius, 0, 0));
                    } else {
                        viewHolder.item.setBackgroundDrawable(new SelectorBtn(mBackgroundColor,
                                mRadius, mRadius, 0, 0));
                    }
                }
            }
            //bottom
            else if (position == getCount() - 1) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.item.setBackground(new SelectorBtn(mBackgroundColor, 0, 0,
                            mRadius, mRadius));
                } else {
                    viewHolder.item.setBackgroundDrawable(new SelectorBtn(mBackgroundColor, 0, 0,
                            mRadius, mRadius));
                }
            }
            //middle
            else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    viewHolder.item.setBackground(new SelectorBtn(mBackgroundColor, 0, 0, 0, 0));
                } else {
                    viewHolder.item.setBackgroundDrawable(new SelectorBtn(mBackgroundColor, 0, 0,
                            0, 0));
                }
            }
            viewHolder.item.setText(String.valueOf(getItem(position).toString()));
            return convertView;
        }
    }
}
