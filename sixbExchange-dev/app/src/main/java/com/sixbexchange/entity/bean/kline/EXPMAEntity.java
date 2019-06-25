package com.sixbexchange.entity.bean.kline;

import java.util.ArrayList;

/**
 * Created by loro on 2017/3/8.
 */

public class EXPMAEntity {

    private ArrayList<Float> EXPMAs;

    public EXPMAEntity(ArrayList<KLineBean> kLineBeens, int n) {
        EXPMAs = new ArrayList<>();

        float ema = 0.0f;
        float t = n + 1;
        float yz = 2 / t;
        if (kLineBeens != null && kLineBeens.size() > 0) {
            int size = kLineBeens.size();
            for (int i = 0; i < size; i++) {
                KLineBean kLineBean = kLineBeens.get(i);
                if (i == 0) {
                    ema = kLineBean.close.floatValue();
                } else {
                    ema = (yz * kLineBean.close.floatValue()) + ((1 - yz) * ema);
                    //                    ema = (kLineBeens.get(i).close - ema) * (2 / (n + 1)) + ema;
                }
                EXPMAs.add(ema);
            }
        }
    }

    public ArrayList<Float> getEXPMAs() {
        return EXPMAs;
    }
}
