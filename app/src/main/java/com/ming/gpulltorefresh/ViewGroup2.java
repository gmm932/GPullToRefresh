package com.ming.gpulltorefresh;

import android.content.Context;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by gmm on 2017/6/7.
 */

public class ViewGroup2 extends ViewGroup {

    private static final String TAG = "VIEWGROUP2";

    private View mTarget;
    private ImageView mRefreshView;

    public ViewGroup2(Context context) {
        super(context);
        initView(context);
    }

    public ViewGroup2(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public ViewGroup2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        mRefreshView = new ImageView(context);
        mRefreshView.setBackgroundColor(ResourcesCompat.getColor(context.getResources(), R.color.colorAccent, context.getTheme()));
        addView(mRefreshView, 0);
        setWillNotDraw(false);
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ensureTarget();
        if (mTarget == null)
            return;


        measureChild(mTarget, widthMeasureSpec, heightMeasureSpec);

        int targetWidth =  mTarget.getMeasuredWidth();
        int targetHeight = mTarget.getMeasuredHeight();
        int childState = 0;
        childState = combineMeasuredStates(childState, mTarget.getMeasuredState());


        setMeasuredDimension(resolveSizeAndState(targetWidth, widthMeasureSpec, childState),
                resolveSizeAndState(targetHeight, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth() - getPaddingRight() - getPaddingLeft(), MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight() - getPaddingTop() - getPaddingBottom(), MeasureSpec.EXACTLY);

        mTarget.measure(widthMeasureSpec, heightMeasureSpec);
        mRefreshView.measure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        ensureTarget();
        if (mTarget == null)
            return;

        int height = getMeasuredHeight();
        int width = getMeasuredWidth();
        int left = getPaddingLeft();
        int top = getPaddingTop();
        int right = getPaddingRight();
        int bottom = getPaddingBottom();

        Log.d(TAG, "left = " + left + " top = " + top + " right = " + right + " bottom = " + bottom);

        Log.d(TAG, "getMeasuredHeight" + height);
        Log.d(TAG, "getMeasuredWidth" + width);
        Log.d(TAG, "getTop" + mTarget.getTop());

        mTarget.layout(left, top + mTarget.getTop(), width - right, top + height - bottom + mTarget.getTop());
        mRefreshView.layout(left, top , width - right, height - bottom);
    }

    private void ensureTarget() {
        if (mTarget != null)
            return;
        if (getChildCount() > 0) {
            for (int i = 0; i < getChildCount(); i++) {
                View child = getChildAt(i);
                if (child != mTarget) {
                    mTarget = child;
                }
            }
        }
    }
}
