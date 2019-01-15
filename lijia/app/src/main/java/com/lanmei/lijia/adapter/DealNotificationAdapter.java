package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.DealNotificationListBean;
import com.lanmei.lijia.helper.DealNotificationContract;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 交易通知
 */
public class DealNotificationAdapter extends SwipeRefreshAdapter<DealNotificationListBean.DealNotificationBean> {

    FormatTime formatTime;
    DealNotificationContract.Presenter presenter;

    public DealNotificationAdapter(Context context, DealNotificationContract.Presenter presenter) {
        super(context);
        this.presenter = presenter;
        formatTime = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_deal_notification, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final DealNotificationListBean.DealNotificationBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (presenter.isEdit()) {//编辑状态
                    bean.setEdit(!bean.isEdit());
                    presenter.showAllSelect(presenter.isAllSelect());
                    notifyDataSetChanged();

                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.compile_iv)
        ImageView compileIv;
        @InjectView(R.id.read_tv)
        TextView readTv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.immediately_check_tv)
        TextView immediatelyCheckTv;//立即查看

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final DealNotificationListBean.DealNotificationBean bean) {
            titleTv.setText(bean.getTitle());
            contentTv.setText(bean.getContent());
            formatTime.setTime(bean.getAddtime());
            timeTv.setText(formatTime.formatterTimeNoSeconds());
            String read = bean.getSee_state();
            if (StringUtils.isSame(CommonUtils.isZero, read)) {//0|1=>未读|已查看
                readTv.setVisibility(View.VISIBLE);
            } else {
                readTv.setVisibility(View.GONE);
            }
            if (presenter.isEdit()) {
                compileIv.setVisibility(View.VISIBLE);
                if (bean.isEdit()) {
                    compileIv.setImageResource(R.drawable.pay_on);
                } else {
                    compileIv.setImageResource(R.drawable.pay_off);
                }
            } else {
                compileIv.setVisibility(View.GONE);
            }
            immediatelyCheckTv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (StringUtils.isSame(CommonUtils.isOne,bean.getValidity())){
                        UIHelper.ToastMessage(context,context.getString(R.string.order_a_timeout));
                        if (StringUtils.isSame(bean.getSee_state(), CommonUtils.isZero)) {//未读时就设置为已读
                            if (listener != null){
                                listener.seeState(bean.getId());
                            }
                        }
                        return;
                    }
                    if (listener != null){
                        listener.immediatelyCheck(bean.getOid(),bean.getId(),bean.getSee_state());
                    }
                }
            });
        }
    }

    ImmediatelyCheckListener listener;

    public interface ImmediatelyCheckListener{
        /**
         * @param oid 订单id
         * @param id 通知id
         * @param see_state 0未读，1已读
         */
        void immediatelyCheck(String oid,String id,String see_state);
        void seeState(String id);
    }

    public void setImmediatelyCheckListener(ImmediatelyCheckListener listener){
        this.listener = listener;
    }

}
