package com.lanmei.lijia.event;

/**
 * Created by xkai on 2018/5/15.
 * 开启接单之前请求定位事件
 */

public class LocationEvent {

    private int type;//0请求定位权限1定位权限已得到用户确认

    public int getType() {
        return type;
    }

    public LocationEvent(int type){
        this.type = type;
    }
}
