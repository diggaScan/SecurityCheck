package com.sunland.securitycheck.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sunland.securitycheck.V_config;
import com.sunland.securitycheck.bean.BaseRequestBean;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.ButterKnife;

public abstract class Frg_base extends Fragment {

    public Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(setFrgLayout(), container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
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

    public abstract int setFrgLayout();

    public abstract void init();
}
