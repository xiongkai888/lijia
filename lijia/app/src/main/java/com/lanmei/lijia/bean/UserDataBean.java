package com.lanmei.lijia.bean;

import com.lanmei.lijia.utils.JsonUtil;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

/**
 * Created by xkai on 2018/6/7.
 */

public class UserDataBean<T> extends DataBean<T>{


    public UserBean getData(){
        try {
            UserBean  bean = JsonUtil.jsonToBean(Des.decode((String) data), UserBean.class);
            L.d("BeanRequest", "用户信息getData：" + Des.decode((String) data));
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
