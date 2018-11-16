package com.sunland.netmodule.def.bean.arg;

import java.io.Serializable;

public class ArgBase implements Serializable {
    private String yhdm = "";//用户名
    private String pdaID = "";//系统PDA标示号
    private String id = "";
    private String gps = "";//GPS坐标	“X值,Y值”
    private String gpsInfo = "";

    public String getYhdm() {
        return yhdm;
    }

    public void setYhdm(String yhdm) {
        this.yhdm = yhdm;
    }

    public String getPdaID() {
        return pdaID;
    }

    public void setPdaID(String pdaID) {
        this.pdaID = pdaID;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public String getGpsInfo() {
        return gpsInfo;
    }

    public void setGpsInfo(String gpsInfo) {
        this.gpsInfo = gpsInfo;
    }
}
