package com.lanmei.lijia.ui.login;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.event.LoginEvent;
import com.lanmei.lijia.ui.LiJiaApp;
import com.lanmei.lijia.ui.SplashActivity;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.DeviceUtils;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 输入手机号
 */
public class LoginActivity extends BaseActivity {

    @InjectView(R.id.phone_et)
    DrawClickableEditText phoneEt;
    @InjectView(R.id.agree_cb)
    CheckBox agreeCb;
    @InjectView(R.id.next_bt)
    Button nextBt;

    @Override
    public int getContentViewId() {
        return R.layout.activity_login;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        nextBt.setEnabled(false);
        agreeCb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    nextBt.setEnabled(true);
                    nextBt.setBackgroundResource(R.drawable.button_corners);
                } else {
                    nextBt.setEnabled(false);
                    nextBt.setBackgroundResource(R.drawable.button_unable);
                }
            }
        });
        phoneEt.setText(SharedAccount.getInstance(this).getMobile());
        EventBus.getDefault().register(this);
    }

    @OnClick(R.id.next_bt)
    public void onViewClicked() {
//        IntentUtil.startActivity(getContext(),CodeActivity.class,"15914369252");
        login();
    }

    private void initIMI() {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, SplashActivity.REQUEST_READ_PHONE_STATE);
        } else {
            //TODO
            LiJiaApp.pcode = DeviceUtils.getIMEI(this);
            SharedAccount.getInstance(this).savePcode(LiJiaApp.pcode);
            loadLogin();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case SplashActivity.REQUEST_READ_PHONE_STATE:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    LiJiaApp.pcode = DeviceUtils.getIMEI(this);
                    SharedAccount.getInstance(this).savePcode(LiJiaApp.pcode);
                    loadLogin();
                }
                break;
            default:
                break;
        }
    }

    String phone;

    private void login() {
        phone = CommonUtils.getStringByEditText(phoneEt);
        if (StringUtils.isEmpty(phone)) {
            Toast.makeText(this, R.string.input_phone_number, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            Toast.makeText(this, R.string.not_mobile_format, Toast.LENGTH_SHORT).show();
            return;
        }
        initIMI();
    }

    private void loadLogin() {
        LiJiaApi api = new LiJiaApi("app/login");
        api.addParams("phone", phone);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                int code = response.getCode();
                if (code == 1) {//1已注册用户
                    //跳转到输入密码界面
                    Bundle bundle = new Bundle();
                    bundle.putString("phone", phone);
                    bundle.putBoolean("first", false);
                    IntentUtil.startActivity(getContext(), LoginPwdActivity.class, bundle);
                } else if (code == 11) {//发送验证码成功
                    //跳转到输入验证码界面
                    IntentUtil.startActivity(getContext(), CodeActivity.class, phone);
                    UIHelper.ToastMessage(getContext(), response.getMsg());
                }
            }
        });
    }

    //登录成功调用
    @Subscribe
    public void loginEvent(LoginEvent event) {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
