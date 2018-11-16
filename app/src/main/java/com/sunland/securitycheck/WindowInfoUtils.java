package com.sunland.securitycheck;

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
	
	
	public static int px2dp(Context context,int px){
		DisplayMetrics displayMetrics=getWindowMetrics(context);
		return (int)(px/displayMetrics.density);
	}
	
	/**
	* @return action_bar_height
	*/
    public static int getActionBarHeight(Context context){
        if(context instanceof AppCompatActivity){
            return ((AppCompatActivity)context).getSupportActionBar().getHeight();
        }
        return -1;
    }
	
	/**
	* @return status_bar_height
	*/
    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
	
	/**
	* @return navigation bar height
	*/
		/**
	* @return navigation bar height
	*/
	public static int getNavigationBarHeight(Context context) {
		int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
		if (resourceId > 0 ) {
			return context.getResources().getDimensionPixelSize(resourceId);
		}
		return 0;	
	}


}
