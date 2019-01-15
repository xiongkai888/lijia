package com.lanmei.lijia.ui.settting.activity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.MerchantAlbumAdapter;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.AlbumBean;
import com.lanmei.lijia.bean.MerchantBean;
import com.lanmei.lijia.event.AddCostBean;
import com.lanmei.lijia.event.MerchantUpdataAdEvent;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.CompressPhotoUtils;
import com.oss.ManageOssUpload;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIBaseUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 更新商家轮播图
 */
public class MerchantAlbumActivity extends BaseActivity {

    MerchantAlbumAdapter mAdapter;
    @InjectView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @InjectView(R.id.done_tv)
    TextView mDoneTv;//完成
    //    ReceiverHelper mReceiverHelper;
    int albumNum = 0;//网络的相片个数
    MerchantBean bean;

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;

    @Override
    public int getContentViewId() {
        return R.layout.activity_album;
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null) {
            bean = (MerchantBean) bundle.getSerializable("bean");
        }

    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        mDoneTv.setEnabled(false);//
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        int padding = UIBaseUtils.dp2pxInt(this, 5);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(padding));
        mAdapter = new MerchantAlbumAdapter(this);
        mAdapter.setAlbumListener(new MerchantAlbumAdapter.MerchantAlbumListener() {
            @Override
            public void deleteAlbums() {
                mAlbumBeanlist = mAdapter.getData();
                numAlbum = CommonUtils.getNativeAlbumsNum(mAlbumBeanlist);
                isChangeAlbum();
            }
        });
        mRecyclerView.setAdapter(mAdapter);
        mAlbumBeanlist = new ArrayList<>();
        setMerchantAlbum();
    }

    private void setMerchantAlbum() {
        if (bean == null || StringUtils.isEmpty(bean.getImgs())) {
            return;
        }
        mAlbumBeanlist = CommonUtils.getAlbumList(bean.getImgs());
        if (mAlbumBeanlist == null) {
            return;
        }
        albumNum = mAlbumBeanlist.size();
        mAdapter.setData(mAlbumBeanlist);
        setAlbum();
    }

    List<AlbumBean> mAlbumBeanlist;//

    public static class SpaceItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

            outRect.top = space / 2;
            outRect.bottom = space / 2;
            outRect.left = space;
        }
    }

    //相册是否有改变
    public void isChangeAlbum() {
        if (!StringUtils.isEmpty(mAlbumBeanlist)) {
            int size = mAlbumBeanlist.size();
            if (size != albumNum) {
                mDoneTv.setEnabled(true);
                mDoneTv.setTextColor(getResources().getColor(R.color.white));
                return;
            } else {
                mDoneTv.setEnabled(false);
                mDoneTv.setTextColor(getResources().getColor(R.color.color999));
            }
            for (int i = 0; i < size; i++) {
                AlbumBean bean = mAlbumBeanlist.get(i);
                if (bean != null) {
                    if (bean.isPicker()) {
                        mDoneTv.setEnabled(true);
                        mDoneTv.setTextColor(getResources().getColor(R.color.white));
                        return;
                    }
                }
            }
        }
    }

    @OnClick(R.id.uploading_bt)
    public void showUploading() {
        if (numAlbum == 9) {
            UIHelper.ToastMessage(this, "一次最多只能上传九张");
            return;
        }
        choicePhotoWrapper();
    }

    @OnClick({R.id.back_iv, R.id.done_tv})
    public void setOnClick(View view) {
        switch (view.getId()) {
            case R.id.back_iv:
                finish();
                break;
            case R.id.done_tv:
                List<String> list = CommonUtils.getUploadingList(mAlbumBeanlist);
                if (!StringUtils.isEmpty(list)) {
                    new CompressPhotoUtils().CompressPhoto(this, list, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                        @Override
                        public void success(List<String> list) {
                            updateAblum(list);
                        }
                    }, "5");
                } else {
                    updateAblum(null);
                }
                break;
        }
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
        if (EasyPermissions.hasPermissions(this, perms)) {//点击加号时调用
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "uploading");
            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, 9 - numAlbum, null, false), REQUEST_CODE_CHOOSE_PHOTO);
        }else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片\n\n2.拍照", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }

    int numAlbum = 0;//已经选择的相片个数

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {//选择图片点击确定时
            ArrayList<String> list = BGAPhotoPickerActivity.getSelectedImages(data);
            List<AlbumBean> albumList = new ArrayList<>();
            if (list != null && list.size() > 0) {
                int size = list.size();
                numAlbum += size;
                for (int i = 0; i < size; i++) {
                    AlbumBean bean = new AlbumBean();
                    bean.setPic(list.get(i));
                    bean.setPicker(true);
                    albumList.add(bean);
                }
            }
            mAlbumBeanlist.addAll(albumList);
            isChangeAlbum();
            setAlbum();
        }
    }

    private void setAlbum() {
        if (mAlbumBeanlist != null) {
            List<String> arry = CommonUtils.getStringArry(mAlbumBeanlist);
            mAdapter.setStringArray(arry);
            mAdapter.setData(mAlbumBeanlist);
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateAblum(List<String> list) {
        if (isFinishing()) {
            return;
        }
        HttpClient httpClient = HttpClient.newInstance(this);
        LiJiaApi api = new LiJiaApi("app/edit_merchants");
        api.addParams("uid", api.getUserId(this));
        JSONArray pics = getImgs(mAlbumBeanlist, list);
        api.addParams("imgs", pics);
        //        api.addParams("toto", 1);
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getMsg().toString());
                EventBus.getDefault().post(new MerchantUpdataAdEvent());
                finish();
            }
        });
    }

    private JSONArray getImgs(List<AlbumBean> list, List<String> stringList) {
        JSONArray jsonArray = new JSONArray();
        if (!StringUtils.isEmpty(list)) {
            for (AlbumBean bean : list) {
                if (!bean.isPicker()) {
                    jsonArray.add(bean.getPic());
                }
            }
        }
        if (!StringUtils.isEmpty(stringList)) {
            for (String url : stringList) {
                jsonArray.add(url);
            }
        }
        return jsonArray;

    }

}
