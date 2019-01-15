package com.lanmei.lijia.jpush;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.MainActivity;
import com.lanmei.lijia.utils.CommonUtils;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class LiJiaReceiver extends XGPushBaseReceiver {

//    private Intent intent = new Intent("com.qq.xgdemo.activity.UPDATE_LISTVIEW");
    public static final String LogTag = "TPushReceiver";
    public static final int TIME = 1800;


    private void show(Context context, String text) {
//        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {

        if (context == null || notifiShowedRlt == null) {
            L.d(LogTag, "+++++++++++++++++++++++++++++onNotifactionShowedResult  null+++++++++++++++++++++++++++++");
            return;
        }
        L.d(LogTag, "+++++++++++++++++++++++++++++展示通知的回调");
        final String customContent = notifiShowedRlt.getCustomContent();
        CommonUtils.playSound(context);
//        XGNotification notific = new XGNotification();
//        notific.setMsg_id(notifiShowedRlt.getMsgId());
//        notific.setTitle(notifiShowedRlt.getTitle());
//        notific.setContent(notifiShowedRlt.getContent());
//        // notificationActionType==1为Activity，2为url，3为intent
//        notific.setNotificationActionType(notifiShowedRlt
//                .getNotificationActionType());
//        //Activity,url,intent都可以通过getActivity()获得
//        notific.setActivity(notifiShowedRlt.getActivity());
//        notific.setUpdate_time(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//                .format(Calendar.getInstance().getTime()));
//        NotificationService.getInstance(context).save(notific);
//        context.sendBroadcast(intent);
//        show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());

    }



    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        Log.d(LogTag, text);
        show(context, text);

    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    @Override
    public void onNotifactionClickedResult(final Context context,
                                           XGPushClickedResult message) {
        L.d(LogTag, "+++++++++++++++ 通知被点击 跳转到指定页面。");
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // 通知在通知栏被点击啦。。。。。
            // APP自己处理点击的相关动作
            // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
            text = "通知被打开 :" + message;
        } else if (message.getActionType() == XGPushClickedResult.NOTIFACTION_DELETED_TYPE) {
            // 通知被清除啦。。。。
            // APP自己处理通知被清除后的相关动作
            text = "通知被清除 :" + message;
        }
//        Toast.makeText(context, "广播接收到通知被点击:" + message.toString(),
//                Toast.LENGTH_SHORT).show();
        // 获取自定义key-value
        final String customContent = message.getCustomContent();
        // APP自主处理的过程。。。
        L.d(LogTag, text+":APP自主处理的过程  message.getContent():"+message.getContent());
        L.d(LogTag, "message.getCustomContent():"+message.getCustomContent());
        L.d(LogTag, "message.getContent():"+message.getContent());
        L.d(LogTag, "message.getActivityName():"+message.getActivityName());
        L.d(LogTag, "message.getTitle():"+message.getTitle());
        L.d(LogTag, "message.getActionType():"+message.getActionType());
        L.d(LogTag, "message.getMsgId():"+message.getMsgId());
        show(context, text);

//        if (!MainActivity.isShow){//MainActivity没启动时不执行下面的
//            return;
//        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!StringUtils.isEmpty(customContent)) {
                    try {
                        JSONObject obj = new JSONObject(customContent);
                        // key1为前台配置的key
                        if (!obj.isNull("type")) {
                            JSONObject value = obj.getJSONObject("type");
                            if (!StringUtils.isEmpty(value)){
                                L.d(LogTag, "LiJiaReceiver=》 推送信息" + value.toString());
                                MainActivity.showMainActivity(context, LiJiaApp.app, value.toString(),true);
                            }
                        }
                        // ...
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        },TIME);
    }

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败错误码：" + errorCode;
        }
        L.d(LogTag, text);
        show(context, text);
    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    L.d(LogTag, "get custom value:" + value);
                }
                // ...
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        L.d(LogTag, "++++++++++++++++透传消息");
        // APP自主处理消息的过程...
        L.d(LogTag, text);
        show(context, text);
    }
}
