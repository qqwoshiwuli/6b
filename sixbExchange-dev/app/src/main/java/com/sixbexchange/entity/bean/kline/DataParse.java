package com.sixbexchange.entity.bean.kline;

import android.text.TextUtils;

import com.blankj.utilcode.util.TimeUtils;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by loro on 2017/2/8.
 */
public class DataParse {
    private ArrayList<MinutesBean> datas;
    private ArrayList<KLineBean> kDatas = new ArrayList<>();

    private ArrayList<String> xVals = new ArrayList<>();//X轴数据
    private ArrayList<BarEntry> barEntries = new ArrayList<>();//成交量数据
    private ArrayList<CandleEntry> candleEntries = new ArrayList<>();//K线数据


    private ArrayList<Entry> ma1DataL = new ArrayList<>();
    private ArrayList<Entry> ma5DataL = new ArrayList<>();
    // private ArrayList<Entry> ma7DataL = new ArrayList<>();
    private ArrayList<Entry> ma10DataL = new ArrayList<>();
    //private ArrayList<Entry> ma15DataL;
    private ArrayList<Entry> ma20DataL = new ArrayList<>();
    //private ArrayList<Entry> ma30DataL = new ArrayList<>();

    //private ArrayList<Entry> ma1DataV;
    private ArrayList<Entry> ma5DataV = new ArrayList<>();
    //private ArrayList<Entry> ma7DataV;
    //private ArrayList<Entry> ma10DataV = new ArrayList<>();
    //private ArrayList<Entry> ma15DataV;
    //private ArrayList<Entry> ma20DataV;
    //private ArrayList<Entry> ma30DataV;

    String unit = "";
    String symbol = "";

    private List<BarEntry> macdData;
    private List<Entry> deaData;
    private List<Entry> difData;

    private List<BarEntry> barDatasKDJ;
    private List<Entry> kData;
    private List<Entry> dData;
    private List<Entry> jData;

    private List<BarEntry> barDatasWR;
    private List<Entry> wrData13;
    private List<Entry> wrData34;
    private List<Entry> wrData89;

    private List<BarEntry> barDatasRSI;
    private List<Entry> rsiData6;
    private List<Entry> rsiData12;
    private List<Entry> rsiData24;

    private List<BarEntry> barDatasBOLL;
    private List<Entry> bollDataUP;
    private List<Entry> bollDataMB;
    private List<Entry> bollDataDN;

    private List<BarEntry> barDatasEXPMA;
    //private List<Entry> expmaData5;
    //private List<Entry> expmaData7 = new ArrayList<>();
    //private List<Entry> expmaData10;
    //private List<Entry> expmaData20;
    //private List<Entry> expmaData30 = new ArrayList<>();
    //private List<Entry> expmaData60;

    private List<BarEntry> barDatasDMI;
    private List<Entry> dmiDataDI1;
    private List<Entry> dmiDataDI2;
    private List<Entry> dmiDataADX;
    private List<Entry> dmiDataADXR;

    private float baseValue;
    private float permaxmin;
    //private float volmax;
    private String code = "sz002081";
    //private SparseArray<String> xValuesLabel = new SparseArray<>();
    public SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    //List<KLineBean> mKLineBeans;


