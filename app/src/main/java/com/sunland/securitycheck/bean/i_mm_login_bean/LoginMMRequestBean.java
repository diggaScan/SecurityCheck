package com.sunland.securitycheck.bean.i_mm_login_bean;


import com.sunland.securitycheck.bean.BaseRequestBean;

public class LoginMMRequestBean extends BaseRequestBean {
    private String dlmk;    //	登录模块(区分不同的APP登录)
    private String sjpp;    //	手机品牌
    private String sjxx;    //	手机型号
    private String zzxt;    //	手机操作系统

    public String getDlmk() {
        return dlmk;
    }

    public void setDlmk(String dlmk) {
        this.dlmk = dlmk;
    }

    public String getSjpp() {
        return sjpp;
    }

    public void setSjpp(String sjpp) {
        this.sjpp = sjpp;
    }

    public String getSjxx() {
        return sjxx;
    }

    public void setSjxx(String sjxx) {
        this.sjxx = sjxx;
    }

    public String getZzxt() {
        return zzxt;
    }

    public void setZzxt(String zzxt) {
        this.zzxt = zzxt;
    }
}
