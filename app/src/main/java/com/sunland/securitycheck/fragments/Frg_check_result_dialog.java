package com.sunland.securitycheck.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.sunland.securitycheck.R;

public class Frg_check_result_dialog extends DialogFragment {

    public Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
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
                        Toast.makeText(context, "跳转核查界面", Toast.LENGTH_SHORT).show();
                    }
                })
                .create();
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    private View getCustomView() {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_check_result_view, null);
        ImageView iv_profile = view.findViewById(R.id.profile);
        ImageView iv_icon = view.findViewById(R.id.icon);
        return view;
    }
}
