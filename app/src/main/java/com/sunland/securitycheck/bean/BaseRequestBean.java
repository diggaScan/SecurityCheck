package com.sunland.securitycheck.bean;

/**
 * Created by PeitaoYe
 * On 2018/8/27
 **/
public class BaseRequestBean {
    public String yhdm;
    public String imei;
    public String imsi;
    public String pdaTime;

    public String getYhdm() {
        return yhdm;
    }

    public void setYhdm(String yhdm) {
        this.yhdm = yhdm;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getPdaTime() {
        return pdaTime;
    }

    public void setPdaTime(String pdaTime) {
        this.pdaTime = pdaTime;
    }
}
