package com.sixbexchange.entity.bean;

import java.io.Serializable;
import java.util.List;

public class OpenChartBean implements Serializable {

        /**
         * id : 1
         * imgTitle : 测试轮播图1
         * imgUrl : http://app.bicoin.com.cn/bld/github1.png
         * imgType : 2
         * cTime : 1534492058000
         * uTime : 1560946698000
         * skipUrl : null
         * isSkip : 1
         * sort : 1
         * isNeed : 1
         * imgUrlSub : null
         * imgSubTitle : null
         * delTime : null
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

}
