package com.lanmei.lijia.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.lanmei.lijia.ui.home.fragment.BusinessOpportunityListFragment;
import com.xson.common.utils.FormatTime;

import java.util.List;


/**
 * 任务商机
 */
public class BusinessOpportunityAdapter extends FragmentPagerAdapter {

    List<Long> list;
    FormatTime formatTime;

    public BusinessOpportunityAdapter(FragmentManager fm) {
        super(fm);
        formatTime = new FormatTime(System.currentTimeMillis()/1000);
        this.list =  formatTime.getList();
    }

    @Override
    public Fragment getItem(int position) {
        BusinessOpportunityListFragment fragment = new BusinessOpportunityListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("dtime", String.valueOf(list.get(position)/1000));//新订单
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getO(position);
    }

    public String getO(int position) {
        if (position == 0){
            return "今天";
        }else if (position == 1){
            return "明天";
        }else if (position == 2){
            return "后天";
        }else {
            return formatTime.getDate(list.get(position));
        }
    }

}
