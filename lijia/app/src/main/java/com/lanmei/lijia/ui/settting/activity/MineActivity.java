package com.lanmei.lijia.ui.settting.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.event.LogoutEvent;
import com.lanmei.lijia.event.SetUserInfoEvent;
import com.lanmei.lijia.ui.home.activity.OrderReceivingSetActivity;
import com.lanmei.lijia.ui.league.MasterLeagueActivity;
import com.lanmei.lijia.ui.login.ForgotPwdActivity;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 我的
 */
public class MineActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.name_tv)
    TextView nameTv;
    int type;

    @Override
    public int getContentViewId() {
        return R.layout.activity_mine;
    }

    //更新用户信息的时候调用
    @Subscribe
    public void setUserInfoEvent(SetUserInfoEvent event) {
        initData();
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.mine);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        type = getIntent().getIntExtra("type", -1);
        initData();

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void initData() {
        UserBean bean = CommonUtils.getUserBean(this);
        if (bean == null) {
            return;
        }
        nameTv.setText(bean.getNickname());
        ImageHelper.load(this, bean.getPic(), headIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
    }

    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void logoutEvent(LogoutEvent event) {
        int type = event.getType();
        if (type == 1) {
            finish();
        }
    }

    @OnClick({R.id.head_iv, R.id.data_tv, R.id.account_tv, R.id.order_tv, R.id.order_setting_tv, R.id.store_tv, R.id.comment_tv, R.id.see_equipment_tv, R.id.pwd_tv, R.id.setting_tv})
    public void onViewClicked(View view) {
        if (type == 0 && view.getId() != R.id.setting_tv){
            UIHelper.ToastMessage(this,"请先注册");
            IntentUtil.startActivity(this, MasterLeagueActivity.class);
            return;
        }else if (type == 2 && view.getId() != R.id.setting_tv){
            UIHelper.ToastMessage(this,R.string.data_auditing);
            return;
        }
        switch (view.getId()) {
            case R.id.head_iv:
//                showIconModeDialog();
                IntentUtil.startActivity(this, PersonalDataSubActivity.class);
                break;
            case R.id.data_tv://我的资料
                IntentUtil.startActivity(this, PersonalDataSubActivity.class);
                break;
            case R.id.account_tv://我的账号
                IntentUtil.startActivity(this, AccountDetailsActivity.class);
                break;
            case R.id.order_tv://我的订单
                IntentUtil.startActivity(this, MyOrderActivity.class);
                break;
            case R.id.order_setting_tv://接单设置
                IntentUtil.startActivity(this, OrderReceivingSetActivity.class);
                break;
            case R.id.store_tv://我的店铺
                IntentUtil.startActivity(this, MerchantDataActivity.class);
                break;
            case R.id.comment_tv://我的评论
                IntentUtil.startActivity(this, MyCommentActivity.class);
                break;
            case R.id.see_equipment_tv://查看设备
                AKDialog.getIdDialog(this, new AKDialog.DialogAffirmIdListener() {
                    @Override
                    public void affirm(String id) {
                        IntentUtil.startActivity(getContext(), Activity_dev_params.class, id);
                    }
                });
                break;
            case R.id.pwd_tv://密码修改
                Bundle bundle = new Bundle();
                bundle.putString("value", SharedAccount.getInstance(this).getMobile());
                bundle.putInt("type", 1);
                IntentUtil.startActivity(this, ForgotPwdActivity.class, bundle);
                break;
            case R.id.setting_tv://我的设置
                IntentUtil.startActivity(this, SettingActivity.class);
                break;
        }
    }

}
