package com.ming.gpulltorefresh;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * Created by ming on 2017/6/6.
 */

public class GPullRefreshLayout extends ViewGroup {

    private View mTarget;
    private View mRefreshView;

    public GPullRefreshLayout(Context context) {
        super(context);
    }

    public GPullRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GPullRefreshLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mRefreshView = new FrameLayout(context);
        addView(mRefreshView, 0);
        //表示要重绘调用onDraw()
        setWillNotDraw(false);
        //是否画它的子布局
        ViewCompat.setChildrenDrawingOrderEnabled(this, true);
    }

    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {

    }
}
