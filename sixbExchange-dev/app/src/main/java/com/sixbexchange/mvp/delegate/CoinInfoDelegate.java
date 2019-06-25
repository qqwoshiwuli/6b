package com.sixbexchange.mvp.delegate;

import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blankj.utilcode.util.TimeUtils;
import com.fivefivelike.mybaselibrary.base.BaseDelegate;
import com.fivefivelike.mybaselibrary.utils.CommonUtils;
import com.fivefivelike.mybaselibrary.utils.ListUtils;
import com.fivefivelike.mybaselibrary.utils.callback.DefaultClickLinsener;
import com.sixbexchange.R;
import com.sixbexchange.entity.bean.ExchangeData;
import com.sixbexchange.entity.bean.kline.DataParse;
import com.sixbexchange.entity.bean.kline.KLineBean;
import com.sixbexchange.utils.BigUIUtil;
import com.sixbexchange.utils.UserSet;
import com.sixbexchange.widget.DropDownView;
import com.sixbexchange.widget.chart.KCombinedChart;
import com.tablayout.listener.CustomTabEntity;
import com.wang.avi.AVLoadingIndicatorView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoinInfoDelegate extends BaseDelegate {
    public ViewHolder viewHolder;
    public ArrayList<CustomTabEntity> mTabEntities2 = new ArrayList<>();
    ExchangeData mExchangeData;
    String klineValue = "1m";
    List<String> timeData;
    List<String> dataset1;
    public String rateUnit = "";
    List<String> trendData;
    List<String> dataset2;
    List<ExchangeData> changeExchange;

    public void setChangeExchange(List<ExchangeData> changeExchange) {
        this.changeExchange = changeExchange;
    }

    public List<ExchangeData> getChangeExchange() {
        return changeExchange;
    }

    DefaultClickLinsener defaultClickLinsener;

    DefaultClickLinsener getRateLinsener;

    public void setGetRateLinsener(DefaultClickLinsener getRateLinsener) {
        this.getRateLinsener = getRateLinsener;
    }

    public void setDefaultClickLinsener(DefaultClickLinsener defaultClickLinsener) {
        this.defaultClickLinsener = defaultClickLinsener;
    }


    public void setShowLoading(boolean isShow) {
        if (isShow) {
            viewHolder.combinedchart.setVisibility(View.INVISIBLE);
            viewHolder.barchart.setVisibility(View.INVISIBLE);
            viewHolder.lin_loading.setVisibility(View.VISIBLE);
            viewHolder.aVLoadingIndicatorView.show();
            //viewHolder.lin_kline_info.setVisibility(View.GONE);
            viewHolder.combinedchart.setTouchEnabled(false);
            viewHolder.barchart.setTouchEnabled(false);
            viewHolder.tv_loading.setText("K线加载中...");
        } else {
            viewHolder.combinedchart.setVisibility(View.VISIBLE);
            viewHolder.barchart.setVisibility(View.VISIBLE);
            viewHolder.lin_loading.setVisibility(View.GONE);
            viewHolder.combinedchart.setTouchEnabled(true);
            viewHolder.barchart.setTouchEnabled(true);
        }
    }


    @Override
    public void initView() {
        viewHolder = new ViewHolder(getRootView());
        klineValue = UserSet.getinstance().getKTime();
        timeData = Arrays.asList(CommonUtils.getStringArray(R.array.sa_select_kline_time_value));
        dataset1 = Arrays.asList(CommonUtils.getStringArray(R.array.sa_select_kline_time));

        viewHolder.lin_time.setDefaultStr(CommonUtils.getString(R.string.str_more));
        viewHolder.lin_time.setSelectPosition(timeData.indexOf(klineValue)).setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                //时间 改变
                if (klineValue != timeData.get(position)) {
                    klineValue = timeData.get(position);
                    setSelectKline(4);
                    defaultClickLinsener.onClick(view, 0, klineValue);
                }
            }
        }).setDatas(dataset1, new LinearLayoutManager(this.getActivity()) {
            public boolean canScrollVertically() {
                return false;
            }
        });


        trendData = Arrays.asList(CommonUtils.getStringArray(R.array.sa_select_kline_trend_value));
        dataset2 = Arrays.asList(CommonUtils.getStringArray(R.array.sa_select_kline_trend));
        viewHolder.lin_trend.setDefaultStr("切换走势");
        viewHolder.lin_trend.setSelectPosition(trendData.indexOf(klineValue)).setDefaultClickLinsener(new DefaultClickLinsener() {
            @Override
            public void onClick(View view, int position, Object item) {
                //时间 改变
                if (klineValue != timeData.get(position)) {
                    klineValue = trendData.get(position);
                    setSelectKline(5);
                    defaultClickLinsener.onClick(view, 0, klineValue);
                }
            }
        }).setDatas(dataset2, new LinearLayoutManager(this.getActivity()) {
            public boolean canScrollVertically() {
                return false;
            }
        });


        viewHolder.tv_minute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectKline != 0) {
                    setSelectKline(0);
                    klineValue = "1m";
                    defaultClickLinsener.onClick(v, 0, klineValue);
                }
            }
        });
        viewHolder.tv_minute_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectKline != 1) {
                    setSelectKline(1);
                    klineValue = "1m";
                    defaultClickLinsener.onClick(v, 0, klineValue);
                }
            }
        });
        viewHolder.tv_5minute_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectKline != 2) {
                    setSelectKline(2);
                    klineValue = "5m";
                    defaultClickLinsener.onClick(v, 0, klineValue);
                }
            }
        });
        viewHolder.tv_hour_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectKline != 3) {
                    setSelectKline(3);
                    klineValue = "1h";
                    defaultClickLinsener.onClick(v, 0, klineValue);
                }
            }
        });
        viewHolder.tv_day_kline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectKline != 4) {
                    setSelectKline(4);
                    klineValue = "1d";
                    defaultClickLinsener.onClick(v, 0, klineValue);
                }
            }
        });

        if (klineValue.contains("1m")) {
            setSelectKline(1);
        } else if (klineValue.equals("5m")) {
            setSelectKline(2);
        } else if (klineValue.equals("1h")) {
            setSelectKline(3);
        } else if (klineValue.equals("1d")) {
            setSelectKline(4);
        } else if (klineValue.split("_").length != 1) {
            setSelectKline(5);
        } else {
            setSelectKline(4);
        }
        viewHolder.combinedchart.setNoDataText("K线暂时没有数据");
        viewHolder.barchart.setNoDataText("");


        //        viewHolder.lin_kline_info.setOnClickListener(new View.OnClickListener() {
        //            @Override
        //            public void onClick(View v) {
        //                viewHolder.combinedchart.clearHightLisht();
        //                viewHolder.barchart.clearHightLisht();
        //                viewHolder.lin_kline_info.setVisibility(View.GONE);
        //            }
        //        });

        //        viewHolder.judgeNestedScrollView.setOnScrollChangeListener(new JudgeNestedScrollView.OnScrollChangeListener() {
        //            @Override
        //            public void onScrollChangeListener(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
        //                if (oldScrollY > scrollY) {
        //                    if (isNeedInvalidate && viewHolder.lin_kline.getMeasuredHeight() - 100 > scrollY) {
        //                        viewHolder.combinedchart.notifyDataSetChanged();
        //                        viewHolder.barchart.notifyDataSetChanged();
        //                        viewHolder.combinedchart.invalidate();
        //                        viewHolder.barchart.invalidate();
        //                        isNeedInvalidate = false;
        //                    }
        //                } else {
        //                    if (viewHolder.lin_kline.getMeasuredHeight() <= scrollY) {
        //                        isNeedInvalidate = true;
        //                    }
        //                }
        //            }
        //        });
    }

    boolean isNeedInvalidate = false;
    int selectKline = 2;

    public int getSelectKline() {
        return selectKline;
    }

    //1天走势：24小时的5分钟k
    //
    //1周走势：7天内的15分钟k
    //
    //1月走势：30天内的1小时k
    //
    //1年走势：365天内的1天k
    //
    //    全部走势：全部的1天k
    private void setSelectKline(int select) {
        selectKline = select;
        int mark_color = CommonUtils.getColor(R.color.mark_color);
        int color_font2 = CommonUtils.getColor(R.color.color_font2);
        //viewHolder.tv_minute.setTextColor(select == 0 ? mark_color : color_font2);
        viewHolder.tv_minute_kline.setTextColor(select == 1 ? mark_color : color_font2);
        viewHolder.tv_5minute_kline.setTextColor(select == 2 ? mark_color : color_font2);
        viewHolder.tv_hour_kline.setTextColor(select == 3 ? mark_color : color_font2);
        viewHolder.tv_day_kline.setTextColor(select == 4 ? mark_color : color_font2);


        //        viewHolder.lin_time.setTextColor(select == 4 ? mark_color : color_font2);
        //        viewHolder.lin_trend.setTextColor(select == 5 ? mark_color : color_font2);
        viewHolder.fl_barchart.setVisibility(View.VISIBLE);
        if (select == 0 || select == 5) {
            viewHolder.lin_ma.setVisibility(View.GONE);
            if (select == 5) {
                viewHolder.fl_barchart.setVisibility(View.GONE);
            }
            //            viewHolder.lin_price.setVisibility(View.VISIBLE);
            //            viewHolder.lin_open.setVisibility(View.GONE);
            //            viewHolder.lin_high.setVisibility(View.GONE);
            //            viewHolder.lin_low.setVisibility(View.GONE);
            //            viewHolder.lin_close.setVisibility(View.GONE);
            //            viewHolder.lin_rise_rate.setVisibility(View.GONE);
            //            viewHolder.lin_rise_vol.setVisibility(View.GONE);
        } else {
            viewHolder.lin_ma.setVisibility(View.VISIBLE);
            //            viewHolder.lin_price.setVisibility(View.GONE);
            //            viewHolder.lin_open.setVisibility(View.VISIBLE);
            //            viewHolder.lin_high.setVisibility(View.VISIBLE);
            //            viewHolder.lin_low.setVisibility(View.VISIBLE);
            //            viewHolder.lin_close.setVisibility(View.VISIBLE);
            //            viewHolder.lin_rise_rate.setVisibility(View.VISIBLE);
            //            viewHolder.lin_rise_vol.setVisibility(View.VISIBLE);
        }
    }


    public void initKline(int klineSelectPosition, DataParse dataParse) {
        if (!ListUtils.isEmpty(dataParse.getKLineDatas())) {
            if (klineSelectPosition >= 0 &&
                    klineSelectPosition < dataParse.getKLineDatas().size() &&
                    klineSelectPosition < dataParse.getMa5DataL().size() &&
                    klineSelectPosition < dataParse.getMa10DataL().size() &&
                    klineSelectPosition < dataParse.getMa20DataL().size()
                    ) {
                viewHolder.tv_ma5.setText("MA5:" + BigUIUtil.getinstance().bigPrice(dataParse.getMa5DataL().get(klineSelectPosition).getVal() + ""));
                viewHolder.tv_ma10.setText("MA10:" + BigUIUtil.getinstance().bigPrice(dataParse.getMa10DataL().get(klineSelectPosition).getVal() + ""));
                viewHolder.tv_ma20.setText("MA20:" + BigUIUtil.getinstance().bigPrice(dataParse.getMa20DataL().get(klineSelectPosition).getVal() + ""));
                KLineBean kLineBean = dataParse.getKLineDatas().get(klineSelectPosition);
                viewHolder.tv_kline_open.setText(
                        "开" + ": " +
                                BigUIUtil.getinstance().bigPrice(kLineBean.open.toPlainString()));
                viewHolder.tv_kline_close.setText(
                        "收" + ": " +
                                BigUIUtil.getinstance().bigPrice(kLineBean.close.toPlainString()));
                viewHolder.tv_kline_high.setText(
                        "高" + ": " +
                                BigUIUtil.getinstance().bigPrice(kLineBean.high.toPlainString()));
                viewHolder.tv_kline_low.setText(
                        "低" + ": " +
                                BigUIUtil.getinstance().bigPrice(kLineBean.low.toPlainString()));
                viewHolder.tv_kline_time.setText(
                        TimeUtils.millis2String(kLineBean.timestamp * 1000,
                                new SimpleDateFormat("yyyy/MM/dd HH:mm:ss"))
                );
                if (kLineBean.open.doubleValue() != 0) {
                    viewHolder.tv_kline_rate.setText(
                            Html.fromHtml(
                                    "涨幅" + ":" +
                                            "<font color=\"" + CommonUtils.getStringColor(
                                            kLineBean.close.floatValue() >
                                                    kLineBean.open.floatValue() ?
                                                    UserSet.getinstance().getRiseColor() : UserSet.getinstance().getDropColor()) + "\">"
                                            + BigUIUtil.getinstance().bigPrice(
                                            kLineBean.close.subtract(
                                                    kLineBean.open).divide(
                                                    kLineBean.open, 8, BigDecimal.ROUND_DOWN
                                            ).multiply(new BigDecimal(100)).toPlainString()
                                    ) + "%"
                                            + "</font>"
                            )
                    );
                    viewHolder.tv_kline_rate.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.tv_kline_rate.setVisibility(View.GONE);
                }
                viewHolder.tv_kline_vol.setText(
                        "量" + ":"
                                + BigUIUtil.getinstance().bigAmount(kLineBean.volume.toPlainString())
                );
            }
        }
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_coin_info;
    }


    public static class ViewHolder {
        public View rootView;

        public TextView tv_title;
        public TextView tv_minute;
        public TextView tv_minute_kline;
        public TextView tv_5minute_kline;
        public TextView tv_hour_kline;
        public TextView tv_day_kline;
        public DropDownView lin_time;
        public DropDownView lin_trend;
        public TextView tv_kline_time;
        public TextView tv_kline_open;
        public TextView tv_kline_high;
        public TextView tv_kline_low;
        public TextView tv_kline_close;
        public TextView tv_kline_rate;
        public TextView tv_ma5;
        public TextView tv_ma10;
        public TextView tv_ma20;
        public LinearLayout lin_ma;
        public KCombinedChart combinedchart;
        public TextView tv_kline_source;
        public KCombinedChart barchart;
        public TextView tv_kline_vol;
        public FrameLayout fl_barchart;
        public AVLoadingIndicatorView aVLoadingIndicatorView;
        public TextView tv_loading;
        public LinearLayout lin_loading;
        public LinearLayout lin_kbg;
        public LinearLayout lin_kline;

        public ViewHolder(View rootView) {
            this.rootView = rootView;

            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_minute = (TextView) rootView.findViewById(R.id.tv_minute);
            this.tv_minute_kline = (TextView) rootView.findViewById(R.id.tv_minute_kline);
            this.tv_5minute_kline = (TextView) rootView.findViewById(R.id.tv_5minute_kline);
            this.tv_hour_kline = (TextView) rootView.findViewById(R.id.tv_hour_kline);
            this.tv_day_kline = (TextView) rootView.findViewById(R.id.tv_day_kline);
            this.lin_time = (DropDownView) rootView.findViewById(R.id.lin_time);
            this.lin_trend = (DropDownView) rootView.findViewById(R.id.lin_trend);
            this.tv_kline_time = (TextView) rootView.findViewById(R.id.tv_kline_time);
            this.tv_kline_open = (TextView) rootView.findViewById(R.id.tv_kline_open);
            this.tv_kline_high = (TextView) rootView.findViewById(R.id.tv_kline_high);
            this.tv_kline_low = (TextView) rootView.findViewById(R.id.tv_kline_low);
            this.tv_kline_close = (TextView) rootView.findViewById(R.id.tv_kline_close);
            this.tv_kline_rate = (TextView) rootView.findViewById(R.id.tv_kline_rate);
            this.tv_ma5 = (TextView) rootView.findViewById(R.id.tv_ma5);
            this.tv_ma10 = (TextView) rootView.findViewById(R.id.tv_ma10);
            this.tv_ma20 = (TextView) rootView.findViewById(R.id.tv_ma20);
            this.lin_ma = (LinearLayout) rootView.findViewById(R.id.lin_ma);
            this.combinedchart = (KCombinedChart) rootView.findViewById(R.id.combinedchart);
            this.tv_kline_source = (TextView) rootView.findViewById(R.id.tv_kline_source);
            this.barchart = (KCombinedChart) rootView.findViewById(R.id.barchart);
            this.tv_kline_vol = (TextView) rootView.findViewById(R.id.tv_kline_vol);
            this.fl_barchart = (FrameLayout) rootView.findViewById(R.id.fl_barchart);
            this.aVLoadingIndicatorView = (AVLoadingIndicatorView) rootView.findViewById(R.id.aVLoadingIndicatorView);
            this.tv_loading = (TextView) rootView.findViewById(R.id.tv_loading);
            this.lin_loading = (LinearLayout) rootView.findViewById(R.id.lin_loading);
            this.lin_kbg = (LinearLayout) rootView.findViewById(R.id.lin_kbg);
            this.lin_kline = (LinearLayout) rootView.findViewById(R.id.lin_kline);
        }

    }
}