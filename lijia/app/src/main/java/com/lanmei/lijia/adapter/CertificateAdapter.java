package com.lanmei.lijia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.utils.L;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 上传证件
 */
public class CertificateAdapter extends SwipeRefreshAdapter<String> {


    public CertificateAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_certificate, parent, false));
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        String bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.nameTv.setText(String.format(context.getString(R.string.certificate),""+(position+1)));
        Bitmap bitmap = BitmapFactory.decodeFile(bean);
        if (bitmap == null){
            return;
        }
        L.d("cameraPreview",bitmap.getByteCount()+"");
        viewHolder.certificateIv.setImageBitmap(bitmap);
        viewHolder.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (l != null){
                    l.delete(position);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.certificate_iv)
        ImageView certificateIv;
        @InjectView(R.id.close_iv)
        ImageView closeIv;
        @InjectView(R.id.name_tv)
        TextView nameTv;

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    DeleteCertificateListener l;

    public void setDeleteCertificateListener(DeleteCertificateListener l){
        this.l = l;
    }
    //删除证书
    public interface DeleteCertificateListener{
        void delete(int position);
    }

}
