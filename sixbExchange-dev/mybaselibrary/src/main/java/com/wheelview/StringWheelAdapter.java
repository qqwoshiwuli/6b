package com.wheelview;


import java.util.List;

public class StringWheelAdapter implements WheelAdapter {
    private List<String> list;
    int size=6;

    public void setSize(int size) {
        this.size = size;
    }

    public StringWheelAdapter(List<String> list) {
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
            String name = list.get(index);
            return name;
        }
        return null;
    }

    @Override
    public int getMaximumLength() {
        return size;
    }

}
