package com.lanmei.lijia.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.bean.AlbumBean;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.adapter.SwipeRefreshAdapter;
import com.xson.common.helper.ImageHelper;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


/**
 * 动态九宫格Adapter
 */
public class MerchantAlbumAdapter extends SwipeRefreshAdapter<AlbumBean> {

    List<String> array;

    public MerchantAlbumAdapter(Context context) {
        super(context);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {

        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_album, parent, false));
    }

    public void setStringArray(List<String> arry) {
        this.array = arry;
    }

    @Override
    public void onBindViewHolder2(RecyclerView.ViewHolder holder, final int position) {
        final AlbumBean bean = getItem(position);
        if (bean == null) {
            return;
        }
        ViewHolder viewHolder = (ViewHolder) holder;
        boolean isPicker = bean.isPicker();
        if (isPicker) {
            Bitmap bmp = BitmapFactory.decodeFile(bean.getPic());
            viewHolder.mPicIv.setImageBitmap(bmp);
        } else {
            ImageHelper.load(context, bean.getPic(), viewHolder.mPicIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
        }
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (array != null) {
                    CommonUtils.showPhotoBrowserActivity(context, array, array.get(position));
                }
            }
        });
        viewHolder.mCrossIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData().remove(position);
                notifyItemRemoved(position);
                notifyDataSetChanged();
                array = CommonUtils.getStringArry(getData());
                if (mAlbumListener != null) {
                    mAlbumListener.deleteAlbums();
                }
            }
        });
    }

    public interface MerchantAlbumListener {
        void deleteAlbums();//
    }

    MerchantAlbumListener mAlbumListener;

    public void setAlbumListener(MerchantAlbumListener l) {
        mAlbumListener = l;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        @InjectView(R.id.pic_iv)
        ImageView mPicIv;
        @InjectView(R.id.cross_iv)
        ImageView mCrossIv;//删除图标

        ViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

}
