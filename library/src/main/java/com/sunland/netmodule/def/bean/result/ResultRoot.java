package com.sunland.netmodule.def.bean.result;

import java.io.Serializable;

public class ResultRoot implements Serializable {
    private boolean IsEncrypted = false;
    private String Method = "";
    private String Data = "";
    private String code = "";
    private String message = "";

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

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

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }
}
