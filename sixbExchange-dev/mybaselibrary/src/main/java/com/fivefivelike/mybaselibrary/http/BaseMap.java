package com.fivefivelike.mybaselibrary.http;

import java.util.Map;

/**
 * Created by 郭青枫 on 2018/12/21 0021.
 */

public class BaseMap<T,D> {
    Map<T,D> trendDatas;

    public Map<T, D> getTrendDatas() {
        return trendDatas;
    }

    public void setTrendDatas(Map<T, D> trendDatas) {
        this.trendDatas = trendDatas;
    }
}
