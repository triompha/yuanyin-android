package com.triompha.yuanyin.db;

public class News {
    private int id;
    private int sid;
    private String titile;
    private String content;

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the sid
     */
    public int getSid() {
        return sid;
    }

    /**
     * @param sid the sid to set
     */
    public void setSid(int sid) {
        this.sid = sid;
    }

    /**
     * @return the titile
     */
    public String getTitile() {
        return titile;
    }

    /**
     * @param titile the titile to set
     */
    public void setTitile(String titile) {
        this.titile = titile;
    }

    /**
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }



}
