package com.lanmei.lijia.event;

/**
 * Created by xkai on 2018/5/11.
 * 退出登录事件(或别人登录了你的账号)
 */

public class LogoutEvent {

    private int type;//1退出登录2提示账号在另一个设备登录

    public int getType() {
        return type;
    }

    public LogoutEvent(int type){
        this.type = type;
    }
}
