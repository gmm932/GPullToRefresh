package com.ming.gpulltorefresh;

/**
 * Created by gmm on 2017/6/7.
 */

public interface IPullToRefresh {
    void onPullRelease();
    boolean isRefreshing();
}
