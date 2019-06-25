package com.fivefivelike.mybaselibrary.http;

/**
 * Created by 郭青枫 on 2018/4/18 0018.
 */

public interface PullDataError {

    /**
     * 停止加载下拉刷新
     *
     */
    void onStopLoading();

    /**
     * page数量 减1  加载状态false
     *
     */
    void pageReduction();

}
