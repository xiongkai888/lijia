package com.lanmei.lijia.ui.settting.activity;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.WithdrawCardListAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.WithdrawCardListBean;
import com.lanmei.lijia.event.BoundEvent;
import com.lanmei.lijia.event.ChooseKaEvent;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
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
 * 选择银行卡
 */
public class ChooseKaActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener{

//    public static String UNBOUND_SUCCEED = "UNBOUND_SUCCEED";//解绑成功

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;

    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    WithdrawCardListAdapter mAdapter;
    int editType = 0;

    @Override
    public int getContentViewId() {
        return R.layout.activity_choose_ka;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        mToolbar.inflateMenu(R.menu.menu_edit);
        mToolbar.setOnMenuItemClickListener(this);
        mToolbar.setTitle("选择银行卡");
        mToolbar.setNavigationIcon(R.drawable.back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(ChooseKaActivity.this));
        mAdapter = new WithdrawCardListAdapter(ChooseKaActivity.this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setChooseCardListener(new WithdrawCardListAdapter.ChooseCardListener() {
            @Override
            public void chooseCard(WithdrawCardListBean.CardBean bean) {
                EventBus.getDefault().post(new ChooseKaEvent(bean));
                finish();
            }

            @Override
            public void unBound(String cardId) {
                dialogCode(cardId);
            }
        });
        ajaxWithdrawCardList();
    }

    AlertDialog alertDialog;
    private void dialogCode(String cardId) {
        if (alertDialog != null && !alertDialog.isShowing()){
            alertDialog.show();
            return;
        }
        UserBean bean = CommonUtils.getUserBean(this);
        if (bean == null){
            return;
        }
        String phone = bean.getPhone();
        if (StringUtils.isEmpty(phone)){
            UIHelper.ToastMessage(this,"您还没有绑定手机号，请先绑定");
            return;
        }
        alertDialog = AKDialog.getChangePhoneDialog(this,"解绑银行卡验证码将以短息方式发送至您的手机，点击获取验证码按钮后请在60s内输入验证码！", phone,cardId, new AKDialog.ChangePhoneListener() {
            @Override
            public void succeed(String newPhone) {
            }

            @Override
            public void unBound() {
                ajaxWithdrawCardList();
                UIHelper.ToastMessage(getContext(),"银行卡解绑成功");
                EventBus.getDefault().post(new BoundEvent());
                alertDialog.cancel();
            }
        });
        alertDialog.show();
    }

    private void ajaxWithdrawCardList() {
        HttpClient httpClient = HttpClient.newInstance(this);
        LiJiaApi api = new LiJiaApi("app/bank_card");
        api.addParams("uid",api.getUserId(this));
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<WithdrawCardListBean>() {
            @Override
            public void onResponse(WithdrawCardListBean response) {
                List<WithdrawCardListBean.CardBean> list = response.getDataList();
                mAdapter.setData(list);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick(R.id.ll_add_ka)
    public void showAddKa() {//添加(绑定)银行卡
        IntentUtil.startActivity(this,BoundKaActivity.class);
    }

    //绑定银行卡成功或解绑时候调用
    @Subscribe
    public void boundEvent(BoundEvent event){
        ajaxWithdrawCardList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_edit) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_done);
            editType = 1;
        } else if (item.getItemId() == R.id.action_done) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_edit);
            editType = 0;
        }
        mAdapter.setEditStype(editType);
        return true;
    }
}