    public void clear(){

        kDatas.clear();
        xVals.clear();
        barEntries.clear();
        candleEntries.clear();
        ma1DataL.clear();
        ma5DataL.clear();
        ma10DataL.clear();
        ma20DataL.clear();
        ma5DataV.clear();
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getDateTime(long time, String klineType) {
        time = time * 1000;
        String data = klineType;
        if (klineType.contains("1m") || klineType.contains("3m") || (klineType.contains("5m") && !klineType.contains("15m")) || klineType.contains("10m")) {
            //17:33
            data = TimeUtils.millis2String(time, new SimpleDateFormat("HH:mm"));
        } else if (klineType.contains("15m") || klineType.contains("30m") || klineType.contains("1h")) {
            //07/12 12:03
            data = TimeUtils.millis2String(time, new SimpleDateFormat("MM/dd HH:mm"));
        } else if (klineType.contains("1d") || klineType.contains("1w")) {
            //2018/04/12
            data = TimeUtils.millis2String(time, new SimpleDateFormat("yyyy/MM/dd"));
        } else {
            data = TimeUtils.millis2String(time, simpleDateFormat);
        }
        return data;
    }

    public void upDataKline(List<KLineBean> datas, String klineType) {
        ArrayList<BarEntry> barEntriesUpdata = new ArrayList<>();//成交量数据
        ArrayList<CandleEntry> candleEntriesUpdata = new ArrayList<>();//K线数据
        ArrayList<String> xValsUpdata = new ArrayList<>();
        KlineBarEntry klineBarEntry = null;
        KCandleEntry candleEntry = null;
        int size = datas.size();
        for (int i = 0, j = 0; i < size; i++, j++) {
            KLineBean kLineBean = datas.get(i);
            if (TextUtils.isEmpty(kLineBean.date)) {
                kLineBean.date = getDateTime(kLineBean.timestamp, klineType);
            }
            xValsUpdata.add(kLineBean.date);
            if (klineBarEntry != null) {
                klineBarEntry = klineBarEntry.clone();
                klineBarEntry.setXIndex(i);
                klineBarEntry.setClose(kLineBean.close.floatValue());
                klineBarEntry.setHigh(kLineBean.high.floatValue());
                klineBarEntry.setLow(kLineBean.low.floatValue());
                klineBarEntry.setOpen(kLineBean.open.floatValue());
                klineBarEntry.setVal(kLineBean.volume.floatValue());
            } else {
                klineBarEntry = new KlineBarEntry(i, kLineBean.high.floatValue(), kLineBean.low.floatValue(), kLineBean.open.floatValue(), kLineBean.close.floatValue(), kLineBean.volume.floatValue());
            }
            barEntriesUpdata.add(klineBarEntry);
            if (candleEntry != null) {
                candleEntry = candleEntry.clone();
                candleEntry.setClose(kLineBean.close.floatValue());
                candleEntry.setHigh(kLineBean.high.floatValue());
                candleEntry.setLow(kLineBean.low.floatValue());
                candleEntry.setOpen(kLineBean.open.floatValue());
                candleEntry.setVal((kLineBean.high.floatValue() + kLineBean.low.floatValue()) / 2);
                candleEntry.setXIndex(i);
            } else {
                candleEntry = new KCandleEntry(i, kLineBean.high.floatValue(), kLineBean.low.floatValue(), kLineBean.open.floatValue(), kLineBean.close.floatValue());
            }
            candleEntry.setData(kLineBean.getTimestamp());
            candleEntriesUpdata.add(candleEntry);
        }

        //        barEntriesUpdata.addAll(barEntries);
        //        barEntries = barEntriesUpdata;
        //
        //        candleEntriesUpdata.addAll(candleEntries);
        //        candleEntries = candleEntriesUpdata;

        candleEntries.addAll(0, candleEntriesUpdata);
        barEntries.addAll(0, barEntriesUpdata);

        xVals.addAll(0, xValsUpdata);
        kDatas.addAll(0, datas);

        int size1 = kDatas.size();
        for (int i = 0; i < size1; i++) {
            barEntries.get(i).setXIndex(i);
            candleEntries.get(i).setXIndex(i);
        }

        initKLineMA(kDatas);
        //initEXPMA(kDatas);
        initVlumeMA(kDatas);


    }




    //得到成交量 只用一次
    public void initLineDatas(List<KLineBean> datas) {
        if (null == datas) {
            return;
        }

        int size = datas.size();
        KlineBarEntry klineBarEntry = null;
        KCandleEntry candleEntry = null;
        for (int i = 0, j = 0; i < size; i++, j++) {
            KLineBean kLineBean = datas.get(i);
            xVals.add(kLineBean.date + "");
            if (klineBarEntry != null) {
                klineBarEntry = klineBarEntry.clone();
                klineBarEntry.setXIndex(i);
                klineBarEntry.setClose(kLineBean.close.floatValue());
                klineBarEntry.setHigh(kLineBean.high.floatValue());
                klineBarEntry.setLow(kLineBean.low.floatValue());
                klineBarEntry.setOpen(kLineBean.open.floatValue());
                klineBarEntry.setVal(kLineBean.volume.floatValue());
            } else {
                klineBarEntry = new KlineBarEntry(i, kLineBean.high.floatValue(), kLineBean.low.floatValue(), kLineBean.open.floatValue(), kLineBean.close.floatValue(), kLineBean.volume.floatValue());
            }
            barEntries.add(klineBarEntry);
            if (candleEntry != null) {
                candleEntry = candleEntry.clone();
                candleEntry.setClose(kLineBean.close.floatValue());
                candleEntry.setHigh(kLineBean.high.floatValue());
                candleEntry.setLow(kLineBean.low.floatValue());
                candleEntry.setOpen(kLineBean.open.floatValue());
                candleEntry.setVal((kLineBean.high.floatValue() + kLineBean.low.floatValue()) / 2);
                candleEntry.setXIndex(i);
            } else {
                candleEntry = new KCandleEntry(i, kLineBean.high.floatValue(), kLineBean.low.floatValue(), kLineBean.open.floatValue(), kLineBean.close.floatValue());
            }
            candleEntry.setData(kLineBean.getTimestamp());
            candleEntries.add(candleEntry);
        }
    }

    //数据处理
    public List<KLineBean> onlyParseKLine(List<KLineBean> kLineBeans,String klineType) {
        List<KLineBean> list = new ArrayList<>();
        if (kLineBeans.size() > 0) {
            //xValuesLabel.clear();
            int count = kLineBeans.size();
            for (int i = 0; i < count; i++) {
                KLineBean kLineBean = kLineBeans.get(i);
                if (TextUtils.isEmpty(kLineBean.date)) {
                    kLineBean.date = getDateTime(kLineBean.timestamp, klineType);                    //kLineBeans.get(i).setNewDate(TimeUtils.string2Date(kLineBeans.get(i).date));
                }

                //xValuesLabel.put(i, kLineBean.date);
                list.add(kLineBean);
            }

        }
        return list;
    }

    private BigDecimal usdRate(KlineRateBean klineRateBean, String unit) {
        if (klineRateBean.getSymbol().contains("USD") && unit.contains("USD")) {
            return new BigDecimal("1");
        }
        return klineRateBean.getRate();
    }

    //当前CNY_USDT汇率
//    private BigDecimal cnyRate(BigDecimal rate, String unit) {
//        if ("CNY".equals(unit)) {
//            if (UserSet.show_usd.equals(UserSet.getinstance().getShowUSdtPrice())) {
//                //USD_USDT
//                BigDecimal usdtAndUsdPrice = BigUIUtil.getinstance().getUsdtAndUsdPrice();
//                rate = rate.multiply(usdtAndUsdPrice);//rate.divide(usdtAndUsdPrice, 8, BigDecimal.ROUND_DOWN);
//            } else if (UserSet.show_customize_usdt.equals(UserSet.getinstance().getShowUSdtPrice())) {
//                //CNY_USD 自定义
//                BigDecimal cnyAndUsdPrice = BigUIUtil.getinstance().getCnyAndUsdPrice();
//                rate = new BigDecimal("1").divide(cnyAndUsdPrice, 8, BigDecimal.ROUND_DOWN);
//            }
//        }
//        //        else if ("USD".equals(unit)) {//USDT_USD
//        //            return new BigDecimal("1");
//        //        }
//        return rate;
//    }

    //数据处理
    public void parseKLine(List<KLineBean> kLineBeans, String klineType) {
        if (kLineBeans.size() > 0) {
            kDatas.clear();
            //xValuesLabel.clear();
            int count = kLineBeans.size();
            int rate1Select = 0;
            int rate2Select = 0;
            for (int i = 0; i < count; i++) {
                KLineBean kLineBean = kLineBeans.get(i);
                if (TextUtils.isEmpty(kLineBean.date)) {
                    kLineBean.date = getDateTime(kLineBean.timestamp, klineType);                    //kLineBeans.get(i).setNewDate(TimeUtils.string2Date(kLineBeans.get(i).date));
                }

                //xValuesLabel.put(i, kLineBean.date);
                kDatas.add(kLineBean);
            }
        }
    }


    /**
     * 初始化K线图均线
     *
     * @param datas
     */
    public void initKLineMA(ArrayList<KLineBean> datas) {
        if (null == datas) {
            return;
        }
        ma1DataL.clear();
        //        if (ma5DataL == null) {
        //            ma5DataL = new ArrayList<>();
        //        } else {
        ma5DataL.clear();
        //        }
        //ma7DataL.clear();
        //        if (ma10DataL == null) {
        //            ma10DataL = new ArrayList<>();
        //        } else {
        ma10DataL.clear();
        //        }
        //        if (ma15DataL == null) {
        //            ma15DataL = new ArrayList<>();
        //        } else {
        //            ma15DataL.clear();
        //        }
        //        if (ma20DataL == null) {
        //            ma20DataL = new ArrayList<>();
        //        } else {
        ma20DataL.clear();
        //        }
        // ma30DataL.clear();
        KMAEntity kmaEntity1 = new KMAEntity(datas, 1);
        KMAEntity kmaEntity5 = new KMAEntity(datas, 5);
        //KMAEntity kmaEntity7 = new KMAEntity(datas, 7);
        KMAEntity kmaEntity10 = new KMAEntity(datas, 10);
        //        KMAEntity kmaEntity15 = new KMAEntity(datas, 15);
        KMAEntity kmaEntity20 = new KMAEntity(datas, 20);
        //KMAEntity kmaEntity30 = new KMAEntity(datas, 30);
        int size = kmaEntity1.getMAs().size();
        KEntry kEntry = new KEntry(1, 1);
        for (int i = 0; i < size; i++) {
            Entry ma1Entry = kEntry.clone();
            ma1Entry.setXIndex(i);
            ma1Entry.setVal(kmaEntity1.getMAs().get(i));
            ma1DataL.add(ma1Entry);

            Entry ma5Entry = kEntry.clone();
            ma5Entry.setXIndex(i);
            ma5Entry.setVal(kmaEntity5.getMAs().get(i));
            ma5DataL.add(ma5Entry);


            Entry ma10Entry = kEntry.clone();
            ma10Entry.setXIndex(i);
            ma10Entry.setVal(kmaEntity10.getMAs().get(i));
            ma10DataL.add(ma10Entry);


            Entry ma20Entry = kEntry.clone();
            ma20Entry.setXIndex(i);
            ma20Entry.setVal(kmaEntity20.getMAs().get(i));
            ma20DataL.add(ma20Entry);


            //            Entry ma7Entry = kEntry.clone();
            //            ma7Entry.setXIndex(i);
            //            ma7Entry.setVal(kmaEntity7.getMAs().get(i));
            //            ma7DataL.add(ma7Entry);


            //ma10DataL.add(new Entry(kmaEntity10.getMAs().get(i), i));
            //ma15DataL.add(new Entry(kmaEntity15.getMAs().get(i), i));
            //ma20DataL.add(new Entry(kmaEntity20.getMAs().get(i), i));

            //            Entry ma30Entry = kEntry.clone();
            //            ma30Entry.setXIndex(i);
            //            ma30Entry.setVal(kmaEntity30.getMAs().get(i));
            //            ma30DataL.add(ma30Entry);


        }
    }

    /**
     * 初始化成交量均线
     *
     * @param datas
     */
    public void initVlumeMA(ArrayList<KLineBean> datas) {
        if (null == datas) {
            return;
        }
        //        if (ma1DataV == null) {
        //            ma1DataV = new ArrayList<>();
        //        } else {
        //            ma1DataV.clear();
        //        }
        ma5DataV.clear();
        //        if (ma7DataV == null) {
        //            ma7DataV = new ArrayList<>();
        //        } else {
        //            ma7DataV.clear();
        //        }
        //ma10DataV.clear();
        //        if (ma15DataV == null) {
        //            ma15DataV = new ArrayList<>();
        //        } else {
        //            ma15DataV.clear();
        //        }
        //        if (ma20DataV == null) {
        //            ma20DataV = new ArrayList<>();
        //        } else {
        //            ma20DataV.clear();
        //        }
        //        if (ma30DataV == null) {
        //            ma30DataV = new ArrayList<>();
        //        } else {
        //            ma30DataV.clear();
        //        }

        //        VMAEntity vmaEntity1 = new VMAEntity(datas, 1);
        VMAEntity vmaEntity5 = new VMAEntity(datas, 5);
        //        VMAEntity vmaEntity7 = new VMAEntity(datas, 7);
        //VMAEntity vmaEntity10 = new VMAEntity(datas, 10);
        //        VMAEntity vmaEntity15 = new VMAEntity(datas, 15);
        //        VMAEntity vmaEntity20 = new VMAEntity(datas, 20);
        //        VMAEntity vmaEntity30 = new VMAEntity(datas, 30);
        int size = vmaEntity5.getMAs().size();
        KEntry kEntry = new KEntry(1, 1);
        for (int i = 0; i < size; i++) {
            //ma1DataV.add(new Entry(vmaEntity1.getMAs().get(i), i));

            Entry ma5Entry = kEntry.clone();
            ma5Entry.setXIndex(i);
            ma5Entry.setVal(vmaEntity5.getMAs().get(i));
            ma5DataV.add(ma5Entry);

            //ma7DataV.add(new Entry(vmaEntity7.getMAs().get(i), i));

            //            Entry ma10Entry = kEntry.clone();
            //            ma10Entry.setXIndex(i);
            //            ma10Entry.setVal(vmaEntity10.getMAs().get(i));
            //            ma10DataV.add(ma10Entry);

            //ma15DataV.add(new Entry(vmaEntity15.getMAs().get(i), i));
            //ma20DataV.add(new Entry(vmaEntity20.getMAs().get(i), i));
            //ma30DataV.add(new Entry(vmaEntity30.getMAs().get(i), i));
        }
    }

    //    /**
    //     * 初始化MACD
    //     *
    //     * @param datas
    //     */
    //    public void initMACD(ArrayList<KLineBean> datas) {
    //        MACDEntity macdEntity = new MACDEntity(datas);
    //
    //        macdData = new ArrayList<>();
    //        deaData = new ArrayList<>();
    //        difData = new ArrayList<>();
    //        for (int i = 0; i < macdEntity.getMACD().size(); i++) {
    //            macdData.add(new BarEntry(macdEntity.getMACD().get(i), i));
    //            deaData.add(new Entry(macdEntity.getDEA().get(i), i));
    //            difData.add(new Entry(macdEntity.getDIF().get(i), i));
    //        }
    //    }
    //
    //    /**
    //     * 初始化KDJ
    //     *
    //     * @param datas
    //     */
    //    public void initKDJ(ArrayList<KLineBean> datas) {
    //        KDJEntity kdjEntity = new KDJEntity(datas, 9);
    //
    //        barDatasKDJ = new ArrayList<>();
    //        kData = new ArrayList<>();
    //        dData = new ArrayList<>();
    //        jData = new ArrayList<>();
    //        for (int i = 0; i < kdjEntity.getD().size(); i++) {
    //            barDatasKDJ.add(new BarEntry(0, i));
    //            kData.add(new Entry(kdjEntity.getK().get(i), i));
    //            dData.add(new Entry(kdjEntity.getD().get(i), i));
    //            jData.add(new Entry(kdjEntity.getJ().get(i), i));
    //        }
    //    }
    //
    //    /**
    //     * 初始化WR
    //     *
    //     * @param datas
    //     */
    //    public void initWR(ArrayList<KLineBean> datas) {
    //        WREntity wrEntity13 = new WREntity(datas, 13);
    //        WREntity wrEntity34 = new WREntity(datas, 34);
    //        WREntity wrEntity89 = new WREntity(datas, 89);
    //
    //        barDatasWR = new ArrayList<>();
    //        wrData13 = new ArrayList<>();
    //        wrData34 = new ArrayList<>();
    //        wrData89 = new ArrayList<>();
    //        for (int i = 0; i < wrEntity13.getWRs().size(); i++) {
    //            barDatasWR.add(new BarEntry(0, i));
    //            wrData13.add(new Entry(wrEntity13.getWRs().get(i), i));
    //            wrData34.add(new Entry(wrEntity34.getWRs().get(i), i));
    //            wrData89.add(new Entry(wrEntity89.getWRs().get(i), i));
    //        }
    //    }
    //
    //    /**
    //     * 初始化RSI
    //     *
    //     * @param datas
    //     */
    //    public void initRSI(ArrayList<KLineBean> datas) {
    //        RSIEntity rsiEntity6 = new RSIEntity(datas, 6);
    //        RSIEntity rsiEntity12 = new RSIEntity(datas, 12);
    //        RSIEntity rsiEntity24 = new RSIEntity(datas, 24);
    //
    //        barDatasRSI = new ArrayList<>();
    //        rsiData6 = new ArrayList<>();
    //        rsiData12 = new ArrayList<>();
    //        rsiData24 = new ArrayList<>();
    //        for (int i = 0; i < rsiEntity6.getRSIs().size(); i++) {
    //            barDatasRSI.add(new BarEntry(0, i));
    //            rsiData6.add(new Entry(rsiEntity6.getRSIs().get(i), i));
    //            rsiData12.add(new Entry(rsiEntity12.getRSIs().get(i), i));
    //            rsiData24.add(new Entry(rsiEntity24.getRSIs().get(i), i));
    //        }
    //    }
    //

    /**
     * 初始化BOLL
     *
     * @param datas
     */
    public void initBOLL(ArrayList<KLineBean> datas) {
        BOLLEntity bollEntity = new BOLLEntity(datas, 20);

        if (barDatasBOLL == null) {
            barDatasBOLL = new ArrayList<>();
        } else {
            barDatasBOLL.clear();
        }
        if (bollDataUP == null) {
            bollDataUP = new ArrayList<>();
        } else {
            bollDataUP.clear();
        }
        if (bollDataMB == null) {
            bollDataMB = new ArrayList<>();
        } else {
            bollDataMB.clear();
        }
        if (bollDataDN == null) {
            bollDataDN = new ArrayList<>();
        } else {
            bollDataDN.clear();
        }
        for (int i = 0; i < bollEntity.getUPs().size(); i++) {
            barDatasBOLL.add(new BarEntry(0, i));
            bollDataUP.add(new Entry(bollEntity.getUPs().get(i), i));
            bollDataMB.add(new Entry(bollEntity.getMBs().get(i), i));
            bollDataDN.add(new Entry(bollEntity.getDNs().get(i), i));
        }
    }

    /**
     * 初始化BOLL
     *
     * @param datas
     */
    public void initEXPMA(ArrayList<KLineBean> datas) {
        //        //EXPMAEntity expmaEntity5 = new EXPMAEntity(datas, 5);
        //        EXPMAEntity expmaEntity7 = new EXPMAEntity(datas, 7);
        //        //EXPMAEntity expmaEntity10 = new EXPMAEntity(datas, 10);
        //        //EXPMAEntity expmaEntity20 = new EXPMAEntity(datas, 20);
        //        EXPMAEntity expmaEntity30 = new EXPMAEntity(datas, 30);
        //        //EXPMAEntity expmaEntity60 = new EXPMAEntity(datas, 60);
        //
        //        //        if (barDatasEXPMA == null) {
        //        //            barDatasEXPMA = new ArrayList<>();
        //        //        } else {
        //        //            barDatasEXPMA.clear();
        //        //        }
        //        expmaData7.clear();
        //        //        if (expmaData5 == null) {
        //        //            expmaData5 = new ArrayList<>();
        //        //        } else {
        //        //            expmaData5.clear();
        //        //        }
        //        //        if (expmaData10 == null) {
        //        //            expmaData10 = new ArrayList<>();
        //        //        } else {
        //        //            expmaData10.clear();
        //        //        }
        //        //        if (expmaData20 == null) {
        //        //            expmaData20 = new ArrayList<>();
        //        //        } else {
        //        //            expmaData20.clear();
        //        //        }
        //        expmaData30.clear();
        //        //        if (expmaData60 == null) {
        //        //            expmaData60 = new ArrayList<>();
        //        //        } else {
        //        //            expmaData60.clear();
        //        //        }
        //        KEntry kEntry = new KEntry(1, 1);
        //        for (int i = 0; i < expmaEntity7.getEXPMAs().size(); i++) {
        //            //barDatasEXPMA.add(new BarEntry(0, i));
        //            //expmaData5.add(new Entry(expmaEntity5.getEXPMAs().get(i), i));
        //            Entry expmaData7Entry = kEntry.clone();
        //            expmaData7Entry.setXIndex(i);
        //            expmaData7Entry.setVal(expmaEntity7.getEXPMAs().get(i));
        //            expmaData7.add(expmaData7Entry);
        //            //expmaData10.add(new Entry(expmaEntity10.getEXPMAs().get(i), i));
        //            //expmaData20.add(new Entry(expmaEntity20.getEXPMAs().get(i), i));
        //            Entry expmaData30Entry = kEntry.clone();
        //            expmaData30Entry.setXIndex(i);
        //            expmaData30Entry.setVal(expmaEntity30.getEXPMAs().get(i));
        //            expmaData30.add(expmaData30Entry);
        //            //expmaData60.add(new Entry(expmaEntity60.getEXPMAs().get(i), i));
        //        }
    }
    //
    //    /**
    //     * 初始化DMI
    //     *
    //     * @param datas
    //     */
    //    public void initDMI(ArrayList<KLineBean> datas) {
    //        DMIEntity dmiEntity = new DMIEntity(datas, 12, 7, 6, true);
    //
    //        barDatasDMI = new ArrayList<>();
    //        dmiDataDI1 = new ArrayList<>();
    //        dmiDataDI2 = new ArrayList<>();
    //        dmiDataADX = new ArrayList<>();
    //        dmiDataADXR = new ArrayList<>();
    //        for (int i = 0; i < dmiEntity.getDI1s().size(); i++) {
    //            barDatasDMI.add(new BarEntry(0, i));
    //            dmiDataDI1.add(new Entry(dmiEntity.getDI1s().get(i), i));
    //            dmiDataDI2.add(new Entry(dmiEntity.getDI2s().get(i), i));
    //            dmiDataADX.add(new Entry(dmiEntity.getADXs().get(i), i));
    //            dmiDataADXR.add(new Entry(dmiEntity.getADXRs().get(i), i));
    //        }
    //    }

    /**
     * 得到Y轴最小值
     *
     * @return
     */
    public float getMin() {
        return baseValue - permaxmin;
    }

    /**
     * 得到Y轴最大值
     *
     * @return
     */
    public float getMax() {
        return baseValue + permaxmin;
    }

    /**
     * 得到百分百最大值
     *
     * @return
     */
    public float getPercentMax() {
        return permaxmin / baseValue;
    }

    /**
     * 得到百分比最小值
     *
     * @return
     */
    public float getPercentMin() {
        return -getPercentMax();
    }


    /**
     * 得到分时图数据
     *
     * @return
     */
    public ArrayList<MinutesBean> getDatas() {
        return datas;
    }

    /**
     * 得到K线图数据
     *
     * @return
     */
    public ArrayList<KLineBean> getKLineDatas() {
        return kDatas;
    }

    /**
     * 得到X轴数据
     *
     * @return
     */
    public ArrayList<String> getXVals() {
        return xVals;
    }

    /**
     * 得到K线数据
     *
     * @return
     */
    public ArrayList<CandleEntry> getCandleEntries() {
        return candleEntries;
    }

    /**
     * 得到成交量数据
     *
     * @return
     */
    public ArrayList<BarEntry> getBarEntries() {
        return barEntries;
    }

    public ArrayList<Entry> getMa1DataL() {
        return ma1DataL;
    }

    /**
     * 得到K线图5日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa5DataL() {
    //        return ma5DataL;
    //    }


    /**
     * 得到K线图10日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa10DataL() {
    //        return ma10DataL;
    //    }

    /**
     * 得到K线图20日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa20DataL() {
    //        return ma20DataL;
    //    }

    /**
     * 得到K线图30日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa30DataL() {
    //        return ma30DataL;
    //    }


    /**
     * 得到成交量5日均线
     *
     * @return
     */
    public ArrayList<Entry> getMa5DataV() {
        return ma5DataV;
    }

    /**
     * 得到成交量10日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa10DataV() {
    //        return ma10DataV;
    //    }

    /**
     * 得到成交量20日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa20DataV() {
    //        return ma20DataV;
    //    }

    /**
     * 得到K线图30日均线
     *
     * @return
     */
    //    public ArrayList<Entry> getMa30DataV() {
    //        return ma30DataV;
    //    }

    /**
     * 得到MACD bar
     *
     * @return
     */
    public List<BarEntry> getMacdData() {
        return macdData;
    }

    /**
     * 得到MACD dea
     *
     * @return
     */
    public List<Entry> getDeaData() {
        return deaData;
    }

    /**
     * 得到MACD dif
     *
     * @return
     */
    public List<Entry> getDifData() {
        return difData;
    }

    /**
     * 得到KDJ bar
     *
     * @return
     */
    public List<BarEntry> getBarDatasKDJ() {
        return barDatasKDJ;
    }

    /**
     * 得到DKJ k
     *
     * @return
     */
    public List<Entry> getkData() {
        return kData;
    }

    /**
     * 得到KDJ d
     *
     * @return
     */
    public List<Entry> getdData() {
        return dData;
    }

    /**
     * 得到KDJ j
     *
     * @return
     */
    public List<Entry> getjData() {
        return jData;
    }

    /**
     * 得到WR bar
     *
     * @return
     */
    public List<BarEntry> getBarDatasWR() {
        return barDatasWR;
    }

    /**
     * 得到WR 13
     *
     * @return
     */
    public List<Entry> getWrData13() {
        return wrData13;
    }

    /**
     * 得到WR 34
     *
     * @return
     */
    public List<Entry> getWrData34() {
        return wrData34;
    }

    /**
     * 得到WR 89
     *
     * @return
     */
    public List<Entry> getWrData89() {
        return wrData89;
    }

    /**
     * 得到RSI bar
     *
     * @return
     */
    public List<BarEntry> getBarDatasRSI() {
        return barDatasRSI;
    }

    /**
     * 得到RSI 6
     *
     * @return
     */
    public List<Entry> getRsiData6() {
        return rsiData6;
    }

    /**
     * 得到RSI 12
     *
     * @return
     */
    public List<Entry> getRsiData12() {
        return rsiData12;
    }

    /**
     * 得到RSI 24
     *
     * @return
     */
    public List<Entry> getRsiData24() {
        return rsiData24;
    }

    public List<BarEntry> getBarDatasBOLL() {
        return barDatasBOLL;
    }

    public List<Entry> getBollDataUP() {
        return bollDataUP;
    }

    public List<Entry> getBollDataMB() {
        return bollDataMB;
    }

    public List<Entry> getBollDataDN() {
        return bollDataDN;
    }

    public List<BarEntry> getBarDatasEXPMA() {
        return barDatasEXPMA;
    }

    //    public List<Entry> getExpmaData5() {
    //        return expmaData5;
    //    }

    //    public List<Entry> getExpmaData7() {
    //        return expmaData7;
    //    }

    //    public List<Entry> getExpmaData10() {
    //        return expmaData10;
    //    }

    //    public List<Entry> getExpmaData20() {
    //        return expmaData20;
    //    }

    //    public List<Entry> getExpmaData30() {
    //        return expmaData30;
    //    }

    //    public List<Entry> getExpmaData60() {
    //        return expmaData60;
    //    }

    public List<BarEntry> getBarDatasDMI() {
        return barDatasDMI;
    }

    public List<Entry> getDmiDataDI1() {
        return dmiDataDI1;
    }

    public List<Entry> getDmiDataDI2() {
        return dmiDataDI2;
    }

    public List<Entry> getDmiDataADX() {
        return dmiDataADX;
    }

    public List<Entry> getDmiDataADXR() {
        return dmiDataADXR;
    }

    public void setMa1DataL(ArrayList<Entry> ma1DataL) {
        this.ma1DataL = ma1DataL;
    }

    public ArrayList<Entry> getMa5DataL() {
        return ma5DataL;
    }

    public void setMa5DataL(ArrayList<Entry> ma5DataL) {
        this.ma5DataL = ma5DataL;
    }

    public ArrayList<Entry> getMa10DataL() {
        return ma10DataL;
    }

    public void setMa10DataL(ArrayList<Entry> ma10DataL) {
        this.ma10DataL = ma10DataL;
    }

    public ArrayList<Entry> getMa20DataL() {
        return ma20DataL;
    }

    public void setMa20DataL(ArrayList<Entry> ma20DataL) {
        this.ma20DataL = ma20DataL;
    }
    //    public ArrayList<Entry> getMa7DataL() {
    //        return ma7DataL;
    //    }

    //    public ArrayList<Entry> getMa15DataL() {
    //        return ma15DataL;
    //    }
    //
    //    public ArrayList<Entry> getMa7DataV() {
    //        return ma7DataV;
    //    }
    //
    //    public ArrayList<Entry> getMa15DataV() {
    //        return ma15DataV;
    //    }
    //
    //    public ArrayList<Entry> getMa1DataV() {
    //        return ma1DataV;
    //    }
}
