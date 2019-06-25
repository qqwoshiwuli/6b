package com.fivefivelike.mybaselibrary.utils.logger.klog;

/**
 * Created by 郭青枫 on 2018/2/3 0003.
 */

public class KlogData {
    String tag;
    String msg;

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMsg() {
        return msg;
    }

    public String getTag() {
        return tag;
    }
}
