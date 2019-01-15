package com.lanmei.lijia.ui.home.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.QuestionFeedbackAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.QuestionFeedbackListBean;
import com.lanmei.lijia.helper.BGASortableNinePhotoHelper;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.CompressPhotoUtils;
import com.lanmei.lijia.utils.SimpleTextWatcher;
import com.lanmei.lijia.view.ScrollEditText;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 意见反馈
 */
public class QuestionFeedbackActivity extends BaseActivity implements BGASortableNinePhotoLayout.Delegate {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.content_et)
    ScrollEditText contentEt;
    @InjectView(R.id.num_tv)
    TextView numTv;//字个数
    @InjectView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;//拖拽排序九宫格控件
    BGASortableNinePhotoHelper mPhotoHelper;
    QuestionFeedbackAdapter adapter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_question_feedback;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.question_feedback);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        initFeedback();
        initPhotoHelper();
        contentEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s)) {
                    numTv.setText(String.format(getString(R.string.num_300), "0"));
                } else {
                    numTv.setText(String.format(getString(R.string.num_300), s.length() + ""));
                }
            }
        });
    }

    private void initFeedback() {
        adapter = new QuestionFeedbackAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setNestedScrollingEnabled(false);
        LiJiaApi api = new LiJiaApi("mall/feedbackclass");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<NoPageListBean<QuestionFeedbackListBean.QuestionFeedbackBean>>() {
            @Override
            public void onResponse(NoPageListBean<QuestionFeedbackListBean.QuestionFeedbackBean> response) {
                if (isFinishing()) {
                    return;
                }
                adapter.setData(response.data);
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void initPhotoHelper() {
        mPhotoHelper = new BGASortableNinePhotoHelper(this, mPhotosSnpl);
        // 设置拖拽排序控件的代理
        mPhotoHelper.setDelegate(this);
    }

    @OnClick(R.id.submit_bt)
    public void onViewClicked() {
        loadFeedback();
    }

    private void loadFeedback() {
        if (StringUtils.isEmpty(adapter)) {
            return;
        }
        final String id = getId();
        if (StringUtils.isEmpty(id)) {
            UIHelper.ToastMessage(this, "请选择反馈类型");
            return;
        }
        final String content = CommonUtils.getStringByEditText(contentEt);
        if (StringUtils.isEmpty(content) || content.length() < 11) {
            UIHelper.ToastMessage(this, "请输入反馈内容,10个字以上");
            return;
        }
        if (StringUtils.isEmpty(mPhotoHelper.getData())) {
            submitFeedback(id, content, null);
        } else {
            new CompressPhotoUtils().CompressPhoto(this, mPhotoHelper.getData(), new CompressPhotoUtils.CompressCallBack() {
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    L.d("CompressPhotoUtils", "submitFeedback");
                    submitFeedback(id, content, list);
                }
            }, "6");
        }
    }

    private void submitFeedback(String id, String content, List<String> list) {
        LiJiaApi api = new LiJiaApi("mall/feedback");
        api.addParams("uid", api.getUserId(this));
        api.addParams("fid", id);
        api.addParams("content", content);
        if (!StringUtils.isEmpty(list)) {
            api.addParams("pic", CommonUtils.getPics(list));
        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                L.d("CompressPhotoUtils", "反馈成功");
                UIHelper.ToastMessage(getContext(), "反馈成功");
                finish();
            }
        });
    }

    private String getId() {
        List<QuestionFeedbackListBean.QuestionFeedbackBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)) {
            return "";
        }
        for (QuestionFeedbackListBean.QuestionFeedbackBean bean : list) {
            if (bean.isChoose()) {
                return bean.getId();
            }
        }
        return "";
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (mPhotoHelper != null) {
            mPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (mPhotoHelper != null) {
            mPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (mPhotoHelper != null) {
            mPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPhotoHelper != null) {
            mPhotoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }
}
