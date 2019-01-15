package com.lanmei.lijia.ui.settting.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.MyInformationListBean;
import com.lanmei.lijia.utils.JsonUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.CenterTitleToolbar;

import butterknife.InjectView;

/**
 * 消息详情
 */
public class InformationDetailsActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.title_tv)
    TextView titleTv;
    @InjectView(R.id.time_tv)
    TextView timeTv;
    @InjectView(R.id.content_tv)
    TextView contentTv;

    @Override
    public int getContentViewId() {
        return R.layout.activity_information_details;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("消息详情");
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        loadInformation();
    }

    private void loadInformation() {
        LiJiaApi api = new LiJiaApi("app/send_backstage_see");
        api.addParams("uid",api.getUserId(this));
        api.addParams("id",getIntent().getStringExtra("value"));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()){
                    return;
                }
                try {
                    L.d("BeanRequest", "消息详情：" + Des.decode(response.data));
                    MyInformationListBean.MyInformationBean bean = JsonUtil.jsonToBean(Des.decode(response.data), MyInformationListBean.MyInformationBean.class);
                    if (bean != null) {
                        setDetails(bean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setDetails(MyInformationListBean.MyInformationBean bean) {
        titleTv.setText(bean.getTitle());
        FormatTime time = new FormatTime(bean.getAddtime());
        timeTv.setText(time.formatterTimeNoSeconds());
        contentTv.setText(bean.getContent());
    }

}
