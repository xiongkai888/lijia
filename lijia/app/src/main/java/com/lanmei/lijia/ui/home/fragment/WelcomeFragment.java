package com.lanmei.lijia.ui.home.fragment;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.WelcomeBean;
import com.lanmei.lijia.ui.home.activity.HelperCenterActivity;
import com.lanmei.lijia.ui.league.MasterLeagueActivity;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.webviewpage.WebViewPhotoBrowserUtil;
import com.xson.common.app.BaseFragment;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2018/4/13.
 * 欢迎加入
 */

public class WelcomeFragment extends BaseFragment {

    @InjectView(R.id.register_bt)
    Button button;
    boolean isAuditing;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.webView)
    WebView webView;

    public static WelcomeFragment newInstance() {
        Bundle args = new Bundle();
        WelcomeFragment fragment = new WelcomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getContentViewId() {
        return R.layout.fragment_welcome;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        isAuditing = StringUtils.isSame(CommonUtils.getUserBean(context).getState(), CommonUtils.isZero);//0未注册
        if (!isAuditing) {
            button.setText(R.string.data_auditing);
        }
        LiJiaApi api = new LiJiaApi("mall/shifuzm");
        HttpClient.newInstance(context).request(api, new BeanRequest.SuccessListener<DataBean<WelcomeBean>>() {
            @Override
            public void onResponse(DataBean<WelcomeBean> response) {
                WelcomeBean bean = response.data;
                if (StringUtils.isEmpty(bean)){
                    return;
                }
                titleTv.setText(bean.getTitle());
                WebViewPhotoBrowserUtil.photoBrowser(getContext(),webView,bean.getContent());
            }
        });
    }

    @OnClick({R.id.ll_questions,R.id.register_bt})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id){
            case R.id.register_bt:
                if (!isAuditing) {
                    UIHelper.ToastMessage(context, R.string.data_auditing);
                } else {
                    IntentUtil.startActivity(context, MasterLeagueActivity.class);
                }
                break;
            case R.id.ll_questions:
                IntentUtil.startActivity(context, HelperCenterActivity.class);
                break;
        }
    }

}
