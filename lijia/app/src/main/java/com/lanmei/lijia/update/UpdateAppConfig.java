package com.lanmei.lijia.update;

import android.content.Context;

import com.lanmei.lijia.api.DevApi;
import com.lanmei.lijia.event.UpdateEvent;
import com.xson.common.utils.L;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.lzh.framework.updatepluginlib.UpdateConfig;
import org.lzh.framework.updatepluginlib.callback.DefaultDownloadCB;
import org.lzh.framework.updatepluginlib.callback.UpdateCheckCB;
import org.lzh.framework.updatepluginlib.creator.DefaultNeedDownloadCreator;
import org.lzh.framework.updatepluginlib.creator.DefaultNeedUpdateCreator;
import org.lzh.framework.updatepluginlib.model.CheckEntity;
import org.lzh.framework.updatepluginlib.model.Update;
import org.lzh.framework.updatepluginlib.model.UpdateParser;
import org.lzh.framework.updatepluginlib.strategy.UpdateStrategy;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by Administrator on 2017/10/19.
 */

public class UpdateAppConfig {

    Context context;

    public UpdateAppConfig(Context context) {
        this.context = context;
    }

    public void initUpdateApp() {
        DevApi api = new DevApi("app/appupdate");
        String url = api.getUrl();
        L.d("updateConfig", url);
        //        url="https://raw.githubusercontent.com/yjfnypeu/UpdatePlugin/master/update.json";
        // UpdateConfig为全局配置。当在其他页面中。使用UpdateBuilder进行检查更新时。
        // 对于没传的参数，会默认使用UpdateConfig中的全局配置
//        ApiBean apiBean = new ApiBean();
//        apiBean.setPcode(LiJiaApp.pcode);
        Map<String, String> params = new HashMap<>();
        Map<String, Object> paramsMap = api.getParams();
        for (Map.Entry<String, Object> item : paramsMap.entrySet()) {
            if (item.getKey() != null && item.getValue() != null) {
                params.put(item.getKey(), (String) item.getValue());
                L.d("updateConfig", "key:" + item.getKey() + ",value:" + (String) item.getValue());
            }
        }
//            params.put("data", Des.encrypt(JsonUtil.beanToJson(apiBean)));
//            L.d("updateConfig", "apiBean:" + JsonUtil.beanToJson(apiBean));
        UpdateConfig.getConfig()
                // 必填：数据更新接口,url与checkEntity两种方式任选一种填写
                //                .url(url)
                .checkEntity(new CheckEntity().setMethod("GET").setUrl(url).setParams(params))
                // 必填：用于从数据更新接口获取的数据response中。解析出Update实例。以便框架内部处理
                .jsonParser(new UpdateParser() {
                    @Override
                    public Update parse(String response) throws JSONException {
                        /* 此处根据上面url或者checkEntity设置的检查更新接口的返回数据response解析出
                         * 一个update对象返回即可。更新启动时框架内部即可根据update对象的数据进行处理
                         */
                        L.d("updateConfig", "response = " + response);
                        JSONObject object = new JSONObject(response);
                        if (object.getInt("code") == 1) {
                            String data = object.getString("data");
                            try {
                                object = new JSONObject(data);
                            } catch (Exception e) {
                                e.printStackTrace();
                                return null;
                            }

                        } else {
                            return null;
                        }
                        UpdateBean update = new UpdateBean();
                        // 此apk包的下载地址
                        L.d("updateConfig", "url = " + object.optString("appurl"));
                        update.setUpdateUrl(object.optString("appurl"));
                        // 此apk包的版本号
                        update.setVersionCode(Integer.parseInt(object.optString("version")));
                        // 此apk包的版本名称
                        update.setVersionName(object.optString("versionName"));
                        // 此apk包的更新内容
                        update.setUpdateContent(object.optString("content"));
                        // 此apk包是否为强制更新
                        update.setForced(false);
                        // 是否显示忽略此次版本更新按钮
                        update.setIgnore(false);
                        return update;

                    }
                })
                // TODO: 2016/5/11 除了以上两个参数为必填。以下的参数均为非必填项。
                // 自定义检查出更新后显示的Dialog，
                .updateDialogCreator(new DefaultNeedUpdateCreator())
                // 自定义下载时的进度条Dialog
                .downloadDialogCreator(new DefaultNeedDownloadCreator())
                // 自定义更新策略，默认WIFI下自动下载更新
                .strategy(new UpdateStrategy() {
                    @Override
                    public boolean isShowUpdateDialog(Update update) {
                        L.d("updateConfig", "isShowUpdateDialog,UpdateUrl = " + update.getUpdateUrl());
                        // 是否在检查到有新版本更新时展示Dialog。
                        return true;
                    }

                    @Override
                    public boolean isAutoInstall() {
                        L.d("updateConfig", "isAutoInstall");
                        // 是否自动更新.当为自动更新时。代表下载成功后不通知用户。直接调起安装。
                        return false;
                    }

                    @Override
                    public boolean isShowDownloadDialog() {
                        L.d("updateConfig", "isShowDownloadDialog");
                        // 在APK下载时。是否显示下载进度的Dialog
                        return true;
                    }
                })
                // 检查更新接口是否有新版本更新的回调。
                .checkCB(new UpdateCheckCB() {
                    @Override
                    public void onCheckStart() {
//                        UIHelper.ToastMessage(context, "开始检查");
                        EventBus.getDefault().post(new UpdateEvent("开始检查"));
                        L.d("updateConfig", "开始检查");
                    }

                    @Override
                    public void hasUpdate(Update update) {
                        L.d("updateConfig", "hasUpdate");
                    }

                    @Override
                    public void noUpdate() {
//                        UIHelper.ToastMessage(context, "当前已是最新版本");
                        EventBus.getDefault().post(new UpdateEvent("当前已是最新版本"));
                        L.d("updateConfig", "当前已是最新版本");
                    }

                    @Override
                    public void onCheckError(Throwable t) {
                        L.d("updateConfig", "onCheckError:" + t.getMessage());
                        //                        UIHelper.ToastMessage(context,""+t.getMessage());
                    }

                    @Override
                    public void onUserCancel() {
                        L.d("updateConfig", "onUserCancel");
                    }

                    @Override
                    public void onCheckIgnore(Update update) {
                        L.d("updateConfig", "更新驳回");
                    }
                })
                // apk下载的回调
                .downloadCB(new DefaultDownloadCB());
    }
}


