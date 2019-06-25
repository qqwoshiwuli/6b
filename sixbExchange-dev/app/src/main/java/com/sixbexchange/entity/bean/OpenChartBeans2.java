package com.sixbexchange.entity.bean;

import java.util.List;

public class OpenChartBeans2 {

    /**
     * code : 0
     * dialog : {"type":"3","title":"OK","content":null,"cancelBtn":null,"confirmBtn":null,"cancelColor":null,"confirmColor":null,"titleColor":null,"contentColor":null,"code":"0","url":null,"time":null,"cancelAndClose":false}
     * data : [{"id":5,"imgTitle":"6B超级返利活动","imgUrl":"https://mapp.bicoin.info/6b/img/kaiping.png","imgType":3,"cTime":1560954922000,"uTime":1560954922000,"skipUrl":"https://mapp.bicoin.info/6b/img/6btop2.html","isSkip":1,"sort":1,"isNeed":0,"imgUrlSub":null,"imgSubTitle":null,"delTime":null,"source":null,"operator":null,"appVersion":null}]
     */

    private int code;
    private DialogBean dialog;
    private Object data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DialogBean getDialog() {
        return dialog;
    }

    public void setDialog(DialogBean dialog) {
        this.dialog = dialog;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public static class DialogBean {
        /**
         * type : 3
         * title : OK
         * content : null
         * cancelBtn : null
         * confirmBtn : null
         * cancelColor : null
         * confirmColor : null
         * titleColor : null
         * contentColor : null
         * code : 0
         * url : null
         * time : null
         * cancelAndClose : false
         */

        private String type;
        private String title;
        private Object content;
        private Object cancelBtn;
        private Object confirmBtn;
        private Object cancelColor;
        private Object confirmColor;
        private Object titleColor;
        private Object contentColor;
        private String code;
        private Object url;
        private Object time;
        private boolean cancelAndClose;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Object getContent() {
            return content;
        }

        public void setContent(Object content) {
            this.content = content;
        }

        public Object getCancelBtn() {
            return cancelBtn;
        }

        public void setCancelBtn(Object cancelBtn) {
            this.cancelBtn = cancelBtn;
        }

        public Object getConfirmBtn() {
            return confirmBtn;
        }

        public void setConfirmBtn(Object confirmBtn) {
            this.confirmBtn = confirmBtn;
        }

        public Object getCancelColor() {
            return cancelColor;
        }

        public void setCancelColor(Object cancelColor) {
            this.cancelColor = cancelColor;
        }

        public Object getConfirmColor() {
            return confirmColor;
        }

        public void setConfirmColor(Object confirmColor) {
            this.confirmColor = confirmColor;
        }

        public Object getTitleColor() {
            return titleColor;
        }

        public void setTitleColor(Object titleColor) {
            this.titleColor = titleColor;
        }

        public Object getContentColor() {
            return contentColor;
        }

        public void setContentColor(Object contentColor) {
            this.contentColor = contentColor;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public Object getUrl() {
            return url;
        }

        public void setUrl(Object url) {
            this.url = url;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }

        public boolean isCancelAndClose() {
            return cancelAndClose;
        }

        public void setCancelAndClose(boolean cancelAndClose) {
            this.cancelAndClose = cancelAndClose;
        }
    }

    public static class DataBean {
        /**
         * id : 5
         * imgTitle : 6B超级返利活动
         * imgUrl : https://mapp.bicoin.info/6b/img/kaiping.png
         * imgType : 3
         * cTime : 1560954922000
         * uTime : 1560954922000
         * skipUrl : https://mapp.bicoin.info/6b/img/6btop2.html
         * isSkip : 1
         * sort : 1
         * isNeed : 0
         * imgUrlSub : null
         * imgSubTitle : null
         * delTime : null
         * source : null
         * operator : null
         * appVersion : null
         */

        private int id;
        private String imgTitle;
        private String imgUrl;
        private int imgType;
        private long cTime;
        private long uTime;
        private String skipUrl;
        private int isSkip;
        private int sort;
        private int isNeed;
        private Object imgUrlSub;
        private Object imgSubTitle;
        private Object delTime;
        private Object source;
        private Object operator;
        private Object appVersion;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getImgTitle() {
            return imgTitle;
        }

        public void setImgTitle(String imgTitle) {
            this.imgTitle = imgTitle;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public int getImgType() {
            return imgType;
        }

        public void setImgType(int imgType) {
            this.imgType = imgType;
        }

        public long getCTime() {
            return cTime;
        }

        public void setCTime(long cTime) {
            this.cTime = cTime;
        }

        public long getUTime() {
            return uTime;
        }

        public void setUTime(long uTime) {
            this.uTime = uTime;
        }

        public String getSkipUrl() {
            return skipUrl;
        }

        public void setSkipUrl(String skipUrl) {
            this.skipUrl = skipUrl;
        }

        public int getIsSkip() {
            return isSkip;
        }

        public void setIsSkip(int isSkip) {
            this.isSkip = isSkip;
        }

        public int getSort() {
            return sort;
        }

        public void setSort(int sort) {
            this.sort = sort;
        }

        public int getIsNeed() {
            return isNeed;
        }

        public void setIsNeed(int isNeed) {
            this.isNeed = isNeed;
        }

        public Object getImgUrlSub() {
            return imgUrlSub;
        }

        public void setImgUrlSub(Object imgUrlSub) {
            this.imgUrlSub = imgUrlSub;
        }

        public Object getImgSubTitle() {
            return imgSubTitle;
        }

        public void setImgSubTitle(Object imgSubTitle) {
            this.imgSubTitle = imgSubTitle;
        }

        public Object getDelTime() {
            return delTime;
        }

        public void setDelTime(Object delTime) {
            this.delTime = delTime;
        }

        public Object getSource() {
            return source;
        }

        public void setSource(Object source) {
            this.source = source;
        }

        public Object getOperator() {
            return operator;
        }

        public void setOperator(Object operator) {
            this.operator = operator;
        }

        public Object getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(Object appVersion) {
            this.appVersion = appVersion;
        }
    }
}
