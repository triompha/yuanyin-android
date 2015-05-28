package com.triompha.yuanyin;

import java.util.ArrayList;
import java.util.List;
import com.triompha.yuanyin.AutoListView.OnRefreshListener;
import com.triompha.yuanyin.R;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

                // Toast.makeText(MainActivity.this, "正在刷新", Toast.LENGTH_SHORT).show();

public class MainActivity extends ListActivity {

    public static final int INIT_TITLE_SIZE = 30;
    private int deviceWidth;


    AutoListView listView;
    ArrayAdapter<String> arrayAdapter;

    public LoadDataService loadDataService;
    
    
    private Handler handler = new Handler(new Handler.Callback() {
        public boolean handleMessage(Message msg) {
            @SuppressWarnings("unchecked")
            List<String> result = (List<String>) msg.obj;
            listView.onRefreshComplete();
            //只有真正获取到数据的时候才更新列表
            if(result!=null && result.size()>0){
                arrayAdapter.clear();
                arrayAdapter.addAll(result);
            }
            return true;
        }
    }); 


    //加载数据
    private void loadData(final boolean init ) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = handler.obtainMessage();
                Boolean loadLatestNews = loadDataService.loadLatestNews();
                msg.obj = null;
                if(loadLatestNews || init){
                   msg.obj = loadDataService.getLastestTitles(INIT_TITLE_SIZE);
                }
                
                if(init && (msg.obj==null || ((List<String>) msg.obj).size()==0) ){
                   loadDataService.loadData(10);
                   msg.obj = loadDataService.getLastestTitles(INIT_TITLE_SIZE);
                }
                
                handler.sendMessage(msg);
            }
        }).start();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initPro();
        setListAdapter(arrayAdapter);
        loadData(true);

        findViewById(R.id.goto_first).setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                MainActivity.this.onListItemClick(null, null, 0, 0);
            }
        });

    }
    
    private void initPro(){
        loadDataService = new LoadDataService(this,deviceWidth);
        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,new ArrayList<String>());
        listView = (AutoListView) findViewById(android.R.id.list);
        listView.setLoadEnable(false);
        listView.setOnRefreshListener(new OnRefreshListener() {
            public void onRefresh() {
                loadData(false);
            }
        });
        arrayAdapter.setNotifyOnChange(true);
    }

    /****
     * 绑定每个子项点击方法
     */
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(MainActivity.this, ViewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", position-1);
        intent.putExtras(bundle);
        startActivity(intent);
    }

}
