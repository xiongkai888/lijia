package com.lanmei.lijia.ui.home.fragment;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.lanmei.lijia.R;
import com.lanmei.lijia.WebSocket.event.WebSocketConnectedEvent;
import com.lanmei.lijia.WebSocket.event.WebSocketConnectionErrorEvent;
import com.lanmei.lijia.adapter.HomeAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.HomeListBean;
import com.lanmei.lijia.event.LocationEvent;
import com.lanmei.lijia.event.OperationOrderEvent;
import com.lanmei.lijia.event.OrderReceivingEvent;
import com.lanmei.lijia.event.StartServiceEvent;
import com.lanmei.lijia.event.StartWebSocketServiceEvent;
import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.MainActivity;
import com.lanmei.lijia.ui.home.activity.OrderReceivingSetActivity;
import com.lanmei.lijia.ui.home.service.WebSocketService;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.SharedAccount;
import com.xson.common.app.BaseFragment;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/13.
 * 首页
 */

public class HomeFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.order_bt)
    Button orderBt;
    HomeAdapter adapter;
    private SwipeRefreshController<HomeListBean> controller;
    boolean isStartService;//是否开启WebSocketService
    public boolean isConnect = false;

    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_home;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(context));
        LiJiaApi api = new LiJiaApi("app/homepage");
        api.addParams("uid", api.getUserId(context));
        adapter = new HomeAdapter(context);
        if (StringUtils.isEmpty(api.getPcode())){
            LiJiaApp.pcode = SharedAccount.getInstance(context).getPcode();
            api.setPcode(LiJiaApp.pcode);
            L.d("BaseAppCompatActivity", "homepage：LiJiaApp.pcode == null");
        }
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<HomeListBean>(context, smartSwipeRefreshLayout, api, adapter) {
            @Override
            public boolean onSuccessResponse(HomeListBean response) {
                if (controller == null) {
                    return true;
                }
                controller.setHasMore(false);
                if (response == null) {
                    return true;
                }
                HomeListBean.HomeBean homeBean = response.getHomeBean();
                if (homeBean == null) {
                    return true;
                }
                adapter.setHomeBean(homeBean);
                adapter.setData(homeBean.getRepair());
                adapter.notifyDataSetChanged();
                return true;
            }
        };
        controller.loadFirstPage();
        adapter.notifyDataSetChanged();
        initStartService();
        setHint(isStartService);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    private void initStartService() {
        if (CommonUtils.isEndorder(context)) {
            if (!initPermission()) {
                return;
            }
            isStartService = true;
            if (WebSocketService.connectStatus == 0) {
                EventBus.getDefault().post(new StartWebSocketServiceEvent(isStartService));//
            }
        } else {
            isStartService = false;
            isConnect = false;
            EventBus.getDefault().post(new StartWebSocketServiceEvent(isStartService));
        }
        L.d("BaseAppCompatActivity", "socket连接状态：" + WebSocketService.connectStatus);
    }


    @Subscribe
    public void startServiceEvent(StartServiceEvent event){
        initStartService();
    }

    private boolean initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, MainActivity.PERMISSION_LOCATION);
            return false;
        } else {
            return true;
        }
    }

    @OnClick({R.id.order_setting_tv, R.id.order_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.order_setting_tv:
                IntentUtil.startActivity(context, OrderReceivingSetActivity.class);
                break;
            case R.id.order_bt:
                if (!initPermission()) {//防止用户清空数据，没有定位权限
                    return;
                }
                EventBus.getDefault().post(new LocationEvent(0));
                break;
        }
    }

    //首页点击接单时候调用
    @Subscribe
    public void locationEvent(LocationEvent event) {
        int type = event.getType();
        if (type == 1) {
            setStartService();
        }
    }

    //接单成功后调用
    @Subscribe
    public void orderReceivingEvent(OrderReceivingEvent event) {
        if (controller != null) {
            controller.loadFirstPage();
        }
    }


    //操作订单状态的时候调用
    @Subscribe
    public void operationOrderEvent(OperationOrderEvent event) {
        if (controller != null) {
            controller.loadFirstPage();
        }
    }

    private void setStartService() {
        if (WebSocketService.connectStatus == 1) {
            UIHelper.ToastMessage(context, getString(R.string.connecting));
            return;
        }
        String msg = "";
        if (isStartService) {
            msg = getString(R.string.confirm_close_the_order);
        } else {
            msg = getString(R.string.confirm_open_the_order);
        }
        AKDialog.getAlertDialog(context, msg, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadReceiving(!isStartService);
            }
        });
    }

    /**
     * 向后台请求要关闭或者socket
     *
     * @param b
     */
    private void loadReceiving(boolean b) {
        CommonUtils.loadReceiving(context, b, true, new CommonUtils.LoadReceivingListener() {
            @Override
            public void receiving(boolean b) {
                if (orderBt == null) {
                    return;
                }
                CommonUtils.setEndorder(context,b);//用于下一次进去应用时判断
                connectSocket(b);
            }
        });
    }

    private void connectSocket(boolean b) {
        EventBus.getDefault().post(new StartWebSocketServiceEvent(b));//
        if (!b) {
            isStartService = b;
            setHint(isStartService);
        }
        isConnect = b;
    }

    //接单连接成功
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketConnectedEvent(WebSocketConnectedEvent event) {
        isStartService = true;
        setHint(isStartService);
        if (isConnect) {//关机后在打开应用不提示(即点击上班时)
            isConnect = !isConnect;//
            UIHelper.ToastMessage(context, getString(R.string.setup_order_successful));
        }
        L.d("BaseAppCompatActivity", getString(R.string.setup_order_successful));
    }

    //提示、设置接单状态
    private void setHint(boolean b) {
        if (b) {
            orderBt.setText(R.string.ordering);
        } else {
            orderBt.setText(R.string.order_onclick);
        }
    }


    //接单连接失败(要是应用在运行就改变文字状态)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void webSocketConnectionErrorEvent(WebSocketConnectionErrorEvent event) {
        connectSocket(false);
        L.d("BaseAppCompatActivity", "设置接单失败:" + event.getCause());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
