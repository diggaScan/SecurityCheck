package com.sunland.netmodule.def.bean.result;

import java.io.Serializable;

public class ResultBase implements Serializable {
    private String code;//返回代码	0-成功，>0 后台失败 <0网络层错误(也有可能后台返回的格式错误)
    private String message;//接口成功或失败的具体描述

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
}
