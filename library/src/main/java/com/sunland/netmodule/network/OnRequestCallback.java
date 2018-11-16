package com.sunland.netmodule.network;


import com.sunland.netmodule.def.bean.result.ResultBase;

/**
 * Created by LSJ on 2017/10/12.
 */

public abstract interface OnRequestCallback {
    public abstract <T> void onRequestFinish(String reqId, String reqName, T bean);

    public abstract <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName);
}
