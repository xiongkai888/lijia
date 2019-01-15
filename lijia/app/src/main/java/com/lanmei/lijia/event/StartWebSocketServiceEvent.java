package com.lanmei.lijia.event;

/**
 * Created by xkai on 2018/4/28.
 * 开启WebSocketService事件（是否开启）
 */

public class StartWebSocketServiceEvent {

    private boolean isStartService;

    public boolean isStartService() {
        return isStartService;
    }

    public StartWebSocketServiceEvent(boolean isStartService){
        this.isStartService = isStartService;
    }
}
