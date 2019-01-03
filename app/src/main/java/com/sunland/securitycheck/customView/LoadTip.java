package com.sunland.securitycheck.customView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by PeitaoYe on 2018/5/7.
 */

public class LoadTip extends View {

    public static final int SUCCESS = 0;
    public static final int FAIL = 1;

    private Dialog mDialog;
    private int tip_type;
    private Context mContext;
    private Paint icon_paint;
    private ValueAnimator mValueAnimator;
    private float fraction = 0;
    private Path mPath;

    private Path dst;
    private PathMeasure pathMeasure;

    private boolean isSet = false;
    private boolean hasFinished = false;
    private boolean autoDismiss = false;

    public LoadTip(Context context, int type) {
        this(context, null, 0);
        tip_type = type;
    }

    public LoadTip(Context context) {
        this(context, null);
    }

    public LoadTip(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadTip(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mPath = new Path();
        dst = new Path();
        pathMeasure = new PathMeasure();
        initPaint();
        mValueAnimator = ValueAnimator.ofFloat(0, 1f);
        mValueAnimator.setDuration(380);
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                fraction = (float) animation.getAnimatedValue();

                invalidate();


            }
        });
        mValueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (pathMeasure.nextContour()) {
                    if (tip_type == SUCCESS) {
                        mValueAnimator.start();
                        hasFinished = true;

                    } else {
                        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1f);
                        valueAnimator.setDuration(300);
                        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                            @Override
                            public void onAnimationUpdate(ValueAnimator animation) {
                                fraction = (float) animation.getAnimatedValue();
                                invalidate();
                            }
                        });
                        valueAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
                                if (pathMeasure.nextContour()) {
                                    valueAnimator.start();
                                    hasFinished = true;
                                }
                            }
                        });
                        valueAnimator.start();
                    }
                }
            }
        });
    }

    private void initPaint() {
        icon_paint = new Paint();
        icon_paint.setColor(Color.WHITE);
        icon_paint.setStyle(Paint.Style.STROKE);
        icon_paint.setAntiAlias(true);
        icon_paint.setStrokeWidth(8);
        icon_paint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawIcon(tip_type);
        if (!isSet) {
            pathMeasure.setPath(mPath, false);
        }
        pathMeasure.getSegment(0, pathMeasure.getLength() * fraction, dst, true);
        canvas.drawPath(dst, icon_paint);
        isSet = true;
        if (hasFinished && fraction == 1 && autoDismiss) {
            if (mDialog != null) {
                mDialog.dismiss();
            }
        }
    }


    private void drawIcon(int type) {
        int r_x = getMeasuredWidth() / 2;
        int r_y = getMeasuredHeight() / 2;
        int radius;
        switch (type) {
            case SUCCESS:
                if (r_x <= r_y) {
                    radius = r_x;
                    int diff = r_y - r_x;
                    mPath.addCircle(r_x, r_y, radius - 5, Path.Direction.CW);
                    mPath.moveTo((float) 0.64 * radius, (float) 0.9766 * radius + diff);
                    mPath.lineTo((float) 0.89 * radius, (float) 1.211 * radius + diff);
                    mPath.lineTo((float) 1.391 * radius, (float) 0.711 * radius + diff);
                } else {
                    radius = r_y;
                    int diff = r_x - r_y;
                    mPath.addCircle(r_x, r_y, radius - 5, Path.Direction.CW);
                    mPath.moveTo((float) 0.64 * radius + diff, (float) 0.9766 * radius);
                    mPath.lineTo((float) 0.89 * radius + diff, (float) 1.211 * radius);
                    mPath.lineTo((float) 1.391 * radius + diff, (float) 0.711 * radius);
                }
                break;
            case FAIL:
                if (r_x >= r_y) {
                    radius = r_y;
                    mPath.addCircle(r_x, r_y, radius - 5, Path.Direction.CW);
                    int offset = (int) ((3 * radius / 7) * (Math.sqrt(2) / 2));
                    mPath.moveTo(r_x - offset, r_y - offset);
                    mPath.lineTo(r_x + offset, r_y + offset);
                    mPath.moveTo(r_x - offset, r_y + offset);
                    mPath.lineTo(r_x + offset, r_y - offset);
                } else {
                    radius = r_x;
                    mPath.addCircle(r_x, r_y, radius - 5, Path.Direction.CW);
                    int offset = (int) ((3 * radius / 7) * (Math.sqrt(2) / 2));
                    mPath.moveTo(r_x - offset, r_y - offset);
                    mPath.lineTo(r_x + offset, r_y + offset);
                    mPath.moveTo(r_x - offset, r_y + offset);
                    mPath.lineTo(r_x + offset, r_y - offset);

                }
                break;
        }
    }

    public void startAnim(Dialog dialog) {
        mDialog = dialog;
        mValueAnimator.start();
    }

    public void setAutoDismiss(boolean autoDismiss) {
        this.autoDismiss = autoDismiss;
    }
}
