package com.fivefivelike.mybaselibrary.base;

/**
 * Created by 郭青枫 on 2018/4/2 0002.
 */

public interface BaseWebLinsener {
    void onLoadEndPage();

    void onWebStart();

    void onLoadTitle(String title);

    void getData(String forward,String data);
}
