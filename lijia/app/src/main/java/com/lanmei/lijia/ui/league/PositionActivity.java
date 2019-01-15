package com.lanmei.lijia.ui.league;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.HotCityAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.HotCityListBean;
import com.lanmei.lijia.event.LeagueEvent;
import com.lanmei.lijia.utils.BaiduLocation;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;

/**
 * 所在城市
 */
public class PositionActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.city_name_tv)
    TextView cityNameTv;
    HotCityAdapter adapter;

    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private static final int PERMISSION_LOCATION = 100;


    private SwipeRefreshController<HotCityListBean> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_position;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        initPermission();

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle("所在城市");
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/hotcity");
        adapter = new HotCityAdapter(this);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<HotCityListBean>(this, smartSwipeRefreshLayout, api, adapter) {
            @Override
            public boolean onSuccessResponse(HotCityListBean response) {
                adapter.setData(response.getDataList());
                adapter.notifyDataSetChanged();
                return true;
            }
        };
        controller.loadFirstPage();
        adapter.setChooseCityListener(new HotCityAdapter.ChooseCityListener() {
            @Override
            public void choose(String city) {
                leagueEvent(city);
                finish();
            }
        });
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_LOCATION);
            } else {
                initBaiDu();
            }
        } else {
            initBaiDu();
        }
    }

    private String city = "";//市

    private void initBaiDu() {
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (location != null && !isFinishing()) {
                    locationClient.stop();
                    city = location.getCity();
                    cityNameTv.setText("当前定位城市：" + city);
                    leagueEvent(city);
                }
            }
        });
    }

    public void leagueEvent(String city){
        LeagueEvent event = new LeagueEvent(1);
        event.setCity(city);
        EventBus.getDefault().post(event);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_LOCATION) {
            initBaiDu();
        }
    }

}
