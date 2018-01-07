package com.loic.cocktail.eventbus;

/**
 * Created by 胡敏浩 on 2018/1/7.
 */

/**
 * 0：传回mainActivity
 * 1：
 *
 * 4：从signInFragment传到FoldableList Activity
 */

public class MyEvent {
    /*private JSONObject msg;
    public MyEvent(JSONObject msg){
        this.msg = msg;
    }

    public JSONObject getMsg(){
        return msg;
    }*/
    private int tag;
    private String msg;
    public MyEvent(String msg,int tag){
        this.msg=msg;
        this.tag=tag;
    }

    public int getTag(){
        return this.tag;
    }

    public String getMsg(){
        return this.msg;
    }
}
