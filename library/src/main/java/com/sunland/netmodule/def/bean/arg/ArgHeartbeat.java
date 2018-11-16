package com.sunland.netmodule.def.bean.arg;

public class ArgHeartbeat extends ArgBase {
    private String pdaTime = "";//Pda时间
    private String os = "";//操作系统
    private String devtype = "";//设备类型
    private String appid = "";//包名
    private String bmdm = "";
    private String softVer = "";//终端软件版本

    public String getPdaTime() {
        return pdaTime;
    }

    public void setPdaTime(String pdaTime) {
        this.pdaTime = pdaTime;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDevtype() {
        return devtype;
    }

    public void setDevtype(String devtype) {
        this.devtype = devtype;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getBmdm() {
        return bmdm;
    }

    public void setBmdm(String bmdm) {
        this.bmdm = bmdm;
    }

    public String getSoftVer() {
        return softVer;
    }

    public void setSoftVer(String softVer) {
        this.softVer = softVer;
    }
}
