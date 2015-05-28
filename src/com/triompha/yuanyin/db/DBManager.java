package com.triompha.yuanyin.db;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;
    
    public DBManager(Context context) {
        helper = new DBHelper(context);
        //因为getWritableDatabase内部调用了mContext.openOrCreateDatabase(mName, 0, mFactory);
        //所以要确保context已初始化,我们可以把实例化DBManager的步骤放在Activity的onCreate里
        db = helper.getWritableDatabase();
    }
    
    
   public List<String>  listTopTitles(int size){
       Cursor c = db.rawQuery("SELECT title FROM news order by sid desc limit 0,?", new String[]{size+""}); 
       List<String> titles = new ArrayList<String>(size);
       while (c.moveToNext()) {
           String string = c.getString(c.getColumnIndex("title"));
           int _index = string.indexOf("-");
           if(_index>0){
               string= string.substring(0, _index-1);
           }
           titles.add(string);
       }
       c.close();
       return  titles;
   }
    
    
    public long add(News news){
       ContentValues cv = new ContentValues();
       cv.put("sid", news.getSid());
       cv.put("title", news.getTitile());
       cv.put("content", news.getContent());
       return  db.insert("news", null, cv) ;
    }
    
    public News get(int postion){
        ArrayList<News> persons = new ArrayList<News>();
        Cursor c = queryTheCursor(postion,1);
        while (c.moveToNext()) {
            News news = new News();
            news.setSid(c.getInt(c.getColumnIndex("sid"))); 
            news.setTitile(c.getString(c.getColumnIndex("title"))); 
            news.setContent(c.getString(c.getColumnIndex("content"))); 
            persons.add(news);
        }
        c.close();
        return persons.size()==0?null:persons.get(0);
    }
    
    /**
     * add persons
     * @param persons
     */
    public void add(List<News> news) {
        db.beginTransaction();  //开始事务
        try {
            for (News n : news) {
                db.execSQL("INSERT INTO news(sid,title,content) VALUES( ?, ?, ?)", new Object[]{n.getSid(),n.getTitile(),n.getContent()});
            }
            db.setTransactionSuccessful();  //设置事务成功完成
        } finally {
            db.endTransaction();    //结束事务
        }
    }
    
    
    /**
     * query all persons, return list
     * @return List<Person>
     */
    public List<News> query(int size) {
        ArrayList<News> persons = new ArrayList<News>();
        Cursor c = queryTheCursor(0,size);
        while (c.moveToNext()) {
            News news = new News();
            news.setSid(c.getInt(c.getColumnIndex("sid"))); 
            news.setTitile(c.getString(c.getColumnIndex("title"))); 
            news.setContent(c.getString(c.getColumnIndex("content"))); 
            persons.add(news);
        }
        c.close();
        return persons;
    }
    
    /**
     * query all persons, return cursor
     * @return  Cursor
     */
    public Cursor queryTheCursor(int from ,int size) {
        Cursor c = db.rawQuery("SELECT * FROM news order by sid desc limit ?,?", new String[]{from+"",size+""});
        return c;
    }
    
    /**
     * close database
     */
    public void closeDB() {
        db.close();
    }
}

