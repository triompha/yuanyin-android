package com.triompha.yuanyin.util;

public class FillterTitle {
    
    public static final String filter(String title){
        if(title== null | title.length()==0){
            return title;
        }
        int _Index = title.indexOf("-");
        if(_Index>1){
            title = title.substring(0, _Index-1);
        }
        return title;
    }

}
