package com.xson.common.api;

import android.content.Context;

import com.xson.common.bean.UserBean;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UserHelper;

/**
 * @author Milk <249828165@qq.com>
 */
public abstract class ApiV2 extends AbstractApi {

    public String key = "lzgj";//必须明钥

    public String getUserId(Context context) {
        UserBean userBean = UserHelper.getInstance(context).getUserBean();
        return StringUtils.isEmpty(userBean) ? "" : userBean.getId();
    }

}
