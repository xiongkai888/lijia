package com.lanmei.lijia.api;

import com.lanmei.lijia.ui.LiJiaApp;
import com.xson.common.api.ApiV2;

/**
 * Created by xkai on 2018/1/8.
 */

public class LiJiaApi extends ApiV2 {

    private String path;
    private String version = LiJiaApp.versionName;//
    private String appid = LiJiaApp.appid;//
    private String pcode = LiJiaApp.pcode;//

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public String getPcode() {
        return pcode;
    }

    public LiJiaApi(String path){
        this.path = path;
    }

    @Override
    protected String getPath() {
        return path;
    }
}
