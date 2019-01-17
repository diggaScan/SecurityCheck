package com.sunland.securitycheck.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.securitycheck.GlideApp;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.V_config;
import com.sunland.securitycheck.utils.UtilsString;

import butterknife.BindView;
import butterknife.OnClick;

public class Ac_check_result extends Ac_base {

    @BindView(R.id.hc)
    public TextView tv_hc;
    @BindView(R.id.profile)
    public ImageView iv_profile;
    @BindView(R.id.icon)
    public ImageView iv_icon;
    @BindView(R.id.info)
    public TextView tv_info;
    private String result;
    private String resultCode;
    private String icon;
    private String sfzh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.ac_check_result);
        showNavIcon(true);
        setToolbarTitle("核查结果");
        handleIntent();
        initView();
    }

    private void handleIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            Bundle bundle = intent.getBundleExtra("bundle");
            if (bundle != null) {
                result = bundle.getString("result");
                resultCode = bundle.getString("resultCode");
                sfzh = bundle.getString("sfzh");
                icon = bundle.getString("icon");
            }
        }
    }

    private void initView() {

        GlideApp.with(this).asBitmap()
                .load("http://" + Global.ip + ":" + Global.port + icon)
                .placeholder(R.drawable.pass_profile)
                .error(R.drawable.pass_profile)
                .into(iv_profile);

        //0表示通过
        if (resultCode.equals("0")) {
            tv_info.setText(Html.fromHtml("<font color=\"#05b163\">通过</font>"));
            iv_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_pass));
//            iv_profile.setImageDrawable(getResources().getDrawable(R.drawable.pass_profile));
        } else if (resultCode.equals("1")) {
            //1表示拦截
            iv_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_block));
            tv_info.setText(Html.fromHtml("<font color=\"#d13931\">拦截</font>"));
//            iv_profile.setImageDrawable(getResources().getDrawable(R.drawable.block_profile));
            tv_hc.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "异常错误", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @OnClick(R.id.hc)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.hc:
                if (UtilsString.checkId(sfzh) == null || sfzh == null || sfzh.isEmpty()) {
                    Toast.makeText(this, "身份证号无或者不符合规范", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent();
                intent.setAction("com.sunland.intent.action.QUERY_ID");
                Bundle bundle = new Bundle();
                bundle.putString("id", sfzh);
                bundle.putString("yhdm", V_config.YHDM);
                bundle.putString("jysfzh", V_config.JYSFZH);
                bundle.putString("jyxm", V_config.JYXM);
                bundle.putString("jybmbh", V_config.JYBMBH);
                bundle.putString("lbr", "02");
                intent.putExtra("bundle", bundle);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, "未安装核查应用", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
