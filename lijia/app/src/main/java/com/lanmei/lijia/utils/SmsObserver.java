package com.lanmei.lijia.utils;

/**
 * Created by xkai on 2018/4/17.
 */

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;

import com.lanmei.lijia.ui.login.CodeActivity;
import com.xson.common.utils.L;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by huangminzheng on 16/3/15.
 *
 * 观察者对象
 */
public class SmsObserver extends ContentObserver {

    private Context mContext;
    private Handler mHandler;

    public SmsObserver(Context context, Handler handler) {
        super(handler);
        mContext = context;
        mHandler = handler;
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);

        L.d("BeanRequest", "SMS has changed!");
        L.d("BeanRequest", uri.toString());
        // 短信内容变化时，第一次调用该方法时短信内容并没有写入到数据库中,return
        if (uri.toString().equals("content://sms/raw")) {
            return;
        }
        getValidateCode();//获取短信验证码

    }

    /**
     * 获取短信验证码
     */
    private void getValidateCode() {
        String code = "";
        Uri inboxUri = Uri.parse("content://sms/inbox");
        Cursor c = mContext.getContentResolver().query(inboxUri, null, null, null, "date desc");//
        if (c != null) {
            if (c.moveToFirst()) {
                String address = c.getString(c.getColumnIndex("address"));
                String body = c.getString(c.getColumnIndex("body"));

                //13162364720为发件人的手机号码
//                if (!address.equals("13162364720")) {
//                    return;
//                }
                L.d("BeanRequest", "发件人为:" + address + " ," + "短信内容为:" + body);

                Pattern pattern = Pattern.compile("(\\d{6})");
                Matcher matcher = pattern.matcher(body);

                if (matcher.find()) {
                    code = matcher.group(0);
                    L.d("BeanRequest", "验证码为: " + code);
                    mHandler.obtainMessage(CodeActivity.MSG_RECEIVED_CODE, code).sendToTarget();
                }
            }
            c.close();
        }
    }
}