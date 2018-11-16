package com.sunland.netmodule.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.sunland.netmodule.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class DialogUtils {

    public static Toast mToast = null;
    public static int mCurIndex = -1;

    /**
     * 创建黑色圈圈样式等待框
     *
     * @param context
     * @return
     */
    public static ProgressDialog showBlackProgressDlg(Context context) {
        return showBlackProgressDlg(context, "请稍候");
    }

    /**
     * 创建黑色圈圈样式等待框
     *
     * @param context
     * @param waitInfo 提示信息
     * @return
     */
    public static ProgressDialog showBlackProgressDlg(Context context, String waitInfo) {
        ProgressDialog progressDlg = new ProgressDialog(context, 0);
        progressDlg.setIndeterminate(false);
        progressDlg.setCancelable(false);
        progressDlg.show();
        Window window = progressDlg.getWindow();
        window.setContentView(R.layout.blackprogress);
        window.setBackgroundDrawable(new ColorDrawable());//5.0后使背景透明
        ((TextView) (window.findViewById(R.id.tv_info))).setText(waitInfo);
        return progressDlg;
    }

    /**
     * 创建请求等待框，不需要取消按钮，则cancelListener传空即可
     *
     * @param context
     * @param cancelListener
     * @return
     */
    public static ProgressDialog createProgressDlg(final Context context, final View.OnClickListener cancelListener) {
        return createProgressDlg(context, "请稍候", "正在发送请求", cancelListener);
    }

    /**
     * 创建请求等待框，不需要取消按钮，则cancelListener传空即可
     *
     * @param context
     * @param title
     * @param info
     * @param cancelListener
     * @return
     */
    public static ProgressDialog createProgressDlg(final Context context, String title, final String info, final View.OnClickListener cancelListener) {
        if (context == null)
            return null;

        final ProgressDialog dlg = new ProgressDialog(context, 0);
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        final Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_single_button);
        window.setBackgroundDrawable(new ColorDrawable());//5.0后使背景透明
        ((TextView) window.findViewById(R.id.tv_title)).setText(title);
        final TextView tv_content = ((TextView) window.findViewById(R.id.tv_content));
        tv_content.setText(info);

        tv_content.setMovementMethod(ScrollingMovementMethod.getInstance());

        if (cancelListener == null)
            ((Button) window.findViewById(R.id.button1)).setVisibility(View.GONE);
        else
            ((Button) window.findViewById(R.id.button1)).setText("取消");
        window.findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (cancelListener != null)
                    cancelListener.onClick(v);
                dlg.dismiss();
            }
        });

        final Timer checkTimer = new Timer();
        //设置开始计数器为0
        tv_content.setTag(0);

        checkTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                ((Activity) context).runOnUiThread(new Runnable() {
                    public void run() {
                        if (tv_content != null) {
                            Object obj = tv_content.getTag();
                            if (obj != null) {
                                int timeticks = Integer.parseInt(obj.toString());
                                String time = "";

                                try {
                                    SimpleDateFormat df = new SimpleDateFormat("mm:ss", Locale.getDefault());
                                    Date date = new Date(timeticks * 1000);
                                    df.setTimeZone(TimeZone.getTimeZone("UTC"));
                                    time = df.format(date);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    time = "" + timeticks;
                                }

                                String str = "...";
                                if (timeticks % 3 == 1)
                                    str = ".";
                                else if (timeticks % 3 == 2)
                                    str = "..";
                                else
                                    str = "...";

                                tv_content.setText(info + str + "\n" + time);
                                timeticks++;
                                tv_content.setTag(timeticks);
                            }
                        }
                    }
                });
            }
        }, 100, 1000);

        dlg.setOnDismissListener(new DialogInterface.OnDismissListener() {

            @Override
            public void onDismiss(DialogInterface dialog) {
                if (checkTimer != null) {
                    checkTimer.cancel();
                }
            }
        });

        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                }

                // 多任务查看
                if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_MENU) { // 82
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }

                return false;
            }
        });
        //		dlg.getWindow().setType(
//				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
        return dlg;
    }

    public static void showInfoMessage(Context context, String title,
                                       String info, final View.OnClickListener okListener) {
        if (context == null)
            return;

        Builder builder = new Builder(context);
        final AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();
        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_single_button);
        window.setBackgroundDrawable(new ColorDrawable());//5.0后使背景透明

        ((TextView) window.findViewById(R.id.tv_title)).setText(title);
        ((TextView) window.findViewById(R.id.tv_content)).setText(info);
        ((TextView) window.findViewById(R.id.tv_content)).setMovementMethod(ScrollingMovementMethod.getInstance());

        ((Button) window.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okListener != null)
                    okListener.onClick(v);
                dlg.dismiss();
            }
        });

        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                }

                // 多任务查看
                if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_MENU) { // 82
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }

                return false;
            }
        });
        //		dlg.getWindow().setType(
