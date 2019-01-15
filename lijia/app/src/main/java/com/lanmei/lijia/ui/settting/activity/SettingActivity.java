package com.lanmei.lijia.ui.settting.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.event.LogoutEvent;
import com.lanmei.lijia.event.UpdateEvent;
import com.lanmei.lijia.update.NotificationDownloadCreator;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.CommonUtils;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.xson.common.app.BaseActivity;
import com.xson.common.helper.DataCleanManager;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.lzh.framework.updatepluginlib.UpdateBuilder;

import butterknife.InjectView;
import butterknife.OnClick;
import rx.functions.Action1;


/**
 * 设置
 */

public class SettingActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.cache_count)
    TextView mCleanCacheTv;


    @Override
    public int getContentViewId() {
        return R.layout.activity_setting;
    }


    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.setting);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);

        try {
            mCleanCacheTv.setText(DataCleanManager.getCacheSize(getCacheDir()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @OnClick({R.id.ll_about_us, R.id.ll_tickling, R.id.ll_info_setting,
            R.id.ll_help_info, R.id.ll_clean_cache, R.id.ll_reset_pwd, R.id.ll_versions, R.id.back_login})
    public void showSettingInfo(View view) {//
//        if (!CommonUtils.isLogin(this)) {
//            return;
//        }
        switch (view.getId()) {
            case R.id.ll_about_us://关于我们
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.ll_tickling://留言反馈
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.ll_info_setting://消息设置
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.ll_help_info://帮助信息
                UIHelper.ToastMessage(this, R.string.developing);
                break;
            case R.id.ll_clean_cache://清除缓存
                showClearCache();
                break;
            case R.id.ll_reset_pwd://修改密码
                UIHelper.ToastMessage(this, R.string.developing);
//                RegisterActivity.startActivity(this, RegisterActivity.RESET_PWD_STYLE);
                break;
            case R.id.ll_versions://版本信息
                requestStoragePermission();
                break;
            case R.id.back_login://退出登录
                AKDialog.getAlertDialog(this, "确定要退出登录?", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CommonUtils.loadReceiving(getApplication(), false, false, null);
                        UserHelper.getInstance(getContext()).cleanLogin();
                        EventBus.getDefault().post(new LogoutEvent(1));
//                        JPushInterface.stopPush(LiJiaApp.app);//暂停接收推送的通知。
//                        XGPushManager.unregisterPush(LiJiaApp.app);//暂停接收推送的通知。
                        CommonUtils.unregisterPush();
//                        EventBus.getDefault().post(new StartWebSocketServiceEvent(false));//关闭Socket
                        finish();
                    }
                });
                break;
        }

    }

    /**
     * 请求文件读写权限。
     */
    private void requestStoragePermission() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            //app更新
                            UpdateBuilder.create()
                                    .downloadDialogCreator(new NotificationDownloadCreator())
//                                    .installDialogCreator(new DefaultNeedInstallCreator())
//                                    .strategy(new AllDialogShowStrategy())
                                    .check();
                        }
                    }
                });
    }

    public void showClearCache() {
        try {
            DataCleanManager.cleanInternalCache(getApplicationContext());
            DataCleanManager.cleanExternalCache(getApplicationContext());
            mCleanCacheTv.setText(DataCleanManager.getCacheSize(getCacheDir()));
            UIHelper.ToastMessage(this, "清理完毕");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void updateEvent(UpdateEvent event) {
        UIHelper.ToastMessage(this, event.getContent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
