package com.sunland.securitycheck.activities;

import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.BannerIndicator;
import com.sunland.securitycheck.DataModel;
import com.sunland.securitycheck.NfcReceiver;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.adapter.VpAdapter_check;
import com.sunland.securitycheck.bean.CheckResponseBean;
import com.sunland.securitycheck.fragments.Frg_IdInput;
import com.sunland.securitycheck.fragments.Frg_IdScan;
import com.sunland.securitycheck.fragments.Frg_nameInput;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Ac_check extends Ac_base implements NfcReceiver.OnGetNfcDataListener, OnRequestCallback {

    @BindView(R.id.check_indicator)
    public BannerIndicator bi_indicator;
    @BindView(R.id.check_pager)
    public ViewPager vp_pager;


    public String area_code;
    public RequestManager requestManager;
    private NfcReceiver rec;
    private Fragment frg_idScan;
    private Fragment frg_idInput;
    private Fragment frg_nameInput;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_check);
        requestManager = new RequestManager(this, this);
        showNavIcon(true);
        handleIntent();
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        rec = new NfcReceiver();
        IntentFilter filter = new IntentFilter();//创建IntentFilter对象
        filter.addAction(DataModel.ACTION_NFC_READ_IDCARD_SUCCESS);
        filter.addAction(DataModel.ACTION_NFC_READ_IDCARD_FAILURE);
        registerReceiver(rec, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(rec);
    }

    @Override
    public void onReceiveDate(Intent intent) {
        if (DataModel.ACTION_NFC_READ_IDCARD_SUCCESS.equals(intent.getAction())) {
            Toast.makeText(this, "读取成功! ", Toast.LENGTH_SHORT).show();
            if (!frg_idScan.isHidden()) {
                ((Frg_IdScan) frg_idScan).updateViews(intent);
            }
        } else if (DataModel.ACTION_NFC_READ_IDCARD_FAILURE.equals(intent.getAction())) {
            Toast.makeText(this, "读取失败!", Toast.LENGTH_SHORT).show();
        }
    }


    public RequestManager getRequestManager() {
        return requestManager;
    }

    public String getArea_code() {
        return area_code;
    }

    private void handleIntent() {
        Intent intent = getIntent();

        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                String title = bundle.getString("title");
                area_code = bundle.getString("area");
                setToolbarTitle(title);
            }
        }
    }

    private void initView() {
        bi_indicator.setItem_nums(3);
        bi_indicator.setDotsColor(Color.WHITE);
        bi_indicator.setMovingDotColor(getResources().getColor(R.color.colorPrimary));

        frg_idScan = new Frg_IdScan();
        frg_idInput = new Frg_IdInput();
        frg_nameInput = new Frg_nameInput();
        List<Fragment> list = new ArrayList<>();
        list.add(frg_idScan);
        list.add(frg_idInput);
        list.add(frg_nameInput);

        VpAdapter_check adapter = new VpAdapter_check(getSupportFragmentManager(), list);
        vp_pager.setAdapter(adapter);
        vp_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                bi_indicator.setCurrentPosition(i, v);

            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        switch (reqName) {
            case "querySummitPerson":
                CheckResponseBean responseBean = (CheckResponseBean) bean;
                if (responseBean != null) {
                    if (responseBean.getCode().equals("0")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("result", responseBean.getResult());
                        bundle.putString("resultCode", responseBean.getResultCode());
                        hop2Activity(Ac_check_result.class, bundle);
                    } else {
                        Toast.makeText(this, "服务异常，无法获取数据", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "数据接入错误", Toast.LENGTH_SHORT).show();
                }
                break;

        }

    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        switch (reqName) {
            case "querySummitPerson":
                return CheckResponseBean.class;
            default:
                return null;
        }
    }
}
