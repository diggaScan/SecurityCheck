package com.sunland.securitycheck.customView.DragToRefreshView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Build;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.sunland.securitycheck.R;


public class DragToRefreshView extends LinearLayout {

    private Context mContext;

    private HeaderLoadingLayout headerlLoadingLayout;
    private FooterLoadingLayout footerLoadingLayout;

    private RecyclerView recyclerView;

    private int mTouchSlop;
    private float initialMotionX, initialMotionY;
    private float lastMotionX, lastMotionY;
    private int maxPullRange;

    private State mState;

    private float fraction = 2.0f;
    private boolean mIsBeingDragged;

    private ValueAnimator mValueAnimator;

    private OnUpdateListener updateListener;

    private boolean allow_footer_refresh = true;
    private boolean allow_header_refresh = true;

    public DragToRefreshView(Context context) {
        this(context, null);
    }

    public DragToRefreshView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public DragToRefreshView(Context context, AttributeSet attributeSet, int defStyleAttr) {
        super(context, attributeSet, defStyleAttr);
        this.mContext = context;
        init();
    }

    private void init() {

        setOrientation(VERTICAL);
        setBackgroundResource(R.color.white);
        headerlLoadingLayout = new HeaderLoadingLayout(mContext);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(headerlLoadingLayout, lp);

        footerLoadingLayout = new FooterLoadingLayout(mContext);
        LayoutParams lpp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        lpp.gravity = Gravity.TOP;
        addView(footerLoadingLayout, -1, lpp);

        ViewConfiguration viewConfiguration = ViewConfiguration.get(mContext);
        mTouchSlop = viewConfiguration.getScaledTouchSlop();

        mValueAnimator = ValueAnimator.ofFloat(1.0F, 0.0F);
        mValueAnimator.setDuration(500);
        mValueAnimator.setInterpolator(new DecelerateInterpolator());

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isFooterRefreshing() || isHeaderRefreshing()) {
            return false;
        }

