package com.sunland.netmodule.def.bean.result;

public class ResultHeartbeat extends ResultBase {
    private String serverTime = "";//服务器系统时间(yyyymmddhh24miss)
    private Integer sms = 0;//站内短信(0 无；N有站内短信)
    private Integer jcjs = 0;//接处警数
    private Integer jcbks = 0;//缉查布控预警数
    private Integer xgtdlqqrs = 0;//协管通登录请求人数
    private Integer xgttcqqs = 0;//协管通拖车请求数
    private String yclx = "";
    private String yhzt = "";//用户需要授权登录和下线的情况 0.未登录 1.申请登录 2.允许使用 3.禁止使用 4.申请超时

    public String getServerTime() {
        return serverTime;
    }

    public void setServerTime(String serverTime) {
        this.serverTime = serverTime;
    }

    public Integer getSms() {
        return sms;
    }

    public void setSms(Integer sms) {
        this.sms = sms;
    }

    public Integer getJcjs() {
        return jcjs;
    }

    public void setJcjs(Integer jcjs) {
        this.jcjs = jcjs;
    }

    public Integer getJcbks() {
        return jcbks;
    }

    public void setJcbks(Integer jcbks) {
        this.jcbks = jcbks;
    }

    public Integer getXgtdlqqrs() {
        return xgtdlqqrs;
    }

    public void setXgtdlqqrs(Integer xgtdlqqrs) {
        this.xgtdlqqrs = xgtdlqqrs;
    }

    public Integer getXgttcqqs() {
        return xgttcqqs;
    }

    public void setXgttcqqs(Integer xgttcqqs) {
        this.xgttcqqs = xgttcqqs;
    }

    public String getYclx() {
        return yclx;
    }

    public void setYclx(String yclx) {
        this.yclx = yclx;
    }

    public String getYhzt() {
        return yhzt;
    }

    public void setYhzt(String yhzt) {
        this.yhzt = yhzt;
    }
}
