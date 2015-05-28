package com.triompha.yuanyin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.triompha.yuanyin.db.DBManager;
import com.triompha.yuanyin.db.ImageFileCache;
import com.triompha.yuanyin.db.News;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.text.Spanned;


public class LoadDataService {

    private int width;
    private DBManager dbManager;

    private ImageFileCache imageFileCache = new ImageFileCache();


    public LoadDataService(Context context, int width) {
        super();
        this.dbManager = new DBManager(context);
        this.width = width;
    }

    public LoadDataService(int width) {
        this.width = width;
    }

    ImageGetter imgGetter = new Html.ImageGetter() {
        public Drawable getDrawable(String source) {
            BitmapDrawable drawable = null;
            URL url;
            try {
                Bitmap image = imageFileCache.getImage(source);
                if (image == null) {
                    url = new URL(source);
                    drawable = (BitmapDrawable) Drawable.createFromStream(url.openStream(), ""); // 获取网路图片
                    imageFileCache.saveBitmap(drawable.getBitmap(), source);
                } else {
                    drawable = new BitmapDrawable(image);
                }

            } catch (Exception e) {
                return null;
            }
            drawable.setBounds(0, 0, width,
                    (int) (1.0 * width * drawable.getBitmap().getHeight() / drawable.getBitmap()
                            .getWidth()));
            return drawable;
        }
    };

    private List<News> initData(String url) throws ClientProtocolException, IOException,
            JSONException {
        List<News> list = new ArrayList<News>();
        String serverURL = url;
        HttpGet httpRequest = new HttpGet(serverURL);// 建立http get联机
        HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);// 发出http请求
        if (httpResponse.getStatusLine().getStatusCode() == 200) {
            String content = EntityUtils.toString(httpResponse.getEntity());// 获取相应的字符串
            JSONArray jsonArray = new JSONArray(content);
            for (int j = 0; j < jsonArray.length(); j++) {
                JSONObject object = (JSONObject) jsonArray.get(j);
                News nj = new News();
                nj.setSid(object.getInt("srcId"));
                nj.setTitile(object.getString("title"));
                nj.setContent(object.getString("content"));

                // 每次加载数据的时候就把图片也加载进来
                try {
                    Html.fromHtml(nj.getContent(), imgGetter, null);
                } catch (Exception e) {
                }
                list.add(nj);
            }
            dbManager.add(list);
        }

        return list;
    }

    /****
     * 远程获取最新的数据，并缓存到本地
     * 
     * @desc
     * @author zhiyong.zzy(尽安)
     * @time 12:18:05 am
     * @return ,如果加载到了则返回true，如果没有加载到数据，则返回false
     */
    public Boolean loadLatestNews() {
        try {
            News news = dbManager.get(0);
            if (news != null) {
                List<News> initData =
                        initData("http://www.triompha.com:8080/r/news/up/" + news.getSid() + "/"
                                + 10);
                if (initData != null && initData.size() > 0) {
                    return true;
                }
            }
        } catch (Exception e) {
        }
        return false;
    }


    public List<String> getLastestTitles(int i) {
        return dbManager.listTopTitles(i);
    }



    public Content loadData(int i) {

        try {

            // 检查是否有数据
            News news = dbManager.get(0);
            if (news == null) {
                initData("http://www.triompha.com:8080/r/news/down/10");
            }

            // 不太严谨，暂时先这样
            news = dbManager.get(i);
            if (news == null) {
                news = dbManager.get(i - 1);
                if (news != null) {
                    initData("http://www.triompha.com:8080/r/news/down/" + news.getSid() + "/10");
                    news = dbManager.get(i);
                }
            }
            Spanned content = Html.fromHtml(news.getContent(), imgGetter, null);
            // Spanned content = Html.fromHtml(news.getContent().replaceAll("&nbsp;",
            // ""),imgGetter,null);
            return new Content(news.getTitile(), content);

        } catch (Exception e) {
            System.out.println(e);
        }
        return new Content("404 oh!", Html.fromHtml("xxxx"));
    }

    public static class Content {
        String title;
        Spanned content;

        public Content(String title, Spanned content) {
            super();
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Spanned getContent() {
            return content;
        }

        public void setContent(Spanned content) {
            this.content = content;
        }
    }

}
