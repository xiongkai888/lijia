package com.lanmei.lijia.api;

/**
 * Created by xkai on 2018/6/1.
 */

public class DevApi extends LiJiaApi{

    public DevApi(String path){
        super(path);
    }

    @Override
    public Method requestMethod() {
        return Method.GET;
    }
}
