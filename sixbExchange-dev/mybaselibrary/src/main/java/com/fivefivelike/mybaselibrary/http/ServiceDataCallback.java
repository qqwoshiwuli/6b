package com.fivefivelike.mybaselibrary.http;

import org.json.JSONException;

/**
 * Created by 郭青枫 on 2017/12/27 0027.
 */

public interface ServiceDataCallback {

    /**
     * 请求成功后 返回数据 status 为成功
     * @param data
     * @param info
     * @param status
     * @param requestCode
     */
    void onDataSuccess(String data, String info, int status, int requestCode) throws JSONException;

    void onDataError(String data, String info, int status, int requestCode);

}
