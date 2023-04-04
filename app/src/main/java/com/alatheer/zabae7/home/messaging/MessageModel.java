package com.alatheer.zabae7.home.messaging;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageModel implements Serializable {
    @SerializedName("msg_id")
    @Expose
    private String msgId;
    @SerializedName("msg_date")
    @Expose
    private String msgDate;
    @SerializedName("msg_time")
    @Expose
    private String msgTime;
    @SerializedName("msg_title")
    @Expose
    private String msgTitle;
    @SerializedName("msg_content")
    @Expose
    private String msgContent;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("customer_id")
    @Expose
    private String customerId;
    @SerializedName("act_ended")
    @Expose
    private String actEnded;
    @SerializedName("act_ended_emp_id")
    @Expose
    private Object actEndedEmpId;
    @SerializedName("act_ended_date")
    @Expose
    private Object actEndedDate;
    @SerializedName("act_ended_time")
    @Expose
    private Object actEndedTime;

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getMsgDate() {
        return msgDate;
    }

    public void setMsgDate(String msgDate) {
        this.msgDate = msgDate;
    }

    public String getMsgTime() {
        return msgTime;
    }

    public void setMsgTime(String msgTime) {
        this.msgTime = msgTime;
    }

    public String getMsgTitle() {
        return msgTitle;
    }

    public void setMsgTitle(String msgTitle) {
        this.msgTitle = msgTitle;
    }

    public String getMsgContent() {
        return msgContent;
    }

    public void setMsgContent(String msgContent) {
        this.msgContent = msgContent;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getActEnded() {
        return actEnded;
    }

    public void setActEnded(String actEnded) {
        this.actEnded = actEnded;
    }

    public Object getActEndedEmpId() {
        return actEndedEmpId;
    }

    public void setActEndedEmpId(Object actEndedEmpId) {
        this.actEndedEmpId = actEndedEmpId;
    }

    public Object getActEndedDate() {
        return actEndedDate;
    }

    public void setActEndedDate(Object actEndedDate) {
        this.actEndedDate = actEndedDate;
    }

    public Object getActEndedTime() {
        return actEndedTime;
    }

    public void setActEndedTime(Object actEndedTime) {
        this.actEndedTime = actEndedTime;
    }
}
