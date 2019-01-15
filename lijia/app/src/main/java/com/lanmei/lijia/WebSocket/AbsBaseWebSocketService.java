package com.lanmei.lijia.WebSocket;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.lanmei.lijia.WebSocket.event.DisconnectedEvent;
import com.lanmei.lijia.WebSocket.event.WebSocketConnectedEvent;
import com.lanmei.lijia.WebSocket.event.WebSocketConnectionErrorEvent;
import com.lanmei.lijia.WebSocket.event.WebSocketSendDataErrorEvent;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.UserDataBean;
import com.lanmei.lijia.event.LogoutEvent;
import com.lanmei.lijia.event.StartWebSocketServiceEvent;
import com.lanmei.lijia.event.StopServiceEvent;
import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.MainActivity;
import com.lanmei.lijia.ui.home.service.OrderReceiver;
import com.lanmei.lijia.utils.BaiduLocation;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.SharedAccount;
import com.neovisionaries.ws.client.HostnameUnverifiedException;
import com.neovisionaries.ws.client.OpeningHandshakeException;
import com.neovisionaries.ws.client.ProxySettings;
import com.neovisionaries.ws.client.StatusLine;
import com.neovisionaries.ws.client.WebSocket;
import com.neovisionaries.ws.client.WebSocketAdapter;
import com.neovisionaries.ws.client.WebSocketException;
import com.neovisionaries.ws.client.WebSocketFactory;
import com.neovisionaries.ws.client.WebSocketFrame;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 抽象类
 * <p>
 * <p>
 * Created by zk721 on 2018/1/28.
 */

public abstract class AbsBaseWebSocketService extends Service implements IWebSocket {

    private static final String TAG = "BaseAppCompatActivity";
    private static final int TIME_OUT = 20000;
    private static WebSocketFactory factory = new WebSocketFactory().setConnectionTimeout(TIME_OUT);
    private int num = 0;//重新连接次数
    private int NUM_LOGOUT = 8;//重新连接次数

    /**
     * 心跳检测时间
     */
    private static final long HEART_BEAT_RATE = 60 * 1000;//一分钟更新位置信息

    private AbsBaseWebSocketService.WebSocketThread webSocketThread;
    private WebSocket webSocket;

    private AbsBaseWebSocketService.ServiceBinder serviceBinder = new AbsBaseWebSocketService.ServiceBinder();

    public class ServiceBinder extends Binder {
        public AbsBaseWebSocketService getService() {
            return AbsBaseWebSocketService.this;
        }
    }

    private boolean stop = false;
    /**
     * 0-未连接
     * 1-正在连接
     * 2-已连接
     */
    public static int connectStatus = 0;//是否已连接

    @Override
    public void onCreate() {
        super.onCreate();
        L.d(TAG, "onCreate()");

        ProxySettings settings = factory.getProxySettings();
        settings.addHeader("Content-Type", "text/json");

        connectStatus = 0;
        webSocketThread = new AbsBaseWebSocketService.WebSocketThread();
        webSocketThread.start();

        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        L.i(TAG, "onCreated");
        mHandler.removeCallbacks(heartBeatRunnable);
        mHandler.postDelayed(heartBeatRunnable, HEART_BEAT_RATE);// 发送心跳包
        initBaiDu();
//        startForeground(1,new Notification());
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (serviceBinder == null) {
            serviceBinder = new AbsBaseWebSocketService.ServiceBinder();
        }
        L.i(TAG, "onBind");
        return serviceBinder;
    }


    //停止服务（即停止WebSocket连接）
    @Subscribe
    public void stopService(StopServiceEvent event) {
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stop = true;
        webSocket.disconnect();
        webSocket.flush();
        webSocket = null;
        connectStatus = 0;
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(heartBeatRunnable);
        mHandler = null;
        L.i(TAG, "onDestroy = 服务已经停止（即停止了socket）HEART_BEAT_RATE:" + HEART_BEAT_RATE);
    }

    /**
     * 获取服务器地址
     */
    protected abstract String getConnectUrl();

    /**
     * 分发响应数据
     */
    protected abstract void dispatchResponse(String textResponse);

    /**
     * 连接成功发送 WebSocketConnectedEvent 事件，
     * 请求成功发送 CommonResponse 事件，
     * 请求失败发送 WebSocketSendDataErrorEvent 事件。
     */
    private class WebSocketThread extends Thread {
        @Override
        public void run() {
            L.i(TAG, "WebSocketThread->run()");
            setupWebSocket();
        }
    }

