package com.fivefivelike.mybaselibrary.http;

import java.util.List;

/**
 * Created by 郭青枫 on 2018/12/21 0021.
 */

public class BaseList<T> {
    List<T> datas;

    public List<T> getDatas() {
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
    }
}
