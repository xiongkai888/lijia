package com.lanmei.lijia.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.lijia.ui.settting.fragment.OrderListFragment;
import com.xson.common.utils.StringUtils;

import java.util.List;


/**
 * 我的订单
 */
public class MyOrderAdapter extends FragmentPagerAdapter {

    List<String> list;

    public MyOrderAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle bundle = new Bundle();
        switch (position) {//0待支付|1待接单|2待处理|3出发中|4已到达|5服务中|6服务完成|7确认完成|8待评价|9 全部|10取消
            case 0:
                bundle.putString("status","2");//新订单
                break;
            case 1:
                bundle.putString("status","4");//待服务
                break;
            case 2:
                bundle.putString("status","5");//服务中
                break;
            case 3:
                bundle.putString("status","9");//已完成
                break;
            case 4:
                bundle.putString("status","99");//全部
                break;

        }
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "新订单\n"+getO(position);
            case 1:
                return "待服务\n"+getO(position);
            case 2:
                return "服务中\n"+getO(position);
            case 3:
                return "已完成\n"+getO(position);
            case 4:
                return "全部\n"+getO(position);
        }
        return "";
    }

    public String getO(int position){
        if (StringUtils.isEmpty(list)){
            return "0";
        }
        return list.get(position);
    }

    public void setCount(List<String> list){
        this.list = list;
        notifyDataSetChanged();
    }

}
