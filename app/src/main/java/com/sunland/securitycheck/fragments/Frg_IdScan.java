package com.sunland.securitycheck.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.activities.Ac_check;
import com.sunland.securitycheck.bean.i_check_person.CheckRequestBean;

import butterknife.BindView;
import butterknife.OnClick;

public class Frg_IdScan extends Frg_base {

    @BindView(R.id.nfc)
    public ImageView iv_nfc;
    @BindView(R.id.id_scan_enter)
    public Button btn_enter;
    @BindView(R.id.id_name)
    public TextView tv_name;
    @BindView(R.id.id_gender)
    public TextView tv_gender;
    @BindView(R.id.id_nation)
    public TextView tv_nation;
    @BindView(R.id.id_year)
    public TextView tv_year;
    @BindView(R.id.id_month)
    public TextView tv_month;
    @BindView(R.id.id_day)
    public TextView tv_day;
    @BindView(R.id.id_address)
    public TextView tv_address;
    @BindView(R.id.id_num)
    public TextView tv_num;
    @BindView(R.id.loading_layout)
    public FrameLayout loading_layout;
    public String num;
    private String name;
    private String gender;
    private RequestManager mRequestManager;
    private String year;
    private String month;
    private String day;

    @Override
    public int setFrgLayout() {
        return R.layout.frg_check_scan;
    }

    @Override
    public void init() {
        mRequestManager = ((Ac_check) context).getRequestManager();
    }

    @OnClick({R.id.nfc, R.id.id_scan_enter, R.id.face_scan})
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.nfc:
                Intent intent = new Intent("cybertech.pstore.intent.action.NFC_READER");
                intent.setPackage("cn.com.cybertech.nfc.reader");
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "请安装相应NFC模块", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.face_scan:
                // TODO: 2018/11/16/016 hop to face_scan
                Toast.makeText(context, "未安装人脸识别组件", Toast.LENGTH_SHORT).show();
                break;
            case R.id.id_scan_enter:
//                ((Ac_check) context).hop2Activity(Ac_check_result.class);
                mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, "querySummitPerson", assembleRequestObj(), 15000);
                mRequestManager.postRequestWithoutDialog();
                loading_layout.setVisibility(View.VISIBLE);
                ((Ac_check) context).which = 0;
//                Frg_check_result_dialog dialog = new Frg_check_result_dialog();
//                dialog.setInfo(num, Integer.valueOf(gender), name, ((Ac_check) context).area_code);
//                dialog.show(((Ac_check) context).getSupportFragmentManager(), "dialog");
                break;
        }
    }

    private CheckRequestBean assembleRequestObj() {
        CheckRequestBean bean = new CheckRequestBean();
        assembleBasicRequest(bean);
        bean.setNation("01");
        bean.setZjlx("01");
        bean.setZjhm(num);
        bean.setArea(((Ac_check) context).area_code);
        bean.setXm(name);
        return bean;

    }

    public void updateViews(Intent intent) {
        btn_enter.setVisibility(View.VISIBLE);
        name = intent.getStringExtra("name");
        gender = intent.getStringExtra("sex");
        String nation = intent.getStringExtra("nation");
        String address = intent.getStringExtra("address");
        num = intent.getStringExtra("identity");
        String birthday = intent.getStringExtra("birthday");

        tv_name.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_gender.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_nation.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_year.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_month.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_day.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_address.setBackgroundColor(Color.argb(0, 0, 0, 0));
        tv_num.setBackgroundColor(Color.argb(0, 0, 0, 0));

        tv_name.setText(name);
        tv_gender.setText(gender);
        tv_nation.setText(nation);
        tv_address.setText(address);
        tv_num.setText(num);

        try {
            year = birthday.substring(0, 4);
            month = birthday.substring(4, 6);
            day = birthday.substring(6, 8);
            tv_year.setText(year);
            tv_month.setText(month);
            tv_day.setText(day);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
