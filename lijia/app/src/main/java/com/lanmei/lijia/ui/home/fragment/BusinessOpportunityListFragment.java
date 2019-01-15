package com.lanmei.lijia.ui.home.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.BusinessOpportunityListAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.BusinessOpportuntyListBean;
import com.lanmei.lijia.event.OrderReceivingEvent;
import com.lanmei.lijia.ui.settting.activity.MyOrderActivity;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;


/**
 * Created by Administrator on 2017/4/27.
 * 任务商机列表
 */

public class BusinessOpportunityListFragment extends BaseFragment {

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    BusinessOpportunityListAdapter mAdapter;
    private SwipeRefreshController<BusinessOpportuntyListBean> controller;

    @InjectView(R.id.ll_num)
    LinearLayout llNum;
    @InjectView(R.id.num_tv)
    TextView numTv;
    @InjectView(R.id.spinner)
    Spinner mSpinner;
    LiJiaApi api;
    String num;
    private int positionSpinner;

    @Override
    public int getContentViewId() {
        return R.layout.fragment_single_listview_business;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        api = new LiJiaApi("app/notification4");
        api.addParams("dtime", getArguments().getString("dtime"));
        api.addParams("uid", api.getUserId(context));
        mAdapter = new BusinessOpportunityListAdapter(context);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<BusinessOpportuntyListBean>(context, smartSwipeRefreshLayout, api, mAdapter) {
            @Override
            public boolean onSuccessResponse(BusinessOpportuntyListBean response) {
                num = response.getCount();
                if (StringUtils.isSame(CommonUtils.isZero, num)) {
                    llNum.setVisibility(View.GONE);
                } else {
                    llNum.setVisibility(View.VISIBLE);
                    numTv.setText(String.format(getString(R.string.business), num));
                }
                return super.onSuccessResponse(response);
            }
        };
        controller.loadFirstPage();

        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (StringUtils.isEmpty(num) || StringUtils.isSame(CommonUtils.isZero, num)) {
                    return;
                }
                if (positionSpinner == position) {
                    return;
                }
                positionSpinner = position;
                api.addParams("type", position);
                controller.loadFirstPage();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mAdapter.setOrderReceivingListener(new BusinessOpportunityListAdapter.OrderReceivingListener() {
            @Override
            public void orderReceiving(final String id) {
                AKDialog.getAlertDialog(context, "确定接单？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderReceiver(id);
                    }
                });
            }
        });
    }

    private void orderReceiver(String id) {
        LiJiaApi api = new LiJiaApi("app/order_receiving");
        api.addParams("uid", api.getUserId(context));
        api.addParams("oid", id);
        api.addParams("state", 1);
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (controller == null || context == null) {
                    return;
                }
                controller.loadFirstPage();
                UIHelper.ToastMessage(context, context.getString(R.string.grab_a_single_success_hint));
                IntentUtil.startActivity(context, MyOrderActivity.class);
                EventBus.getDefault().post(new OrderReceivingEvent());//通知刷新首页
            }
        });
    }

}
