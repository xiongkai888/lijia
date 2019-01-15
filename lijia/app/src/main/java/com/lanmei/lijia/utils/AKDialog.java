package com.lanmei.lijia.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.OrderInfoBean;
import com.lanmei.lijia.view.ChangePhoneView;
import com.xson.common.utils.CodeCountDownTimer;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

/**
 * Dialog工具类
 * Created by benio on 2015/10/11.
 */
public class AKDialog {

    public static AlertDialog.Builder getDialog(Context context) {
        return new AlertDialog.Builder(context);
    }

    public static ProgressDialog getProgressDialog(Context context, String message) {
        ProgressDialog waitDialog = new ProgressDialog(context);
        if (!TextUtils.isEmpty(message)) {
            waitDialog.setMessage(message);
        }
        return waitDialog;
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg) {
        return getMessageDialog(context, null, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg) {
        return getMessageDialog(context, title, msg, null);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String msg, DialogInterface.OnClickListener okListener) {
        return getMessageDialog(context, null, msg, okListener);
    }

    /**
     * 提示信息Dialog
     */
    public static AlertDialog.Builder getMessageDialog(Context context, String title, String msg, DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(R.string.sure, okListener);
        return builder;
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, null, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener) {
        return getConfirmDialog(context, title, msg, okListener, null);
    }

    /**
     * 确认对话框
     */
    public static AlertDialog.Builder getConfirmDialog(Context context, String title, String msg,
                                                       DialogInterface.OnClickListener okListener, DialogInterface.OnClickListener cancelListener) {
        AlertDialog.Builder builder = getDialog(context);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        if (!TextUtils.isEmpty(msg)) {
            builder.setMessage(msg);
        }
        builder.setPositiveButton(R.string.sure, okListener);
        builder.setNegativeButton(R.string.cancel, cancelListener);
        return builder;
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String[] arrays, DialogInterface.OnClickListener onClickListener) {
        return getSelectDialog(context, null, arrays, onClickListener);
    }

    /**
     * 列表对话框
     */
    public static AlertDialog.Builder getSelectDialog(Context context, String title,
                                                      String[] arrays, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setItems(arrays, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        return builder;
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        return getSingleChoiceDialog(context, null, arrays, selectIndex, onClickListener);
    }

    /**
     * 单选对话框
     */
    public static AlertDialog.Builder getSingleChoiceDialog(Context context, String title, String[] arrays,
                                                            int selectIndex, DialogInterface.OnClickListener onClickListener) {
        AlertDialog.Builder builder = getDialog(context);
        builder.setSingleChoiceItems(arrays, selectIndex, onClickListener);
        if (!TextUtils.isEmpty(title)) {
            builder.setTitle(title);
        }
        // builder.setNegativeButton("取消", null);
        return builder;
    }

    public AKDialog() {
    }

    /**
     * 拍照、选择相册底部弹框提示
     *
     * @param context
     * @param activity
     * @param listener
     */
    public static void showBottomListDialog(Context context, Activity activity, final AlbumDialogListener listener) {
        final Dialog dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        View inflate = LayoutInflater.from(context).inflate(R.layout.album_dialog_layout, null);
        Button choosePhoto = (Button) inflate.findViewById(R.id.choosePhoto);
        Button takePhoto = (Button) inflate.findViewById(R.id.takePhoto);
        Button cancel = (Button) inflate.findViewById(R.id.btn_cancel);

        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {//拍照
                    listener.photograph();
                }
                dialog.cancel();
            }
        });
        choosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//相册
                if (listener != null) {
                    listener.photoAlbum();
                }
                dialog.cancel();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//取消
                dialog.cancel();
            }
        });
        dialog.setContentView(inflate);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        WindowManager m = activity.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用
//        lp.y = 20;
        lp.width = (int) (d.getWidth()); // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp);
        dialog.show();
    }

    public interface AlbumDialogListener {

        void photograph();//拍照

        void photoAlbum();//相册
    }


    public static void getAlertDialog(Context context, String content, DialogInterface.OnClickListener l) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                .setMessage(content)
                .setPositiveButton(R.string.sure, l)
                .setNegativeButton(R.string.cancel, null)
                .setCancelable(false).create();
        dialog.show();
    }

    public static AlertDialog getYseNoDialog(Context context, String yesStr, String noStr, String msg, final ConfirmListener confirmListener) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_yes_or_no, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        TextView mMessage = (TextView) view.findViewById(R.id.msg_tv);
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        mMessage.setText(msg);//弹框内容
        yes.setText(yesStr);
        no.setText(noStr);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    dialog.cancel();
                    confirmListener.yes();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (confirmListener != null) {
                    dialog.cancel();
                    confirmListener.no();
                }
            }
        });