    private void setupWebSocket() {
        if (connectStatus != 0) return;
        connectStatus = 1;
        try {
            webSocket = factory.createSocket(getConnectUrl());
            webSocket.addListener(new WebSocketAdapter() {
                @Override
                public void onTextMessage(WebSocket websocket, String text) throws Exception {
                    super.onTextMessage(websocket, text);
                    L.i(TAG, String.format("onTextMessage->%s", text));
                    if (StringUtils.isEmpty(text) || StringUtils.isSame(text, "hi!") || text.contains("\"MSG\":\"logout\"")) {//"MSG":"logout"
                        L.d(TAG, "包涵了hi!或者\"MSG\":\"logout\"");
                        return;
                    }
                    if (!MainActivity.isShow) {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 3;
                        msg.obj = text;
                        mHandler.sendMessage(msg);
                        L.d(TAG, "发广播");
                    } else {
                        Message msg = mHandler.obtainMessage();
                        msg.what = 2;
                        msg.obj = text;
                        mHandler.sendMessage(msg);
//                        dispatchResponse(text);
                    }
                }

                @Override
                public void onTextMessageError(WebSocket websocket, WebSocketException cause, byte[] data) throws Exception {
                    super.onTextMessageError(websocket, cause, data);
                    L.e(TAG, "onTextMessageError()", cause);
                    EventBus.getDefault().post(new WebSocketSendDataErrorEvent("", "", "onTextMessageError():" + cause.toString()));
                }

                @Override
                public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) throws Exception {
                    super.onDisconnected(websocket, serverCloseFrame, clientCloseFrame, closedByServer);
                    EventBus.getDefault().post(new DisconnectedEvent());
                    L.e(TAG, "onDisconnected()");
                    L.d(TAG, "onDisconnected：currentThread().getName() = " + Thread.currentThread().getName());
                    connectStatus = 0;
                    if (num == NUM_LOGOUT) {
                        num = 0;
                        CommonUtils.loadReceiving(getApplication(), false, false, null);//退出登录时请求后台自己关闭了接单
                        UserHelper.getInstance(AbsBaseWebSocketService.this).cleanLogin();//清空 用户数据
                        EventBus.getDefault().post(new LogoutEvent(2));//在另一个设备上登录时候请求重新登录（要是开启应用就进入登录界面）
                        AbsBaseWebSocketService.this.stopSelf();//退出服务
//                        JPushInterface.stopPush(LiJiaApp.app);//暂停接收推送的通知。
//                        XGPushManager.unregisterPush(LiJiaApp.app);//暂停接收推送的通知。
                        CommonUtils.unregisterPush();//解绑信鸽
                    }
                    if (!stop) {
                        num++;
                        //断开之后自动重连
                        L.e(TAG, "断开之后自动重连");
                        setupWebSocket();
                    }

                }

                @Override
                public void onConnected(WebSocket websocket, Map<String, List<String>> headers) throws Exception {
                    super.onConnected(websocket, headers);
                    L.i(TAG, "onConnected()");
                    connectStatus = 2;
                    EventBus.getDefault().post(new WebSocketConnectedEvent());
                }

                @Override
                public void onError(WebSocket websocket, WebSocketException cause) throws Exception {
                    super.onError(websocket, cause);
                    L.d(TAG, "onError()", cause);
                    EventBus.getDefault().post(new WebSocketConnectionErrorEvent("onError:" + cause.getMessage()));
                }
            });
            try {
                webSocket.connect();
            } catch (NullPointerException e) {
                connectStatus = 0;
                L.i(TAG, String.format("NullPointerException()->%s", e.getMessage()));
                L.e(TAG, "NullPointerException()", e);
                EventBus.getDefault().post(new WebSocketConnectionErrorEvent("NullPointerException:" + e.getMessage()));
            } catch (OpeningHandshakeException e) {
                connectStatus = 0;
                L.i(TAG, String.format("OpeningHandshakeException()->%s", e.getMessage()));
                L.e(TAG, "OpeningHandshakeException()", e);
                StatusLine sl = e.getStatusLine();
                L.i(TAG, "=== Status Line ===");
                L.e(TAG, "=== Status Line ===");
                L.e(TAG, String.format("HTTP Version  = %s\n", sl.getHttpVersion()));
                L.i(TAG, String.format("Status Code   = %s\n", sl.getStatusCode()));
                L.e(TAG, String.format("Status Code   = %s\n", sl.getStatusCode()));
                L.i(TAG, String.format("Reason Phrase = %s\n", sl.getReasonPhrase()));
                L.e(TAG, String.format("Reason Phrase = %s\n", sl.getReasonPhrase()));

                Map<String, List<String>> headers = e.getHeaders();
                L.i(TAG, "=== HTTP Headers ===");
                L.e(TAG, "=== HTTP Headers ===");
                for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                    // Header name.
                    String name = entry.getKey();

                    // Values of the header.
                    List<String> values = entry.getValue();

                    if (values == null || values.size() == 0) {
                        // Print the name only.
                        System.out.println(name);
                        continue;
                    }

                    for (String value : values) {
                        // Print the name and the value.
                        L.e(TAG, String.format("%s: %s\n", name, value));
                        L.i(TAG, String.format("%s: %s\n", name, value));
                    }
                }
                EventBus.getDefault().post(new WebSocketConnectionErrorEvent("OpeningHandshakeException:" + e.getMessage()));
            } catch (HostnameUnverifiedException e) {
                connectStatus = 0;
                // The certificate of the peer does not match the expected hostname.
                L.i(TAG, String.format("HostnameUnverifiedException()->%s", e.getMessage()));
                EventBus.getDefault().post(new WebSocketConnectionErrorEvent("HostnameUnverifiedException:" + e.getMessage()));
            } catch (WebSocketException e) {
                connectStatus = 0;
                // Failed to establish a WebSocket connection.
                L.i(TAG, String.format("WebSocketException()->%s", e.getMessage()));
                EventBus.getDefault().post(new WebSocketConnectionErrorEvent("WebSocketException:" + e.getMessage()));
            }
        } catch (IOException e) {
            connectStatus = 0;
            L.e(TAG, "IOException()", e);
            EventBus.getDefault().post(new WebSocketConnectionErrorEvent("IOException:" + e.getMessage()));
        }
    }


    @Override
    public void sendText(String text) {
        if (TextUtils.isEmpty(text)) {
            return;
        }
        L.d(TAG, String.format("sendText()->%s", text));
        if (webSocket != null && connectStatus == 2) {
            webSocket.sendText(text);
        }
    }

    int connectNum = 0;

    //接单连接失败
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketConnectionErrorEvent(WebSocketConnectionErrorEvent event) {
        UIHelper.ToastMessage(this, "网络连接出错");
        if (CommonUtils.isEndorder(this) && connectNum < 10) {
            EventBus.getDefault().post(new StartWebSocketServiceEvent(true));
            connectNum++;
        } else {
            CommonUtils.loadReceiving(getApplication(), false, false, null);//连接失败10次后请求后台自己关闭了接单
            stopSelf();
        }
        L.d("BaseAppCompatActivity", "设置接单失败:" + event.getCause());
    }

    @Override
    public int getConnectStatus() {
        return connectStatus;
    }

    @Override
    public void reconnect() {
        L.d(TAG, "reconnect()");
        new Thread(new Runnable() {
            @Override
            public void run() {
                L.d(TAG, "reconnect()->begin restart...");
                if (webSocketThread != null && !webSocketThread.isAlive()) {
                    connectStatus = 0;
                    webSocketThread = new WebSocketThread();
                    webSocketThread.start();
                }
            }
        }).start();
    }

    @Override
    public void stop() {
        L.d(TAG, "stop()");
        webSocket.disconnect();
        stop = true;
        L.d(TAG, "stop()->success");
    }

    private String hi = "hi,告诉我你是否还活着";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://
                    initBaiDu();//
                    num = 0;
                    L.d(TAG, "heartBeatRunnable:" + num);
                    //发心跳包给socket
                    sendText(hi);
                    break;
                case 2://
                    String text = (String) msg.obj;
                    MainActivity.showMainActivity(getBaseContext(), getApplication(), text, false);
                    break;
                case 3://0
                    String textCast = (String) msg.obj;
