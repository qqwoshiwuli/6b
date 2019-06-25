package com.wheelview;


import com.fivefivelike.mybaselibrary.entity.AreaObj;

import java.util.List;

public class CityChooseAdapter implements WheelAdapter {
    private List<AreaObj> list;

    public CityChooseAdapter(List<AreaObj> list) {
        super();
        this.list = list;
    }

    @Override
    public int getItemsCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public String getItem(int index) {
        if (index >= 0 && index < list.size()) {//处理换行覆盖问
            String name = list.get(index).getName().toString().trim();
            return name.length() > 5 ? name.substring(5) : name;
        }
        return null;
    }

    @Override
    public int getMaximumLength() {
        return 6;
    }

}
