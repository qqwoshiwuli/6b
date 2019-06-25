package com.fivefivelike.mybaselibrary.utils.helper;

/**
 * Item移动后 触发
 * Created by YoKeyword on 15/12/28.
 */
public interface OnItemMoveListener extends com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener {
    void onItemMove(int fromPosition, int toPosition);
}
