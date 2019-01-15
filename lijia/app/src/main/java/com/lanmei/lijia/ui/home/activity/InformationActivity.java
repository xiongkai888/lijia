package com.lanmei.lijia.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.BusinessOpportunityBean;
import com.lanmei.lijia.bean.InformationDataBean;
import com.lanmei.lijia.event.InformationEvent;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 消息
 */
public class InformationActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.head_iv)
    ImageView headIv;
    @InjectView(R.id.num_tv)
    TextView numTv;
    @InjectView(R.id.num3_tv)
    TextView num3Tv;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.content_tv)
    TextView contentTv;
    @InjectView(R.id.head2_iv)
    ImageView head2Iv;
    @InjectView(R.id.num2_tv)
    TextView num2Tv;
    @InjectView(R.id.title2_tv)
    TextView title2Tv;
    @InjectView(R.id.time2_tv)
    TextView time2Tv;
    @InjectView(R.id.content2_tv)
    TextView content2Tv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_information;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.information);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        loadXX(true);
        loadBusiness();
    }

    private void loadXX(boolean isLoading) {
        LiJiaApi api = new LiJiaApi("app/xx");
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).request(isLoading, api, new BeanRequest.SuccessListener<InformationDataBean<String>>() {
            @Override
            public void onResponse(InformationDataBean<String> response) {
                if (isFinishing()){
                    return;
                }
                InformationDataBean.InformationBean bean = response.getData();
                String seecount = response.getSeecount();
                if (StringUtils.isEmpty(seecount) || StringUtils.isSame(CommonUtils.isZero, seecount)) {
                    numTv.setVisibility(View.GONE);
                } else {
                    numTv.setVisibility(View.VISIBLE);
                    numTv.setText(CommonUtils.getNum(seecount));
                }
                if (!StringUtils.isEmpty(bean)) {
                    FormatTime time = new FormatTime(bean.getAddtime());
                    timeTv.setText(time.getAgoDateFomat());
                    contentTv.setText(bean.getContent());
                }
            }
        });
    }

    //商机通知
    private void loadBusiness() {
        LiJiaApi api = new LiJiaApi("app/notification3");
//        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).request(false, api, new BeanRequest.SuccessListener<BusinessOpportunityBean>() {
            @Override
            public void onResponse(BusinessOpportunityBean response) {
                if (isFinishing()){
                    return;
                }
                String count = response.getCount();
                if (StringUtils.isEmpty(count) || StringUtils.isSame(CommonUtils.isZero, count)) {
                    num3Tv.setVisibility(View.GONE);
                } else {
                    num3Tv.setVisibility(View.VISIBLE);
                    num3Tv.setText(CommonUtils.getNum(count));
                }
            }
        });
    }

    @OnClick({R.id.ll_information1, R.id.ll_information2, R.id.ll_information3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_information1://交易通知
                IntentUtil.startActivity(this, DealNotificationActivity.class);
                break;
            case R.id.ll_information2://系统通知
                IntentUtil.startActivity(this, MyInformationActivity.class);
                break;
            case R.id.ll_information3://商机通知
                IntentUtil.startActivity(this, BusinessOpportunityActivity.class);
                break;
        }
    }

    @Subscribe
    public void informationEvent(InformationEvent event) {
        loadXX(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
