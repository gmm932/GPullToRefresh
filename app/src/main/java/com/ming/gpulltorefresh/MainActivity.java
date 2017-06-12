package com.ming.gpulltorefresh;

import android.app.admin.DeviceAdminReceiver;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private ListView listView;
    private LinkedHashMap<String, Class> data = new LinkedHashMap<>();
    private ArrayList<Class> foo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(android.R.id.list);
        ArrayList<String> items = new ArrayList<>();
        initList();
        for (Map.Entry<String, Class> entry : data.entrySet()) {
            String key = entry.getKey();
            items.add(key);
            foo.add(entry.getValue());
        }
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items));
        listView.setOnItemClickListener(this);

        //在内部存储中internal不能被其他应用访问
        Log.d("CacheDir",getCacheDir().getPath());
        Log.d("getFilesDir", getFilesDir().getPath());
        //在外部存储中external可以被其他应用访问，随应用卸载而删除
        Log.d("ExternalCacheDir", getExternalCacheDir().getPath());
        Log.d("ExternalStorageDictory", Environment.getExternalStorageDirectory().getPath());
        Log.d("getDataDirectory", Environment.getDataDirectory().getPath());
    }

    private void initList() {
        data.put(RecyclerViewActivity.class.getSimpleName(), RecyclerViewActivity.class);
        data.put(ScrollerActivity.class.getSimpleName(), ScrollerActivity.class);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, foo.get(position));
        startActivity(intent);
    }
}
