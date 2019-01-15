package com.lanmei.lijia.ui.settting.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.OrderCancelAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.OrderCancelListBean;
import com.lanmei.lijia.event.OperationOrderEvent;
import com.lanmei.lijia.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 取消订单原因
 */
public class OrderCancelActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    OrderCancelAdapter mAdapter;
    private SwipeRefreshController<OrderCancelListBean> controller;
    String oid;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_cancel;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("取消原因");
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        oid = getIntent().getStringExtra("value");

        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/endorder");
        api.addParams("oid",oid);
        mAdapter = new OrderCancelAdapter(this);
//        mAdapter.setData(getList());
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<OrderCancelListBean>(this, smartSwipeRefreshLayout, api, mAdapter) {
            @Override
            public boolean onSuccessResponse(OrderCancelListBean response) {
                if (isFinishing()){
                    return true;
                }
                mAdapter.setData(response.getDataList());
//                mAdapter.notifyDataSetChanged();
                return true;
            }
        };
        controller.loadFirstPage();
    }


    public void loadState(final String state, String id,String yid) {
        LiJiaApi api = new LiJiaApi("app/del_master_order");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("state", state);
        api.addParams("yid", yid);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), getString(R.string.operate_success));
                EventBus.getDefault().post(new OperationOrderEvent());
                finish();
            }
        });
    }

    @OnClick(R.id.submit_bt)
    public void onViewClicked() {
        List<OrderCancelListBean.OrderCancelBean> list = mAdapter.getData();
        if (StringUtils.isEmpty(list)) {
            UIHelper.ToastMessage(this,"请先选择取消原因");
            return;
        }
        final String yid = getYid(list);
        if (StringUtils.isEmpty(yid)){
            UIHelper.ToastMessage(this,"请先选择取消原因");
            return;
        }
        AKDialog.getYseNoDialog(this, "确认取消", "放弃", "取消订单需要用户确认\n确认要取消吗？", new AKDialog.ConfirmListener() {
            @Override
            public void yes() {
                loadState("11", oid,yid);
            }

            @Override
            public void no() {

            }
        }).show();
    }

    private String getYid(List<OrderCancelListBean.OrderCancelBean> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            OrderCancelListBean.OrderCancelBean bean = list.get(i);
            if (bean.isChoose()) {
                return bean.getId();
            }
        }
        return "";
    }
}
