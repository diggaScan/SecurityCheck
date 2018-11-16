package com.sunland.securitycheck;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by PeitaoYe on 2018/3/28.
 */

public class BannerIndicator extends LinearLayout {

    Paint paint;
    int padding;
    int radius;
    int rect_width;
    int rect_height;
    private MovingView moving;
    private int currentPosition=0;
    private float offset=0;
    private int item_nums;
    private int gap=(padding+radius)*2;
    private Context context;
    private WindowManager windowManager;
    public BannerIndicator(Context context){
        this(context,null);
    }

    public BannerIndicator(Context context, AttributeSet attributeSet){
        this(context,attributeSet,0);
    }


    public BannerIndicator(Context context, AttributeSet attributeSet, int defStyleaAttr){
        super(context,attributeSet,defStyleaAttr);
        this.context=context;
        init();
    }

    public void setPadding(int padding) {
        this.padding = padding;
    }

    public Paint getPaint() {
        return paint;
    }

    public int getItem_nums() {
        return item_nums;
    }

    public void setItem_nums(int item_nums) {
        this.item_nums = item_nums;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    private void init(){
        //setOrientation(LinearLayout.HORIZONTAL);
        windowManager=(WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics=new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        radius=(int)metrics.density*3;
        padding=(int)metrics.density*5;
        rect_width=(int)metrics.density*30;
        rect_height=(int)metrics.density*5;
        setWillNotDraw(false);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.GRAY);

        moving=new MovingView(context);
        addView(moving);
    }

    public void setMovingDotColor(int color){
        moving.setDotColor(color);
    }

    public void setDotsColor(int color){
        paint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension((rect_width+padding)*item_nums+padding,rect_height+padding*2);

    }

    public void setCurrentPosition(int position,float offset){
        this.currentPosition=position;
        this.offset=offset;
        requestLayout();
    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int unit_distance=padding+rect_width;
        moving.layout((int)(padding+unit_distance*(currentPosition+offset)),padding,(int)(unit_distance*(currentPosition+offset+1)),padding+rect_height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(Build.VERSION.SDK_INT>21){
            for(int i=0;i<item_nums;i++){
                canvas.drawRoundRect(padding+i*(rect_width+padding),padding,(padding+rect_width)*(i+1),padding+rect_height,30,30,paint);
            }
        }else {
            for(int i=0;i<item_nums;i++){
                canvas.drawRect(padding+i*(rect_width+padding),padding,(padding+rect_width)*(i+1),padding+rect_height,paint);
            }
        }

    }

    //the moving indicator which shows position of the page.
    private class MovingView extends View {

        Paint paint;

        public MovingView(Context context) {
            this(context,null);
        }

        public MovingView(Context context, @Nullable AttributeSet attrs) {
            this(context, attrs,0);
        }

        public MovingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            init();
        }

        void init(){
            paint=new Paint();
            paint.setColor(Color.CYAN);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        private void setDotColor(int color){
            paint.setColor(color);
        }
        @Override

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setMeasuredDimension(rect_width,rect_height);
        }
        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            if(Build.VERSION.SDK_INT>21){
                canvas.drawRoundRect(0,0,rect_width,rect_height,30,30,paint);
            }else {
                canvas.drawRect(0,0,rect_width,rect_height,paint);
            }



        }
    }
}