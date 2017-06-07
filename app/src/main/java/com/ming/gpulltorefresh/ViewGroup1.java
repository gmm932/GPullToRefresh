package com.ming.gpulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by ming on 2017/6/6.
 */

public class ViewGroup1 extends ViewGroup{


    public ViewGroup1(Context context) {
        super(context);
    }

    public ViewGroup1(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewGroup1(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        //测量子view的宽高
        measureChildren(widthMeasureSpec, heightMeasureSpec);


        //保存测量的宽高值
        setMeasuredDimension(getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec),
                getDefaultSize(getSuggestedMinimumHeight(), heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean b, int left, int top, int right, int bottom) {
        int count = getChildCount();
        int childMeasureWidth = 0;
        int childMeasureHeight = 0;
        int layoutWidth = 0;    //容器以占宽度
        int layoutHeight = 0;   //容器以占的高度
        int childMaxMeasureHeight = 0; //最大的子视图高度

        for (int j = 0; j < count; j++) {
            View child = getChildAt(j);

            childMeasureWidth = child.getMeasuredWidth();
            childMeasureHeight = child.getMeasuredHeight();

            if (layoutWidth  + childMeasureWidth < getWidth()) {
                left = layoutWidth;
                top = layoutHeight;
                right = layoutWidth + childMeasureWidth;
                bottom = top + childMeasureHeight;
            } else {
                layoutWidth = 0;
                layoutHeight += childMaxMeasureHeight;
                childMaxMeasureHeight = 0;

                left = layoutWidth;
                right = left+childMeasureWidth;
                top = layoutHeight;
                bottom = top+childMeasureHeight;
            }

            if (childMeasureHeight > childMaxMeasureHeight) {
                childMaxMeasureHeight = childMeasureHeight;
            }

            layoutWidth = layoutWidth + childMeasureWidth;


            child.layout(left, top, right, bottom);


        }
    }



//    /**
//     *遍历ViewGroup中所有的子控件，调用measuireChild测量宽高
//     */
//    protected void measureChildren (int widthMeasureSpec, int heightMeasureSpec) {
//        final int size = mChildrenCount;
//        final View[] children = mChildren;
//        for (int i = 0; i < size; ++i) {
//            final View child = children[i];
//            if ((child.mViewFlags & VISIBILITY_MASK) != GONE) {
//                //测量某一个子控件宽高
//                measureChild(child, widthMeasureSpec, heightMeasureSpec);
//            }
//        }
//    }
//
//    /**
//     * 测量某一个child的宽高
//     */
//    protected void measureChild (View child, int parentWidthMeasureSpec,
//                                 int parentHeightMeasureSpec) {
//        final LayoutParams lp = child.getLayoutParams();
//        //获取子控件的宽高约束规则
//        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//                mPaddingLeft + mPaddingRight, lp. width);
//        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
//                mPaddingTop + mPaddingBottom, lp. height);
//
//        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//    }
//
//    /**
//     * 测量某一个child的宽高，考虑margin值
//     */
//    protected void measureChildWithMargins (View child,
//                                            int parentWidthMeasureSpec, int widthUsed,
//                                            int parentHeightMeasureSpec, int heightUsed) {
//        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();
//        //获取子控件的宽高约束规则
//        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
//                mPaddingLeft + mPaddingRight + lp. leftMargin + lp.rightMargin
//                        + widthUsed, lp. width);
//        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
//                mPaddingTop + mPaddingBottom + lp. topMargin + lp.bottomMargin
//                        + heightUsed, lp. height);
//        //测量子控件
//        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
//    }
}
