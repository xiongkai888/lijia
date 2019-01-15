package com.lanmei.lijia.ui.settting.activity.details;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.view.View;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.FixPicsAdapter;
import com.lanmei.lijia.bean.OrderListBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.EmptyRecyclerView;

import java.util.List;

import butterknife.InjectView;

/**
 * 报修照片
 */
public class FixPicsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.recyclerView)
    EmptyRecyclerView mRecyclerView;
    @InjectView(R.id.empty_view)
    View mEmptyView;
    OrderListBean.OrderBean bean;
    boolean fix_pics;

    @Override
    public int getContentViewId() {
        return (R.layout.activity_fix_pics);
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle == null) {
            return;
        }
        bean = (OrderListBean.OrderBean) bundle.getSerializable("bean");
        fix_pics = bundle.getBoolean("fix_pics");
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        FixPicsAdapter adapter = new FixPicsAdapter(this);

        if (fix_pics) {
            setData(actionbar, getString(R.string.shi_fix_pics), bean.getImgs1(), adapter);
        } else {
            setData(actionbar, getString(R.string.client_fix_pics), bean.getImgs(), adapter);
        }

        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setEmptyView(mEmptyView);
        mRecyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public void setData(ActionBar actionbar, String title, List<String> list, FixPicsAdapter adapter) {
        actionbar.setTitle(title);
        if (!StringUtils.isEmpty(list)) {
            adapter.setData(list);
        }
    }

}
