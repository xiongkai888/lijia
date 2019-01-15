package com.lanmei.lijia.event;

/**
 * Created by xkai on 2018/4/28.
 */

public class ReceiveSocketInfoEvent {

    private String msg;

    public ReceiveSocketInfoEvent(String msg){
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }
}
