package com.lanmei.lijia.event;

/**
 * Created by xkai on 2018/5/16.
 * 操作订单事件
 */

public class OperationOrderEvent {

    private boolean isDelete;

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDelete() {
        return isDelete;
    }
}
