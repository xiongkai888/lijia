package com.lanmei.lijia.jpush;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;

import com.huawei.hms.support.api.push.PushReceiver;
import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.MainActivity;
import com.xson.common.utils.L;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by xkai on 2018/6/4.
 */

public class HuaWeiReceiver extends PushReceiver {
    @Override
    public void onEvent(final Context context, Event arg1,final Bundle arg2) {
        super.onEvent(context, arg1, arg2);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONArray jsonObject = new JSONArray(arg2.getString("pushMsg"));
                    int size = jsonObject.length();
                    for (int i = 0; i < size; i++) {
                        JSONObject job = jsonObject.getJSONObject(i);
                        if (job.has("type")){
                            MainActivity.showMainActivity(context, LiJiaApp.app, job.getString("type"), true);
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        },LiJiaReceiver.TIME);
    }

    @Override
    public boolean onPushMsg(Context context, byte[] arg1, Bundle arg2) {

        showToast("onPushMsg" + new String(arg1) + " Bundle: " + arg2, context);
        return super.onPushMsg(context, arg1, arg2);
    }

    @Override
    public void onPushMsg(Context context, byte[] arg1, String arg2) {

        showToast("onPushMsg" + new String(arg1) + " arg2: " + arg2, context);
        super.onPushMsg(context, arg1, arg2);
    }

    @Override
    public void onPushState(Context context, boolean arg1) {

        showToast("onPushState" + arg1, context);
        super.onPushState(context, arg1);
    }

    @Override
    public void onToken(Context context, String arg1, Bundle arg2) {
        super.onToken(context, arg1, arg2);

        showToast(" onToken" + arg1 + "bundke " + arg2, context);
    }

    @Override
    public void onToken(Context context, String arg1) {
        super.onToken(context, arg1);
        showToast(" onToken" + arg1, context);
    }

    public void showToast(final String toast, final Context context) {

//        new Thread(new Runnable() {
//
//            @Override
//            public void run() {
//                Looper.prepare();
//                L.d(LiJiaReceiver.LogTag, toast);
//                Toast.makeText(context, toast, Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//        }).start();
        L.d(LiJiaReceiver.LogTag, toast);
    }

    private void writeToFile(String conrent) {
        String SDPATH = Environment.getExternalStorageDirectory() + "/huawei.txt";
        try {
            FileWriter fileWriter = new FileWriter(SDPATH, true);

            fileWriter.write(conrent + "\r\n");
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
