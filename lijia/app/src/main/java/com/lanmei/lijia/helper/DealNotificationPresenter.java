package com.lanmei.lijia.helper;

import android.content.Context;

import com.lanmei.lijia.bean.DealNotificationListBean;

import java.util.List;

/**
 * Created by xkai on 2018/6/7.
 */

public class DealNotificationPresenter implements DealNotificationContract.Presenter {

    private Context content;
    private DealNotificationContract.View view;
    private DealNotificationDataHelper dataHelper;
    private boolean isEdit = false;//是否为编辑状态

    @Override
    public boolean isSelect() {
        return dataHelper.isSelect();
    }

    @Override
    public String getIdBySelected() {
        return dataHelper.getIdBySelected();
    }

    @Override
    public boolean isEdit() {
        return isEdit;
    }


    @Override
    public void setList(List<DealNotificationListBean.DealNotificationBean> list) {
        dataHelper.setList(list);
    }

    @Override
    public void setAllSelect(boolean isAllSelect) {
        dataHelper.setAllSelect(isAllSelect);
        view.showTextView(isAllSelect);
    }

    @Override
    public void showAllSelect(boolean isAllSelect) {
        view.showTextView(isAllSelect);
    }

    @Override
    public void setEdit(boolean isEdit) {
        this.isEdit = isEdit;
        view.showBottom(isEdit);//是否显示底部
    }

    @Override
    public String getIdsBySelectedAndNoRead() {
        return dataHelper.getIdsBySelectedAndNoRead();
    }

    public DealNotificationPresenter(Context content, DealNotificationContract.View view) {
        this.content = content;
        this.view = view;
        dataHelper = new DealNotificationDataHelper();
    }

    @Override
    public boolean isAllSelect() {
        return dataHelper.isAllSelect();
    }



}
