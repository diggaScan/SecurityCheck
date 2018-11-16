package com.sunland.sunlandkeyboard;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class WindowInfoUtils {

    public static DisplayMetrics getWindowMetrics(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = getWindowMetrics(context);
        return (int) (displayMetrics.density * dp);
    }


    public static int getActionBarHeight(Context context){
        if(context instanceof AppCompatActivity){
            return ((AppCompatActivity)context).getSupportActionBar().getHeight();
        }
        return -1;
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }

}
