package com.lanmei.lijia.ui.settting.fragment;

import android.os.Bundle;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.OrderListAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.OrderListBean;
import com.lanmei.lijia.event.OperationOrderEvent;
import com.lanmei.lijia.event.OrderCountEvent;
import com.xson.common.app.BaseFragment;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 订单列表
 */

public class OrderListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    OrderListAdapter mAdapter;
    private SwipeRefreshController<OrderListBean> controller;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    private void initSwipeRefreshLayout() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/service_order");
        api.addParams("uid",api.getUserId(context));
        api.addParams("state",getArguments().getString("status"));
        mAdapter = new OrderListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<OrderListBean>(context, smartSwipeRefreshLayout, api, mAdapter) {
            @Override
            public boolean onSuccessResponse(OrderListBean response) {
                if (StringUtils.isEmpty(response)){
                    return super.onSuccessResponse(response);
                }
                List<String> list = new ArrayList<>();
                list.add(response.getCount1());
                list.add(response.getCount2());
                list.add(response.getCount3());
                list.add(response.getCount4());
                list.add(response.getCount5());
                EventBus.getDefault().post(new OrderCountEvent(list));
                return super.onSuccessResponse(response);
            }
        };
        controller.loadFirstPage();
    }

//操作订单状态的时候调用
    @Subscribe
    public void operationOrderEvent(OperationOrderEvent event){
        if (controller != null){
            controller.loadFirstPage();
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

}