        int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!canScrollUp()) {
                    initialMotionX = lastMotionX = ev.getX();
                    initialMotionY = lastMotionY = ev.getY();
                    mIsBeingDragged = false;
                }
                if (!canScrollDown()) {
                    initialMotionX = lastMotionX = ev.getX();
                    initialMotionY = lastMotionY = ev.getY();
                    mIsBeingDragged = false;
                }
                break;

            case MotionEvent.ACTION_MOVE:
                float v_diff, h_diff, abs_v_diff, abs_h_diff;
                v_diff = ev.getY() - lastMotionY;
                h_diff = ev.getX() - lastMotionX;

                abs_v_diff = Math.abs(v_diff);
                abs_h_diff = Math.abs(h_diff);

                if (abs_v_diff > abs_h_diff && abs_v_diff > mTouchSlop && v_diff > 1.0F && !canScrollUp() && allow_header_refresh) {
                    lastMotionY = ev.getY();
                    lastMotionX = ev.getX();
                    return mIsBeingDragged = true;
                }
                if (abs_v_diff > abs_h_diff && abs_v_diff > mTouchSlop && v_diff < -1.0F && !canScrollDown() && allow_footer_refresh) {
                    lastMotionY = ev.getY();
                    lastMotionX = ev.getX();
                    return mIsBeingDragged = true;
                }
                break;
        }
        return false;
    }

    public void unableHeaderRefresh(boolean allow_header_refresh) {
        this.allow_header_refresh = allow_header_refresh;
    }

    public void unableFooterRefresh(boolean allow_footer_refresh) {
        this.allow_footer_refresh = allow_footer_refresh;


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                lastMotionX = event.getX();
                lastMotionY = event.getY();
                pull_operation();
                break;
            case MotionEvent.ACTION_UP:
                if (mState != null && mState == State.header_release_to_load) {
                    smoothScrollTo(-headerlLoadingLayout.getContentHeight());
                    headerlLoadingLayout.setTips("正在刷新...");
                    if (updateListener != null) {
                        updateListener.onRefreshing(this);
                    }
                } else if (mState != null && mState == State.header_pull_to_load) {
                    smoothScrollTo(0);
                }

                if (mState != null && mState == State.footer_release_to_load) {
                    smoothScrollTo(footerLoadingLayout.getContentHeight());
                    footerLoadingLayout.setTips("正在刷新...");
                    if (updateListener != null) {
                        updateListener.onRefreshing(this);
                    }
                } else if (mState != null && mState == State.footer_pull_to_load) {
                    smoothScrollTo(0);
                }
                mIsBeingDragged = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        adjusViewSize();
    }

    private void adjusViewSize() {
        maxPullRange = (int) (getMeasuredHeight() / fraction);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, maxPullRange);
        headerlLoadingLayout.setLayoutParams(lp);
        footerLoadingLayout.setLayoutParams(lp);
        if (Build.VERSION.SDK_INT == 25) {
            setPadding(0, -120, 0, 0);
        } else {
            setPadding(0, -maxPullRange, 0, 0);
        }

    }

    private boolean canScrollUp() {
        return recyclerView.canScrollVertically(-1);
    }

    private boolean canScrollDown() {
        return recyclerView.canScrollVertically(1);
    }

    private void pull_operation() {

        float scroll_value = (initialMotionY - lastMotionY) / fraction;
        if (scroll_value >= 0) {
            if (scroll_value > footerLoadingLayout.getContentHeight()) {
                footerLoadingLayout.setTips("释放刷新");
                mState = State.footer_release_to_load;
            } else {
                footerLoadingLayout.setTips("上拉加载...");
                mState = State.footer_pull_to_load;
            }
        } else {
            if (-scroll_value > headerlLoadingLayout.getContentHeight()) {
                headerlLoadingLayout.setTips("释放刷新...");
                mState = State.header_release_to_load;
            } else {
                headerlLoadingLayout.setTips("下拉加载...");
                mState = State.header_pull_to_load;
            }
        }

//        if ((!canScrollUp()) && scroll_value >= 0) {
//            return;
//        }
//        if ((!canScrollDown()) && scroll_value <= 0) {
//            return;
//        }
        scrollTo(0, (int) scroll_value);
    }

    private void smoothScrollTo(final int scrollValue) {
        final int current_position = getScrollY();
        mValueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float value = (Float) animation.getAnimatedValue();
                int diff = current_position - scrollValue;
                scrollTo(0, (int) (value * diff) + scrollValue);
            }
        });
        mValueAnimator.start();
    }

    public void dismiss() {
        if (mState == State.header_release_to_load) {
            post(new Runnable() {
                @Override
                public void run() {
                    smoothScrollTo(0);
                    if (updateListener != null) {
                        updateListener.onFinished(DragToRefreshView.this);
                        mState = State.header_idle;
                    }
                }
            });
        } else if (mState == State.footer_release_to_load) {
            // TODO: 2018/8/31 可能存在不执行的情况
//            post(new Runnable() {
//                @Override
//                public void run() {
//                    scrollTo(0, 0);
//                    if (updateListener != null) {
//                        updateListener.onFinished(DragToRefreshView.this);
//                        mState=State.footer_idle;
//                    }
//                }
//            });
            smoothScrollTo(0);
            if (updateListener != null) {
                updateListener.onFinished(DragToRefreshView.this);
                mState = State.footer_idle;
            }
        }
    }

    public State getState() {
        return mState;
    }

    public void addMainContent(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        removeView(recyclerView);
        addView(recyclerView, 1, lp);
    }

    public void setUpdateListener(OnUpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public boolean isHeaderRefreshing() {
        return mState == State.header_release_to_load;
    }

    public boolean isFooterRefreshing() {
        return mState == State.footer_release_to_load;
    }

    public enum State {
        header_idle,
        footer_idle,
        header_pull_to_load,
        footer_pull_to_load,
        header_release_to_load,
        footer_release_to_load;
    }

    public interface OnUpdateListener {
        void onRefreshing(DragToRefreshView view);

        void onFinished(DragToRefreshView view);
    }


}
