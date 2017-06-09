package com.ming.gpulltorefresh;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewActivity extends AppCompatActivity {

    GPullToRefreshLayout pullToRefreshLayout;
    RecyclerView recyclerView;
    SimpleAdapter adapter;
    int startPage = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);


        pullToRefreshLayout = (GPullToRefreshLayout) findViewById(R.id.pullTorefrsh);

        pullToRefreshLayout.setOnRefreshListener(new GPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pullToRefreshLayout.setRefreshing(false);
                        adapter.data.clear();
                        startPage ++ ;
                        adapter.data.addAll(genTestData(startPage));
                        adapter.notifyDataSetChanged();
                    }
                }, 2000);
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        adapter = new SimpleAdapter(genTestData(startPage));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private List<String> genTestData(int page) {
        List<String> data = new ArrayList<>();

        for (int i = page * 10 ; i >= 0; i--) {
            data.add("这是第 " + i + " item");
        }

        return data;
    }

    private class SimpleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public List<String> data;

        SimpleAdapter(List<String> data) {
            this.data = data;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((ItemHolder)holder).onBind(data.get(position));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class ItemHolder extends RecyclerView.ViewHolder {

            TextView textView;

            ItemHolder(View itemView) {
                super(itemView);
                textView = (TextView) itemView.findViewById(android.R.id.text1);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(), data.get(getLayoutPosition()), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            void onBind(String str) {
                textView.setText(str);
            }
        }
    }

}
