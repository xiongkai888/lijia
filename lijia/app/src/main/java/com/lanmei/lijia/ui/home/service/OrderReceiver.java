package com.lanmei.lijia.ui.home.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.MainActivity;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;

/**
 * Created by xkai on 2018/5/11.
 * 接单广播
 */

public class OrderReceiver extends BroadcastReceiver{

    public static String ORDER_ACTION = "com.lanmei.lijia.ORDERACTION";

    @Override
    public void onReceive(Context context, Intent intent) {
        String msg = intent.getStringExtra("msg");
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        L.d(L.TAG,"接单广播收到信息："+msg);
        MainActivity.showMainActivity(context, LiJiaApp.app,msg,false);
    }
}
