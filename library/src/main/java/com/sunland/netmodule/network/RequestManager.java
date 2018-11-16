package com.sunland.netmodule.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;


import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LSJ on 2017/10/12.
 */

public class RequestManager implements OnRequestCallback {

    private List<HttpRequest> mRequestList;
    private ProgressDialog mProgressDlg;
    private Context mContext;
    private OnRequestCallback mCallback; //正常请求回调
    private OnRequestManagerCancel mCancelCallback;//取消请求回调
    private String mDlgTile = "", mDlgInfo = "";

    public RequestManager(Context context, OnRequestCallback callback) {
        mRequestList = new ArrayList<HttpRequest>();
        mContext = context;
        mCallback = callback;
        mCancelCallback = null;
    }

    public RequestManager(Context context, OnRequestCallback callback, OnRequestManagerCancel cancelCallback) {
        mRequestList = new ArrayList<HttpRequest>();
        mContext = context;
        mCallback = callback;
        mCancelCallback = cancelCallback;
    }

    public RequestManager(Context context, String title, String info, OnRequestCallback callback) {
        mRequestList = new ArrayList<HttpRequest>();
        mContext = context;
        mDlgTile = title;
        mDlgInfo = info;
        mCallback = callback;
        mCancelCallback = null;
    }

    public RequestManager(Context context, String title, String info, OnRequestCallback callback, OnRequestManagerCancel cancelCallback) {
        mRequestList = new ArrayList<HttpRequest>();
        mContext = context;
        mDlgTile = title;
        mDlgInfo = info;
        mCallback = callback;
        mCancelCallback = cancelCallback;
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param <T>
     */
    public <T> void addRequest(String reqName, T obj) {
        mRequestList.add(new HttpRequest(reqName, obj, this));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param timeout 超时时间
     * @param <T>
     */
    public <T> void addRequest(String reqName, T obj, int timeout) {
        mRequestList.add(new HttpRequest(reqName, obj, this, timeout));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param id      接口ID(区分同请求名才需要传入)
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param <T>
     */
    public <T> void addRequest(String id, String reqName, T obj) {
        mRequestList.add(new HttpRequest(id, reqName, obj, this));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param id      接口ID(区分同请求名才需要传入)
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param timeout 超时时间
     * @param <T>
     */
    public <T> void addRequest(String id, String reqName, T obj, int timeout) {
        mRequestList.add(new HttpRequest(id, reqName, obj, this, timeout));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param ip      服务IP
     * @param port    服务端口
     * @param postfix 服务后缀
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param <T>
     */
    public <T> void addRequest(String ip, String port, String postfix, String reqName, T obj) {
        mRequestList.add(new HttpRequest(ip, port, postfix, reqName, obj, this));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param ip      服务IP
     * @param port    服务端口
     * @param postfix 服务后缀
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param timeout 超时时间
     * @param <T>
     */
    public <T> void addRequest(String ip, String port, String postfix, String reqName, T obj, int timeout) {
        mRequestList.add(new HttpRequest(ip, port, postfix, reqName, obj, this, timeout));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param id      接口ID(区分同请求名才需要传入)
     * @param ip      服务IP
     * @param port    服务端口
     * @param postfix 服务后缀
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param <T>
     */
    public <T> void addRequest(String id, String ip, String port, String postfix, String reqName, T obj) {
        mRequestList.add(new HttpRequest(id, ip, port, postfix, reqName, obj, this));
    }

    /**
     * 添加发送请求，最后需要调用postRequest才会发送
     *
     * @param id      接口ID(区分同请求名才需要传入)
     * @param ip      服务IP
     * @param port    服务端口
     * @param postfix 服务后缀
     * @param reqName 接口名
     * @param obj     请求参数对象
     * @param timeout 超时时间
     * @param <T>
     */
    public <T> void addRequest(String id, String ip, String port, String postfix, String reqName, T obj, int timeout) {
        mRequestList.add(new HttpRequest(id, ip, port, postfix, reqName, obj, this, timeout));
    }

    public synchronized void postRequest() {
        if (mRequestList == null || mRequestList.size() == 0)
            return;
        for (HttpRequest request : mRequestList)
            request.postRequest();
        if (mProgressDlg == null || !mProgressDlg.isShowing()) {
            if (mDlgTile.equals("") || mDlgInfo.equals(""))
                mProgressDlg = DialogUtils.createProgressDlg(mContext, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelAll();
                    }
                });
            else
                mProgressDlg = DialogUtils.createProgressDlg(mContext, mDlgTile, mDlgInfo, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cancelAll();
                    }
                });
        }
    }

    public synchronized void postRequestWithoutDialog() {
        if (mRequestList == null || mRequestList.size() == 0)
            return;
        for (HttpRequest request : mRequestList)
            request.postRequest();
    }

    /**
     * 取消队列中所有网络请求
     */
    private synchronized void cancelAll() {
        for (HttpRequest request : mRequestList) {
            request.cancel();
        }
        if (mCancelCallback != null)
            mCancelCallback.onHttpRequestCancel();
        mRequestList.clear();
        System.out.println("RequestManager:request cancelAll");
    }

    /**
     * 回调进入请求管理类中，移除队列中已经返回的请求
     */
    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        for (HttpRequest request : mRequestList) {
            if (request.getmReqId().equals(reqId)) {
                mRequestList.remove(request);
                break;
            }
        }

        if (mRequestList.size() == 0) {
            if (mProgressDlg != null)
                mProgressDlg.dismiss();
        }

        if (mContext != null && mCallback != null)
            mCallback.onRequestFinish(reqId, reqName, bean);
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        return mCallback.getBeanClass(reqId, reqName);
    }
}
