package com.sunland.securitycheck.bean;

import com.sunland.netmodule.def.bean.result.ResultBase;

public class CheckResponseBean extends ResultBase {
    String result;
    String resultCode;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }
}
