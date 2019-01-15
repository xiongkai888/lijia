package com.lanmei.lijia.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.CodeCountDownTimer;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.DrawClickableEditText;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 忘记密码
 */
public class ForgotPwdActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener{

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.phone_et)
    DrawClickableEditText phoneEt;
    @InjectView(R.id.code_et)
    DrawClickableEditText codeEt;
    @InjectView(R.id.obtainCode_bt)
    Button obtainCodeBt;
    @InjectView(R.id.pwd_et)
    DrawClickableEditText pwdEt;
    @InjectView(R.id.pwd_again_et)
    DrawClickableEditText pwdAgainEt;

    int type;

    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时

    @Override
    public int getContentViewId() {
        return R.layout.activity_forgot_pwd;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            type = bundle.getInt("type");
            phone = bundle.getString("value");
        }

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        //初始化倒计时
        initCountDownTimer();
        if (type == 0){
            toolbar.inflateMenu(R.menu.menu_login);
            toolbar.setOnMenuItemClickListener(this);
            toolbar.setTitle("忘记密码");
        }else if (type == 1){
            toolbar.setTitle("修改密码");
        }
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        phoneEt.setText(phone);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.action_login) {
            finish();
        }
        return true;
    }

    private void initCountDownTimer() {
        mCountDownTimer = new CodeCountDownTimer(this,60 * 1000, 1000,obtainCodeBt);
    }

    private String phone;


    @OnClick({R.id.confirm_bt, R.id.obtainCode_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.confirm_bt://确定
//                UIHelper.ToastMessage(this,R.string.developing);
                confirm();
                break;
            case R.id.obtainCode_bt://获取验证码
//                UIHelper.ToastMessage(this,R.string.developing);
                loadObtainCode();
                break;
        }
    }

    private void confirm() {
        String code = CommonUtils.getStringByEditText(codeEt);//
        if (StringUtils.isEmpty(code) || code.length() < 6) {
            UIHelper.ToastMessage(this, R.string.input_six_code);
            return;
        }
        String pwd = CommonUtils.getStringByEditText(pwdEt);//
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            UIHelper.ToastMessage(this, R.string.input_password_count);
            return;
        }
        String pwdAgain = CommonUtils.getStringByEditText(pwdAgainEt);//
        if (StringUtils.isEmpty(pwdAgain)) {
            UIHelper.ToastMessage(this, R.string.input_password_again);
            return;
        }
        if (!StringUtils.isSame(pwd, pwdAgain)) {
            UIHelper.ToastMessage(this, R.string.password_inconformity);
            return;
        }
        LiJiaApi api = new LiJiaApi("app/forgot_pwd");
        api.addParams("phone", phone);
        api.addParams("password", pwd);
        api.addParams("phone_code", code);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getMsg());
                finish();
            }
        });
    }

    private void loadObtainCode() {
        phone = CommonUtils.getStringByEditText(phoneEt);//电话号码
        if (StringUtils.isEmpty(phone)) {
            UIHelper.ToastMessage(this, R.string.input_phone_number);
            return;
        }
        if (!StringUtils.isMobile(phone)) {
            UIHelper.ToastMessage(this, R.string.not_mobile_format);
            return;
        }
        LiJiaApi api = new LiJiaApi("app/login");
        api.addParams("phone", phone);
        api.addParams("send", "sendcode");
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mCountDownTimer.start();
                UIHelper.ToastMessage(getContext(), response.getMsg());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

}