//				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    }

    public static void showQuestionMessage(Context context, String title,
                                           String info, final View.OnClickListener okListener,
                                           final View.OnClickListener cancelListener) {
        showQuestionMessage(context, title, info, "", "", okListener, cancelListener);
    }

    public static void showQuestionMessage(Context context, String title,
                                           String info, String leftBtnText, String rightBtnText, final View.OnClickListener leftListener,
                                           final View.OnClickListener rightListener) {
        if (context == null)
            return;

        Builder builder = new Builder(context);
        final AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        Window window = dlg.getWindow();
        window.setBackgroundDrawable(new ColorDrawable());//5.0后使背景透明
        window.setContentView(R.layout.dialog_common);

        ((TextView) window.findViewById(R.id.tv_title)).setText(title);
        ((TextView) window.findViewById(R.id.tv_content)).setText(info);
        if (!TextUtils.isEmpty(leftBtnText))
            ((Button) window.findViewById(R.id.button_left)).setText(leftBtnText);
        if (!TextUtils.isEmpty(rightBtnText))
            ((Button) window.findViewById(R.id.button_right)).setText(rightBtnText);

        ((Button) window.findViewById(R.id.button_left)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (leftListener != null)
                    leftListener.onClick(v);
                dlg.dismiss();
            }
        });

        ((Button) window.findViewById(R.id.button_right)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rightListener != null)
                    rightListener.onClick(v);
                dlg.dismiss();
            }
        });

        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                }

                // 多任务查看
                if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_MENU) { // 82
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }

                return false;
            }
        });
        //		dlg.getWindow().setType(
//				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    }

    public static void showToast(Context context, CharSequence text,
                                 int duration) {
        if (context == null)
            return;

        if (mToast == null) {
            mToast = Toast.makeText(context, text, duration);
        } else {
            mToast.setDuration(duration);
            mToast.setText(text);
        }

        mToast.show();
    }

    public static void showShortToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_SHORT);
    }

    public static void showLongToast(Context context, CharSequence text) {
        showToast(context, text, Toast.LENGTH_LONG);
    }

    //service中弹出框
    public static void showServiceInfoMessage(Context context, String title, String info, final View.OnClickListener okListener) {
        if (context == null)
            return;

        Builder builder = new Builder(context);
        final AlertDialog dlg = builder.create();
        dlg.setCanceledOnTouchOutside(false);
        dlg.show();

        Window window = dlg.getWindow();
        window.setContentView(R.layout.dialog_single_button);
        window.setBackgroundDrawable(new ColorDrawable());//5.0后使背景透明

        ((TextView) window.findViewById(R.id.tv_title)).setText(title);
        ((TextView) window.findViewById(R.id.tv_content)).setText(info);
        ((TextView) window.findViewById(R.id.tv_content)).setMovementMethod(ScrollingMovementMethod.getInstance());

        ((Button) window.findViewById(R.id.button1)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (okListener != null)
                    okListener.onClick(v);
                dlg.dismiss();
            }
        });

        dlg.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_HOME) {
                    return true;
                }

                // 多任务查看
                if (keyCode == KeyEvent.KEYCODE_APP_SWITCH) {
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_MENU) { // 82
                    return true;
                }

                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }

                return false;
            }
        });
        //		dlg.getWindow().setType(
//				WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG);
    }

    /**
     * 单选弹出框，带确认和取消按钮
     *
     * @param context
     * @param title          标题
     * @param items          供选择的内容选项数组
     * @param index          默认索引
     * @param oklistener     确定按钮回调
     * @param cancellistener 取消按钮回调
     */
    public static void showSingleChoiceItems(Context context, String title, String[] items, int index, final OnClickListener oklistener, final OnClickListener cancellistener) {
        mCurIndex = index;
        Builder b = new Builder(context);
        if (title != null)
            b.setTitle(title);
        b.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (oklistener != null)
                    oklistener.onClick(dialogInterface, mCurIndex);
            }
        });
        b.setNegativeButton("取消", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (cancellistener != null)
                    cancellistener.onClick(dialogInterface, mCurIndex);
            }
        });
        b.setSingleChoiceItems(items, mCurIndex, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mCurIndex = i;
            }
        });

        AlertDialog a = b.create();
        a.setCanceledOnTouchOutside(false);
        a.show();
    }

    public static void showSingleChoiceItemsNoCancel(Context context, String title, String[] items, int index, final OnClickListener oklistener) {
        mCurIndex = index;
        Builder b = new Builder(context);
        if (title != null)
            b.setTitle(title);
        b.setPositiveButton("确定", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (oklistener != null)
                    oklistener.onClick(dialogInterface, mCurIndex);
            }
        });
        b.setSingleChoiceItems(items, mCurIndex, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mCurIndex = i;
            }
        });

        AlertDialog a = b.create();
        a.setCanceledOnTouchOutside(false);
        a.show();
    }
}
