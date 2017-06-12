package com.ming.gpulltorefresh;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ScrollerActivity extends AppCompatActivity {

    GPullToRefreshLayout pullToRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroller);

        pullToRefreshLayout = (GPullToRefreshLayout) findViewById(R.id.pullTorefrsh);

        pullToRefreshLayout.setOnRefreshListener(new GPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshLayout.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }
}
