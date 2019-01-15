package com.lanmei.lijia.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.MyInformationAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.MyInformationListBean;
import com.lanmei.lijia.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 我的消息
 *
 */
public class MyInformationActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<MyInformationListBean> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_information;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_information);
        actionbar.setHomeAsUpIndicator(R.drawable.back);


        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/send_backstage");
        api.addParams("uid",api.getUserId(this));
        MyInformationAdapter adapter = new MyInformationAdapter(this);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<MyInformationListBean>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                AKDialog.getAlertDialog(this, "确认要清空我的消息？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        UIHelper.ToastMessage(getContext(),R.string.developing);
                    }
                });
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
