package com.sixbexchange.entity.bean;

import java.util.List;

/**
 * Created by 郭青枫 on 2019/5/6 0006.
 */

public class SelectExchCoinBean {
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
