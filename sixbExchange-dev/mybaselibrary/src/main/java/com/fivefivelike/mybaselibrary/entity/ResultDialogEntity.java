package com.fivefivelike.mybaselibrary.entity;

/**
 * Created by 郭青枫 on 2017/11/24.
 */

public class ResultDialogEntity {

    /**
     * type : 0
     * title : 保证金不足
     * content : 该模式下发布广告您需要在平台充值至少0.05BTC的保证金，请先到资产中心进行充币操作。
     * cancelBtn : 取消
     * confirmBtn : 去充值
     * cancelColor :
     * confirmColor :
     * titleColor :
     * contentColor :
     * code : E204
     * url :
     * cancelAndClose : false
     */

    private String type;
    private String title;
    private String content;
    private String cancelBtn;
    private String confirmBtn;
    private String cancelColor;
    private String confirmColor;
    private String titleColor;
    private String contentColor;
    private String code;
    private String url;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCancelBtn() {
        return cancelBtn;
    }

    public void setCancelBtn(String cancelBtn) {
        this.cancelBtn = cancelBtn;
    }

    public String getConfirmBtn() {
        return confirmBtn;
    }

    public void setConfirmBtn(String confirmBtn) {
        this.confirmBtn = confirmBtn;
    }

    public String getCancelColor() {
        return cancelColor;
    }

    public void setCancelColor(String cancelColor) {
        this.cancelColor = cancelColor;
    }

    public String getConfirmColor() {
        return confirmColor;
    }

    public void setConfirmColor(String confirmColor) {
        this.confirmColor = confirmColor;
    }

    public String getTitleColor() {
        return titleColor;
    }

    public void setTitleColor(String titleColor) {
        this.titleColor = titleColor;
    }

    public String getContentColor() {
        return contentColor;
    }

    public void setContentColor(String contentColor) {
        this.contentColor = contentColor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isCancelAndClose() {
        return cancelAndClose;
    }

    public void setCancelAndClose(boolean cancelAndClose) {
        this.cancelAndClose = cancelAndClose;
    }
}
