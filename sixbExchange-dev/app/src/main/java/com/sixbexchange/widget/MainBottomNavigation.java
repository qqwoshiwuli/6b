package com.sixbexchange.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 郭青枫 on 2018/5/14 0014.
 */

public class MainBottomNavigation extends FrameLayout {
    Context mContext;
    public AppCompatImageView iv_icon1;
    public TextView tv_name1;
    public LinearLayout lin_bottom1;
    public LinearLayout lin_navigation;
    public AppCompatImageView iv_icon2;
    public TextView tv_name2;
    public LinearLayout lin_bottom2;
    public AppCompatImageView iv_icon3;
    public TextView tv_name3;
    public LinearLayout lin_bottom3;
    public AppCompatImageView iv_icon4;
    public TextView tv_name4;
    public LinearLayout lin_bottom4;

    private int selectColorId = R.color.mark_color;
    private int unSelectColorId = R.color.color_font3;
    List<AppCompatImageView> icons;
    List<TextView> textViews;
    ArrayList<CustomTabEntity> mTabEntities;
    int selectPosition = 0;

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setmTabEntities(ArrayList<CustomTabEntity> mTabEntities) {
        this.mTabEntities = mTabEntities;
    }

    DefaultClickLinsener defaultClickLinsener;

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }

    public void setSelectPosition(int position) {
        if (!ListUtils.isEmpty(icons) && !ListUtils.isEmpty(mTabEntities)) {
            if (position < icons.size()) {
                selectPosition = position;
                for (int i = 0; i < icons.size(); i++) {
                    if (i == position) {
                        icons.get(i).setImageResource(mTabEntities.get(i).getTabSelectedIcon());
                        textViews.get(i).setTextColor(CommonUtils.getColor(selectColorId));
                    } else {
                        icons.get(i).setImageResource(mTabEntities.get(i).getTabUnselectedIcon());
                        textViews.get(i).setTextColor(CommonUtils.getColor(unSelectColorId));
                    }
                    textViews.get(i).setText(mTabEntities.get(i).getTabTitle());
                }
            }
        }
    }

    public void setSelectColorId(int selectColorId) {
        this.selectColorId = selectColorId;
    }

    public void setUnSelectColorId(int unSelectColorId) {
        this.unSelectColorId = unSelectColorId;
    }

    public void setCurrentTab(int position) {
        setSelectPosition(position);
    }


    public MainBottomNavigation(@NonNull Context context) {
        super(context);
        init(context);
    }

    public MainBottomNavigation(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MainBottomNavigation(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        View rootView = ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.layout_bottom_navigation, null);
        rootView.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        this.iv_icon1 = (AppCompatImageView) rootView.findViewById(R.id.iv_icon1);
        this.tv_name1 = (TextView) rootView.findViewById(R.id.tv_name1);
        this.lin_bottom1 = (LinearLayout) rootView.findViewById(R.id.lin_bottom1);
        this.iv_icon2 = (AppCompatImageView) rootView.findViewById(R.id.iv_icon2);
        this.tv_name2 = (TextView) rootView.findViewById(R.id.tv_name2);
        this.lin_bottom2 = (LinearLayout) rootView.findViewById(R.id.lin_bottom2);
        this.iv_icon3 = (AppCompatImageView) rootView.findViewById(R.id.iv_icon3);
        this.tv_name3 = (TextView) rootView.findViewById(R.id.tv_name3);
        this.lin_bottom3 = (LinearLayout) rootView.findViewById(R.id.lin_bottom3);
        this.iv_icon4 = (AppCompatImageView) rootView.findViewById(R.id.iv_icon4);
        this.tv_name4 = (TextView) rootView.findViewById(R.id.tv_name4);
        this.lin_bottom4 = (LinearLayout) rootView.findViewById(R.id.lin_bottom4);
        this.lin_navigation = (LinearLayout) rootView.findViewById(R.id.lin_navigation);

        icons = new ArrayList<>();
        textViews = new ArrayList<>();
        icons.add(iv_icon1);
        icons.add(iv_icon2);
        icons.add(iv_icon3);
        icons.add(iv_icon4);


        textViews.add(tv_name1);
        textViews.add(tv_name2);
        textViews.add(tv_name3);
        textViews.add(tv_name4);


        lin_bottom1.setOnClickListener(onClickListener);
        lin_bottom2.setOnClickListener(onClickListener);
        lin_bottom3.setOnClickListener(onClickListener);
        lin_bottom4.setOnClickListener(onClickListener);


        this.addView(rootView);
    }

    long nowTime = 0;

    OnClickListener onClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (nowTime + 500 < System.currentTimeMillis()) {
                int position = 0;
                if (v.getId() == R.id.lin_bottom1) {
                    position = 0;
                } else if (v.getId() == R.id.lin_bottom2) {
                    position = 1;
                } else if (v.getId() == R.id.lin_bottom3) {
                    position = 2;
                } else if (v.getId() == R.id.lin_bottom4) {
                    position = 3;
                }
                nowTime = System.currentTimeMillis();
                setSelectPosition(position);
                if (defaultClickLinsener != null) {
                    defaultClickLinsener.onClick(v, position, null);
                }
            }
        }
    };

}
