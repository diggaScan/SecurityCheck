package com.sunland.securitycheck.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.adapter.Rv_Item_decoration;
import com.sunland.securitycheck.adapter.Rv_name_list_adapter;
import com.sunland.securitycheck.bean.i_check_person_list.NameListRequestBean;
import com.sunland.securitycheck.bean.i_check_person_list.NameListResponseBean;
import com.sunland.securitycheck.bean.i_check_person_list.TSummitPersion;
import com.sunland.securitycheck.customView.DragToRefreshView.DragToRefreshView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class Ac_name_list extends Ac_base implements OnRequestCallback {

    @BindView(R.id.rv_nameList)
    public RecyclerView rv_name_list;
    @BindView(R.id.loading_layout)
    public FrameLayout loading_layout;
    @BindView(R.id.refresh)
    public DragToRefreshView d2r_refresh;
    private RequestManager mRequestManager;
    private List<TSummitPersion> dataset;
    private Rv_name_list_adapter adapter;
    private String paperName;
    private String area_code;
    private int sex;

    private int cur_page = 1;
    private int items_per_page = 50;

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

        d2r_refresh.unableHeaderRefresh(false);
        d2r_refresh.unableFooterRefresh(false);
        d2r_refresh.setUpdateListener(new DragToRefreshView.OnUpdateListener() {
            @Override
            public void onRefreshing(DragToRefreshView view) {
                cur_page = dataset.size() % cur_page;//重置当前页数
                if (view.isFooterRefreshing()) {
                    cur_page++;
                    queryNameList();
                }
            }

            @Override
            public void onFinished(DragToRefreshView view) {
                if (view.getState() == DragToRefreshView.State.footer_release_to_load) {
                    int scroll_position = dataset.size() - items_per_page;
                    if (scroll_position > 0) {
                        rv_name_list.scrollToPosition(dataset.size());
                    }
                }
            }
        });
        d2r_refresh.addMainContent(rv_name_list);
    }

    private void queryNameList() {
        String reqName = "querySummitPersonList";
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, reqName, assembleRequestObj(), 15000);
        mRequestManager.postRequestWithoutDialog();
        loading_layout.setVisibility(View.VISIBLE);
    }


    private NameListRequestBean assembleRequestObj() {
        NameListRequestBean requestBean = new NameListRequestBean();
        assembleBasicRequest(requestBean);
        requestBean.setSex(sex);
        requestBean.setArea(area_code);
        // TODO: 2019/1/16/016
        requestBean.setPageNo(items_per_page);
        requestBean.setPageIndex(cur_page);
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
        d2r_refresh.dismiss();
        NameListResponseBean responseBean = (NameListResponseBean) bean;
        loading_layout.setVisibility(View.GONE);
        if (responseBean != null) {
            if (responseBean.getCode().equals("0")) {
                List<TSummitPersion> list = responseBean.gettSummitPersions();
                if ((list == null || list.isEmpty()) && dataset.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("resultCode", "1");
                    hop2Activity(Ac_check_result.class, bundle);
                    finish();
                } else {
                    if (list.size() == items_per_page) {
                        d2r_refresh.unableFooterRefresh(true);
                    }
                    dataset.addAll(list);
                    adapter.notifyItemRangeChanged(dataset.size(), list.size());
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