//                    if (textCast.contains(CommonUtils.ORDER_RECEIVER)) {
                    Intent intent = new Intent();
                    intent.setAction(OrderReceiver.ORDER_ACTION);        //设置Action
                    intent.putExtra("msg", textCast);    //添加附加信息
                    sendBroadcast(intent);
                    L.d(TAG, "发广播发广播");
                    break;
            }
        }
    };

    private Runnable heartBeatRunnable = new Runnable() {//心跳包请求位置信息
        @Override
        public void run() {
            mHandler.postDelayed(this, HEART_BEAT_RATE);
            Message msg = mHandler.obtainMessage();
            msg.what = 1;
            mHandler.sendMessage(msg);
        }
    };

    private void initBaiDu() {
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (location != null) {
                    locationClient.stop();
                    baiduLocation(location);
                }
            }
        });
    }

    private void baiduLocation(BDLocation location) {
        LiJiaApi api = new LiJiaApi("app/upmember");
        String id = api.getUserId(this);
        if (StringUtils.isEmpty(id)) {//防止在开启socket的时候在应用管理里面清空数据
            stopSelf();
            return;
        }
        if (StringUtils.isEmpty(api.getPcode())){
            L.d(TAG, "LiJiaApp.pcode == null");
            LiJiaApp.pcode = SharedAccount.getInstance(this).getPcode();
            api.setPcode(LiJiaApp.pcode);
        }
        api.addParams("uid", api.getUserId(this));
        api.addParams("lon", location.getLongitude());
        api.addParams("lat", location.getLatitude());
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<UserDataBean<String>>() {
            @Override
            public void onResponse(UserDataBean<String> response) {
                if (connectStatus != 2) {
                    return;
                }
                UserBean bean = response.getData();
                if (bean != null) {
                    UserHelper.getInstance(AbsBaseWebSocketService.this).saveBean(bean);
                }
            }
        });
    }

}