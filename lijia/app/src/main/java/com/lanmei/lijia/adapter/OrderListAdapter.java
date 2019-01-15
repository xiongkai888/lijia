package com.lanmei.lijia.adapter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.OrderListBean;
import com.lanmei.lijia.event.LocationOrderEvent;
import com.lanmei.lijia.event.OperationOrderEvent;
import com.lanmei.lijia.ui.settting.activity.OrderInfoActivity;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.BaiduLocation;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 订单列表
 */
public class OrderListAdapter extends SwipeRefreshAdapter<OrderListBean.OrderBean> {

    FormatTime time;

    public OrderListAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final OrderListBean.OrderBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, OrderInfoActivity.class, bean.getId());
            }
        });
    }


    double lat1;
    double lon1;

    private void initBaiDu(final String state,final String id) {
        new BaiduLocation(context, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (location != null) {
                    locationClient.stop();
                    lat1 = location.getLatitude();
                    lon1 = location.getLongitude();
                    loadState(state,id);
                }else {
                    initBaiDu(state,id);
                }
            }
        });
    }


    public void loadState(String state,String id){
        LiJiaApi api = new LiJiaApi("app/service_order_edit");
        api.addParams("uid",api.getUserId(context));
        api.addParams("oid",id);
        api.addParams("state",state);
        if (StringUtils.isSame("4",state)){
            api.addParams("lat1",lat1);
            api.addParams("lon1",lon1);
        }
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null){
                    return;
                }
                EventBus.getDefault().post(new OperationOrderEvent());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.user_name_tv)
        TextView userNameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.fix_content_tv)
        TextView fixContentTv;
        @InjectView(R.id.subscribe_time_tv)
        TextView subscribeTimeTv;
        @InjectView(R.id.address_tv)
        TextView addressTv;
        @InjectView(R.id.price_tv)
        TextView priceTv;
        @InjectView(R.id.go_tv)
        TextView goTv;
        @InjectView(R.id.contact_tv)
        TextView contactTv;
        OrderListBean.OrderBean bean;
        String state = "";

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick({R.id.see_map_tv, R.id.go_tv, R.id.contact_tv})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.see_map_tv://查看地图
                    CommonUtils.location(context, bean.getLat(), bean.getLon());
                    break;
                case R.id.go_tv://去上门
                    if (StringUtils.isSame(bean.getTotime(),CommonUtils.isZero)){
                        IntentUtil.startActivity(context, OrderInfoActivity.class, bean.getId());//直接进入修改时间
                        break;
                    }
                    String stateString = "";
                    switch (bean.getState()) {
                        case "2":
                            if (!CommonUtils.isWithinThreeHours(context,bean.getMake_an_appointment())) {
                                return;
                            }
                            stateString = "确定要上门？";
                            state = "3";
                            break;
                        case "3":
                            if (getPermission()){//
                                stateString = "";
                                EventBus.getDefault().post(new LocationOrderEvent());
                                break;
                            }
                            stateString = "确定已到达？";
                            state = "4";
                            break;
                        case "6":
                            stateString = "确定要开始服务？";
                            state = "5";
                            break;
                        case "4"://直接进入订单信息详情里面修改
                        case "5":
                        case "7":
                        case "8":
                        case "9":
                        case "10":
                        case "11":
                            stateString = "";
                            state = "";
                            break;
                    }
                    if (StringUtils.isEmpty(stateString)){
                        IntentUtil.startActivity(context, OrderInfoActivity.class, bean.getId());
                        return;
                    }
                    AKDialog.getAlertDialog(context, stateString, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (StringUtils.isSame("4",state)){
                                initBaiDu(state,bean.getId());
                            }else {
                                loadState(state,bean.getId());
                            }
                        }
                    });
                    break;
                case R.id.contact_tv://联系用户
                    UIHelper.callPhone(context, bean.getPhone());
                    break;
            }
        }

        private boolean getPermission() {//是否需要位置权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }

        public void setParameter(final OrderListBean.OrderBean bean) {

            goTv.setVisibility(View.VISIBLE);

            this.bean = bean;
            OrderListBean.OrderBean.UserBean userBean = bean.getUser();
            if (!StringUtils.isEmpty(userBean)) {
                ImageHelper.load(context, userBean.getPic(), headIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
                userNameTv.setText(userBean.getNickname());
            }
            time.setTime(bean.getAddtime());
            timeTv.setText(time.formatterTimeNoSeconds());
            fixContentTv.setText(bean.getClasstype());
            if (StringUtils.isEmpty(bean.getMake_an_appointment()) || StringUtils.isSame(bean.getMake_an_appointment(), CommonUtils.isZero)) {//
                subscribeTimeTv.setText(R.string.arrange_self);
            }else {
                time.setTime(bean.getMake_an_appointment());
                subscribeTimeTv.setText(time.getTime());
            }
            addressTv.setText(bean.getAddress());
            priceTv.setText(String.format(context.getString(R.string.go_price), bean.getTotal_price()));

            switch (bean.getState()) {
                case "2":
                    goTv.setText(context.getString(R.string.go));
                    if (StringUtils.isSame(bean.getTotime(),CommonUtils.isZero)){
                        goTv.setText(R.string.wait_affirm);
                    }
                    break;
                case "3":
                    goTv.setText(R.string.have_left);
                    break;
                case "4":
                    if (StringUtils.isEmpty(bean.getMaintenance_projects())){
                        goTv.setText(R.string.arrive);
                    }else {
                        goTv.setText(R.string.client_affirm);
                    }
                    break;
                case "5":
                    goTv.setText(R.string.serving);
                    break;
                case "6":
                    goTv.setText(R.string.agree_serve);
                    break;
                case "7":
                    goTv.setText(R.string.serve_done);
                    break;
                case "8":
                    goTv.setText(R.string.pay_succeed);
                    break;
                case "9":
                    goTv.setText(R.string.order_done);
                    break;
                case "10":
                case "11":
                    goTv.setText(R.string.order_cancel);
                    break;
            }
        }
    }

}
