package com.lanmei.lijia.ui.settting.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.WithdrawCardListBean;
import com.lanmei.lijia.event.BoundEvent;
import com.lanmei.lijia.event.ChooseKaEvent;
import com.lanmei.lijia.event.DepositEvent;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 提现
 */
public class DepositActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.card_tv)
    TextView cardTv;//银行卡名
    @InjectView(R.id.tail_number_tv)
    TextView tailNumberTv;//尾号
    @InjectView(R.id.withdraw_money_et)
    EditText withdrawMoneyEt;//提现金额
    WithdrawCardListBean.CardBean bean;//卡号信息
    private String mMoney = "";//可提取金额
    @InjectView(R.id.money_tv)
    TextView mMoneyTv;//可用余额

    @Override
    public int getContentViewId() {
        return R.layout.activity_deposit;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.deposit);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        EventBus.getDefault().register(this);
        ajaxWithdraw();
        setMoney(CommonUtils.getUserBean(this).getBalance());
        CommonUtils.loadUserInfo(getApplication(), new CommonUtils.UserInfoListener() {
            @Override
            public void error(String error) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(getContext(),error);
            }

            @Override
            public void userInfo(UserBean bean) {
                if (isFinishing()){
                   return;
                }
                userBean = bean;
                setMoney(userBean.getBalance());
            }
        });
    }

    UserBean userBean;

    private void setMoney(String money){
        mMoney = money;
        mMoneyTv.setText("可用余额" + (StringUtils.isEmpty(mMoney)?0:mMoney) + "元");
    }

    private void ajaxWithdraw() {
        HttpClient httpClient = HttpClient.newInstance(this);
        LiJiaApi api = new LiJiaApi("app/bank_card");
        api.addParams("uid", api.getUserId(this));
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<WithdrawCardListBean>() {
            @Override
            public void onResponse(WithdrawCardListBean response) {
                if (isFinishing()) {
                    return;
                }
                List<WithdrawCardListBean.CardBean> list = response.getDataList();
                if (StringUtils.isEmpty(list)) {
                    alertDialog();
                    setAccount(null);
                } else {
                    setAccount(list.get(0));
                }
            }
        });
    }

    private void setAccount(WithdrawCardListBean.CardBean cardBean) {
        bean = cardBean;
        if (bean == null){
            cardTv.setText("添加银行卡");
            tailNumberTv.setVisibility(View.GONE);
            return;
        }
        tailNumberTv.setVisibility(View.VISIBLE);
        cardTv.setText(cardBean.getBanks_name());
        String tail = cardBean.getBanks_no();
        if (!StringUtils.isEmpty(tail) && tail.length() > 4) {
            tailNumberTv.setText("尾号"+tail.substring(tail.length()-4,tail.length())+"储蓄卡");
        }
    }

    private void alertDialog() {
        AKDialog.getAlertDialog(this, getString(R.string.no_bound_card), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                IntentUtil.startActivity(getContext(),BoundKaActivity.class);
            }
        });
    }

    @OnClick({R.id.ll_add_wd_account, R.id.withdraw_bt,R.id.all_withdraw_tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_wd_account://添加银行卡
                IntentUtil.startActivity(this,ChooseKaActivity.class);
                break;
            case R.id.withdraw_bt://提现
                deposit();
                break;
            case R.id.all_withdraw_tv://全部提现
                if (StringUtils.isEmpty(mMoney)){
                    return;
                }
                withdrawMoneyEt.setText(mMoney);
                break;
        }
    }

    private void deposit() {
        if (bean == null){
            UIHelper.ToastMessage(this,"请选择银行号");
            return;
        }
        String money = CommonUtils.getStringByEditText(withdrawMoneyEt);
        if (StringUtils.isEmpty(money) || DoubleUtil.formatFloatNumber(money) == 0){
            UIHelper.ToastMessage(this,"请输入提现金额");
            return;
        }
        LiJiaApi api = new LiJiaApi("app/withdraw");
        api.addParams("uid",api.getUserId(this));
        api.addParams("banks_id",bean.getId());
        api.addParams("money",money);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()){
                    return;
                }
                UIHelper.ToastMessage(getContext(),response.getMsg());
                CommonUtils.loadUserInfo(getApplication(),null);
                EventBus.getDefault().post(new DepositEvent());//提现成功
                finish();
            }
        });
    }

    //绑定银行卡成功或解绑时候调用
    @Subscribe
    public void boundEvent(BoundEvent event){
        setAccount(null);
        ajaxWithdraw();
    }
    //选择银行卡时候调用
    @Subscribe
    public void chooseKaEvent(ChooseKaEvent event){
        setAccount(event.getBean());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
