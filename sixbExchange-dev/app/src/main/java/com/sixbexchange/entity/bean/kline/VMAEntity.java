package com.sixbexchange.entity.bean.kline;


import com.sixbexchange.utils.NdkUtils;

import java.util.ArrayList;

import static java.lang.Float.NaN;

/**
 * Created by loro on 2017/3/7.
 */

public class VMAEntity {

    private ArrayList<Float> MAs;

    /**
     * 得到已N日为单位的均值
     *
     * @param kLineBeen
     * @param n         几日均值
     */
    public VMAEntity(ArrayList<KLineBean> kLineBeen, int n) {
        this(kLineBeen, n, NaN);
    }

    /**
     * 得到已N日为单位的均值
     *
     * @param kLineBeen
     * @param n         几日均值
     * @param defult    不足N日时的默认值
     */
    public VMAEntity(ArrayList<KLineBean> kLineBeen, int n, float defult) {
        MAs = new ArrayList<Float>();
        float ma = 0.0f;
        int index = n - 1;
        float[] nums = new float[kLineBeen.size()];
        for (int i = 0; i < kLineBeen.size(); i++) {
            nums[i] = kLineBeen.get(i).volume.floatValue();
        }
        if (kLineBeen != null && kLineBeen.size() > 0) {
            for (int i = 0; i < kLineBeen.size(); i++) {
                if (i >= index) {
                    ma = NdkUtils.getSum(i - index, i, nums, n);
                } else {
                    ma = defult;
                }
                MAs.add(ma);
            }
        }
    }




//    private float getSum(Integer a, Integer b, ArrayList<KLineBean> datas) {
//        float sum = 0;
//        for (int i = a; i <= b; i++) {
//            sum += datas.get(i).volume.floatValue();
//        }
//        return sum;
//    }
//
//    public float getLastMA(ArrayList<KLineBean> datas, int n) {
//        if (null != datas && datas.size() > 0) {
//            int count = datas.size() - 1;
//            int index = n - 1;
//            if (datas.size() >= n) {
//                return getSum(count - index, count, datas) / n;
//            }
//            return NaN;
//        }
//        return NaN;
//    }

    public ArrayList<Float> getMAs() {
        return MAs;
    }

}
