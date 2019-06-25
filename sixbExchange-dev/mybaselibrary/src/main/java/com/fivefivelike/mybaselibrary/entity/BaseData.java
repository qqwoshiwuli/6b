package com.fivefivelike.mybaselibrary.entity;

/**
 * Created by 郭青枫 on 2018/5/25 0025.
 */

public class BaseData {
    String key;
    String name;
    String value;

    public BaseData(String key,
                    String name,
                    String value) {
        this.key = key;
        this.name = name;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
