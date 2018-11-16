package com.sunland.securitycheck.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.adapter.Rv_Item_decoration;
import com.sunland.securitycheck.adapter.Rv_name_list_adapter;
import com.sunland.securitycheck.bean.NameListRequestBean;
import com.sunland.securitycheck.bean.NameListResponseBean;
import com.sunland.securitycheck.bean.TSummitPersion;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;

public class Ac_name_list extends Ac_base implements OnRequestCallback {

    @BindView(R.id.rv_nameList)
    public RecyclerView rv_name_list;

    private RequestManager mRequestManager;
    private List<TSummitPersion> dataset;
    private Rv_name_list_adapter adapter;
    private String paperName;
    private String area_code;
    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_name_list);
        setToolbarTitle("人员列表");
        showNavIcon(true);
        mRequestManager = new RequestManager(this, this);
        handleIntent();
        initRlc();
        queryNameList();
    }

    private void initRlc() {
        dataset = new ArrayList<>();
        adapter = new Rv_name_list_adapter(this, dataset);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rv_name_list.setLayoutManager(manager);
        rv_name_list.setAdapter(adapter);
        rv_name_list.addItemDecoration(new Rv_Item_decoration(this));
    }

    private void queryNameList() {
        String reqName = "querySummitPersonList";
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(), 15000);
        mRequestManager.postRequest();
    }


    private NameListRequestBean assembleRequestObj() {
        NameListRequestBean requestBean = new NameListRequestBean();
        requestBean.setYhdm("test");
        requestBean.setImei(Global.imei);
        requestBean.setImsi(Global.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        requestBean.setPdaTime(pda_time);

        requestBean.setSex(sex);
        requestBean.setArea(area_code);
        requestBean.setPageNo(100);
        requestBean.setPageIndex(1);
        requestBean.setName(paperName);
        return requestBean;
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                paperName = bundle.getString("paperName");
                sex = bundle.getInt("sex");
                area_code = bundle.getString("area_code");
            }
        }
    }


    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        NameListResponseBean responseBean = (NameListResponseBean) bean;
        if (responseBean != null) {
            if (responseBean.getCode().equals("0")) {
                List<TSummitPersion> list = responseBean.gettSummitPersions();
                if (list == null || list.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("resultCode", "1");
                    hop2Activity(Ac_check_result.class, bundle);
                    finish();
                } else {
                    dataset.clear();
                    dataset.addAll(list);
                    adapter.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(this, "服务异常，无法获取数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "数据接入错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        return NameListResponseBean.class;
    }
}
