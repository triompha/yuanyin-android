package com.triompha.yuanyin;

import java.net.URL;
import java.util.List;

import com.triompha.yuanyin.LoadDataService.Content;

import android.app.Activity;
import android.graphics.drawable.Drawable;
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

    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub
        
    }

    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub
        
    }

    public void onPageSelected(int position) {
        ScrollView scrollView = imageViewList.get(position % imageViewList.size());
        TextView childAt = (TextView) scrollView.getChildAt(0);
        Content loadData = loadDataService.loadData(position);
        childAt.setText(loadData.getContent());
        activity.setTitle(loadData.getTitle());
//        fullScreen(activity);
    }
    
    private void fullScreen(Activity activity){
        // 设置无标题
      activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
//      // 设置全屏
      activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

}
