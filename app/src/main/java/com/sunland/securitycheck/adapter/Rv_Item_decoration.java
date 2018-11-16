package com.sunland.securitycheck.adapter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sunland.securitycheck.R;
import com.sunland.securitycheck.WindowInfoUtils;


public class Rv_Item_decoration extends RecyclerView.ItemDecoration {
    private int offset;
    private Paint paint;
    private Context context;

    public Rv_Item_decoration(Context context) {
        this.context = context;
        paint = new Paint();
        paint.setColor(context.getResources().getColor(R.color.divider_color));
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
//        if (parent.getChildAdapterPosition(view) != 0) {
        outRect.bottom = WindowInfoUtils.dp2px(context, 1);
        offset = outRect.bottom;
//        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childrenCount = parent.getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            View view = parent.getChildAt(i);
//            if (parent.getChildAdapterPosition(view) == 0) {
//                continue;
//            }
            int top = view.getBottom();
            int left = parent.getLeft() + parent.getPaddingLeft();
            int bottom = view.getBottom() + offset;
            int right = parent.getWidth() - parent.getPaddingRight();
            Rect rect = new Rect(left, top, right, bottom);
            c.drawRect(rect, paint);
        }

    }
}
