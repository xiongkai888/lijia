package com.lanmei.lijia.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.QuestionClassifyAdapter;
import com.lanmei.lijia.adapter.QuestionHotAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.AdListBean;
import com.lanmei.lijia.bean.QuestionClassifyListBean;
import com.lanmei.lijia.bean.QuestionHotListBean;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.SimpleTextWatcher;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 帮助中心
 */
public class HelperCenterActivity extends BaseActivity implements TextView.OnEditorActionListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.banner)
    ConvenientBanner banner;//广告轮播图
    @InjectView(R.id.keywordEditText)
    DrawClickableEditText keywordEditText;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;//热门问题
    @InjectView(R.id.c_recyclerView)
    RecyclerView cRecyclerView;//热门分类
    QuestionHotAdapter adapter;
    List<QuestionHotListBean.QuestionHotBean> list;

    @Override
    public int getContentViewId() {
        return R.layout.activity_helpe_center;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.helper_center);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        keywordEditText.setHint("输入你的问题,搜一搜吧！");
        keywordEditText.setOnEditorActionListener(this);
        keywordEditText.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s + "")) {
                    notifyData(list);
                }
            }
        });
        initQuestionHot();//热门问题
        initQuestionClassify();//热门分类
        initAd();//广告轮播图
    }

    private void initAd() {
        LiJiaApi api = new LiJiaApi("app/adpic");
        api.addParams("classid", 7);
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<AdListBean>() {
            @Override
            public void onResponse(AdListBean response) {
                if (isFinishing()) {
                    return;
                }
                CommonUtils.setBanner(banner, getList(response.getDataList()), false);
            }
        });
    }

    private List<String> getList(List<AdListBean.AdBean> list) {
        if (StringUtils.isEmpty(list)) {
            return null;
        }
        List<String> stringList = new ArrayList<>();
        for (AdListBean.AdBean bean:list) {
            stringList.add(bean.getPic());
        }
        return stringList;
    }

    private void initQuestionHot() {
        adapter = new QuestionHotAdapter(getContext());//热门问题
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        LiJiaApi api = new LiJiaApi("mall/hotlist");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<QuestionHotListBean.QuestionHotBean>>() {
            @Override
            public void onResponse(NoPageListBean<QuestionHotListBean.QuestionHotBean> response) {
                if (isFinishing()) {
                    return;
                }
                list = response.data;
                notifyData(list);
            }
        });
    }

    private void initQuestionClassify() {
        cRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        cRecyclerView.setNestedScrollingEnabled(false);
        LiJiaApi api = new LiJiaApi("mall/helpclass");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<QuestionClassifyListBean.QuestionClassifyBean>>() {
            @Override
            public void onResponse(NoPageListBean<QuestionClassifyListBean.QuestionClassifyBean> response) {
                if (isFinishing()) {
                    return;
                }
                QuestionClassifyAdapter classifyAdapter = new QuestionClassifyAdapter(getContext());//热门分类
                classifyAdapter.setData(response.data);
                cRecyclerView.setAdapter(classifyAdapter);

            }
        });
    }


    @OnClick(R.id.couple_tv)
    public void onViewClicked() {//意见反馈
        IntentUtil.startActivity(this, QuestionFeedbackActivity.class);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String key = v.getText().toString().trim();
            if (StringUtils.isEmpty(key)) {
                UIHelper.ToastMessage(getContext(), R.string.input_keyword);
                return true;
            }
            loadSearch(key);
            return false;
        }
        return false;
    }

    private void loadSearch(String key) {
        LiJiaApi api = new LiJiaApi("mall/helpsearch");
        api.addParams("searchword", key);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<QuestionHotListBean.QuestionHotBean>>() {
            @Override
            public void onResponse(NoPageListBean<QuestionHotListBean.QuestionHotBean> response) {
                if (isFinishing()) {
                    return;
                }
                notifyData(response.data);
            }
        });
    }

    private void notifyData(List<QuestionHotListBean.QuestionHotBean> list) {
        adapter.setData(list);
        adapter.notifyDataSetChanged();
    }
}
