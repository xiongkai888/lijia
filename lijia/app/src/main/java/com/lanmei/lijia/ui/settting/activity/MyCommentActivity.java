package com.lanmei.lijia.ui.settting.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.MyCommentAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.MyCommentListBean;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import butterknife.InjectView;

/**
 * 我的评论
 */
public class MyCommentActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    private SwipeRefreshController<MyCommentListBean> controller;

    @Override
    public int getContentViewId() {
        return R.layout.activity_single_listview;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.my_comment);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        initSwipeRefreshLayout();
    }

    private void initSwipeRefreshLayout() {

        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/wx_evaluation_list");
        api.addParams("uid", api.getUserId(this));
        MyCommentAdapter adapter = new MyCommentAdapter(this);
//        mAdapter.setData(getList());
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<MyCommentListBean>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.loadFirstPage();
        adapter.setReplyListener(new MyCommentAdapter.ReplyListener() {
            @Override
            public void reply(String id, String content) {
                loadReply(id,content);
            }
        });
    }

    private void loadReply(String id, String content) {
        LiJiaApi api = new LiJiaApi("app/wx_evaluation2");
        api.addParams("uid",api.getUserId(this));
        api.addParams("id",id);
        api.addParams("content2",content);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(getContext(),"回复成功");
                controller.loadFirstPage();
            }
        });
    }

}
