package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.OrderCancelListBean;
import com.lanmei.lijia.bean.OrderListBean;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 取消订单原因
 */
public class OrderCancelAdapter extends SwipeRefreshAdapter<OrderCancelListBean.OrderCancelBean> {


    public OrderCancelAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_choose_work, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final OrderCancelListBean.OrderCancelBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        if (bean.isChoose()) {
            viewHolder.chooseIv.setImageResource(R.drawable.pay_on);
        } else {
            viewHolder.chooseIv.setImageResource(R.drawable.pay_off);
        }
        viewHolder.nameTv.setText(bean.getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.isChoose()){
                    return;
                }
                int size = getItemCount();
                for (int i = 0; i < size; i++) {
                    OrderCancelListBean.OrderCancelBean cancelBean = getItem(i);
                    cancelBean.setChoose(false);
                }
                bean.setChoose(true);
                notifyDataSetChanged();
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.choose_iv)
        ImageView chooseIv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final OrderListBean bean, final int position) {
        }
    }

}
