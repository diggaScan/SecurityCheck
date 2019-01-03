package com.sunland.securitycheck.utils;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sunland.securitycheck.R;
import com.sunland.securitycheck.customView.LoadTip;


/**
 * Created by PeitaoYe on 2018/5/4.
 */

public class DialogUtils {

    public static final int TYPE_WARNING = 0;
    public static final int TYPE_INFO = 1;
    public static final int TYPE_QUESTION = 2;
    public static final int TYPE_PROGRESS = 3;
    private static DialogUtils dialogUtils = null;
    private Dialog mDialog;
    private Window mWindow;
    private Context mContext;
    private OnCancelClickListener mOnCancelClickListener;
    private OnConfirmClickListener mOnConfirmClickListener;
    private View mView;
    private Button cancel_button;
    private Button confirm_button;
    private TextView mTextView;
    private String info;
    private LinearLayout icon_container;
    private LinearLayout button_container;
    private int window_height;
    private int window_width;
    private View divideLine;
    private int dialog_type;
    private boolean unstopable;
    private ProgressBar progressBar;
    private boolean isShown = false;
    private View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnCancelClickListener != null) {
                mOnCancelClickListener.onCancel();
            }
            dialogDismiss();
        }
    };
    private View.OnClickListener confirmListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnConfirmClickListener != null) {
                mOnConfirmClickListener.onConfirm();
            }
        }
    };

    private DialogUtils() {
    }

    public static DialogUtils getInstance() {
        if (dialogUtils == null) {
            synchronized (DialogUtils.class) {
                if (dialogUtils == null) {
                    dialogUtils = new DialogUtils();
                }
            }
        }
        return dialogUtils;
    }

    public Window getmWindow() {
        return mWindow;
    }

    public void showSelectDialog(Context context, String info, String[] button_text, View.OnClickListener[] listeners) {
        if (isShown) {
            return;
        }
        mContext = context;
        this.info = info;

        mDialog = new MyDialog(mContext);
        mDialog.onBackPressed();
        mWindow = mDialog.getWindow();
        layoutWindow();

        mDialog.setCanceledOnTouchOutside(false);
        mView = getInflaterView();
        LinearLayout.LayoutParams lp = getLayoutParams();

        mTextView = mView.findViewById(R.id.info);
        mTextView.setText(info);

        icon_container = mView.findViewById(R.id.icon_container);
        ImageView warning_img = new ImageView(mContext);
        warning_img.setImageResource(R.drawable.ic_select_all_black_24dp);
        warning_img.setLayoutParams(lp);
        icon_container.addView(warning_img);

        button_container = mView.findViewById(R.id.button_container);

        button_container.removeAllViews();

        if (button_text.length != listeners.length) {
            throw new IllegalArgumentException("num of buttons should be matched with the num of Click listeners");
        }

        for (int i = 0; i < button_text.length; i++) {
            Button button = new Button(mContext);
            lp.width = 0;
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.weight = 1;
            button.setBackgroundResource(R.drawable.button_corner_background);
            button.setText(button_text[i]);
            button.setLayoutParams(lp);
            button.setOnClickListener(listeners[i]);
            button_container.addView(button);


        }
        mDialog.setContentView(mView);
        mWindow.setLayout(window_width, window_height);
        mDialog.show();
        isShown = true;

    }

    public void showDialog(Context context, String info, int type, @Nullable OnCancelClickListener onCancelClickListener, @Nullable OnConfirmClickListener confirmClickListener) {
        if (isShown) {
            return;
        }
        mContext = context;
        dialog_type = type;
        mOnCancelClickListener = onCancelClickListener;
        mOnConfirmClickListener = confirmClickListener;

        this.info = info;

        mDialog = new MyDialog(mContext);
        mWindow = mDialog.getWindow();
        layoutWindow();
        dialog_type = type;
        initDialog(type);

        if (cancel_button != null) {
            cancel_button.setOnClickListener(cancelListener);
        }

        if (confirm_button != null) {
            confirm_button.setOnClickListener(confirmListener);
        }

    }

    public void showProgress(Context context) {
        if (isShown) {
            return;
        }
        mContext = context;
        mDialog = new MyDialog(mContext);
        mWindow = mDialog.getWindow();
        layoutProgress();
        initDialog();
    }

    private void initDialog() {
        mDialog.setCanceledOnTouchOutside(false);
        mView = getInflaterView();

        mTextView = mView.findViewById(R.id.info);
        button_container = mView.findViewById(R.id.button_container);
        View padding = mView.findViewById(R.id.padding);

        button_container.setVisibility(View.GONE);
        mTextView.setVisibility(View.GONE);
        padding.setVisibility(View.GONE);

        progressBar = mView.findViewById(R.id.progress);
        mDialog.setContentView(mView);

        progressBar.setVisibility(View.VISIBLE);
        mDialog.show();

    }


    private void initDialog(int type) {

        mDialog.setCanceledOnTouchOutside(false);
        mView = getInflaterView();

        cancel_button = mView.findViewById(R.id.cancel_button);
        confirm_button = mView.findViewById(R.id.confirm_button);
        divideLine = mView.findViewById(R.id.divide_line);
        mTextView = mView.findViewById(R.id.info);
        icon_container = mView.findViewById(R.id.icon_container);
        button_container = mView.findViewById(R.id.button_container);
        progressBar = mView.findViewById(R.id.progress);
        if (unstopable) {
            button_container.setVisibility(View.GONE);
        }

        mTextView.setText(info);

        if (mOnConfirmClickListener == null) {
            confirm_button.setVisibility(View.GONE);
            divideLine.setVisibility(View.GONE);
        }
        mDialog.setContentView(mView);
        mWindow.setLayout(window_width, window_height);
        LinearLayout.LayoutParams lp = getLayoutParams();


        switch (type) {
            case TYPE_PROGRESS:

                progressBar.setVisibility(View.VISIBLE);
                mDialog.show();
                isShown = true;
                break;
            case TYPE_WARNING:
                ImageView warning_img = new ImageView(mContext);
                warning_img.setImageResource(R.drawable.ic_warning_black_24dp);
                warning_img.setLayoutParams(lp);
                icon_container.addView(warning_img);
                mDialog.show();
                isShown = true;
                break;
            case TYPE_INFO:
                ImageView info_img = new ImageView(mContext);
                info_img.setImageResource(R.drawable.ic_info_outline_black_24dp);
                info_img.setLayoutParams(lp);
                icon_container.addView(info_img);
                mDialog.show();
                isShown = true;
                break;
            case TYPE_QUESTION:
                ImageView ques_img = new ImageView(mContext);
                ques_img.setImageResource(R.drawable.ic_help_outline_black_24dp);
                ques_img.setLayoutParams(lp);
                icon_container.addView(ques_img);
                confirm_button.setVisibility(View.VISIBLE);
                divideLine.setVisibility(View.VISIBLE);
                mDialog.show();
                isShown = true;
                break;
            default:
                break;
        }
    }

    private void layoutWindow() {
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setBackgroundDrawable(new ColorDrawable());
        window_width = (int) (((AppCompatActivity) mContext).getWindow().getDecorView().getMeasuredWidth() * 0.65);
        window_height = (int) (((AppCompatActivity) mContext).getWindow().getDecorView().getMeasuredWidth() * 0.65 * 0.618);
    }

    private void layoutProgress() {
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);
        mWindow.setGravity(Gravity.CENTER);
        mWindow.setBackgroundDrawable(new ColorDrawable());
        window_width = (int) (((AppCompatActivity) mContext).getWindow().getDecorView().getMeasuredWidth() * 0.3);
        window_height = (int) (((AppCompatActivity) mContext).getWindow().getDecorView().getMeasuredWidth() * 0.3 * 0.618);
    }

    private View getInflaterView() {
        return LayoutInflater.from(mContext).inflate(R.layout.dialog_layout, null);
    }

    public void dialogDismiss() {
        if (mDialog != null) {
            mDialog.dismiss();
        }

        recycle();
    }

    public void setUnstopable(boolean unstopable) {
        this.unstopable = unstopable;
    }

    public void loadSuccess(String tips, boolean isDismiss) {
        if (dialog_type != TYPE_PROGRESS)
            return;
        icon_container.removeAllViews();
        LoadTip successLoad = new LoadTip(mContext, LoadTip.SUCCESS);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        successLoad.setLayoutParams(lp);
        icon_container.addView(successLoad);
        cancel_button.setText("确定");
        cancel_button.setOnClickListener(confirmListener);
        mTextView.setText(tips);
        successLoad.setAutoDismiss(isDismiss);
        successLoad.startAnim(mDialog);

    }

    public void setmOnConfirmClickListener(OnConfirmClickListener onConfirmClickListener) {
        this.mOnConfirmClickListener = onConfirmClickListener;
    }

    public void loadFail(String tip, boolean isDismiss) {
        if (dialog_type != TYPE_PROGRESS)
            return;
        icon_container.removeAllViews();
        LoadTip fail = new LoadTip(mContext, LoadTip.FAIL);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        fail.setLayoutParams(lp);
        icon_container.addView(fail);
        mTextView.setText(tip);
        cancel_button.setText("确定");
        fail.setAutoDismiss(isDismiss);
        fail.startAnim(mDialog);
    }

    private void recycle() {
        icon_container.removeAllViews();
        unstopable = false;
        isShown = false;
        if (mDialog != null) {
            mDialog = null;
        }

        mContext = null;
    }

    private LinearLayout.LayoutParams getLayoutParams() {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        lp.gravity = Gravity.CENTER;
        return lp;
    }

    public interface OnCancelClickListener {
        void onCancel();
    }

    public interface OnConfirmClickListener {
        void onConfirm();
    }

    private class MyDialog extends Dialog {
        public MyDialog(Context context) {
            super(context);
        }

        @Override
        public void onBackPressed() {
            return;
        }
    }
}
