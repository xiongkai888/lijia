package com.lanmei.lijia.event;

import java.util.List;

/**
 * Created by xkai on 2018/5/18.
 */

public class OrderCountEvent {
    public List<String> list;

    public List<String> getList() {
        return list;
    }
    public OrderCountEvent(List<String> list){
        this.list = list;
    }
}
