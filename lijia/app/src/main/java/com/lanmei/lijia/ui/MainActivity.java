package com.lanmei.lijia.ui;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.JPushBean;
import com.lanmei.lijia.bean.OrderInfoBean;
import com.lanmei.lijia.bean.OrderListBean;
import com.lanmei.lijia.event.LocationEvent;
import com.lanmei.lijia.event.LogoutEvent;
import com.lanmei.lijia.event.OrderReceivingEvent;
import com.lanmei.lijia.event.StartServiceEvent;
import com.lanmei.lijia.event.StartWebSocketServiceEvent;
import com.lanmei.lijia.event.StopServiceEvent;
import com.lanmei.lijia.event.SubmitDataEvent;
import com.lanmei.lijia.helper.TTSHelper;
import com.lanmei.lijia.ui.home.activity.InformationActivity;
import com.lanmei.lijia.ui.home.fragment.HomeFragment;
import com.lanmei.lijia.ui.home.fragment.WelcomeFragment;
import com.lanmei.lijia.ui.home.service.WebSocketService;
import com.lanmei.lijia.ui.settting.activity.MineActivity;
import com.lanmei.lijia.ui.settting.activity.MyOrderActivity;
import com.lanmei.lijia.ui.settting.activity.OrderInfoActivity;
import com.lanmei.lijia.update.NotificationDownloadCreator;
import com.lanmei.lijia.update.UpdateAppConfig;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.JsonUtil;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateBuilder;

import butterknife.InjectView;
import rx.functions.Action1;

