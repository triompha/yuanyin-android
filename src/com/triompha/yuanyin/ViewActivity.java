package com.triompha.yuanyin;

import java.util.ArrayList;
import java.util.List;

import com.triompha.yuanyin.R;
import com.triompha.yuanyin.LoadDataService.Content;

import android.R.color;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.support.v4.view.ViewPager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class ViewActivity extends Activity {

    private List<ScrollView> imageViewList;
    private ViewPager mViewPager;
    public static final int initTabSize = 5;


    public LoadDataService loadDataService;

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fullScreen();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        loadDataService = new LoadDataService(this,getWindowManager().getDefaultDisplay().getWidth());


        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectDiskReads()
                .detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()
                .detectLeakedClosableObjects().penaltyLog().penaltyDeath().build());

        int i = 0;
        Bundle bundle = this.getIntent().getExtras();
        if (bundle != null)
            i = bundle.getInt("position");
        initView(i);
    }

    public void initView(final int i) {

        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        imageViewList = new ArrayList<ScrollView>();
        ScrollView scrollView;
        for (int j= 0; j < initTabSize; j++) {
            scrollView = new ScrollView(this);
            TextView textView = new TextView(this);
            textView.setMovementMethod(LinkMovementMethod.getInstance());
            textView.setOnLongClickListener(new OnLongClickListener() {
                
                public boolean onLongClick(View v) {
                    ((TextView)findViewById(R.id.news_title)).setVisibility(View.VISIBLE);
                    ((LinearLayout)findViewById(R.id.viewtitle)).setVisibility(View.VISIBLE);
                    return true;
                }
            });
            
            textView.setOnClickListener(new OnClickListener() {
                public void onClick(View v) {
                    ((TextView)findViewById(R.id.news_title)).setVisibility(View.INVISIBLE);
                    ((LinearLayout)findViewById(R.id.viewtitle)).setVisibility(View.INVISIBLE);
                }
            });
            scrollView.addView(textView);
            imageViewList.add(scrollView);
        }
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Content loadData = loadDataService.loadData(0);
//                ((TextView) imageViewList.get(0).getChildAt(0)).setText(loadData.getContent());
//                
//                ViewPagerAdapter adapter = new ViewPagerAdapter(imageViewList);
//                mViewPager.setAdapter(adapter);
//                mViewPager.setOnPageChangeListener(new ViewPageChangeListener(imageViewList,
//                        loadDataService, ViewActivity.this));
//                mViewPager.setCurrentItem(i);
//            }
//        }).start(); 
        
        Content loadData = loadDataService.loadData(0);
        ((TextView) imageViewList.get(0).getChildAt(0)).setText(loadData.getContent());
        ((TextView)findViewById(R.id.news_title)).setText(loadData.getTitle());
        
        ViewPagerAdapter adapter = new ViewPagerAdapter(imageViewList);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(new ViewPageChangeListener(imageViewList,
                loadDataService, this));
        mViewPager.setCurrentItem(i);
    }
    private void fullScreen(){
        // 设置无标题
      requestWindowFeature(Window.FEATURE_NO_TITLE);
//      // 设置全屏
      getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    } 

}
