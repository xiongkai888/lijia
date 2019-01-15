package com.lanmei.lijia.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Process;
import android.widget.EditText;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.BannerHolderView;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.AlbumBean;
import com.lanmei.lijia.bean.UserDataBean;
import com.lanmei.lijia.event.SetUserInfoEvent;
import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.login.LoginActivity;
import com.lanmei.lijia.webviewpage.PhotoBrowserActivity;
import com.meizu.cloud.pushsdk.util.MzSystemUtils;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommonUtils {

    public final static String isTwo = "2";
    public final static String isOne = "1";
    public final static String isZero = "0";

    public static int quantity = 3;

    /**
     * 获取TextView 字符串
     *
     * @param textView
     * @return
     */
    public static String getStringByTextView(TextView textView) {
        return textView.getText().toString().trim();
    }

    /**
     * 获取EditText 字符串
     *
     * @param editText
     * @return
     */
    public static String getStringByEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    public static boolean isLogin(Context context) {
        if (!UserHelper.getInstance(context).hasLogin()) {
            IntentUtil.startActivity(context, LoginActivity.class);
            return false;
        }
        return true;
    }

    public static String[] listToArray(List<String> list) {
        return list.toArray(new String[list.size()]);
    }

    public static List<String> arrayToList(String[] list) {
        return Arrays.asList(list);
    }


    //防止出现null
    public static String getString(String txt) {
        return StringUtils.isEmpty(txt) ? "    " : txt;
    }

    public static String getString(BaseBean response) {
        return StringUtils.isEmpty(response.getMsg()) ? response.getInfo() : response.getMsg();
    }

    public static UserBean getUserBean(Context context) {
        return UserHelper.getInstance(context).getUserBean();
    }

    public static String getUid(Context context) {
        if (getUserBean(context) == null) {
            return "";
        }
        return getUserBean(context).getId();
    }

    /**
     * @param context
     * @return 1 为开启socket 0 未开
     */
    public static String getEndorder(Context context) {
        if (getUserBean(context) == null) {
            return "";
        }
        return getUserBean(context).getEndorder();
    }

    /**
     * 是否开启
     *
     * @param context
     * @return 1 为开启socket 0 未开
     */
    public static boolean isEndorder(Context context) {
        return StringUtils.isSame(getEndorder(context), isOne);
    }

    /**
     * @param context
     * @param b       ture 为开启socket false为不开启socket
     */
    public static void setEndorder(Context context, boolean b) {
        UserBean bean = getUserBean(context);
        if (bean == null) {
            return;
        }
        bean.setEndorder(b ? isOne : isZero);
        L.d("BaseAppCompatActivity", bean.getEndorder());
        UserHelper.getInstance(context).saveBean(bean);
    }


    //获取用户信息
    public static void loadUserInfo(final Context context, final UserInfoListener l) {
        LiJiaApi api = new LiJiaApi("app/sel_user");
        api.addParams("uid", api.getUserId(context));
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<UserDataBean<String>>() {
            @Override
            public void onResponse(UserDataBean<String> response) {
                if (context == null) {
                    return;
                }
                UserBean bean = response.getData();
                if (bean != null) {
                    if (l != null) {
                        l.userInfo(bean);
                    }
                    UserHelper.getInstance(context).saveBean(bean);
                    EventBus.getDefault().post(new SetUserInfoEvent());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (l != null) {
                    l.error(error.getMessage());
                }
            }
        });
    }


    public interface UserInfoListener {
        void userInfo(UserBean bean);

        void error(String error);
    }

    /**
     * 是否审核通过
     *
     * @param context
     * @return
     */
    public static int isAudit(Context context) {//state = 0 未注册  1 已经注册
        String state = getUserBean(context).getState();
        if (!UserHelper.getInstance(context).hasLogin()) {
            return 3;//用户信息为空，退出
        }
        if (StringUtils.isSame(isOne, state)) {
            return 1;//审核通过
        } else if (!StringUtils.isSame(isZero, state) && !StringUtils.isSame(isOne, state)) {
            return 2;//审核中
        }
        return 0;//未注册
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        if (StringUtils.isEmpty(cardId) || cardId.startsWith("0")) {
            return false;
        }
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0 || !nonCheckCodeCardId.matches("\\d+")) {
            return 'N';//如果传的不是数据返回N
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    public static void setBanner(ConvenientBanner banner, List<String> list, boolean isTurning) {
        if (StringUtils.isEmpty(list)) {
            return;
        }
        //初始化商品图片轮播
        banner.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new BannerHolderView();
            }
        }, list);
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        banner.setPageIndicator(new int[]{R.drawable.shape_item_index_white, R.drawable.shape_item_index_red});
        banner.setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        if (list.size() == 1) {
            return;
        }
        if (!isTurning) {
            return;
        }
        banner.startTurning(3000);
    }

    /**
     * 浏览图片
     *
     * @param context
     * @param arry     图片地址数组
     * @param imageUrl 点击的图片地址
     */
    public static void showPhotoBrowserActivity(Context context, List<String> arry, String imageUrl) {
        Intent intent = new Intent();
        intent.putExtra("imageUrls", (Serializable) arry);
        intent.putExtra("curImageUrl", imageUrl);
        intent.setClass(context, PhotoBrowserActivity.class);
        context.startActivity(intent);
    }

    /**
     * 获取要上传相册的数量
     *
     * @param list
     * @return
     */
    public static int getNativeAlbumsNum(List<AlbumBean> list) {
        int num = 0;
        if (StringUtils.isEmpty(list)) {
            return num;
        }
        for (AlbumBean bean : list) {
            if (bean != null && bean.isPicker()) {
                num++;
            }
        }
        return num;
    }

    /**
     * 商家相册
     *
     * @param stringList
     * @return
     */
    public static List<AlbumBean> getAlbumList(List<String> stringList) {
        if (!StringUtils.isEmpty(stringList)) {
            int size = stringList.size();
            List<AlbumBean> list = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                AlbumBean bean = new AlbumBean();
                bean.setPic(stringList.get(i));
                bean.setPicker(false);
                list.add(bean);
            }
            return list;
        }
        return null;
    }


    /**
     * @param b
     * @param isLoad 是否显示加载
     * @param l
     */
    public static void loadReceiving(Context context, final boolean b, final boolean isLoad, final LoadReceivingListener l) {
        LiJiaApi api = new LiJiaApi("app/order_to_set_save");
        api.addParams("endorder", b ? 1 : 0);
        api.addParams("uid", api.getUserId(context));
        HttpClient.newInstance(context).request(isLoad, api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (l != null) {
                    l.receiving(b);
                }
            }
        });

    }

    public interface LoadReceivingListener {
        void receiving(boolean b);
    }


    /**
     * 是不是在三小时之内
     *
     * @param context
     * @param stamp
     * @return
     */
    public static boolean isWithinThreeHours(Context context, String stamp) {
        if (StringUtils.isEmpty(stamp)) {
            return false;
        }
        long now = System.currentTimeMillis() / 1000;
        long time = Long.parseLong(stamp);
        long click = (time - now);
        long mistiming = 3 * 60 * 60;
        if (click >= mistiming) {
            FormatTime formatTime = new FormatTime();
            time = time - mistiming;
            formatTime.setTime(time + "");
            UIHelper.ToastMessage(context, "在" + formatTime.getTime() + "后才可以点击出发哦");
            return false;
        } else {
            return true;
        }
    }


    /**
     * 获取上传相册的图片本地地址
     *
     * @param list
     * @return
     */
    public static List<String> getUploadingList(List<AlbumBean> list) {
        List<String> upList = null;
        if (list != null && list.size() > 0) {
            upList = new ArrayList<>();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                AlbumBean bean = list.get(i);
                if (bean != null && bean.isPicker()) {
                    upList.add(bean.getPic());
                }
            }
        }
        return upList;
    }

    /**
     * List<AlbumBean> 传化未String[]
     *
     * @param list
     * @return
     */
    public static List<String> getStringArry(List<AlbumBean> list) {
        List<String> arr = new ArrayList<>();
        if (list != null && list.size() > 0) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                AlbumBean bean = list.get(i);
                if (bean != null) {
                    arr.add(bean.getPic());
                }
            }
        }
        return arr;
    }

    /**
     * 去掉后面最后一个字符
     *
     * @param decs
     * @return
     */
    public static String getSubString(String decs) {
        if (StringUtils.isEmpty(decs)) {
            return "";
        }
        return decs.substring(0, decs.length() - 1);
    }


    /**
     * @param list
     * @return
     */
    public static String getPics(List<String> list) {
        String pics = "";
        if (StringUtils.isEmpty(list)) {
            return pics;
        }
        for (String pic : list) {
            pics = pics + pic + ",";
        }
        return StringUtils.isEmpty(pics) ? pics : getSubString(pics);
    }


    public static void location(Context context, String lat, String lon) {
        Uri location = Uri.parse("geo:" + lat + "," + lon);
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
        PackageManager packageManager = context.getPackageManager();
        List<ResolveInfo> activities = packageManager.queryIntentActivities(mapIntent, 0);
        boolean isIntentSafe = activities.size() > 0;
        if (isIntentSafe) {
            context.startActivity(mapIntent);
        } else {
            UIHelper.ToastMessage(context, "当前设备尚未安装任何地图应用");
        }
    }

    //初始化信鸽
    public static void initXINGE() {
        UserBean bean = getUserBean(LiJiaApp.app);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        //打开第三方推送
        XGPushConfig.enableOtherPush(LiJiaApp.app, true);

//        XGPushConfig.setMiPushAppId(LiJiaApp.app, "2882303761517802497");
//        XGPushConfig.setMiPushAppKey(LiJiaApp.app, "5531780240497");

        if (shouldInit(LiJiaApp.app) && StringUtils.isSame("xiaomi", getClientType())) {
            L.d("MzSystemUtils", "小米手机");
            MiPushClient.registerPush(LiJiaApp.app, "2882303761517802497", "5531780240497");
            MiPushClient.setUserAccount(LiJiaApp.app, bean.getId(), null);
        }

        if (MzSystemUtils.isBrandMeizu()) {//魅族推送只适用于Flyme系统,因此可以先行判断是否为魅族机型，再进行订阅，避免在其他机型上出现兼容性问题
            //设置魅族APPID和APPKEY
            XGPushConfig.setMzPushAppId(LiJiaApp.app, "1001437");
            XGPushConfig.setMzPushAppKey(LiJiaApp.app, "50bb4dacca1b43a99f3b5e0bfbc08164");
            L.d("MzSystemUtils", "魅族手机");
        }

        XGPushConfig.enableDebug(LiJiaApp.app, true);//信鸽推送开启debug日志数据
        XGPushManager.registerPush(LiJiaApp.app, new XGIOperateCallback() {
            @Override
            public void onSuccess(Object data, int flag) {
                //token在设备卸载重装的时候有可能会变
                L.d("MzSystemUtils", "注册成功，设备token为：" + data);
            }

            @Override
            public void onFail(Object data, int errCode, String msg) {
                L.d("MzSystemUtils", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
            }
        });
        XGPushManager.bindAccount(LiJiaApp.app, bean.getId());
//        XGPushManager.setTag(LiJiaApp.app, bean.getId());
    }

    /**
     *
     */
    public static String getClientType() {

        String manufacturer = Build.MANUFACTURER;
        if (manufacturer.equalsIgnoreCase("Xiaomi")) {
            return "xiaomi";
        } else if (manufacturer.equalsIgnoreCase("HUAWEI") || Build.BRAND.equals("Huawei") || Build.BRAND.equals("HONOR")) {//华为
            return "huawei";
        } else if (manufacturer.equalsIgnoreCase("meizu")) {//魅族
            return "meizu";
        } else {
            return "jiguang";//其他手机
        }
    }

    private static boolean shouldInit(Context context) {
        ActivityManager am = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = context.getPackageName();
        int myPid = Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }

    public static void playSound(Context context) {
        String uri = "android.resource://" + context.getPackageName() + "/" + R.raw.speek;
        L.d(L.TAG, uri);
        Uri no = Uri.parse(uri);
        Ringtone r = RingtoneManager.getRingtone(context, no);
        r.play();
    }

    //解绑信鸽
    public static void unregisterPush() {
        XGPushManager.registerPush(LiJiaApp.app, "*", new XGIOperateCallback() {
            @Override
            public void onSuccess(Object o, int i) {
                L.d("TPush", "账号解绑成功：" + o + ",i = " + i);
            }

            @Override
            public void onFail(Object o, int i, String s) {
                L.d("TPush", "账号解绑失败：" + o + ",i = " + i + ",s = " + s);
            }
        });
    }

    /**
     * 是否为过去的时间
     *
     * @param time
     * @return
     */
    public static boolean isPastTime(String time) {
        return System.currentTimeMillis() / 1000 > Long.parseLong(time);
    }

    public static String getNum(String num) {
        if (StringUtils.isEmpty(num)) {
            return isZero;
        }
        int i = Integer.parseInt(num);
        if (i > 100) {
            return "99+";
        }
        return Integer.toString(i);
    }

}

