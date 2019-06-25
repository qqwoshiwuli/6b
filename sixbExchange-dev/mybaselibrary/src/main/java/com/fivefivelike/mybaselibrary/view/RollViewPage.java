package com.fivefivelike.mybaselibrary.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fivefivelike.mybaselibrary.R;

import java.util.ArrayList;
import java.util.List;

import static android.graphics.drawable.GradientDrawable.OVAL;

public class RollViewPage implements OnPageChangeListener {

    private ChildViewPager vp;//子ViewPager
    private RelativeLayout rl;//这是要ViewPager的父控件
    private Context context;
    private boolean isShowDot;//是否显示轮播小点
    private List<View> list_image;//ViewPager中的条目图片
    private Mode mode;//轮播点的位置
    private LinearLayout ll_dot;//轮播的小点
    private boolean isCirculation;//是否开始轮播
    private List banList;//图片的地址集合
    private ImageClickBack back;//ViewPager的条目点击事件回调
    private GradientDrawable selectorBg, normalBg;
    private int dotNormalColor;//小圆点正常颜色
    private int dotSelectorColor;//小圆点选中颜色
    private int dotSize;//小圆点尺寸
    private boolean isCirMode;
    private int cirTime;//切换时间
    private int imageSize;//是否无限轮播
    private boolean isNotInterceptTouch;//是否不阻断触摸事件  当条目有子条目需要点击的时候,设置为阻断

    /*
     new RollViewPage.BannerBuilder(getActivity())
                .setBanList(viewDelegate.getViewList(LocalSaveUtil.getLoginInfo().getCate()))
                .setRl(viewDelegate.viewHolder.relativelayout_sort)
                .setCirculation(false)
                .setShowDot(true)
                .setDotSize(getResources().getDimensionPixelSize(R.dimen.trans_16px))
                .setDotNormalColor(Color.parseColor("#e2e2e2"))
                .setDotSelectorColorResouse(R.color.footChoose)
                .setMode(RollViewPage.Mode.middle)
                .setDotSizeResouse(R.dimen.trans_10px)
                .setNotInterceptTouch(true)
                .build();
     */
    private RollViewPage(BannerBuilder builder) {
        this.mode = builder.mode;
        this.rl = builder.rl;
        this.context = builder.context;
        this.isCirculation = builder.isCirculation;
        this.banList = builder.banList;
        this.back = builder.back;
        this.isShowDot = builder.isShowDot;
        this.isNotInterceptTouch = builder.isNotInterceptTouch;
        this.cirTime = builder.cirTime;
        this.dotSize = builder.dotSize > 0 ? builder.dotSize : (int) (10f * (context.getResources().getDisplayMetrics().density) + 0.5f);
        this.dotSelectorColor = builder.dotSelectorColor != 0 ? builder.dotSelectorColor : Color.RED;
        this.dotNormalColor = builder.dotNormalColor != 0 ? builder.dotNormalColor : Color.WHITE;
        this.isCirMode = builder.isCirMode;
        initDotBg();
        init();
    }

    /**
     * 初始化小圆点背景
     */
    private void initDotBg() {
        selectorBg = new GradientDrawable();
        selectorBg.setShape(OVAL);
        selectorBg.setColor(dotSelectorColor);
        normalBg = new GradientDrawable();
        normalBg.setShape(OVAL);
        normalBg.setColor(dotNormalColor);
    }

    private void init() {
        list_image = new ArrayList<>();
        rl.removeAllViews();
        initViewpager();
        if (banList == null || banList.size() == 0) {
            return;
        } else {
            addView(banList.size());
        }
        initViewpagerListener();
    }

    private void initViewpagerListener() {
        vp.setAdapter(new PageAdapter());
        vp.setOnPageChangeListener(this);
        vp.setOnSingleTouchListener(new ChildViewPager.OnSingleTouchListener() {//点击回调
            public void onSingleTouch(int index) {
                if (back != null) {
                    back.imageClickListener(isCirMode ? index % imageSize : index);
                }
            }
        });
    }

    private void initViewpager() {
        if (vp == null) {
            vp = new ChildViewPager(context, isNotInterceptTouch);
            int wiht = LayoutParams.MATCH_PARENT;
            int hei = LayoutParams.WRAP_CONTENT;
            LayoutParams lp = new LayoutParams(wiht, hei);
            vp.setLayoutParams(lp);
        }
        rl.addView(vp);
    }

    private void addView(int count) {
        imageSize = count;
        for (Object object : banList) {
            if (object instanceof View) {
                list_image.add((View) object);
            } else {
                ImageView iv = new ImageView(context);
                int wiht = LayoutParams.MATCH_PARENT;
                int hight = LayoutParams.WRAP_CONTENT;
                LayoutParams lp = new LayoutParams(wiht, hight);
                iv.setLayoutParams(lp);
                iv.setScaleType(ScaleType.CENTER_CROP);
                Glide.with(context).load(object.toString())
                        .apply(new RequestOptions()
                                .error(R.drawable.loaderor)).into(iv);
                list_image.add(iv);
            }
        }
        if (isShowDot) {//控制是否显示小点
            ll_dot = new LinearLayout(context);
            ll_dot.setOrientation(LinearLayout.HORIZONTAL);
            LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            lp.setMargins(10, 0, 20, 20);
            ll_dot.setLayoutParams(lp);
            if (count > 1) {
                if (mode.equals(Mode.left)) {
                    ll_dot.setGravity(Gravity.LEFT);
                } else if (mode.equals(Mode.middle)) {
                    ll_dot.setGravity(Gravity.CENTER);
                } else if (mode.equals(Mode.right)) {
                    ll_dot.setGravity(Gravity.RIGHT);
                }
                for (int i = 0; i < count; i++) {
                    ImageView iv_dot = new ImageView(context);
                    LinearLayout.LayoutParams lp_dot = new LinearLayout.LayoutParams(dotSize, dotSize);
                    lp_dot.setMargins(dotSize, 0, 0, 0);
                    iv_dot.setLayoutParams(lp_dot);
                    iv_dot.setBackground(i == 0 ? selectorBg : normalBg);
                    ll_dot.addView(iv_dot);
                }
                rl.addView(ll_dot);
            }

        }
        circulation();
    }

