package com.lanmei.lijia.event;

import com.lanmei.lijia.bean.WithdrawCardListBean;

/**
 * Created by xkai on 2018/5/3.
 * 选择卡号事件
 */

public class ChooseKaEvent {

    public WithdrawCardListBean.CardBean bean;

    public WithdrawCardListBean.CardBean getBean() {
        return bean;
    }

    public ChooseKaEvent(WithdrawCardListBean.CardBean bean){
        this.bean = bean;
    }
}
