package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.HomeListBean;
import com.lanmei.lijia.event.OperationOrderEvent;
import com.lanmei.lijia.ui.settting.activity.AccountDetailsActivity;
import com.lanmei.lijia.ui.settting.activity.MyOrderActivity;
import com.lanmei.lijia.ui.settting.activity.OrderInfoActivity;
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
 *
 */
public class HomeAdapter extends SwipeRefreshAdapter<HomeListBean.HomeBean.RepairBean> {

    final public static int TYPE_BANNER = 100;
    HomeListBean.HomeBean homeBean;

    FormatTime time;

    public HomeAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    public void setHomeBean(HomeListBean.HomeBean homeBean) {
        this.homeBean = homeBean;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        if (viewType == TYPE_BANNER) { // banner
            BannerViewHolder bannerHolder = new BannerViewHolder(LayoutInflater.from(context).inflate(R.layout.head_home, parent, false));
            return bannerHolder;
        }
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_home, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {

        if (getItemViewType(position) == TYPE_BANNER) {
            onBindBannerViewHolder(holder); // banner
            return;
        }
        final HomeListBean.HomeBean.RepairBean bean = getItem(position - 1);
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

    @Override
    public int getCount() {
        return super.getCount() + 1;
    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.user_head_iv)
        CircleImageView userHeadIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.state_tv)
        TextView stateTv;
        @InjectView(R.id.delete_order_tv)
        TextView deleteOrderTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final HomeListBean.HomeBean.RepairBean bean) {
            HomeListBean.HomeBean.RepairBean.UserBean userBean = bean.getUser();
            if (!StringUtils.isEmpty(userBean)) {
                ImageHelper.load(context, userBean.getPic(), userHeadIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
                nameTv.setText(userBean.getNickname());
            }
            boolean appointment = StringUtils.isEmpty(bean.getMake_an_appointment()) || StringUtils.isSame(bean.getMake_an_appointment(), CommonUtils.isZero);
            if (appointment) {//
                timeTv.setText(R.string.arrange_self);
            } else {
                time.setTime(bean.getMake_an_appointment());
                timeTv.setText(time.getTime());
            }
            contentTv.setText("服务项目：" + CommonUtils.getString(bean.getClasstypename()) + "  服务费用：" + bean.getTotal_price() + "元");
            switch (bean.getState()) {
                case "2":
                    if (appointment){
                        stateTv.setText(context.getString(R.string.wait_affirm));
                    }else {
                        stateTv.setText(context.getString(R.string.go));
                    }
                    break;
                case "3":
                    stateTv.setText(R.string.have_left);
                    break;
                case "4":
                    if (StringUtils.isEmpty(bean.getMaintenance_projects())) {
                        stateTv.setText(R.string.arrive);
                    } else {
                        stateTv.setText(R.string.client_affirm);
                    }
                    break;
                case "5":
                    stateTv.setText(R.string.serving);
                    break;
                case "6":
                    stateTv.setText(R.string.agree_serve);
                    break;
                case "7":
                    stateTv.setText(R.string.serve_done);
                    break;
                case "8":
                    stateTv.setText(R.string.pay_succeed);
                    break;
                case "9":
                    stateTv.setText(R.string.order_done);
                    break;
                case "10":
                case "11":
                    stateTv.setText(R.string.order_cancel);
                    break;
            }
//            if (StringUtils.isSame("9", bean.getState())) {
//                deleteOrderTv.setVisibility(View.VISIBLE);
//                deleteOrderTv.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        AKDialog.getAlertDialog(context, "确认要删除该订单？", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                deleteOrder(bean.getId());
//                            }
//                        });
//                    }
//                });
//            } else {
//                deleteOrderTv.setVisibility(View.GONE);
//            }

        }
    }

    private void deleteOrder(String id) {
        LiJiaApi api = new LiJiaApi("app/del_repair_order");
        api.addParams("uid", api.getUserId(context));
        api.addParams("oid", id);//
        HttpClient.newInstance(context).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (time == null) {
                    return;
                }
                UIHelper.ToastMessage(context, R.string.delete_succeed);
                EventBus.getDefault().post(new OperationOrderEvent());
            }
        });
    }


    public void onBindBannerViewHolder(RecyclerView.ViewHolder holder) {
        if (homeBean == null) {
            return;
        }
        BannerViewHolder viewHolder = (BannerViewHolder) holder;
        viewHolder.ordermoneyTv.setText(String.format(context.getString(R.string.yuan),homeBean.getOrdermoney()+""));
        viewHolder.serveTv.setText(String.format(context.getString(R.string.fen),homeBean.getScore()));
        viewHolder.countTv.setText(String.format(context.getString(R.string.dan),homeBean.getCount()+""));
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.serve_tv)
        TextView serveTv;
        @InjectView(R.id.count_tv)
        TextView countTv;
        @InjectView(R.id.ordermoney_tv)
        TextView ordermoneyTv;

        BannerViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        @OnClick({R.id.ll_my_order, R.id.ll_my_account})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.ll_my_order://我的订单
                    IntentUtil.startActivity(context, MyOrderActivity.class);
                    break;
                case R.id.ll_my_account://我的账户
                    IntentUtil.startActivity(context, AccountDetailsActivity.class);
                    break;
            }
        }
    }
}
