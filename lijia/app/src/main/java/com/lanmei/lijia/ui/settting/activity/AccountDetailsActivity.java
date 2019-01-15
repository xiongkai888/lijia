package com.lanmei.lijia.ui.settting.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.AccountDetailsAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.AccountDetailsListBean;
import com.lanmei.lijia.event.DepositEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 账户明细
 */
public class AccountDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    AccountDetailsAdapter mAdapter;
    private SwipeRefreshController<AccountDetailsListBean> controller;


    @Override
    public int getContentViewId() {
        return R.layout.activity_account_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.account_details);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/usermoney_log");
        api.addParams("uid", api.getUserId(this));
        mAdapter = new AccountDetailsAdapter(this);
        smartSwipeRefreshLayout.setAdapter(mAdapter);
        controller = new SwipeRefreshController<AccountDetailsListBean>(this, smartSwipeRefreshLayout, api, mAdapter) {
        };
        controller.loadFirstPage();
    }

    @OnClick({R.id.account_details_tv, R.id.deposit_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.account_details_tv://账户明细
                break;
            case R.id.deposit_tv://提现
                IntentUtil.startActivity(this, DepositActivity.class);
                break;
        }
    }

    //申请提现成功时调用
    @Subscribe
    public void depositEvent(DepositEvent event){
        if (controller != null){
            controller.loadFirstPage();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
