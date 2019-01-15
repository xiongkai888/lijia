package com.lanmei.lijia.ui;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mapapi.SDKInitializer;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseApp;
import com.xson.common.utils.DeviceUtils;

/**
 * Created by xkai on 2018/4/13.
 */

public class LiJiaApp extends BaseApp {

    public static String versionName;
    public static String pcode;
    //    public final static String appid = "835186401";
    public final static String appid = "007915602";//利佳
    public static LiJiaApp app;


    @Override
    public void onCreate() {
        SDKInitializer.initialize(this);//百度地图
        super.onCreate();
        app = this;
        CommonUtils.initXINGE();
    }



    @Override
    protected void installMonitor() {
//        LeakCanary.install(this);
        versionName = DeviceUtils.getVersionName(this);
//        L.d("BaseAppCompatActivity", "versionName = " + versionName);
    }

    @Override
    public void watch(Object object) {

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

}
