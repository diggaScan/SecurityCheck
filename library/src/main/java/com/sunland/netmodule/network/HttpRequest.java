package com.sunland.netmodule.network;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.Xml;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.def.bean.result.ResultRoot;
import com.sunland.netmodule.utils.JsonUtils;

import org.xmlpull.v1.XmlPullParser;

import java.io.IOException;
import java.io.StringReader;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.content.ContentValues.TAG;

/**
 * Created by LSJ on 2017/10/12.
 */
public class HttpRequest {

    /*默认接口超时时间*/
    private final static int DEFAULT_TIMEOUT = 90;
    private final int MESSAGE_CALLBACK = 0x1000;
    /*默认连接服务时间*/
    private final static int DEFAULT_CONNECT_TIMEOUT = 15;

    private OnRequestCallback mCallback = null;
    private OnRequestCancel mCancelCallback = null;
    private ResponseInfo mResponseInfo = null;
    private Call mRequest = null;
    private String mReqId = "";
    private String mReqName = "";
    private Object mParamObj = null;
    private int mTimeOut;
    private String mIp = "";
    private String mPort = "";
    private String mPostfix = "";

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MESSAGE_CALLBACK) {
                if (mCallback != null) {
                    //小于0的错误转换一下，赋值到ResultBase中，便于上层区分
                    if (Integer.parseInt(mResponseInfo.getCode()) < 0) {
                        ResultBase resultBase = new ResultBase();
                        resultBase.setCode(mResponseInfo.getCode());
                        resultBase.setMessage(mResponseInfo.getResponse());
                        mResponseInfo.setResponse(JsonUtils.toJson(resultBase));
                    }

                    Log.d(TAG, "handleMessage: " + mResponseInfo.getResponse());
                    mCallback.onRequestFinish(mReqId, mReqName,
                            JsonUtils.fromJson(mResponseInfo.getResponse(), mCallback.getBeanClass(mReqId, mReqName)));
                }
            }
        }
    };

    public <T> HttpRequest(String reqName, T obj, OnRequestCallback callback) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = DEFAULT_TIMEOUT;
        mIp = Global.ip;
        mPort = Global.port;
        mPostfix = Global.postfix;
        mReqId = UUID.randomUUID().toString();
    }

    public <T> HttpRequest(String id, String reqName, T obj, OnRequestCallback callback) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = DEFAULT_TIMEOUT;
        mIp = Global.ip;
        mPort = Global.port;
        mPostfix = Global.postfix;
        if (TextUtils.isEmpty(id))
            mReqId = UUID.randomUUID().toString();
        else
            mReqId = id;
    }

    public <T> HttpRequest(String reqName, T obj, OnRequestCallback callback, int timeout) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = timeout;
        mIp = Global.ip;
        mPort = Global.port;
        mPostfix = Global.postfix;
        mReqId = UUID.randomUUID().toString();
    }

    public <T> HttpRequest(String id, String reqName, T obj, OnRequestCallback callback, int timeout) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = timeout;
        mIp = Global.ip;
        mPort = Global.port;
        mPostfix = Global.postfix;
        if (TextUtils.isEmpty(id))
            mReqId = UUID.randomUUID().toString();
        else
            mReqId = id;
    }

    public <T> HttpRequest(String ip, String port, String postfix, String reqName, T obj, OnRequestCallback callback) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = DEFAULT_TIMEOUT;
        mIp = ip;
        mPort = port;
        mPostfix = postfix;
        mReqId = UUID.randomUUID().toString();
    }

    public <T> HttpRequest(String id, String ip, String port, String postfix, String reqName, T obj, OnRequestCallback callback) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = DEFAULT_TIMEOUT;
        mIp = ip;
        mPort = port;
        mPostfix = postfix;
        if (TextUtils.isEmpty(id))
            mReqId = UUID.randomUUID().toString();
        else
            mReqId = id;
    }

    public <T> HttpRequest(String ip, String port, String postfix, String reqName, T obj, OnRequestCallback callback, int timeOut) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = timeOut;
        mIp = ip;
        mPort = port;
        mPostfix = postfix;
        mReqId = UUID.randomUUID().toString();
    }

    public <T> HttpRequest(String id, String ip, String port, String postfix, String reqName, T obj, OnRequestCallback callback, int timeOut) {
        mReqName = reqName;
        mParamObj = obj;
        mCallback = callback;
        mTimeOut = timeOut;
        mIp = ip;
        mPort = port;
        mPostfix = postfix;
        if (TextUtils.isEmpty(id))
            mReqId = UUID.randomUUID().toString();
        else
            mReqId = id;
    }

    public void setCancelCallback(OnRequestCancel cancelCallback) {
        this.mCancelCallback = cancelCallback;
    }

    public <T> void postRequest() {
        mResponseInfo = new ResponseInfo();
        mResponseInfo.setReqName(mReqName);
        if (TextUtils.isEmpty(mReqId))
            mResponseInfo.setId(UUID.randomUUID().toString());
        else
            mResponseInfo.setId(mReqId);

        // set up soap
        String ENVHEADER = "<?xml version='1.0' encoding='utf-8'?>\n" +
                "<soap:Envelope xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xmlns:xsd='http://www.w3.org/2001/XMLSchema' xmlns:soap='http://schemas.xmlsoap.org/soap/envelope/'>\n" +
                "<SOAP-ENV:Header xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n" +
                "<Username>sunland</Username><Password>sunlandgroup</Password>\n" +
                "</SOAP-ENV:Header>\n" +
                "<soap:Body>\n";

        String ENVTAIL = "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
        String PARAMMASK = "<in0>%s</in0>\n";

        Log.d(TAG, "postRequest:mPort" + mPort);

        String SERVER_URL = "http://" + mIp + ":" + mPort + mPostfix;

        StringBuilder sb = new StringBuilder();
        sb.append(ENVHEADER);
        sb.append("<" + mReqName + ">");

        try {
            sb.append(String.format(PARAMMASK, URLEncoder.encode(JsonUtils.toJson(mParamObj), "utf-8")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        sb.append("</" + mReqName + ">\n");
        sb.append(ENVTAIL);
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), sb.toString());
            Request request = new Request.Builder()
//                    .header("Accept", "text/xml")
//                    .addHeader("Connection", "close")//默认为Keep-Alive
                    .url(SERVER_URL)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(mTimeOut, TimeUnit.SECONDS)
                    .writeTimeout(mTimeOut, TimeUnit.SECONDS)
                    .build();
            mRequest = client.newCall(request);
        } catch (Exception e) {
            e.printStackTrace();
            mResponseInfo.setResponse("发送请求失败!\n" + e.getMessage() == null ? e.toString() : e.getMessage());
            if (mCallback != null)
                mHandler.sendEmptyMessage(MESSAGE_CALLBACK);
        }

        mRequest.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (call.isCanceled()) {
                    if (mCancelCallback != null)
                        mCancelCallback.onHttpRequestCancel(mReqId, mReqName);
                    return;
                }
                if (e != null) {
                    String error = (e.getMessage() != null ? e.getMessage() : e.toString());
                    if (e.toString().contains("SocketTimeoutException"))
                        mResponseInfo.setResponse("发送请求超时!\n" + error);
                    else if (e.toString().contains(("ConnectException")))
                        mResponseInfo.setResponse("网络连接失败!\n" + error);
                    else
                        mResponseInfo.setResponse("发送请求异常!\n" + error);
                } else
                    mResponseInfo.setResponse("发送请求出现未知错误!");
                if (mCallback != null) {
                    mHandler.sendEmptyMessage(MESSAGE_CALLBACK);
                }
            }

            @Override
            public void onResponse(Call call, Response response) {
                Log.d(TAG, "onResponse: " + response.message());
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body().string();
                        responseBody = parseRequest(responseBody);//获取xml文件中的json部分
                        responseBody = URLDecoder.decode(responseBody);//有时候后台传回来的是经过encode的url
                        if (!TextUtils.isEmpty(responseBody)) {
                            ResultRoot resultRoot = JsonUtils.fromJson(responseBody, ResultRoot.class);
                            if (resultRoot == null) {
                                mResponseInfo.setResponse("后台返回数据格式异常");
                            } else {
                                mResponseInfo.setCode(resultRoot.getCode());
                                mResponseInfo.setResponse(responseBody);//直接传入整段json内容到handler中进行解析。
//                                if (TextUtils.isEmpty(resultRoot.getData()) && !resultRoot.getCode().equals("0")) {
//                                    mResponseInfo.setResponse("请求失败：" + resultRoot.getMessage());
//                                } else {
//                                    mResponseInfo.setCode("0");
////                                    mResponseInfo.setResponse(resultRoot.getData());//返回的json格式与resultRoot不对应，
//                                    mResponseInfo.setResponse(responseBody);//直接传入整段json内容到handler中进行解析。
//                                }
                            }
                        } else {
                            mResponseInfo.setResponse("后台返回为空");
                        }
                    } else {
                        mResponseInfo.setResponse("请求失败(" + String.valueOf(response.code()) + ":" + response.message() + ")");
                    }
                } catch (IOException e) {
                    mResponseInfo.setResponse(e.getMessage() != null ? e.getMessage() : e.toString());
                } finally {
                    if (mCallback != null)
                        mHandler.sendEmptyMessage(MESSAGE_CALLBACK);
                }
            }
        });
    }


    private static String parseRequest(String xml) {
        if (TextUtils.isEmpty(xml))
            return "";
        String retStr = "";
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(new StringReader(xml));
            int eventType = parser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        break;

                    case XmlPullParser.START_TAG://开始元素事件
                        String name = parser.getName();
                        if (name.equalsIgnoreCase("out")) {
                            retStr = parser.nextText();
                            return retStr;
                        }
                        break;

                    case XmlPullParser.END_TAG://结束元素事件
                        break;
                }
                eventType = parser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retStr;
    }


    /*获取请求接口名*/
    public String getRequestName() {
        return mReqName;
    }

    /*获取请求请求id*/
    public String getmReqId() {
        return mReqId;
    }

    /*取消请求*/
    public void cancel() {
        if (mRequest != null)
            mRequest.cancel();
    }

    /**
     * 请求是否已经取消
     *
     * @return
     */
    public boolean isCanceled() {
        if (mRequest == null)
            return true;
        else
            return mRequest.isCanceled();
    }
    /*------------以下是以同步方式调用(必须放在子线程调用) start-----------------*/

    /**
     * 同步方式请求
     *
     * @param reqName   接口名
     * @param argObj    请求参数对象
     * @param destClass 需要返回的类名 必须继承ResultBase
     * @param <T>
     * @return
     */
    public static <T extends ResultBase> T postSyncRequest(String reqName, Object argObj, Class<T> destClass) {
        return postSyncRequest(Global.ip, Global.port, Global.postfix, reqName, argObj, destClass, DEFAULT_TIMEOUT);
    }

    /**
     * 同步方式请求
     *
     * @param reqName   接口名
     * @param argObj    请求参数对象
     * @param destClass 需要返回的类名 必须继承ResultBase
     * @param timeout   超时时间
     * @param <T>
     * @return
     */
    public static <T extends ResultBase> T postSyncRequest(String reqName, Object argObj, Class<T> destClass, int timeout) {
        return postSyncRequest(Global.ip, Global.port, Global.postfix, reqName, argObj, destClass, timeout);
    }

    /**
     * 同步方式请求
     *
     * @param ip        服务ip
     * @param port      服务端口
     * @param postfix   服务后缀
     * @param reqName   接口名
     * @param argObj    请求参数对象
     * @param destClass 需要返回的类名 必须继承ResultBase
     * @param timeout   超时时间
     * @param <T>
     * @return
     */
    public static <T extends ResultBase> T postSyncRequest(String ip, String port, String postfix, String reqName, Object argObj, Class<T> destClass, int timeout) {
        ResultBase resultBase = new ResultBase();
        resultBase.setCode("-1");
        try {
            RequestBody body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded;charset=utf-8"), "request=" + URLEncoder.encode(JsonUtils.toJson(argObj), "utf-8"));
            Request request = new Request.Builder()
                    .header("Accept", "text/xml")
                    .addHeader("Connection", "close")//默认为Keep-Alive
                    .url("http://" + ip + ":" + port + postfix)
                    .post(body)
                    .build();

            OkHttpClient client = new OkHttpClient().newBuilder()
                    .connectTimeout(DEFAULT_CONNECT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(timeout, TimeUnit.SECONDS)
                    .writeTimeout(timeout, TimeUnit.SECONDS)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                if (!TextUtils.isEmpty(responseBody)) {
                    ResultRoot rootResult = JsonUtils.fromJson(responseBody, ResultRoot.class);
                    if (rootResult == null) {
                        resultBase.setMessage("后台返回数据格式异常");
                    } else {
                        if (TextUtils.isEmpty(rootResult.getData()))
                            resultBase.setMessage("请求失败：" + rootResult.getMessage());
                        else {
                            resultBase.setCode("0");
                            resultBase.setMessage(rootResult.getData());
                        }
                    }
                } else {
                    resultBase.setMessage("后台返回为空");
                }
            } else {
                resultBase.setMessage("请求失败：(" + String.valueOf(response.code()) + ":" + response.message() + ")");
            }
        } catch (Exception e) {
            if (e != null) {
                String error = (e.getMessage() != null ? e.getMessage() : e.toString());
                if (e.toString().contains("SocketTimeoutException"))
                    resultBase.setMessage("发送请求超时!\n" + error);
                else if (e.toString().contains(("ConnectException")))
                    resultBase.setMessage("网络连接失败!\n" + error);
                else
                    resultBase.setMessage("发送请求异常!\n" + error);
            } else
                resultBase.setMessage("发送请求出现未知错误!");
        } finally {
            if (Integer.parseInt(resultBase.getCode()) < 0)
                return JsonUtils.fromJson(JsonUtils.toJson(resultBase), destClass);
            else
                return JsonUtils.fromJson(resultBase.getMessage(), destClass);
        }
    }
    /*----------------end---------------*/
}
