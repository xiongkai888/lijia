package com.lanmei.lijia.ui.home.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.DealNotificationAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.DealNotificationListBean;
import com.lanmei.lijia.bean.OrderInfoBean;
import com.lanmei.lijia.bean.OrderListBean;
import com.lanmei.lijia.event.InformationEvent;
import com.lanmei.lijia.event.OrderReceivingEvent;
import com.lanmei.lijia.helper.DealNotificationContract;
import com.lanmei.lijia.helper.DealNotificationPresenter;
import com.lanmei.lijia.helper.TTSHelper;
import com.lanmei.lijia.ui.MainActivity;
import com.lanmei.lijia.ui.settting.activity.MyOrderActivity;
import com.lanmei.lijia.ui.settting.activity.OrderInfoActivity;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.JsonUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.SwipeRefreshController;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.SmartSwipeRefreshLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 交易通知
 */
public class DealNotificationActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener, DealNotificationContract.View {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;


    @InjectView(R.id.pull_refresh_rv)
    SmartSwipeRefreshLayout smartSwipeRefreshLayout;
    @InjectView(R.id.read_tv)
    TextView readTv;
    @InjectView(R.id.all_pitch_on_tv)
    TextView allPitchOnTv;
    @InjectView(R.id.delete_tv)
    TextView deleteTv;
    @InjectView(R.id.ll_bottom)
    LinearLayout ll_bottom;//底部
    private SwipeRefreshController<DealNotificationListBean> controller;
    DealNotificationAdapter adapter;
    private TTSHelper ttsHelper;

    DealNotificationContract.Presenter presenter;

