package com.sunland.securitycheck;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DrawableMarginSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PeitaoYe on 2018/4/25.
 */
public class SpinButton extends AppCompatTextView {

    private Context mContext;
    private Window mWindow;

    private PopupWindow mPop_window;

    //header part of Popup Window
    private String mPopup_title;
    private TextView pop_textView;
    private String title;
    //content of Popup Window
    private RecyclerView pop_recycle;

    private int mHeight;

    private List<String> dataSet;
    private int mPopup_layout;

    private TextView mPopup_item;
    private Popup_Adapter<String> mAdapter;
    //selected info
    private String selectedItem;
    private int selectedItemPosition;

    private boolean isEnabled = true;

    private Drawable mRightDrawable;

    private List<GradientDrawable> mSpanDrawables;


    private OnClickListener selfOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            initPopUpWindow();
            mPop_window.showAtLocation(((AppCompatActivity) mContext).getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            mPop_window.setFocusable(true);
            dimWindow();
        }
    };

    private OnItemSelectedListener mOnItemSelectedListener;
    private PopupWindow.OnDismissListener onDismissListener = new PopupWindow.OnDismissListener() {
        @Override
        public void onDismiss() {
            mPop_window = null;
            restoreDimWindow();
        }
    };

    public SpinButton(Context context) {
        this(context, null);
    }

    public SpinButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }


    public SpinButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.setOnClickListener(selfOnClickListener);

    }

    private void initPopUpWindow() {
        mPop_window = new PopupWindow(mContext);
        mWindow = ((Activity) mContext).getWindow();
        View view = getPopupView();
        mPop_window.setContentView(view);
        mPop_window.setBackgroundDrawable(new ColorDrawable());
        mPop_window.setAnimationStyle(R.style.popmenu_animation);
        mPop_window.setOnDismissListener(onDismissListener);
        mPop_window.setOutsideTouchable(false);
        mPop_window.setFocusable(true);

        // TODO: 2018/4/27 这个算法可以优化。
        float title_height = mContext.getResources().getDimension(R.dimen.plateform_item_height);
        int pop_height = (int) (((AppCompatActivity) mContext).getWindow().getDecorView().getMeasuredHeight() * 0.60);
        if (dataSet.size() < 7) {
            pop_height = (int) ((pop_height - title_height) * dataSet.size() / 7 + title_height);
        }
        mPop_window.setWidth((int) (((AppCompatActivity) mContext).getWindow().getDecorView().getMeasuredWidth() * 0.75));
        mPop_window.setHeight(pop_height);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if (right == null) {
            mRightDrawable = right;
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    public void setPopup_layout(int layout) {
        this.mPopup_layout = layout;
    }

    public void setPopup_title(String mPopup_title) {
        this.mPopup_title = mPopup_title;
    }

    public void setDataSet(List<String> dataSet) {
        this.dataSet = dataSet;
    }

    public void setPrompt(String prompt) {
        this.setHint(prompt);
    }


    public void emptyDataset() {
        this.dataSet.clear();
        mAdapter.notifyDataSetChanged();

    }

    public void setEnable(boolean isEnabled) {
        this.isEnabled = isEnabled;
        if (!isEnabled) {
            this.setTextColor(Color.GRAY);
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.unable_arrow_button, 0);
            this.setOnClickListener(null);
        } else {
            this.setTextColor(Color.BLACK);
            this.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_spinner_arrow, 0);
            this.setOnClickListener(selfOnClickListener);

        }
    }

    public String getSelectedItem() {
        return selectedItem;
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    public int getSelectedItemPosition() {
        return selectedItemPosition;
    }

    public void setSelection(int position) {
        if (position < 0) {
            selectedItemPosition = position;
            selectedItem = null;
            this.setText(null);
        } else {
            selectedItemPosition = position;
            selectedItem = dataSet.get(position);
            if (mSpanDrawables != null && !mSpanDrawables.isEmpty()) {
                SpannableStringBuilder ssb = new SpannableStringBuilder(selectedItem);
                ssb.setSpan(new DrawableMarginSpan(mSpanDrawables.get(position), 20), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                this.setText(ssb);
            } else {
                this.setText(selectedItem);
            }

        }
    }

    public void setHeaderTitle(String title) {
        this.title = title;
    }

    private View getPopupView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.popup_layout, null, false);
        initPopUpWidget(view);
        return view;
    }

    public void initPopUpWidget(View view) {
        pop_textView = view.findViewById(R.id.popup_title);
        pop_textView.setText(title);

        pop_recycle = view.findViewById(R.id.popup_content);
        pop_recycle.addItemDecoration(new MyItemDividerItemDecoration(mContext));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        pop_recycle.setLayoutManager(linearLayoutManager);
        if (selectedItemPosition >= 0) {
            pop_recycle.scrollToPosition(selectedItemPosition - 1);
        }
        mAdapter = new Popup_Adapter<String>(mContext, dataSet);
        pop_recycle.setAdapter(mAdapter);
//        if (dataSet == null) {
//            throw new IllegalArgumentException("please specify the dataSet!");
//        } else {
//            mAdapter = new Popup_Adapter<String>(mContext, dataSet);
//            pop_recycle.setAdapter(mAdapter);
//        }

    }

    public boolean isShowing() {
        if (mPop_window != null) {
            return mPop_window.isShowing();
        } else {
            return false;
        }

    }

    private void dimWindow() {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 0.25f;
        mWindow.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        // TODO: 2018/4/26  可能需要版本或者机型上的适配。
        mWindow.setAttributes(lp);
    }

    private void restoreDimWindow() {
        WindowManager.LayoutParams lp = mWindow.getAttributes();
        lp.alpha = 1f;
        mWindow.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mWindow.setAttributes(lp);
    }


    public void setRoundSpanDrawable(int[] spanColors) {
        mSpanDrawables = new ArrayList<>();
        for (int i = 0; i < spanColors.length; i++) {
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setSize(50, 50);
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(mContext.getResources().getColor(spanColors[i]));
            mSpanDrawables.add(gradientDrawable);
        }
    }

    public class Popup_Adapter<T> extends RecyclerView.Adapter<Popup_Adapter.MyViewholder> {
        public List<T> dataSet;
        private Context context;
        private LayoutInflater mInflater;

        public Popup_Adapter(Context context, List<T> dataSet) {
            this.context = context;
            this.dataSet = dataSet;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getItemViewType(int position) {
            if (selectedItemPosition == position) {
                return 1;
            } else {
                return 0;
            }
        }

        @Override
        public void onBindViewHolder(Popup_Adapter.MyViewholder holder, int position) {

            String item = (String) dataSet.get(position);
            SpannableStringBuilder ssb = new SpannableStringBuilder(item);
            if (item.equals("") || item == null) {
                holder.popup_item.setText("空");
            } else if (mSpanDrawables != null && !mSpanDrawables.isEmpty()) {
                ssb.setSpan(new DrawableMarginSpan(mSpanDrawables.get(position), 20), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                holder.popup_item.setText(ssb);
            } else {
                holder.popup_item.setText(item);
            }
            holder.popup_item.setTag(position);
        }

        @Override
        public Popup_Adapter.MyViewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = mInflater.inflate(R.layout.recycle_item_layout, parent, false);
            return new MyViewholder(view, viewType);
        }

        @Override
        public int getItemCount() {
            return dataSet.size();
        }

        public class MyViewholder extends RecyclerView.ViewHolder {
            TextView popup_item;

            public MyViewholder(View view, int viewType) {
                super(view);
                popup_item = view.findViewById(R.id.recycle_item);
                mPopup_item = popup_item;
                if (viewType == 1) {
                    popup_item.setTextColor(mContext.getResources().getColor(R.color.recycler_selected_item));
                } else {
                    popup_item.setTextColor(Color.BLACK);
                }
                popup_item.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedItem = popup_item.getText().toString().trim();
                        selectedItemPosition = (int) popup_item.getTag();
                        if (mSpanDrawables != null && !mSpanDrawables.isEmpty()) {
                            SpannableStringBuilder ssb = new SpannableStringBuilder(selectedItem);
                            ssb.setSpan(new DrawableMarginSpan(mSpanDrawables.get(selectedItemPosition), 20), 0, 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                            SpinButton.this.setText(ssb);
                        } else {
                            SpinButton.this.setText(selectedItem);
                        }
                        mPop_window.dismiss();
                        if (mOnItemSelectedListener != null)
                            mOnItemSelectedListener.onItemSelected(selectedItemPosition);
                    }
                });
            }
        }
    }

    private class MyItemDividerItemDecoration extends RecyclerView.ItemDecoration {
        private int offset;
        private Paint paint;

        public MyItemDividerItemDecoration(Context context) {
            paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.gray_back_screen));
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            if (parent.getChildAdapterPosition(view) != 0) {
                outRect.top = 1;
                offset = 1;
            }
        }

        @Override
        public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
            super.onDraw(c, parent, state);
            int childrenCount = parent.getChildCount();
            for (int i = 0; i < childrenCount; i++) {
                View view = parent.getChildAt(i);
                if (parent.getChildAdapterPosition(view) == 0) {
                    continue;
                }
                float top = view.getTop() - offset;
                float left = parent.getLeft();
                float bottom = view.getTop();
                float right = parent.getWidth() - parent.getPaddingRight();
                c.drawRect(left, top, right, bottom, paint);
            }

        }
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position);
    }

}