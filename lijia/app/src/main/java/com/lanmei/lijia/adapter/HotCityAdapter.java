package com.lanmei.lijia.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.HotCityListBean;
import com.lanmei.lijia.utils.AKDialog;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 热门城市
 */
public class HotCityAdapter extends SwipeRefreshAdapter<HotCityListBean.HotCityBean> {


    public HotCityAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_hot_city, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final HotCityListBean.HotCityBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameTv.setText(bean.getName());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AKDialog.getAlertDialog(context, "确认选择" + bean.getName() + "作为自己所在城市？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (l !=  null){
                            l.choose(bean.getName());
                        }
                    }
                });
            }
        });
    }

    ChooseCityListener l;

    public void setChooseCityListener(ChooseCityListener l){
        this.l = l;
    }

    public interface ChooseCityListener{
        void choose(String city);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
