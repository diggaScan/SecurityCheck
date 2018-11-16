package com.sunland.netmodule.network;

import java.io.Serializable;

/**
 * Created by LSJ on 2017/10/12.
 */

public class ResponseInfo implements Serializable {
    /**
     * 接口名
     */
    private String reqName = "";
    /*请求是否正常，0：正常 <0为网络错误，以区分后台错误（后台错误>0）*/
    private String code = "-1";
    /**
     * 后台网络请求返回内容
     */
    private String response = "";
    /*请求ID*/
    private String id = "";

    public String getReqName() {
        return reqName;
    }

    public void setReqName(String reqName) {
        this.reqName = reqName;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
