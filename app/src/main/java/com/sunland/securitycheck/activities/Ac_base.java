package com.sunland.securitycheck.activities;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.inputmethodservice.KeyboardView;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sunland.securitycheck.MyApplication;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.V_config;
import com.sunland.securitycheck.bean.BaseRequestBean;
import com.sunland.securitycheck.utils.DialogUtils;
import com.sunland.securitycheck.utils.WindowInfoUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;
import cn.com.cybertech.pdk.OperationLog;


public class Ac_base extends AppCompatActivity {

    public Toolbar toolbar;
    public TextView tb_title;
    public ImageView iv_nav;
    public FrameLayout container;
    public KeyboardView keyboard;
    public MyApplication mApplication;
    public DialogUtils dialogUtils;
    ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_base);
        mApplication = (MyApplication) getApplication();
        toolbar = findViewById(R.id.toolbar);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP && setImmersive()) {
            Window window = getWindow();
            window.setStatusBarColor(Color.TRANSPARENT);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            TypedArray actionbarSizeTypedArray = obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
            int actionBarHeight = (int) actionbarSizeTypedArray.getDimension(0, 0);
            actionbarSizeTypedArray.recycle();
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    WindowInfoUtils.getStatusBarHeight(this) + actionBarHeight);
            toolbar.setLayoutParams(lp);
        }

        tb_title = findViewById(R.id.toolbar_title);
        iv_nav = findViewById(R.id.nav_back);
        container = findViewById(R.id.container);
        keyboard = findViewById(R.id.myKeyb);
        iv_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialogUtils = DialogUtils.getInstance();
        setSupportActionBar(toolbar);
    }

    public boolean setImmersive() {
        return true;
    }

    public void setContentLayout(int layout) {
        getLayoutInflater().inflate(layout, container, true);
        ButterKnife.bind(this);
    }

    public void setToolbarTitle(String title) {
        tb_title.setText(title);
    }

    public void showNavIcon(boolean isShow) {
        if (isShow)
            iv_nav.setVisibility(View.VISIBLE);
        else
            iv_nav.setVisibility(View.GONE);
    }

    public void saveLog(int operateType, int operationResult, String operateCondition) {
        try {
            OperationLog.saveLog(this
                    , "95337103EF738979AE46420631D4A62D"
                    , getApplication().getPackageName()
                    , operateType
                    , OperationLog.OperationResult.CODE_SUCCESS
                    , 1
                    , operateCondition);
        } catch (Exception e) {
            //未适配Fileprovider
            e.printStackTrace();
        }
    }

    public String appendString(String... strings) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            sb.append(strings[i]);
            if (i != strings.length - 1) {
                sb.append("@");
            }
        }
        return sb.toString();
    }

    public void assembleBasicRequest(BaseRequestBean requestBean) {
        requestBean.setYhdm(V_config.YHDM);
        requestBean.setImei(V_config.imei);
        requestBean.setImsi(V_config.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        requestBean.setPdaTime(pda_time);
        requestBean.setGpsX(V_config.gpsX);
        requestBean.setGpsY(V_config.gpsY);
    }

    public void hop2Activity(Class<? extends Ac_base> clazz) {
        Intent intent = new Intent(this, clazz);
        startActivity(intent);
    }

    public void hop2Activity(Class<? extends Ac_base> clazz, Bundle bundle) {
        Intent intent = new Intent(this, clazz);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }
}
