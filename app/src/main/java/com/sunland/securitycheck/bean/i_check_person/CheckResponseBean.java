package com.sunland.securitycheck.bean.i_check_person;

import com.sunland.netmodule.def.bean.result.ResultBase;

public class CheckResponseBean extends ResultBase {
    String result;
    String resultCode;
    String icon;

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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
