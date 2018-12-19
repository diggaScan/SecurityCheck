package com.sunland.securitycheck.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.V_config;
import com.sunland.securitycheck.bean.BaseRequestBean;
import com.sunland.securitycheck.bean.i_login_bean.LoginRequestBean;
import com.sunland.securitycheck.bean.i_login_bean.LoginResBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * A login screen that offers login via email/password.
 */
public class Ac_login extends Ac_base implements OnRequestCallback {

    private final String TAG = "LoginActivity";
    @BindView(R.id.user_name)
    public EditText et_username;
    @BindView(R.id.password)
    public EditText et_password;
    @BindView(R.id.email_sign_in_button)
    public Button mEmailSignInButton;
    private RequestManager mRequestManager;
    private LoginRequestBean loginRequestBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_login);
        setToolbarTitle("登录界面");
        // Set up the login form.
        et_password.requestFocus();
        mRequestManager = new RequestManager(this, this);
    }

    public void queryYdjwData(String reqName) {
        loginRequestBean = (LoginRequestBean) assembleRequestObj(reqName);
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, loginRequestBean, 15000);
        mRequestManager.postRequestWithoutDialog();
    }

    private BaseRequestBean assembleRequestObj(String reqName) {
        switch (reqName) {
            case V_config.USER_LOGIN:
                LoginRequestBean requestBean = new LoginRequestBean();
                requestBean.setYhdm(et_username.getText().toString());
                requestBean.setImei(Global.imei);
                requestBean.setImsi(Global.imsi1);
                Date date = new Date();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String pda_time = simpleDateFormat.format(date);
                requestBean.setPdaTime(pda_time);
                requestBean.setGpsX("");
                requestBean.setGpsY("");
                requestBean.setPassword(et_password.getText().toString());
                return requestBean;
        }
        return null;
    }

    @OnClick(R.id.email_sign_in_button)
    public void onClick(View view) {
        queryYdjwData(V_config.USER_LOGIN);
    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        LoginResBean loginResBean = (LoginResBean) bean;
        //code 0 表示允许登录
        //code 1 登录失败
        if (loginResBean.getCode().equals("0")) {
//            saveLog(0, OperationLog.OperationResult.CODE_SUCCESS, appendString(loginRequestBean.getYhdm(),
//                    loginRequestBean.getPdaTime(), loginRequestBean.getImei()));//yhdm,手机品牌，手机型号，警号
            hop2Activity(Ac_main.class);
        } else {
//            saveLog(0, OperationLog.OperationResult.CODE_FAILURE,
//                    appendString(loginRequestBean.getYhdm(),
//                            loginRequestBean.getPdaTime(), loginRequestBean.getImei()));
            Toast.makeText(this, loginResBean.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        return LoginResBean.class;
    }
}

