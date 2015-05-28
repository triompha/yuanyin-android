package com.triompha.yuanyin;

import java.net.URL;
import java.util.List;

import com.triompha.yuanyin.LoadDataService.Content;
import com.triompha.yuanyin.util.FillterTitle;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

public class ViewPageChangeListener implements OnPageChangeListener {
    
    private List<ScrollView> imageViewList;
   
    private LoadDataService loadDataService;
    
    private Activity activity;
    
    ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            Drawable drawable = null;
            URL url;
            try {
                url = new URL(source);
                drawable = Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
            } catch (Exception e) {
                return null;
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight());
            return drawable;
        }
    };
   

 

    public ViewPageChangeListener(List<ScrollView> imageViewList, LoadDataService loadDataService,
            Activity activity) {
        super();
        this.imageViewList = imageViewList;
        this.loadDataService = loadDataService;
        this.activity = activity;
    }

    public void onPageScrollStateChanged(int arg0) {}

    public void onPageScrolled(int arg0, float arg1, int arg2) {}

    public void onPageSelected(final int position) {
        ScrollView scrollView = imageViewList.get(position % imageViewList.size());
        final TextView childAt = (TextView) scrollView.getChildAt(0);
        Content loadData = loadDataService.loadData(position);
        childAt.setText(loadData.getContent()); 
        ((TextView)activity.findViewById(R.id.news_title)).setText(FillterTitle.filter(loadData.getTitle()));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Content loadData = loadDataService.loadData(position);
//                childAt.setText(loadData.getContent());
//            }
//        }).start();
    }
    

}
