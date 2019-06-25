package com.sixbexchange.entity.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 郭青枫 on 2017/11/20.
 */

public class AppVersion implements Parcelable {

    /**
     * content : 更新内容
     * createTime : 1518317396000
     * downloadAddr : fir.im/xx
     * id : 2
     * minVersion : 1.5
     * mustUpdate : true
     * systemVersion : 1.9
     * title : 标题
     * type : 1
     * updateTime : 1518317399000
     */

    private String content;
    private long createTime;
    private String downloadAddr;
    private int id;
    private String minVersion;
    private boolean mustUpdate;
    private boolean isLoadSuccess=false;
    private String systemVersion;
    private String title;
    private int type;
    private long updateTime;
    private String host;
    private String visible;

    public String getVisible() {
        return visible;
    }

    public void setVisible(String visible) {
        this.visible = visible;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public boolean isLoadSuccess() {
        return isLoadSuccess;
    }

    public void setLoadSuccess(boolean loadSuccess) {
        isLoadSuccess = loadSuccess;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getDownloadAddr() {
        return downloadAddr;
    }

    public void setDownloadAddr(String downloadAddr) {
        this.downloadAddr = downloadAddr;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMinVersion() {
        return minVersion;
    }

    public void setMinVersion(String minVersion) {
        this.minVersion = minVersion;
    }

    public boolean isMustUpdate() {
        return mustUpdate;
    }

    public void setMustUpdate(boolean mustUpdate) {
        this.mustUpdate = mustUpdate;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeLong(this.createTime);
        dest.writeString(this.downloadAddr);
        dest.writeInt(this.id);
        dest.writeString(this.minVersion);
        dest.writeByte(this.mustUpdate ? (byte) 1 : (byte) 0);
        dest.writeString(this.systemVersion);
        dest.writeString(this.title);
        dest.writeInt(this.type);
        dest.writeLong(this.updateTime);
    }

    public AppVersion() {
    }

    protected AppVersion(Parcel in) {
        this.content = in.readString();
        this.createTime = in.readLong();
        this.downloadAddr = in.readString();
        this.id = in.readInt();
        this.minVersion = in.readString();
        this.mustUpdate = in.readByte() != 0;
        this.systemVersion = in.readString();
        this.title = in.readString();
        this.type = in.readInt();
        this.updateTime = in.readLong();
    }

    public static final Creator<AppVersion> CREATOR = new Creator<AppVersion>() {
        @Override
        public AppVersion createFromParcel(Parcel source) {
            return new AppVersion(source);
        }

        @Override
        public AppVersion[] newArray(int size) {
            return new AppVersion[size];
        }
    };
}
