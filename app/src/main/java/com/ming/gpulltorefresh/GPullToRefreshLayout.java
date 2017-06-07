package com.ming.gpulltorefresh;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

/**
 * Created by ming on 2017/6/6.
 */

public class GPullToRefreshLayout extends ViewGroup {


    private static final int RADIO = 2;
    private static final float REFRESH_MIN_DISTANCE = 64;
    private View mTarget;
    private View mRefreshView;
    private int mTotalDragDistance = 0;
    private float mLastDistance = 0;
    private float radio = RADIO;
    private int mEvent = 0;
    private OnRefreshListener mListener;

    public GPullToRefreshLayout(Context context) {
        this(context, null);

    }

    public GPullToRefreshLayout(Context context, AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public GPullToRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRefreshView = new FrameLayout(context);
        mRefreshView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, context.getTheme()));
        addView(mRefreshView, 0);
        //表示要重绘调用onDraw()
        setWillNotDraw(false);
        //是否画它的子布局
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;

        measureChild(mTarget, widthMeasureSpec, heightMeasureSpec);

        //保存测量结果
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);

        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mLastDistance = ev.getY();
                mEvent = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_POINTER_UP:
                mEvent = -1;
                break;
            case MotionEvent.ACTION_MOVE:
                if (mEvent == 0) {
                    if (mTotalDragDistance > 0 || true) {
                        mTotalDragDistance += (ev.getY() - mLastDistance) / radio;
                        if (mTotalDragDistance < 0) {
                            mTotalDragDistance = 0;
                        }

                        if (mTotalDragDistance > getMeasuredHeight()) {
                            mTotalDragDistance = getMeasuredHeight();
                        }
                    }
                } else {
                    mEvent = 0;
                }


                mLastDistance = ev.getY();
                // 根据下拉距离改变比例
                radio = (float) (2 + 2 * Math.tan(Math.PI / 2 / getMeasuredHeight() * mTotalDragDistance));
                if (mTotalDragDistance > 0) {
                    requestLayout();
                }
                break;
            case MotionEvent.ACTION_UP:
                onPullRelease();
                break;
        }


        super.dispatchTouchEvent(ev);
        return true;
    }

    private void onPullRelease() {
        if (mTotalDragDistance >= dp2px(REFRESH_MIN_DISTANCE)) {
            backToRefresh(REFRESH_MIN_DISTANCE);
            if (mListener != null) {
                mListener.onRefresh();
            }
        } else {
            setRefreshing(false);
        }
    }

    private boolean canViewScrollUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(view, -1);
        }
    }



    private void backToRefresh(float dest) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mTotalDragDistance, dp2px(dest));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mTotalDragDistance = (int) animation.getAnimatedValue();
                requestLayout();
            }
        });
        valueAnimator.start();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureTarget();
        if (mTarget == null)
            return;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        mRefreshView.layout(left, top + mTotalDragDistance - height, width - right, top + mTotalDragDistance);
        mTarget.layout(left, top + mTotalDragDistance, width - right, height - bottom + mTotalDragDistance);

    }

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mTarget)
                    mTarget = child;
            }
        }
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    public void setRefreshing(boolean refreshing) {
        if (!refreshing) {
            backToRefresh(0);
        }
    }

    public void setOnRefreshListener(OnRefreshListener listener) {
        mListener = listener;
    }

    public static interface OnRefreshListener {
        public void onRefresh();
    }}
