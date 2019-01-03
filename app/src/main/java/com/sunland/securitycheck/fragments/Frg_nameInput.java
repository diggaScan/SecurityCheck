package com.sunland.securitycheck.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.DataModel;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.activities.Ac_check;
import com.sunland.securitycheck.activities.Ac_name_list;
import com.sunland.securitycheck.customView.SpinButton;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class Frg_nameInput extends Frg_base {
    @BindView(R.id.name)
    public EditText et_name;
    @BindView(R.id.gender)
    public SpinButton sb_gender;
    @BindView(R.id.name_input_enter)
    public Button btn_enter;

    private RequestManager mRequestManager;
    private String area_code;

    @Override
    public int setFrgLayout() {
        return R.layout.frg_check_name;
    }

    @Override
    public void init() {
        mRequestManager = ((Ac_check) context).getRequestManager();
        area_code = ((Ac_check) context).getArea_code();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sb_gender.setDataSet(Arrays.asList(DataModel.gender));
        sb_gender.setSelection(0);
        sb_gender.setHeaderTitle("性别");
    }

    @OnClick(R.id.name_input_enter)
    public void onClick(View view) {
        String name = et_name.getText().toString();
        if (name == null || name.isEmpty()) {
            Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT).show();
            return;
        }
        int gender = sb_gender.getSelectedItemPosition();
        Bundle bundle = new Bundle();
        bundle.putString("paperName", name);
        bundle.putInt("sex", gender);
        bundle.putString("area_code", area_code);
        ((Ac_check) context).hop2Activity(Ac_name_list.class, bundle);
    }

}
