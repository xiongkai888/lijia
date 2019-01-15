package com.lanmei.lijia.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.event.LoginEvent;
import com.lanmei.lijia.utils.SmsObserver;
import com.lanmei.lijia.view.VerificationCodeView;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.CodeCountDownTimer;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 输入验证码
 */
public class CodeActivity extends BaseActivity {

    public static final int MSG_RECEIVED_CODE = 1;
//    @InjectView(R.id.verificationCodeInput)
//    VerificationCodeInput verificationCodeInput;
    @InjectView(R.id.send_code_to_tv)
    TextView sendCodeToTv;
    //    @InjectView(R.id.code_et)
//    DrawClickableEditText codeEt;
    @InjectView(R.id.icv)
    VerificationCodeView codeView;
    @InjectView(R.id.anew_send_tv)
    TextView anewSendTv;
    private CodeCountDownTimer mCountDownTimer;//获取验证码倒计时
    String phone;
    private SmsObserver mObserver;

    @Override
    public int getContentViewId() {
        return R.layout.activity_code;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        phone = getIntent().getStringExtra("value");
        sendCodeToTv.setText(String.format(getString(R.string.send_code_to), phone));
        initCountDownTimer();
//        registSmsReciver();
        EventBus.getDefault().register(this);

//        verificationCodeInput.setOnCompleteListener(new VerificationCodeInput.Listener() {
//            @Override
//            public void onComplete(String content) {
//
//            }
//        });

//        mObserver = new SmsObserver(this, mHandler);
//        Uri uri = Uri.parse("content://sms");
//        //注册短信的监听
//        getContentResolver().registerContentObserver(uri, true, mObserver);
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_RECEIVED_CODE) {
                String code = (String) msg.obj;
//                codeEt.setText(code);
            }
        }
    };

    private void initCountDownTimer() {
        mCountDownTimer = new CodeCountDownTimer(this, 60 * 1000, 1000, anewSendTv);
        mCountDownTimer.start();
    }

    @OnClick({R.id.anew_send_tv, R.id.next_bt, R.id.back_iv})
    public void onViewClicked(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.anew_send_tv:
                login();
                break;
            case R.id.next_bt:
                String code = codeView.getInputContent();
                if (StringUtils.isEmpty(code) || code.length() < 6) {
                    UIHelper.ToastMessage(this,code);
                    UIHelper.ToastMessage(this, R.string.input_six_code);
                    return;
                }
                phoneVerify(code);
                break;
            case R.id.back_iv:
                finish();
                break;
        }
    }

    private void phoneVerify(String code) {
        LiJiaApi api = new LiJiaApi("app/pcode");
        api.addParams("phone", phone);
        api.addParams("phone_code", code);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putString("phone", phone);
                bundle.putBoolean("first", true);
                IntentUtil.startActivity(getContext(), LoginPwdActivity.class, bundle);
            }
        });
    }


    private void login() {
        LiJiaApi api = new LiJiaApi("app/login");
        api.addParams("phone", phone);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                mCountDownTimer.start();
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
        //解除注册短信的监听
        if (mObserver != null) {
            getContentResolver().unregisterContentObserver(mObserver);
        }

        EventBus.getDefault().unregister(this);
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        // 取消短信广播注册
//        if (smsReciver != null) {
//            unregisterReceiver(smsReciver);
//            smsReciver = null;
//        }
    }

//    private SmsReciver smsReciver = new SmsReciver();
    /**
     * 收到短信Action
     **/
    String ACTION_SMS_RECIVER = "android.provider.Telephony.SMS_RECEIVED";

    /**
     * 注册广播接受者监听短信验证码自动回写  可在onCreate()中进行注册;
     */
    private void registSmsReciver() {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(ACTION_SMS_RECIVER);
//        // 设置优先级 不然监听不到短信
//        filter.setPriority(1000);
//        registerReceiver(smsReciver, filter);
    }

    /**
     * 短信广播接受者 用户监听短信，自动填写验证码
     */
    private class SmsReciver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                byte[] pdu = (byte[]) obj;
                SmsMessage sms = SmsMessage.createFromPdu(pdu);
                // 短信的内容
                String message = sms.getMessageBody();
                L.d("BeanRequest", "message     " + message);
                // 短息的手机号，如果你们公司发送验证码的号码是固定的这里可以进行一个号码的校验
                String from = sms.getOriginatingAddress();
                L.d("BeanRequest", "from     " + from);
                analysisVerify(message);

            }
        }

    }

    /**
     * 解析短信并且回写  这里解析的是纯数字的短信，如果小伙伴的验证码包含字母的话，可用正则替换
     *
     * @param message
     */
    private void analysisVerify(String message) {
        char[] msgs = message.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < msgs.length; i++) {
            if ('0' <= msgs[i] && msgs[i] <= '9') {
                sb.append(msgs[i]);
            }
        }
//        codeEt.setText(sb.toString());
    }

}