public class MainActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    public static boolean isShow = false;//有没有开启应用
    public static TTSHelper ttsHelper;
    public static final String ACTION_SHOW_MAINACTIVITY = "android.intent.action.SHOW_MAINACTIVITY";

    @Override
    public int getContentViewId() {
        return R.layout.activity_main;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        if (!CommonUtils.isLogin(this)) {
            EventBus.getDefault().post(new StopServiceEvent());//防止开启接单模式后用户手动清理应用信息
            finish();
            return;
        }
        ttsHelper = new TTSHelper(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//屏幕长亮(有订单的时候保持长亮)
        EventBus.getDefault().register(this);
        init();
        loadUser();
        isShow = true;
        setHasId(getIntent());
        new UpdateAppConfig(this).initUpdateApp();//app版本更新
        requestStoragePermission();
    }

    private void setHasId(Intent intent) {
        String msg = intent.getStringExtra("msg");
        boolean hasID = intent.getBooleanExtra("hasID", false);
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        if (hasID) {//极光推送的id
            loadNewOrder(msg);
        } else {
            showOrderDialog(msg);
        }
    }

    private void init() {
        toolbar.setTitle(getString(R.string.home));
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.menu_home_message);
        toolbar.setOnMenuItemClickListener(this);
        toolbar.setNavigationIcon(R.drawable.home_setting);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int type = CommonUtils.isAudit(getContext());
                switch (type) {
                    case 2://审核中
                    case 1://审核通过
                    case 0://未注册
                        IntentUtil.startActivity(getContext(), type, MineActivity.class);
                        break;
                    case 3://用户信息为空，退出
                        finish();
                        break;
                }
            }
        });
    }

    //提交身份证等信息成功的时候调用
    @Subscribe
    public void submitDataEvent(SubmitDataEvent event) {
        loadUser();
    }

    //首页点击接单时候调用
    @Subscribe
    public void locationEvent(LocationEvent event) {
        int type = event.getType();
        if (type == 0) {
            initPermission();
        }
    }

    public static final int PERMISSION_LOCATION = 100;

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
        } else {
            setStartService();
        }
    }

    private void setStartService() {
        EventBus.getDefault().post(new LocationEvent(1));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (StringUtils.isEmpty(grantResults)) {
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            if (requestCode == PERMISSION_LOCATION) {
                setStartService();
            }
        } else {
            // Permission Denied
            switch (requestCode) {
                case PERMISSION_LOCATION:
                    Toast.makeText(this, R.string.can_not_locate, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    private void loadUser() {
        showType();
        CommonUtils.loadUserInfo(this, new CommonUtils.UserInfoListener() {
            @Override
            public void userInfo(UserBean bean) {
                if (!isFinishing()) {
                    EventBus.getDefault().post(new StartServiceEvent());
                }
            }

            @Override
            public void error(String error) {
                if (!isFinishing()) {
                    UIHelper.ToastMessage(getContext(), error);
                }
            }
        });
    }


    //开始连接Socket或关闭Socket时调用
    @Subscribe
    public void startWebSocketServiceEvent(StartWebSocketServiceEvent event) {
        if (event.isStartService()) {
            startService(new Intent(this, WebSocketService.class));
            L.d("BaseAppCompatActivity", "开启接单");
        } else {
            EventBus.getDefault().post(new StopServiceEvent());
            L.d("BaseAppCompatActivity", "关闭接单");
        }
    }

    public void showType() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (StringUtils.isSame(CommonUtils.getUserBean(this).getState(), CommonUtils.isOne)) {
            transaction.replace(R.id.fl_content, HomeFragment.newInstance());
        } else { //state = 0 未注册  1 已经注册
            transaction.replace(R.id.fl_content, WelcomeFragment.newInstance());
        }
        transaction.commitAllowingStateLoss();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_home_message:
                IntentUtil.startActivity(this, InformationActivity.class);
//                try {
//                    L.d(L.TAG, "test解密：" + Des.decode("aDxgk6tEmCkJmWnaOHpx4Vios/AOBhvbhX2rDGzitvkl6EaigUXh0VT5csBqK7COYoafoGyOMiv/FrUkXBrbsE99J8AxRumbc3WUEL+iRjE="));
//                } catch (Exception e) {
//                    L.d(L.TAG, "test解密不了：");
//                    e.printStackTrace();
//                }
                break;
        }
        return true;
    }

    AlertDialog alertDialog;

    private void showOrderDialog(String msg) {
        if (StringUtils.isEmpty(msg)) {
            return;
        }
        if (!StringUtils.isEmpty(alertDialog)) {//只能接收一个弹框
            return;
        }
        setJSONDialog(msg);
    }

    private void setJSONDialog(String msg) {
        JSONObject object = JsonUtil.stringToJson(msg);
        if (StringUtils.isEmpty(object)) {
            return;
        }
        try {
            if (object.getInt("TYPE") == 2) {
                JSONObject jsonObject = object.getJSONObject("MSG");
                setOrderDialog(jsonObject);
            }
        } catch (JSONException e) {
            L.d("BaseAppCompatActivity", "JSONException:msg");
            e.printStackTrace();
        }
    }

    private void setOrderDialog(JSONObject jsonObject) {
        if (StringUtils.isEmpty(jsonObject)) {
            return;
        }
        try {
            OrderInfoBean.MSGBean infoBean = JsonUtil.jsonToBean(jsonObject.toString(), OrderInfoBean.MSGBean.class);
            setMSGBeanDialog(infoBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setMSGBeanDialog(OrderInfoBean.MSGBean infoBean) {
        if (StringUtils.isEmpty(infoBean)) {
            return;
        }
        ttsHelper.stop();
        String time = null;
        String make_an_appointment = infoBean.getMake_an_appointment();
        if (StringUtils.isEmpty(make_an_appointment) || StringUtils.isSame(make_an_appointment, CommonUtils.isZero)) {
            time = getString(R.string.arrange_self);
        } else {
            FormatTime formatTime = new FormatTime(make_an_appointment);
            time = formatTime.getReserveTime();
        }
        String speech = String.format(getString(R.string.speech), infoBean.getClasstype(), time, infoBean.getAddress(), infoBean.getContent(), infoBean.getTotal_price());
        ttsHelper.initTTSPermission();
        ttsHelper.speak(speech);
        alertDialog = AKDialog.getReceivingOrderDialog(this, infoBean, new AKDialog.OrderReceivingListener() {
            @Override
            public void getOrderId(String id) {
                orderReceiver(id, 1);//接单
                ttsHelper.stop();
                alertDialog = null;
            }

            @Override
            public void abandon(String id) {
                orderReceiver(id, 0);//拒绝接单
                ttsHelper.stop();
                alertDialog = null;
            }

            @Override
            public void cancelDialog() {
                alertDialog = null;
            }
        });

    }

    private void orderReceiver(String id, final int state) {
        LiJiaApi api = new LiJiaApi("app/order_receiving");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("state", state);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                if (state == 1) {
                    ttsHelper.speak(getString(R.string.grab_a_single_success));
                    UIHelper.ToastMessage(getContext(), getString(R.string.grab_a_single_success_hint));
                    IntentUtil.startActivity(getContext(), MyOrderActivity.class);
                    EventBus.getDefault().post(new OrderReceivingEvent());//通知刷新首页
                } else {
                    //                    UIHelper.ToastMessage(getContext(), "已放弃该订单");
                    ttsHelper.speak(getString(R.string.give_up_the_order));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                String errorStr = error.getMessage();
//                if (state == 1) {
//                    ttsHelper.speak(getString(R.string.grab_a_single_failure));
//                } else {
//                    ttsHelper.speak(error.getMessage());
//                }
                ttsHelper.speak(errorStr);
                UIHelper.ToastMessage(getContext(), errorStr);
            }
        });
    }


    @Override
    protected void onDestroy() {
        if (ttsHelper != null) {
            ttsHelper.onDestroy();
            ttsHelper = null;
        }
        super.onDestroy();
        isShow = false;
        EventBus.getDefault().unregister(this);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);//关闭长亮
        if (alertDialog != null) {
            alertDialog.dismiss();
        }
    }


    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void logoutEvent(LogoutEvent event) {
        int type = event.getType();
        if (type == 1) {
            if (!CommonUtils.isLogin(this)) {
                EventBus.getDefault().post(new StopServiceEvent());//防止开启接单模式后用户手动清理应用信息
                finish();
            }
        } else if (type == 2) {
            finish();
            UIHelper.ToastMessage(this, getString(R.string.login_elsewhere));
            IntentUtil.startActivity(this, MainActivity.class);
        }
    }

    /**
     * @param context
     * @param application
     * @param msg
     * @param hasID       有无订单id
     */
    public static void showMainActivity(Context context, Application application, String msg, boolean hasID) {
        Intent intent = new Intent(ACTION_SHOW_MAINACTIVITY);
        intent.setClass(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("msg", msg);
        intent.putExtra("hasID", hasID);
        application.startActivity(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (ACTION_SHOW_MAINACTIVITY.equals(intent.getAction())) {
            setHasId(intent);
        }
    }

    private void loadNewOrder(final String msg) {
        try {
            JPushBean bean = JsonUtil.jsonToBean(msg, JPushBean.class);
            if (StringUtils.isEmpty(bean) || bean.getOid() == 0) {
                return;
            }
            final String oid = bean.getOid() + "";
            if (bean.getType() == 1) {
                IntentUtil.startActivity(this, OrderInfoActivity.class, oid);
                return;
            }
            LiJiaApi api = new LiJiaApi("app/repair_order_details");
            api.addParams("uid", api.getUserId(this));
            api.addParams("id", oid);
            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
                @Override
                public void onResponse(DataBean<String> response) {
                    if (isFinishing()) {
                        return;
                    }
                    try {
                        L.d("BaseAppCompatActivity", "订单详情：" + Des.decode(response.data));
                        OrderListBean.OrderBean bean = JsonUtil.jsonToBean(Des.decode(response.data), OrderListBean.OrderBean.class);
                        if (StringUtils.isEmpty(bean)) {
                            return;
                        }
                        OrderListBean.OrderBean.User5Bean userBean = bean.getUser5();
                        if (!StringUtils.isEmpty(userBean) && StringUtils.isSame(userBean.getId(), CommonUtils.getUid(getContext()))) {//是自己的单子就是进去订单信息
                            IntentUtil.startActivity(getContext(), OrderInfoActivity.class, oid);
                        } else {
                            setMSGBean(bean);
                        }
                    } catch (Exception e) {
                        L.d("BaseAppCompatActivity", "订单详情：catch");
                        UIHelper.ToastMessage(getContext(),getString(R.string.sorry_order_error));
                        e.printStackTrace();
                    }
                }
            });
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setMSGBean(OrderListBean.OrderBean bean) {
        OrderInfoBean.MSGBean infoBean = new OrderInfoBean.MSGBean();
        infoBean.setOid(bean.getId());
        infoBean.setMake_an_appointment(bean.getMake_an_appointment());
        infoBean.setTotal_price(bean.getTotal_price());
        infoBean.setContent(bean.getContent());
        infoBean.setAddress(bean.getAddress());
        infoBean.setPlatform_product(bean.getPlatform_product());
        infoBean.setClasstype(bean.getClasstypename());
        setMSGBeanDialog(infoBean);
    }

    /**
     * 请求文件读写权限。
     */
    private void requestStoragePermission() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //app更新
                            UpdateBuilder.create()
                                    .downloadDialogCreator(new NotificationDownloadCreator())
                                    .check();
                        }
                    }
                });
    }

}
