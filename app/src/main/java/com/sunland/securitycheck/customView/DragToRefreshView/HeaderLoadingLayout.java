package com.sunland.securitycheck.customView.DragToRefreshView;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sunland.securitycheck.R;


public class HeaderLoadingLayout extends FrameLayout {

    private TextView tv_tips;
    private FrameLayout fl_content;

    public HeaderLoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public HeaderLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public HeaderLoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.header_loading_layout, this);
        tv_tips = view.findViewById(R.id.tips);
        fl_content = view.findViewById(R.id.container);

    }

    public void setTips(String tips) {
        tv_tips.setText(tips);
    }

    public int getContentHeight() {
        return tv_tips.getHeight();
    }

}
