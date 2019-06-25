package com.fivefivelike.mybaselibrary.http;

/**
 * Created by 郭青枫 on 2017/7/6.
 */

public interface RequestCallback {
    /**
     * 请求服务器成功
     *
     * @param requestCode
     */
    void success(int requestCode, String data,String errorData);

    /**
     * 请求服务器失败
     *
     * @param requestCode
     * @param exThrowable
     */
    void error(int requestCode, Throwable exThrowable, String errorData);


}