    @Override
    public int getContentViewId() {
        return R.layout.activity_deal_notification;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {

        ttsHelper = MainActivity.ttsHelper;

        toolbar.setOnMenuItemClickListener(this);
        toolbar.setTitle(R.string.deal_notification);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        presenter = new DealNotificationPresenter(this, this);

        smartSwipeRefreshLayout.initWithLinearLayout();
        LiJiaApi api = new LiJiaApi("app/send_log");
        api.addParams("uid", api.getUserId(this));
        adapter = new DealNotificationAdapter(this, presenter);
        smartSwipeRefreshLayout.setAdapter(adapter);
        controller = new SwipeRefreshController<DealNotificationListBean>(this, smartSwipeRefreshLayout, api, adapter) {
        };
        controller.setIsFirstPageListener(new SwipeRefreshController.IsFirstPageListener() {
            @Override
            public void isFirst() {
                setState();
            }

            @Override
            public void isMore() {
                setState();
            }
        });
        controller.loadFirstPage();
        adapter.setImmediatelyCheckListener(new DealNotificationAdapter.ImmediatelyCheckListener() {
            @Override
            public void seeState(String id) {
                loadSomething(false, id, false);
            }

            @Override
            public void immediatelyCheck(String oid, String id, String see_state) {
                loadOrder(oid, id, see_state);
            }
        });
    }


    private void loadOrder(final String oid, final String id, final String see_state) {
        LiJiaApi api = new LiJiaApi("app/repair_order_details");
        api.addParams("uid", api.getUserId(this));
        api.addParams("id", oid);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                L.d("BaseAppCompatActivity", "订单详情:" + response.data);
                try {
                    OrderListBean.OrderBean bean = JsonUtil.jsonToBean(Des.decode(response.data), OrderListBean.OrderBean.class);
                    if (StringUtils.isEmpty(bean)) {
                        L.d("BaseAppCompatActivity", "订单详情：为空");
                        return;
                    }
                    setReaded(id,see_state);
                    OrderListBean.OrderBean.User5Bean userBean = bean.getUser5();
                    if (!StringUtils.isEmpty(userBean) && StringUtils.isSame(userBean.getId(), CommonUtils.getUid(getContext()))) {//是自己的单子就是进去订单信息
                        IntentUtil.startActivity(getContext(), OrderInfoActivity.class, oid);
                    } else {
                        setMSGBean(bean);
                    }
                    L.d("BaseAppCompatActivity", "订单详情：" + Des.decode(response.data));
                } catch (Exception e) {
                    setReaded(id,see_state);
                    UIHelper.ToastMessage(getContext(),getString(R.string.sorry_order_error));
                    L.d("BaseAppCompatActivity", "订单详情：catch");
                    e.printStackTrace();
                }
            }
        });
    }


    private void setReaded(final String id, final String see_state){
        if (StringUtils.isSame(see_state, CommonUtils.isZero)) {//未读时就设置为已读
            loadSomething(false, id, false);
        }
    }

    private void setMSGBean(OrderListBean.OrderBean bean) {
        OrderInfoBean.MSGBean infoBean = new OrderInfoBean.MSGBean();
        infoBean.setOid(bean.getId());
        infoBean.setMake_an_appointment(bean.getMake_an_appointment());
        infoBean.setTotal_price(bean.getTotal_price());
        infoBean.setContent(bean.getContent());
        infoBean.setAddress(bean.getAddress());
        infoBean.setPlatform_product(bean.getPlatform_product());
        infoBean.setClasstype(bean.getClasstypename());
        setMSGBeanDialog(infoBean);
    }

    AlertDialog alertDialog;

    private void setMSGBeanDialog(OrderInfoBean.MSGBean infoBean) {
        if (StringUtils.isEmpty(infoBean)) {
            return;
        }
        String time = null;
        String make_an_appointment = infoBean.getMake_an_appointment();
        if (StringUtils.isEmpty(make_an_appointment) || StringUtils.isSame(make_an_appointment, CommonUtils.isZero)) {
            time = getString(R.string.arrange_self);
        } else {
            FormatTime formatTime = new FormatTime(make_an_appointment);
            time = formatTime.getReserveTime();
        }
        String speech = String.format(getString(R.string.speech), infoBean.getClasstype(), time, infoBean.getAddress(), infoBean.getContent(), infoBean.getTotal_price());
        if (ttsHelper != null) {
            ttsHelper.initTTSPermission();
            ttsHelper.speak(speech);
        }
        alertDialog = AKDialog.getReceivingOrderDialog(this, infoBean, new AKDialog.OrderReceivingListener() {
            @Override
            public void getOrderId(String id) {
                if (ttsHelper != null) {
                    ttsHelper.stop();
                }
                orderReceiver(id, 1);//接单
                alertDialog = null;
            }

            @Override
            public void abandon(String id) {
                if (ttsHelper != null) {
                    ttsHelper.stop();
                }
                orderReceiver(id, 0);//拒绝接单
                alertDialog = null;
            }

            @Override
            public void cancelDialog() {
                alertDialog = null;
            }
        });

    }


    private void orderReceiver(String id, final int state) {

        LiJiaApi api = new LiJiaApi("app/order_receiving");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("state", state);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                String speak;
                if (state == 1) {
                    speak = getString(R.string.grab_a_single_success);
                    UIHelper.ToastMessage(getContext(), getString(R.string.grab_a_single_success_hint));
                    EventBus.getDefault().post(new OrderReceivingEvent());//通知刷新首页
                    IntentUtil.startActivity(getContext(), MyOrderActivity.class);
                } else {
                    speak = getString(R.string.give_up_the_order);
                }
                if (ttsHelper != null) {
                    ttsHelper.speak(speak);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                String errorStr = error.getMessage();
                if (ttsHelper != null) {
                    ttsHelper.speak(errorStr);
                }
                UIHelper.ToastMessage(getContext(), errorStr);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ttsHelper != null) {//防止在弹框时突然来订单 先停止说话
            ttsHelper.stop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (alertDialog != null) {//防止在弹框时突然来订单 倒计时完成后报错
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    private void setState() {
        if (isFinishing()) {
            return;
        }
        List<DealNotificationListBean.DealNotificationBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)) {
            presenter.setEdit(false);
            showBottom(false);
            toolbar.getMenu().clear();
        } else {
            boolean isEdit = presenter.isEdit();
            if (isEdit) {
                toolbar.inflateMenu(R.menu.menu_done);
                presenter.setList(list);
                showTextView(false);
            }
            toolbar.getMenu().clear();
            toolbar.inflateMenu(isEdit ? R.menu.menu_done : R.menu.menu_edit);
            presenter.setEdit(isEdit);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (StringUtils.isEmpty(adapter.getData())) {
            return true;
        }
        boolean isEdit = false;//是否是编辑状态
        if (item.getItemId() == R.id.action_edit) {
            presenter.setList(adapter.getData());
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_done);
            isEdit = true;
        } else if (item.getItemId() == R.id.action_done) {
            toolbar.getMenu().clear();
            toolbar.inflateMenu(R.menu.menu_edit);
            isEdit = false;
        }
        presenter.setEdit(isEdit);
        adapter.notifyDataSetChanged();
        return true;
    }

    @OnClick({R.id.read_tv, R.id.all_pitch_on_tv, R.id.delete_tv})
    public void onViewClicked(View view) {
        if (!presenter.isEdit()) {
            return;
        }
        switch (view.getId()) {
            case R.id.read_tv:
                String ids = presenter.getIdsBySelectedAndNoRead();
                if (!presenter.isSelect() || StringUtils.isEmpty(ids)) {
                    UIHelper.ToastMessage(this, "请选择要设置为已读的未读通知");
                    break;
                }
                if (presenter.isAllSelect()) {//全部为已读
                    load("确认全部设为已读？", false, "");
                } else {//设置部分为已读
                    load("确认设为已读？", false, ids);
                }
                break;
            case R.id.all_pitch_on_tv:
//                presenter.setList(adapter.getData());//首先要获取最新的数据，数据是分页加载的
                presenter.setAllSelect(!presenter.isAllSelect());
                adapter.notifyDataSetChanged();
                break;
            case R.id.delete_tv:
                if (!presenter.isSelect()) {
                    UIHelper.ToastMessage(this, "请选择要删除的通知");
                    break;
                }
                if (presenter.isAllSelect()) {//删除全部通知
                    load("确认要删除全部通知？", true, presenter.getIdBySelected());
                } else {//删除部分通知
                    load("确认要删除通知？", true, presenter.getIdBySelected());
                }
                break;
        }

    }

    private void load(String content, final boolean isDelete, final String id) {
        AKDialog.getAlertDialog(this, content, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                loadSomething(isDelete, id, true);
            }
        });
    }


    //删除通知
    private void loadSomething(final boolean isDelete, final String id, final boolean isLoading) {
        String apiStr = "";
        if (isDelete) {//删除通知
            apiStr = "app/del_send_log";
        } else {
            if (StringUtils.isEmpty(id)) {
                apiStr = "app/see_state_all";//全部设为已读
            } else {
                apiStr = "app/see_state";//部分设为已读
            }
        }
        LiJiaApi api = new LiJiaApi(apiStr);
        api.addParams("uid", api.getUserId(this));
        if (!StringUtils.isEmpty(id)) {
            api.addParams("id", id);
        }
        HttpClient.newInstance(this).request(isLoading, api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                EventBus.getDefault().post(new InformationEvent());
                if (isLoading) {
                    UIHelper.ToastMessage(getContext(), CommonUtils.getString(response));
                    controller.loadFirstPage();
                } else {
                    setReadedById(id);
                }
            }
        });
    }

    private void setReadedById(String id) {
        List<DealNotificationListBean.DealNotificationBean> list = adapter.getData();
        if (StringUtils.isEmpty(list)) {
            return;
        }
        for (DealNotificationListBean.DealNotificationBean bean : list) {
            if (StringUtils.isSame(id, bean.getId())) {
                bean.setSee_state(CommonUtils.isOne);
                adapter.notifyDataSetChanged();
                return;
            }
        }
    }

    @Override
    public void showTextView(boolean isAllSelect) {
        if (isAllSelect) {
            readTv.setText(R.string.all_read);
            allPitchOnTv.setText(R.string.all_cancel);
            deleteTv.setText(R.string.all_delete);
        } else {
            readTv.setText(R.string.set_read);
            allPitchOnTv.setText(R.string.all_pitch_on);
            deleteTv.setText(R.string.delete);
        }
    }

    @Override
    public void showBottom(boolean isShow) {
        if (isShow) {
            ll_bottom.setVisibility(View.VISIBLE);
        } else {
            ll_bottom.setVisibility(View.GONE);
        }
    }
}
