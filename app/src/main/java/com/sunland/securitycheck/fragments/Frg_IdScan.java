package com.sunland.securitycheck.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sunland.securitycheck.R;
import com.sunland.securitycheck.activities.Ac_check;
import com.sunland.securitycheck.activities.Ac_check_result;

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

    @Override
    public int setFrgLayout() {
        return R.layout.frg_check_scan;
    }

    @Override
    public void init() {

    }

    @OnClick({R.id.nfc, R.id.id_scan_enter})
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
// TODO: 2018/11/2/002  delete this part 
//                Intent intent = new Intent(DataModel.ACTION_NFC_READ_IDCARD_SUCCESS);
//                intent.putExtra("name", "叶培涛");
//                intent.putExtra("sex", "男");
//                intent.putExtra("nation", "汉");
//                intent.putExtra("address", "扎实大大大多撒所大所大所多爱仕达多");
//                intent.putExtra("identity", "330226199312016717");
//                context.sendBroadcast(intent);

                break;
            case R.id.id_scan_enter:
//                ((Ac_check) context).hop2Activity(Ac_check_result.class);
                Frg_check_result_dialog dialog=new Frg_check_result_dialog();
                dialog.show(((Ac_check)context).getSupportFragmentManager(),"dialog");
                break;
        }
    }

    public void updateViews(Intent intent) {
        btn_enter.setVisibility(View.VISIBLE);
        String name = intent.getStringExtra("name");
        String gender = intent.getStringExtra("sex");
        String nation = intent.getStringExtra("nation");
        String address = intent.getStringExtra("address");
        String num = intent.getStringExtra("identity");
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


    }
}
