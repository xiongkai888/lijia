package com.lanmei.lijia.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.QuestionDetailsBean;
import com.lanmei.lijia.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 问题详情
 */
public class QuestionDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.webView)
    WebView webView;

    @Override
    public int getContentViewId() {
        return R.layout.activity_question_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.question_details);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        String id = getIntent().getStringExtra("value");
        if (StringUtils.isEmpty(id)){
            return;
        }
        LiJiaApi api = new LiJiaApi("mall/helpdetails");
        api.addParams("id",id);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<QuestionDetailsBean>>() {
            @Override
            public void onResponse(DataBean<QuestionDetailsBean> response) {
                if (isFinishing()){
                    return;
                }
                QuestionDetailsBean bean = response.data;
                if (StringUtils.isEmpty(bean)){
                    return;
                }
                titleTv.setText(bean.getTitle());
                WebViewPhotoBrowserUtil.photoBrowser(getContext(),webView,bean.getContent());
            }
        });

    }

    @OnClick({R.id.ll_feedback, R.id.ll_contact})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_feedback:
                IntentUtil.startActivity(this,QuestionFeedbackActivity.class);
                break;
            case R.id.ll_contact:
                UIHelper.ToastMessage(this,R.string.developing);
                break;
        }
    }
}
