package com.sunland.securitycheck.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.bean.i_login_bean.LoginRequestBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.com.cybertech.models.User;


public class Ac_splash extends Ac_base implements OnRequestCallback {

    private final String TAG = this.getClass().getSimpleName();

    private LoginRequestBean loginBean;
    private String jh;
    private RequestManager mRequestManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (mApplication.isAppCyber()) {
            mRequestManager = new RequestManager(this, this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mApplication.isAppCyber()) {
            User user = cn.com.cybertech.pdk.UserInfo.getUser(this);
            try {
                jh = user.getAccount();
            } catch (NullPointerException e) {
                Toast.makeText(this, "无法获取警号", Toast.LENGTH_LONG).show();
                finish();
                return;
            }
            login();
        } else {
//            startActivity(LoginActivity.class);
        }
    }

    public void login() {
        loginBean = assembleRequestObj();
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, "userLogin", loginBean, 10000);
        mRequestManager.postRequestWithoutDialog();
    }

    public LoginRequestBean assembleRequestObj() {

        if (jh == null) {
            Toast.makeText(this, "警号异常", Toast.LENGTH_SHORT).show();
        }

        LoginRequestBean loginBean = new LoginRequestBean();
        loginBean.setYhdm(jh);
        loginBean.setImei(Global.imei);
        if (Global.imsi1 == null) {
            loginBean.setImsi(" ");
        } else {
            loginBean.setImsi(Global.imsi1);
        }
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        loginBean.setPdaTime(pda_time);
        loginBean.setGpsY("");
        loginBean.setGpsX("");
        loginBean.setPassword("");
        return loginBean;
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        Log.d(TAG, "onRequestFinish: " + bean.getClass().getSimpleName());
        ResultBase response = (ResultBase) bean;

        if (!response.getCode().equals("0")) {
//            saveLog(0, OperationLog.OperationResult.CODE_FAILURE,
//                    appendString(loginBean.yhdm, loginBean.pdaBrand, loginBean.pdaModel));
            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        } else {
//            saveLog(0, OperationLog.OperationResult.CODE_SUCCESS, appendString(loginBean.yhdm, loginBean.pdaBrand, loginBean.pdaModel));
            Bundle bundle = new Bundle();
            bundle.putString("jh", jh);
//            NewsActivity.startActivity(this, bundle);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        return ResultBase.class;
    }

}
