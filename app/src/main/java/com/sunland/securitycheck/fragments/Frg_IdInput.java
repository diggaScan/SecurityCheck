package com.sunland.securitycheck.fragments;

import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.sunland.netmodule.Global;
import com.sunland.netmodule.network.RequestManager;
import com.sunland.securitycheck.DataModel;
import com.sunland.securitycheck.R;
import com.sunland.securitycheck.activities.Ac_check;
import com.sunland.securitycheck.bean.i_check_person.CheckRequestBean;
import com.sunland.securitycheck.customView.SpinButton;
import com.sunland.securitycheck.utils.UtilsString;
import com.sunland.sunlandkeyboard.SunlandKeyBoard;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

public class Frg_IdInput extends Frg_base {
    private String TAG = "nmh";
    @BindView(R.id.nation)
    public SpinButton sb_nation;
    @BindView(R.id.identifier)
    public SpinButton sb_identifier;
    @BindView(R.id.id)
    public EditText et_id;
    @BindView(R.id.id_input_enter)
    public Button btn_enter;
    @BindView(R.id.relativeLayout)
    public RelativeLayout relativeLayout;
    @BindView(R.id.scrollView)
    public ScrollView scrollView;
    @BindView(R.id.loading_layout)
    public FrameLayout loading_layout;

    private RequestManager mRequestManager;
    private String areaCode;
    public String sfzh;

    @Override
    public int setFrgLayout() {
        return R.layout.frg_check_id;
    }

    @Override
    public void init() {
        ComponentName componentName = new ComponentName("com.sunland.securitycheck", "com.sunland.securitycheck.Ac_check"); //用于获取该activity的softInputMode
        SunlandKeyBoard sunlandKeyBoard = new SunlandKeyBoard(context);
        sunlandKeyBoard.bindMainLayout(relativeLayout, scrollView);
        sunlandKeyBoard.bindTarget(((Ac_check) context).keyboard, et_id, SunlandKeyBoard.KeyboardMode.IDENTITY, componentName);
        mRequestManager = ((Ac_check) context).getRequestManager();
        areaCode = ((Ac_check) context).getArea_code();
        et_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!UtilsString.checkId(s.toString()).equals("")) {
                    mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, "querySummitPerson", assembleRequestObj(), 15000);
                    mRequestManager.postRequestWithoutDialog();
                    loading_layout.setVisibility(View.VISIBLE);
                    ((Ac_check) context).which = 1;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        sb_nation.setDataSet(Arrays.asList(DataModel.NATIONS));
        sb_nation.setSelection(0);
        sb_nation.setHeaderTitle("国籍");

        sb_identifier.setDataSet(Arrays.asList(DataModel.CERTIFICATIONS));
        sb_identifier.setSelection(0);
        sb_identifier.setHeaderTitle("证件种类");


    }

    @OnClick(R.id.id_input_enter)
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.id_input_enter:
                if (UtilsString.checkId(et_id.getText().toString()).equals("")) {
                    Toast.makeText(context, "请输入正确的身份证号", Toast.LENGTH_SHORT).show();
                    break;
                }
//                ((Ac_check) context).hop2Activity(Ac_check_result.class);
                mRequestManager.addRequest(Global.ip, Global.port, Global.postfix, "querySummitPerson", assembleRequestObj(), 15000);
                mRequestManager.postRequestWithoutDialog();
                loading_layout.setVisibility(View.VISIBLE);
                ((Ac_check) context).which = 1;
//                Frg_check_result_dialog dialog=new Frg_check_result_dialog();
//                dialog.show(((Ac_check)context).getSupportFragmentManager(),"dialog");
                break;
        }
    }

    private CheckRequestBean assembleRequestObj() {
        CheckRequestBean bean = new CheckRequestBean();
        assembleBasicRequest(bean);
        String nation_code;
        int code = sb_nation.getSelectedItemPosition() + 1;
        if (code < 10) {
            nation_code = "0" + code;
        } else {
            nation_code = Integer.toString(code);
        }
        String id_class;
        int id = sb_identifier.getSelectedItemPosition() + 1;
        if (id < 10) {
            id_class = "0" + id;
        } else {
            id_class = Integer.toString(id);
        }
        sfzh = et_id.getText().toString().trim();
        bean.setNation(nation_code);
        bean.setZjlx(id_class);
        bean.setZjhm(sfzh);
        bean.setArea(areaCode);
        bean.setXm("");
        return bean;

    }


}
