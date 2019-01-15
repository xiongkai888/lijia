package com.lanmei.lijia.ui.league;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.ChooseWorkAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.ChooseWorkListBean;
import com.lanmei.lijia.event.LeagueEvent;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DividerItemDecoration;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;

/**
 * 选择工种
 */
public class ChooseWorkActivity extends BaseActivity {


    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    ChooseWorkAdapter adapter;
    private SwipeRefreshController<ChooseWorkListBean> controller;
    List<ChooseWorkListBean.ChooseWorkBean> chooseWorkList;


    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            chooseWorkList = (List<ChooseWorkListBean.ChooseWorkBean>) bundle.getSerializable("list");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.choose_work);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        smartSwipeRefreshLayout.initWithLinearLayout();
        smartSwipeRefreshLayout.getRecyclerView().addItemDecoration(new DividerItemDecoration(this));
        LiJiaApi api = new LiJiaApi("app/craft");
        adapter = new ChooseWorkAdapter(this);
//        adapter.setData(getList());
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<ChooseWorkListBean>(this, smartSwipeRefreshLayout, api, adapter) {
            @Override
            public boolean onSuccessResponse(ChooseWorkListBean response) {
                List<ChooseWorkListBean.ChooseWorkBean> list = response.getDataList();
                if (!StringUtils.isEmpty(chooseWorkList) && !StringUtils.isEmpty(list)) {
                    int size = chooseWorkList.size();
                    int size1 = list.size();
                    for (int i = 0; i < size; i++) {
                        String className = chooseWorkList.get(i).getClassname();
                        boolean b = chooseWorkList.get(i).isChoose();
                        if (!b){
                            L.d("cameraPreview","continue:i="+i);
                            continue;
                        }
                        for (int j = 0; j < size1; j++) {
                            if (b && StringUtils.isSame(className, list.get(j).getClassname())) {
                                list.get(j).setChoose(true);
                                L.d("cameraPreview","break:j="+j);
                                break;
                            }
                        }
                    }
                }
                chooseWorkList = list;
                adapter.setData(list);
                adapter.notifyDataSetChanged();
                return true;
            }
        };
        controller.loadFirstPage();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                choose();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void choose() {
        List<ChooseWorkListBean.ChooseWorkBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)) {
            UIHelper.ToastMessage(this, "暂无工种可选");
            return;
        }
        List<ChooseWorkListBean.ChooseWorkBean> chooseList = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ChooseWorkListBean.ChooseWorkBean bean = list.get(i);
            if (bean.isChoose()) {
                chooseList.add(bean);
            }
        }
        if (StringUtils.isEmpty(chooseList)) {
            UIHelper.ToastMessage(this, "请选择工种");
            return;
        }
        LeagueEvent event = new LeagueEvent(4);
        event.setChooseWorkList(chooseList);
        EventBus.getDefault().post(event);
        finish();
    }

}
