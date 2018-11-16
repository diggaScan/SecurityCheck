package com.sunland.netmodule.def.bean.arg;

import java.io.Serializable;

public class ArgRoot implements Serializable {
    private boolean IsEncrypted = false;
    private String Method = "";
    private Object Parameter;
    private String csjyw = "";
    private String jkxlh = "";

    public boolean isEncrypted() {
        return IsEncrypted;
    }

    public void setEncrypted(boolean encrypted) {
        IsEncrypted = encrypted;
    }

    public String getMethod() {
        return Method;
    }

    public void setMethod(String method) {
        Method = method;
    }

    public Object getParameter() {
        return Parameter;
    }

    public void setParameter(Object parameter) {
        Parameter = parameter;
    }

    public String getCsjyw() {
        return csjyw;
    }

    public void setCsjyw(String csjyw) {
        this.csjyw = csjyw;
    }

    public String getJkxlh() {
        return jkxlh;
    }

    public void setJkxlh(String jkxlh) {
        this.jkxlh = jkxlh;
    }
}
