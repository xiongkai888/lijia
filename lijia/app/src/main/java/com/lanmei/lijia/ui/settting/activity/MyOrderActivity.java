package com.lanmei.lijia.ui.settting.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.MyOrderAdapter;
import com.lanmei.lijia.event.LocationOrderEvent;
import com.lanmei.lijia.event.OrderCountEvent;
import com.lanmei.lijia.ui.home.activity.HelperCenterActivity;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;


/**
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.viewPager)
    ViewPager mViewPager;
    @InjectView(R.id.tabLayout)
    TabLayout mTabLayout;
    MyOrderAdapter mAdapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_order);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        mAdapter = new MyOrderAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(3);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_something, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_something:
                IntentUtil.startActivity(this, HelperCenterActivity.class);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (StringUtils.isEmpty(grantResults)){
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Permission Granted
            if (requestCode == 100) {

            }
        } else {
            // Permission Denied
            switch (requestCode){
                case 100:
                    Toast.makeText(this, "访问被拒绝,无法获取定位信息！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    //(订单列表)确定点击上面前请求定位
    @Subscribe
    public void LocationOrderEvent(LocationOrderEvent event){
        initPermission();
    }
    //订单数量
    @Subscribe
    public void orderCountEvent(OrderCountEvent event){
        mAdapter.setCount(event.getList());
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
