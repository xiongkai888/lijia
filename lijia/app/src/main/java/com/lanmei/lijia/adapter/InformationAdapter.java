package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lanmei.lijia.R;
import com.xson.common.adapter.SwipeRefreshAdapter;

import butterknife.ButterKnife;


/**
 *
 */
public class InformationAdapter extends SwipeRefreshAdapter<String> {

    public int TYPE_BANNER = 100;

    public InformationAdapter(Context context) {
        super(context);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_deal_notification, parent, false));
    }


    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {

    }

    @Override
    public int getItemViewType2(int position) {
        if (position == 0 || position == 1) {
            return TYPE_BANNER;
        }
        return super.getItemViewType2(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

    }


    @Override
    public int getCount() {
        return super.getCount() + 2;
    }


}
