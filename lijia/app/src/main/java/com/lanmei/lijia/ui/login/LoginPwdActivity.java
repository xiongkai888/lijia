package com.lanmei.lijia.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.event.LoginEvent;
import com.lanmei.lijia.ui.MainActivity;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.JsonUtil;
import com.lanmei.lijia.utils.SharedAccount;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.UserHelper;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.DrawClickableEditText;

import org.greenrobot.eventbus.EventBus;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 输入登录密码
 */
public class LoginPwdActivity extends BaseActivity {


    @InjectView(R.id.pwd_et)
    DrawClickableEditText pwdEt;
    @InjectView(R.id.pwd_again_et)
    DrawClickableEditText pwdAgainEt;
    @InjectView(R.id.ll_pwd_again)
    LinearLayout llPwdAgain;
    @InjectView(R.id.forgot_pwd_tv)
    TextView forgotPwdTv;

    private String phone;
    private boolean isFirst;//是不是获取验证码进入的

    @Override
    public int getContentViewId() {
        return R.layout.activity_login_pwd;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        if (isFirst) {
            forgotPwdTv.setVisibility(View.GONE);
        } else {
            llPwdAgain.setVisibility(View.GONE);
        }
//        pwdEt.setText("123456");
    }

    @OnClick(R.id.next_bt)
    public void onViewClicked() {

    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        phone = bundle.getString("phone");
        isFirst = bundle.getBoolean("first");

    }

    @OnClick({R.id.next_bt, R.id.back_iv, R.id.forgot_pwd_tv})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.next_bt://下一步
                login();
                break;
            case R.id.back_iv:
                finish();
                break;
            case R.id.forgot_pwd_tv://忘记密码
                Bundle bundle = new Bundle();
                bundle.putString("value", phone);
                bundle.putInt("type", 0);
                IntentUtil.startActivity(this, ForgotPwdActivity.class, bundle);
                break;
        }
    }

    private void login() {
        final String pwd = CommonUtils.getStringByEditText(pwdEt);
        if (StringUtils.isEmpty(pwd) || pwd.length() < 6) {
            Toast.makeText(this, R.string.input_password_count, Toast.LENGTH_SHORT).show();
            return;
        }
        String type;
        if (isFirst) {
            String pwdAgain = CommonUtils.getStringByEditText(pwdAgainEt);
            if (StringUtils.isEmpty(pwdAgain) || pwdAgain.length() < 6) {
                Toast.makeText(this, R.string.input_password_again, Toast.LENGTH_SHORT).show();
                return;
            }
            if (!StringUtils.isSame(pwd, pwdAgain)) {
                Toast.makeText(this, R.string.password_inconformity, Toast.LENGTH_SHORT).show();
                return;
            }
            type = "app/registered";//注册
        } else {
            type = "app/enter";//登录
        }
        LiJiaApi api = new LiJiaApi(type);
        api.addParams("password", pwd);
        api.addParams("phone", phone);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String>>() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                if (isFirst) {//注册后重新回到登录界面
                    IntentUtil.startActivity(getContext(), LoginActivity.class);
                } else {
                    UserBean bean;
                    try {
                        String data = Des.decode(response.data);
                        bean = JsonUtil.jsonToBean(data, UserBean.class);
                        saveUser(bean);
                        UserHelper.getInstance(getContext()).savePwd(pwd);
                    } catch (Exception e) {
                        try {
                            bean = JsonUtil.jsonToBean(response.data, UserBean.class);
                            saveUser(bean);
                            UserHelper.getInstance(getContext()).savePwd(pwd);
                        } catch (InstantiationException e1) {
                            e1.printStackTrace();
                        } catch (IllegalAccessException e1) {
                            e1.printStackTrace();
                        }
                        e.printStackTrace();
                    }
                    UIHelper.ToastMessage(getContext(), response.getMsg());
                    EventBus.getDefault().post(new LoginEvent());
                }
                SharedAccount.getInstance(getContext()).saveMobile(phone);
                finish();
            }
        });
    }

    private void saveUser(UserBean bean) {
        if (bean != null) {
            UserHelper.getInstance(getContext()).saveBean(bean);
            UserHelper.getInstance(getContext()).savePhone(phone);
            IntentUtil.startActivity(getContext(), MainActivity.class);

            CommonUtils.initXINGE();

        }
    }

}
