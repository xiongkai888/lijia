package com.xson.common.utils;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import com.xson.common.R;

/**
 * 倒计时
 * Created by xkai on 2017/5/4.
 */

public class CodeCountDownTimer extends CountDownTimer {

    TextView anewSendTv;
    Context context;
    boolean isDialog = false;

    public CodeCountDownTimer(Context context, long millisInFuture, long countDownInterval, TextView anewSendTv) {
        super(millisInFuture, countDownInterval);
        this.anewSendTv = anewSendTv;
        this.context = context;
    }


    @Override
    public void onTick(long l) {
        if (isDialog) {
            anewSendTv.setText(l / 1000 + "");
            return;
        }
        if (anewSendTv != null) {
            anewSendTv.setText(l / 1000 + context.getString(R.string.s_regain));
            anewSendTv.setClickable(false);
            anewSendTv.setTextSize(11);
        }
    }

    @Override
    public void onFinish() {
        if (isDialog) {
            if (l != null){
                l.cancelDialog();
            }
            return;
        }
        if (anewSendTv != null) {
            anewSendTv.setText(context.getString(R.string.obtain_code));
            anewSendTv.setClickable(true);
            anewSendTv.setTextSize(14);
        }
    }

    //设置是不是订单倒计时（默认不是）
    public void setDialogState(boolean isDialog) {
        this.isDialog = isDialog;
    }

    CancelDialogListener l;
    public void setCancelDialogListener(CancelDialogListener l){
        this.l = l;
    }

    public interface CancelDialogListener {
        void cancelDialog();
    }

}
