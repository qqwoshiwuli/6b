package com.sixbexchange.entity.bean.kline;

import com.github.mikephil.charting.data.CandleEntry;

/**
 * Created by 郭青枫 on 2018/4/9 0009.
 */

public class KCandleEntry extends CandleEntry implements Cloneable{
    public KCandleEntry(int xIndex, float shadowH, float shadowL, float open, float close) {
        super(xIndex, shadowH, shadowL, open, close);
    }

    public KCandleEntry(int xIndex, float shadowH, float shadowL, float open, float close, Object data) {
        super(xIndex, shadowH, shadowL, open, close, data);
    }

    @Override
    public KCandleEntry clone() {
        KCandleEntry stu = null;
        try{
            stu = (KCandleEntry)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
