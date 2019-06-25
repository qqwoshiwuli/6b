package com.sixbexchange.entity.bean.kline;

import android.os.Parcel;

import com.github.mikephil.charting.data.Entry;

/**
 * Created by 郭青枫 on 2018/4/9 0009.
 */

public class KEntry extends Entry implements Cloneable{
    public KEntry(float val, int xIndex) {
        super(val, xIndex);
    }

    public KEntry(float val, int xIndex, Object data) {
        super(val, xIndex, data);
    }

    protected KEntry(Parcel in) {
        super(in);
    }

    @Override
    public KEntry clone() {
        KEntry stu = null;
        try{
            stu = (KEntry)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return stu;
    }
}
