package com.triompha.yuanyin;

import android.os.Handler;
import android.widget.ListAdapter;
import android.widget.ListView;

public class ListViewHandler extends Handler{
    private ListView listView;
    private ListAdapter listAdapter;
    public ListViewHandler(ListView listView, ListAdapter listAdapter) {
        super();
        this.listView = listView;
        this.listAdapter = listAdapter;
    }
    
    
    
}
