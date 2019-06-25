package com.fivefivelike.mybaselibrary.adapter.entity;

/**
 * Created by 郭青枫 on 2017/9/30.
 */

public class ShareItemEntity {
    private String name;
    private String platfornName;
    private int res;
    private String packageName;
    private String activityName;
    private boolean isSystemShare;
    public ShareItemEntity() {
    }

    /**
     * 调用系统分享的构造器
     * @param name
     * @param platfornName
     * @param res
     * @param packageName
     * @param activityName
     */
    public ShareItemEntity(String name, String platfornName, int res, String packageName, String activityName) {
        this.name = name;
        this.platfornName = platfornName;
        this.res = res;
        this.packageName = packageName;
        this.activityName = activityName;
        this.isSystemShare=true;
    }

    /**
     * 调用sharesdk的构造器
     * @param name
     * @param platfornName
     * @param res
     */
    public ShareItemEntity(String name, String platfornName, int res) {
        this.name = name;
        this.res = res;
        this.platfornName=platfornName;
        this.isSystemShare=false;
    }

    public String getPlatfornName() {
        return platfornName;
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean isSystemShare() {
        return isSystemShare;
    }

    public void setSystemShare(boolean systemShare) {
        isSystemShare = systemShare;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public void setPlatfornName(String platfornName) {
        this.platfornName = platfornName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getRes() {
        return res;
    }

    public void setRes(int res) {
        this.res = res;
    }
}
