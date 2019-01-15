package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.MyInformationListBean;
import com.lanmei.lijia.ui.settting.activity.InformationDetailsActivity;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.widget.CircleImageView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的消息
 */
public class MyInformationAdapter extends SwipeRefreshAdapter<MyInformationListBean.MyInformationBean> {


    FormatTime time;

    public MyInformationAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_information, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final MyInformationListBean.MyInformationBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentUtil.startActivity(context, InformationDetailsActivity.class,bean.getId());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        @InjectView(R.id.head_iv)
        CircleImageView headIv;
        @InjectView(R.id.title_tv)
        TextView titleTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(MyInformationListBean.MyInformationBean bean){
            titleTv.setText(bean.getTitle());
            time.setTime(bean.getAddtime());
            timeTv.setText(time.formatterTimeNoSeconds());
        }
    }

}
