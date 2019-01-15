package com.lanmei.lijia.ui.home.service;

import com.alibaba.fastjson.JSONObject;
import com.lanmei.lijia.WebSocket.AbsBaseWebSocketService;
import com.lanmei.lijia.WebSocket.event.WebSocketConnectedEvent;
import com.lanmei.lijia.event.ReceiveSocketInfoEvent;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.UserHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by zk721 on 2018/1/28.
 */

public class WebSocketService extends AbsBaseWebSocketService {

    @Override
    protected String getConnectUrl() {
        return "ws://120.25.205.197:8087";
    }

    @Override
    protected void dispatchResponse(final String textResponse) {
        L.d("BaseAppCompatActivity", "返回的数据：" + textResponse);
        EventBus.getDefault().post(new ReceiveSocketInfoEvent(textResponse));
    }


    /**
     * 连接成功
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(WebSocketConnectedEvent event) {
        JSONObject param = new JSONObject();
        param.put("TYPE", 1);
        param.put("ID", CommonUtils.getUid(this));
        param.put("PW", UserHelper.getInstance(this).getPwd());
        sendText(param.toString());//调用 WebSocket 发送数据
    }

}
