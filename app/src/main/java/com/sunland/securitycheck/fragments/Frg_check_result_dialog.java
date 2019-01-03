package com.sunland.securitycheck.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.def.bean.result.ResultBase;
import com.sunland.netmodule.network.OnRequestCallback;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.V_config;
import com.sunland.securitycheck.bean.CheckResponseBean;
import com.sunland.securitycheck.bean.NameListRequestBean;
import com.sunland.securitycheck.utils.UtilsString;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Frg_check_result_dialog extends DialogFragment implements OnRequestCallback {

    public Context context;
    private String sfzh;
    private String name;
    private int gender;
    private String area;
    private RequestManager mRequestManager;
    ImageView iv_profile;
    ImageView iv_icon;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    public void setInfo(String sfzh, int gender, String name, String area) {
        this.sfzh = sfzh;
        this.gender = gender;
        this.name = name;
        this.area = area;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        mRequestManager = new RequestManager(context, this);
        mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, "querySummitPerson", assembleRequestObj(), 15000);
        mRequestManager.postRequestWithoutDialog();

        View view = getCustomView();
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setTitle("核查结果")
                .setView(view)
                .setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .setPositiveButton("核查此人", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = new Bundle();
                        bundle.putString("id", sfzh);
                        Intent intent = new Intent();
                        intent.setAction("com.sunland.intent.action.QUERY_ID");
                        intent.putExtra("bundle", bundle);
                        if (UtilsString.checkId(sfzh).equals("")) {
                            Toast.makeText(context, "此人身份证号为无效格式", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (intent.resolveActivity(context.getPackageManager()) != null) {
                            context.startActivity(intent);
                        } else {
                            Toast.makeText(context, "未安装核查应用", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_check_result_view, null);
        iv_profile = view.findViewById(R.id.profile);
        iv_icon = view.findViewById(R.id.icon);
        return view;
    }

    private NameListRequestBean assembleRequestObj() {
        NameListRequestBean requestBean = new NameListRequestBean();
        requestBean.setYhdm(V_config.YHDM);
        requestBean.setImei(V_config.imei);
        requestBean.setImsi(V_config.imsi1);
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String pda_time = simpleDateFormat.format(date);
        requestBean.setPdaTime(pda_time);
        requestBean.setGpsX(V_config.gpsX);
        requestBean.setGpsY(V_config.gpsY);
        requestBean.setSex(gender);
        requestBean.setArea(area);
        requestBean.setPageNo(100);
        requestBean.setPageIndex(1);
        requestBean.setName(name);
        return requestBean;
    }

    @Override
    public <T> void onRequestFinish(String reqId, String reqName, T bean) {
        CheckResponseBean responseBean = (CheckResponseBean) bean;
        if (responseBean != null) {
            if (responseBean.getCode().equals("0")) {
                responseBean.getResult();
                String resultCode = responseBean.getResultCode();
                if (resultCode.equals("0")) {
                    iv_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_pass));
                    iv_profile.setImageDrawable(getResources().getDrawable(R.drawable.pass_profile));
                } else if (resultCode.equals("1")) {
                    //1表示拦截
                    iv_icon.setImageDrawable(getResources().getDrawable(R.drawable.ic_block));
                    iv_profile.setImageDrawable(getResources().getDrawable(R.drawable.block_profile));
                } else {
                    Toast.makeText(context, "异常错误", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "服务异常，无法获取数据", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "数据接入错误", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public <T extends ResultBase> Class<?> getBeanClass(String reqId, String reqName) {
        return CheckResponseBean.class;
    }
}