//        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        return dialog;
    }

    public static AlertDialog replyDialog(final Context context, final ReplyListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_reply, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        final EditText replyEt = (EditText) view.findViewById(R.id.reply_et);//回复内容
        final TextView numTv = (TextView) view.findViewById(R.id.num_tv);//字数
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        replyEt.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (StringUtils.isEmpty(s)) {
                    numTv.setText(String.format(context.getString(R.string.num_120), "0"));
                } else {
                    numTv.setText(String.format(context.getString(R.string.num_120), s.length() + ""));
                }
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = CommonUtils.getStringByEditText(replyEt);
                if (StringUtils.isEmpty(content)) {
                    UIHelper.ToastMessage(context, R.string.input_reply_content);
                    return;
                }
                if (l != null) {
                    dialog.cancel();
                    l.reply(content);
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.show();
        return dialog;
    }


    public interface ReplyListener {
        void reply(String content);
    }


    public static AlertDialog getReceivingOrderDialog(Context context, final OrderInfoBean.MSGBean bean, final OrderReceivingListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_receiving_order, null);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog dialog = builder.create();
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        TextView productTv = (TextView) view.findViewById(R.id.product_tv);
        TextView addressTv = (TextView) view.findViewById(R.id.address_tv);
        TextView contentTv = (TextView) view.findViewById(R.id.content_tv);
        TextView timeTv = (TextView) view.findViewById(R.id.time_tv);
        TextView priceTv = (TextView) view.findViewById(R.id.price_tv);
        TextView downTv = (TextView) view.findViewById(R.id.down_tv);//倒计时I
        final CodeCountDownTimer mCountDownTimer = new CodeCountDownTimer(context, 60 * 1000, 1000, downTv);
        //获取验证码倒计时
        mCountDownTimer.setDialogState(true);
        mCountDownTimer.setCancelDialogListener(new CodeCountDownTimer.CancelDialogListener() {
            @Override
            public void cancelDialog() {
                dialog.dismiss();
                if (l != null) {
                    l.cancelDialog();
                }
            }
        });
        mCountDownTimer.start();
        productTv.setText(bean.getClasstype());
        addressTv.setText(bean.getAddress());
        contentTv.setText(bean.getContent());
        String time = "";
        String make_an_appointment = bean.getMake_an_appointment();
        if (StringUtils.isEmpty(make_an_appointment) || StringUtils.isSame(make_an_appointment, CommonUtils.isZero)) {
            time = "由师傅自已安排";
        } else {
            FormatTime formatTime = new FormatTime(bean.getMake_an_appointment());
            time = formatTime.formatterTimeNoSeconds();
        }
        timeTv.setText(time);
        priceTv.setText("￥" + bean.getTotal_price());
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.getOrderId(bean.getOid());
                    if (mCountDownTimer != null) {
                        mCountDownTimer.onFinish();
                        mCountDownTimer.cancel();
                    }
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null) {
                    l.abandon(bean.getOid());
                    if (mCountDownTimer != null) {
                        mCountDownTimer.onFinish();
                        mCountDownTimer.cancel();
                    }
                }
            }
        });
        dialog.show();
        return dialog;
    }


    public interface OrderReceivingListener {

        void getOrderId(String id);

        void abandon(String id);

        void cancelDialog();

    }


    public static AlertDialog getIdDialog(final Context context, final DialogAffirmIdListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = View.inflate(context, R.layout.dialog_id, null);
        builder.setView(view);
        builder.setCancelable(true);
        final AlertDialog dialog = builder.create();
        final EditText idET = (EditText) view.findViewById(R.id.input_pwd_et);//输入支付密码
        TextView yes = (TextView) view.findViewById(R.id.yes_tv);
        TextView no = (TextView) view.findViewById(R.id.no_tv);
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = CommonUtils.getStringByEditText(idET);
                if (StringUtils.isEmpty(id)) {
                    UIHelper.ToastMessage(context, R.string.input_des_id);
                    return;
                }
                if (l != null) {
                    dialog.cancel();
                    l.affirm(id);
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        return dialog;
    }

    //订单详情确认监听

    public interface DialogAffirmIdListener {
        void affirm(String id);
    }

    public interface ConfirmListener {

        void yes();

        void no();
    }


    public static AlertDialog getChangePhoneDialog(Context context, String title, String phone, String type, final ChangePhoneListener l) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        ChangePhoneView view = (ChangePhoneView) View.inflate(context, R.layout.dialog_change_phone, null);
        view.setTitle(title);
        view.setPhone(phone);
        view.setType(type);
        view.setChangePhoneListener(new ChangePhoneView.ChangePhoneListener() {
            @Override
            public void succeed(String newPhone) {
                if (l != null) {
                    l.succeed(newPhone);
                }
            }

            @Override
            public void unBound() {
                if (l != null) {
                    l.unBound();
                }
            }
        });
        builder.setView(view);
        builder.setCancelable(true);
        AlertDialog dialog = builder.create();
        return dialog;
    }


    public interface ChangePhoneListener {

        void succeed(String newPhone);//更换手机号

        void unBound();//解绑银行卡
    }

}
