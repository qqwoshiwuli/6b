package com.fivefivelike.mybaselibrary.http;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by 郭青枫 on 2018/5/2 0002.
 */

public class BaseResponse {

    boolean isSuccess;

    @JSONField(name = "dialog")
    String info;
    @JSONField(name = "code")
    int status;
    @JSONField(name = "data")
    String data;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