    public LinearLayout getLl_dot() {

        return ll_dot;
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
                               int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (isShowDot) {
            for (int i = 0; i < ll_dot.getChildCount(); i++) {
                if (isCirMode) {
                    ll_dot.getChildAt(i).setBackground(i == position % imageSize ? selectorBg : normalBg);
                } else {
                    ll_dot.getChildAt(i).setBackground(i == position ? selectorBg : normalBg);
                }
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        switch (state) {
            case 1://滑动
                onPauseCirculation();
                break;
            case 0://滑动完毕
                reStarCirculation();
                break;
            default:
                break;
        }
    }

    private class PageAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return isCirMode ? Integer.MAX_VALUE : list_image.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (isCirMode) {
                position %= imageSize;
                View view = list_image.get(position);
                ViewParent viewParent = view.getParent();
                if (viewParent != null) {
                    ViewGroup parent = (ViewGroup) viewParent;
                    parent.removeView(view);
                }
                container.addView(view);
                return view;
            } else {
                container.addView(list_image.get(position));
                return list_image.get(position);
            }
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (!isCirMode) {
                container.removeView(list_image.get(position));
            }
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (vp != null) {
                vp.setCurrentItem(msg.what);
            } else {
                stopCirculation();
            }
        }

        ;
    };
    private int currentIndex;
    private boolean ispause = true;

    private void circulation() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                currentIndex = 0;
                int time = cirTime;
                while (isCirculation) {
                    try {
                        if (ispause) {
                            if (!isCirMode) {
                                if (currentIndex > (list_image.size())) {
                                    currentIndex = 0;
                                }
                            }
                            Thread.sleep(time);
                            handler.sendEmptyMessage(currentIndex);
                            currentIndex++;
                        }
                    } catch (InterruptedException e) {
                        isCirculation = false;
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    //停止
    public void stopCirculation() {
        isCirculation = false;
        list_image.clear();
    }

    //暂停
    public void onPauseCirculation() {
        ispause = false;
    }

    //重启
    public void reStarCirculation() {
        ispause = true;
        currentIndex = vp.getCurrentItem();
    }

    public enum Mode {
        left, middle, right
    }

    public interface ImageClickBack {
        void imageClickListener(int index);
    }

    public ViewPager getViewPager() {
        return vp;
    }

    public static class BannerBuilder {
        private Context context;
        private RelativeLayout rl;
        private Mode mode = Mode.right;
        private boolean isCirculation = true;
        private List banList;
        private boolean isShowDot = true;
        private ImageClickBack back;
        private int dotNormalColor;
        private int dotSelectorColor;
        private int dotSize;
        private boolean isCirMode;
        private int cirTime = 2000;
        private boolean isNotInterceptTouch;

        public BannerBuilder(Context context) {
            this.context = context;
        }

        public BannerBuilder setNotInterceptTouch(boolean notInterceptTouch) {
            isNotInterceptTouch = notInterceptTouch;
            return this;
        }

        public BannerBuilder setRl(RelativeLayout rl) {
            this.rl = rl;
            return this;
        }

        public BannerBuilder setCirMode(boolean cirMode) {
            isCirMode = cirMode;
            return this;
        }

        public BannerBuilder setCirTime(int cirTime) {
            this.cirTime = cirTime;
            return this;
        }

        public BannerBuilder setDotNormalColor(int dotNormalColor) {
            this.dotNormalColor = dotNormalColor;
            return this;
        }

        public BannerBuilder setDotSelectorColor(int dotSelectorColor) {
            this.dotSelectorColor = dotSelectorColor;
            return this;
        }

        public BannerBuilder setDotSize(int dotSize) {
            this.dotSize = dotSize;
            return this;
        }

        public BannerBuilder setDotNormalColorResouse(int dotNormalColor) {
            this.dotNormalColor = ContextCompat.getColor(context, dotNormalColor);
            return this;
        }

        public BannerBuilder setDotSelectorColorResouse(int dotSelectorColor) {
            this.dotSelectorColor = ContextCompat.getColor(context, dotSelectorColor);
            return this;
        }

        public BannerBuilder setDotSizeResouse(int dotSize) {
            this.dotSize = context.getResources().getDimensionPixelSize(dotSize);
            return this;
        }

        public BannerBuilder setMode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public BannerBuilder setBanList(List banList) {
            this.banList = banList;
            return this;
        }

        public BannerBuilder setCirculation(boolean circulation) {
            isCirculation = circulation;
            return this;
        }

        public BannerBuilder setShowDot(boolean showDot) {
            isShowDot = showDot;
            return this;
        }

        public BannerBuilder setBack(ImageClickBack back) {
            this.back = back;
            return this;
        }

        public RollViewPage build() {
            return new RollViewPage(this);
        }
    }
}
