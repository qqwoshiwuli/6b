package com.fivefivelike.mybaselibrary.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Activity 统一管理类
 */
public class ActUtil {
    private static Stack<Activity> mActivityStack;
    private static ActUtil mActUtil;

    private ActUtil() {
    }

    /**
     * 单一实例
     */
    public static ActUtil getInstance() {
        if (mActUtil == null) {
            synchronized (ActUtil.class) {
                if (mActUtil == null) {
                    mActUtil = new ActUtil();
                }
            }
        }
        return mActUtil;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack<>();
        }
        mActivityStack.add(activity);
    }

    /**
     * 获取栈顶Activity（堆栈中最后一个压入的）
     */
    public Activity getTopActivity() {
        Activity activity = mActivityStack.lastElement();
        return activity;
    }

    /**
     * 结束栈顶Activity（堆栈中最后一个压入的）
     */
    public void killTopActivity() {
        Activity activity = mActivityStack.lastElement();
        killActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void killActivity(Activity activity) {
        if (activity != null && mActivityStack != null) {
            mActivityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 从栈中移除activity
     *
     * @param activity
     */
    public void removeActivitiyFromStack(Activity activity) {
        if (mActivityStack != null) {
            if (mActivityStack.contains(activity)) {
                mActivityStack.remove(activity);
                if (!activity.isFinishing()) {
                    activity.finish();
                }
            }
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void killActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    killActivity(activity);
                }
            }
        }
    }

    /**
     * 判断页面是否在站中
     */
    public boolean isHaveActivity(Class<?> cls) {
        if (mActivityStack != null) {
            for (Activity activity : mActivityStack) {
                if (activity.getClass().equals(cls)) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 结束除了指定的页面之外的所有页面
     *
     * @param activityStr
     */
    public void killAllActivity(Activity... activityStr) {
        if (mActivityStack != null) {
            List<Integer> list = new ArrayList<>();
            for (int i = 0; i < mActivityStack.size(); i++) {
                Activity itemAct = mActivityStack.get(i);
                if (null != itemAct) {
                    if (judgeIsContainActivity(itemAct, activityStr)) {
                        continue;
                    } else {
                        itemAct.finish();
                        list.add(i);
                    }
                }
            }
            int size = list.size();
            for (int i = 0; i < size; i++) {
                mActivityStack.remove(list.get(i));
            }
        }
    }

    /**
     * 判断可变数组中是否包含页面
     *
     * @param activity
     * @param activityStr 可变数组
     * @return
     */
    private boolean judgeIsContainActivity(Activity activity, Activity... activityStr) {
        if (activity != null) {
            for (Activity item : activityStr) {
                if (item.equals(activity)) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * 退出应用程序
     */
    @SuppressWarnings("deprecation")
    public void AppExit(Context context) {
        try {
            killAllActivity(new Activity[0]);
            ActivityManager activityMgr = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.killBackgroundProcesses(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }
}
