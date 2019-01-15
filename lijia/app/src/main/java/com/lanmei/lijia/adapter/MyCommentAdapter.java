package com.lanmei.lijia.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.MyCommentListBean;
import com.lanmei.lijia.utils.AKDialog;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.StringUtils;
import com.xson.common.widget.FormatTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 我的评论
 */
public class MyCommentAdapter extends SwipeRefreshAdapter<MyCommentListBean.MyCommentBean> {

    FormatTime time;

    public MyCommentAdapter(Context context) {
        super(context);
        time = new FormatTime();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_my_comment, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, int position) {
        final MyCommentListBean.MyCommentBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.setParameter(bean);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.ratingbar)
        RatingBar ratingbar;
        @InjectView(R.id.name_tv)
        TextView nameTv;
        @InjectView(R.id.time_tv)
        TextView timeTv;
        @InjectView(R.id.content_tv)
        TextView contentTv;
        @InjectView(R.id.reply_ft)
        FormatTextView replyFt;
        @InjectView(R.id.reply_bt)
        Button replyBt;


        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }

        public void setParameter(final MyCommentListBean.MyCommentBean bean) {
            MyCommentListBean.MyCommentBean.UserBean userBean = bean.getUser();
            String client = "";
            if (!StringUtils.isEmpty(userBean)) {
//                ImageHelper.load(context,userBean.getPic(),userHeadIv,null,true,R.drawable.default_pic,R.drawable.default_pic);
                client = userBean.getUsername();
                nameTv.setText(client);
            }
            ratingbar.setRating(Float.parseFloat(bean.getLev()));
            time.setTime(bean.getAddtime());
            timeTv.setText(time.getFormatTime());
            contentTv.setText(bean.getContent());
            final String content2 = bean.getContent2();
            if (StringUtils.isEmpty(content2)) {//师傅评论不为空时
                replyFt.setVisibility(View.GONE);//隐藏师傅回复客户内容
                replyBt.setVisibility(View.VISIBLE);//显示回复按钮
                replyBt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AKDialog.replyDialog(context, new AKDialog.ReplyListener() {
                            @Override
                            public void reply(String content) {
                                if (listener != null){
                                    listener.reply(bean.getId(),content);
                                }
                            }
                        });
                    }
                });
            } else {
                replyFt.setVisibility(View.VISIBLE);//显示师傅回复客户内容
                replyBt.setVisibility(View.GONE);//不显示回复按钮
                String reply = String.format(context.getString(R.string.reply), client, "%s");
                replyFt.setFormatText(reply);
                replyFt.setTextValue(content2);
            }
        }
    }

    ReplyListener listener;

    public void setReplyListener(ReplyListener listener){
        this.listener = listener;
    }

    public interface ReplyListener{
        void reply(String id,String content);
    }

}
