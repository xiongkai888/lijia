package com.lanmei.lijia.helper;

import com.lanmei.lijia.bean.DealNotificationListBean;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.utils.StringUtils;

import java.util.List;

/**
 * Created by xkai on 2018/6/7.
 */

public class DealNotificationDataHelper {

    private String d = ",";

    public DealNotificationDataHelper() {

    }

    public boolean isAllSelect() {//是否全部选中
        if (isNull()) {
            return false;
        }
        for (DealNotificationListBean.DealNotificationBean bean : list) {
            if (!bean.isEdit()) {
                return false;
            }
        }
        return true;
    }

    private List<DealNotificationListBean.DealNotificationBean> list;

    public void setList(List<DealNotificationListBean.DealNotificationBean> list) {
        this.list = list;
    }

    private boolean isNull() {
        return StringUtils.isEmpty(list);
    }


    //被选中的通知ids 用户删除通知
    public String getIdBySelected() {
        if (isNull()){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (DealNotificationListBean.DealNotificationBean bean : list) {
            if (bean.isEdit()) {
                buffer.append(bean.getId() + d);
            }
        }
        return CommonUtils.getSubString(buffer.toString());
    }

    //用于设置为已读
    public String getIdsBySelectedAndNoRead() {
        if (isNull()){
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        for (DealNotificationListBean.DealNotificationBean bean : list) {
            if (bean.isEdit() && StringUtils.isSame(bean.getSee_state(), CommonUtils.isZero)) {
                buffer.append(bean.getId() + d);
            }
        }
        return CommonUtils.getSubString(buffer.toString());
    }


    //设置全选还是全不选
    public void setAllSelect(boolean isAllSelect) {
        if (isNull()){
            return;
        }
        for (DealNotificationListBean.DealNotificationBean bean : list) {
            bean.setEdit(isAllSelect);
        }
    }

    //是否交易通知被选中
    public boolean isSelect() {
        if (isNull()){
            return false;
        }
        for (DealNotificationListBean.DealNotificationBean bean : list) {
            if (bean.isEdit()) {
                return true;
            }
        }
        return false;
    }

}
