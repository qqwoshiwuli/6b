package com.sixbexchange.entity.bean;

/**
 * Created by 郭青枫 on 2019/4/15 0015.
 */

public class ExchCoinBean {
    /**
     * id : null
     * name : btc
     * imgUrl : null
     * upperName : BTC
     * isDeleted : 0
     * fullName : null
     */

    private String id;
    private String name;
    private String imgUrl;
    private String upperName;
    private int isDeleted;
    private String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getUpperName() {
        return upperName;
    }

    public void setUpperName(String upperName) {
        this.upperName = upperName;
    }

    public int getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(int isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
