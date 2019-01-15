package com.lanmei.lijia.ui.settting.activity.details;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 报修描述
 */
public class FixDescribeActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.content_tv)
    TextView contentTv;

    @Override
    public int getContentViewId() {
        return (R.layout.activity_fix_describe);
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("报修描述");
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        String content = getIntent().getStringExtra("value");
        if (StringUtils.isEmpty(content)){
            return;
        }
        contentTv.setText(content);
    }

}
