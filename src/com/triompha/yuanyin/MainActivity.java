package com.triompha.yuanyin;

import java.util.ArrayList;
import java.util.List;

import com.triompha.yuanyin.R;
import com.triompha.yuanyin.LoadDataService.Content;
import com.triompha.yuanyin.db.DBManager;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

@SuppressLint("NewApi")
public class MainActivity extends Activity {

    private List<ScrollView> imageViewList;
    private ViewPager mViewPager;
    public static final int initTabSize = 5;
    
    private int deviceWidth;
    
    public  LoadDataService loadDataService; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen();
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main);
        Display display = getWindowManager().getDefaultDisplay();

        deviceWidth =display.getWidth();
        
        
        loadDataService = new LoadDataService(deviceWidth, new DBManager(this));
        
        // 设置无标题
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        // 设置全屏
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置为主显示
        
        
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
        .detectDiskReads()
        .detectDiskWrites()
        .detectNetwork()
        .penaltyLog()
        .build());
StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder()
        .detectLeakedSqlLiteObjects()
        .detectLeakedClosableObjects()
        .penaltyLog()
        .penaltyDeath()
        .build());
        
        initView();
    }

    public void initView() {
        
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        prepareData();
        ViewPagerAdapter adapter = new ViewPagerAdapter(imageViewList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPageChangeListener(imageViewList,loadDataService,this));
        mViewPager.setCurrentItem(0);
    }

    private void prepareData() {
        imageViewList = new ArrayList<ScrollView>();
        ScrollView iv;
        for (int i = 0; i < initTabSize; i++) {
            iv = new ScrollView(this);
            TextView textView = new TextView(this);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            iv.addView(textView);
            imageViewList.add(iv);
        }
        //初始化第一个tab的内容
//        fullScreen();
        Content loadData = loadDataService.loadData(0);
        ((TextView)imageViewList.get(0).getChildAt(0) ).setText(loadData.getContent());
        setTitle(loadData.getTitle());
//        fullScreen();
    }
    
    private void fullScreen(){
        // 设置无标题
      requestWindowFeature(Window.FEATURE_NO_TITLE);
//      // 设置全屏
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
