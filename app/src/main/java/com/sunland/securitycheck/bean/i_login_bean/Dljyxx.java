package com.sunland.securitycheck.bean.i_login_bean;

public class Dljyxx {
    private String jycode;//	警员编码（警号）
    private String jyname;//	警员姓名
    private String usernumber;//	身份证
    private String jztype;//	警种类别
    private String dsjh	;//地市警号
    private String rylb ;//	人员类别：1.正式民警2：现役军人3.协辅警4：文职5.行政职工6事业职工 7.其他
    private String rylbstr;//	人员类别（中文）
    private String bmcode;//	所属部门code
    private String  bmmc;//	部门名称
    private String  menuids;//	菜单ID    (以 # 隔开)

    public String getJycode() {
        return jycode;
    }

    public void setJycode(String jycode) {
        this.jycode = jycode;
    }

    public String getJyname() {
        return jyname;
    }

    public void setJyname(String jyname) {
        this.jyname = jyname;
    }

    public String getUsernumber() {
        return usernumber;
    }

    public void setUsernumber(String usernumber) {
        this.usernumber = usernumber;
    }

    public String getJztype() {
        return jztype;
    }

    public void setJztype(String jztype) {
        this.jztype = jztype;
    }

    public String getDsjh() {
        return dsjh;
    }

    public void setDsjh(String dsjh) {
        this.dsjh = dsjh;
    }

    public String getRylb() {
        return rylb;
    }

    public void setRylb(String rylb) {
        this.rylb = rylb;
    }

    public String getRylbstr() {
        return rylbstr;
    }

    public void setRylbstr(String rylbstr) {
        this.rylbstr = rylbstr;
    }

    public String getBmcode() {
        return bmcode;
    }

    public void setBmcode(String bmcode) {
        this.bmcode = bmcode;
    }

    public String getBmmc() {
        return bmmc;
    }

    public void setBmmc(String bmmc) {
        this.bmmc = bmmc;
    }

    public String getMenuids() {
        return menuids;
    }

    public void setMenuids(String menuids) {
        this.menuids = menuids;
    }
}
