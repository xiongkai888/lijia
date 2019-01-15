package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.BusinessOpportuntyListBean;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 任务商机列表
 */
public class BusinessOpportunityListAdapter extends SwipeRefreshAdapter<BusinessOpportuntyListBean.BusinessOpportuntyBean> {

    FormatTime time;

    public BusinessOpportunityListAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_order, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final BusinessOpportuntyListBean.BusinessOpportuntyBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
//        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                IntentUtil.startActivity(context, OrderInfoActivity.class, bean.getId());
//            }
//        });
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
        @InjectView(R.id.contact_tv)
        TextView contactTv;
        BusinessOpportuntyListBean.BusinessOpportuntyBean bean;

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
                case R.id.contact_tv://联系用户
                    if (listener != null){
                        listener.orderReceiving(bean.getId());
                    }
                    break;
            }
        }

        public void setParameter(final BusinessOpportuntyListBean.BusinessOpportuntyBean bean) {

            contactTv.setText(R.string.affirm_order);

            this.bean = bean;
            BusinessOpportuntyListBean.BusinessOpportuntyBean.UserBean userBean = bean.getUser();
            if (!StringUtils.isEmpty(userBean)) {
                ImageHelper.load(context, userBean.getPic(), headIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
                userNameTv.setText(userBean.getNickname());
            }
            time.setTime(bean.getAddtime());
            timeTv.setText(time.formatterTimeNoSeconds());
            fixContentTv.setText(bean.getClasstypename());
            if (StringUtils.isEmpty(bean.getMake_an_appointment()) || StringUtils.isSame(bean.getMake_an_appointment(), CommonUtils.isZero)) {//
                subscribeTimeTv.setText(R.string.arrange_self);
            } else {
                time.setTime(bean.getMake_an_appointment());
                subscribeTimeTv.setText(time.getTime());
            }
            addressTv.setText(bean.getAddress());
            priceTv.setText(String.format(context.getString(R.string.go_price), bean.getTotal_price()));

        }

    }


    OrderReceivingListener listener;

    public void setOrderReceivingListener(OrderReceivingListener listener){
        this.listener = listener;
    }

    public interface OrderReceivingListener{
        void orderReceiving(String id);
    }

}
