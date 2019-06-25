package com.sixbexchange.entity.bean;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/4/12 0012.
 */

public class ExchCurrencyPairBean {
    String name;

    List<TradeDetailBean> list;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<TradeDetailBean> getList() {
        return list;
    }

    public void setList(List<TradeDetailBean> list) {
        this.list = list;
    }
}
