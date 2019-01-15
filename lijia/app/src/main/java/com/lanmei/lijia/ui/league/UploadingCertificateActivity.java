package com.lanmei.lijia.ui.league;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.adapter.CertificateAdapter;
import com.lanmei.lijia.certificate.CameraActivity;
import com.lanmei.lijia.event.LeagueEvent;
import com.lanmei.lijia.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.ProgressHUD;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 上传证书
 */
public class UploadingCertificateActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.recyclerView)
    RecyclerView recyclerView;
    @InjectView(R.id.again_tv)
    TextView againTv;
    public ArrayList<String> list;
    CertificateAdapter adapter;

    ProgressHUD mProgressHUD;

    private void initProgressDialog() {
        mProgressHUD = ProgressHUD.show(this, "", true, false, null);
        mProgressHUD.cancel();
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            list = bundle.getStringArrayList("list");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_uploading_certificate;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initProgressDialog();

        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.uploading_certificate);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        againTv.setText("上传证书");
        adapter = new CertificateAdapter(this);
        if (StringUtils.isEmpty(list)){
            list = new ArrayList<>();
        }else {
            adapter.setData(list);
        }
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.setDeleteCertificateListener(new CertificateAdapter.DeleteCertificateListener() {
            @Override
            public void delete(int position) {
                deleteCertificate(position);

            }
        });
    }

    private void deleteCertificate(final int position) {
        AKDialog.getAlertDialog(getContext(), "确认删除？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                adapter.getData().remove(position);
                adapter.notifyItemRemoved(position);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @OnClick({R.id.again_tv, R.id.cancel_tv, R.id.uploading_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.again_tv:
                dialog();
                break;
            case R.id.cancel_tv:
                finish();
                break;
            case R.id.uploading_bt:
                uploading();
                break;
        }
    }

    private void uploading() {
        if (StringUtils.isEmpty(list)){
            UIHelper.ToastMessage(this,"请选择证书");
            return;
        }
        LeagueEvent event = new LeagueEvent(3);
        event.setCertificateList(list);
        EventBus.getDefault().post(event);
        finish();
//        mProgressHUD.show();
//        new CompressPhotoUtils().CompressPhoto(this, list, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
//            @Override
//            public void success(List<String> list) {
//                if (StringUtils.isEmpty(list)) {
//                    return;
//                }
//                int size = list.size();
//                for (int i = 0; i < size; i++) {
//                    L.d("cameraPreview", "压缩路径：" + list.get(i));
//                }
//                L.d("cameraPreview", "压缩完成");
//                adapter.setData(list);
//                adapter.notifyDataSetChanged();
//                mProgressHUD.cancel();
//            }
//        }, "4");
    }

    boolean isCamera = false;
    private static final int CHOOSE_FROM_GALLAY = 1;
    private static final int CHOOSE_FROM_CAMERA = 2;
    private static final int RESULT_FROM_CROP = 3;

    private void dialog() {
        AKDialog.showBottomListDialog(this, this, new AKDialog.AlbumDialogListener() {
            @Override
            public void photograph() {
                isCamera = true;
                applyWritePermission();

            }

            @Override
            public void photoAlbum() {
                isCamera = false;
                applyWritePermission();

            }
        });
    }

    /**
     * 相机拍照
     */
    private void startActionCamera() {
        tempImage = ImageUtils.getTempFile(this, "head");
        if (tempImage == null) {
            UIHelper.ToastMessage(this, R.string.cannot_create_temp_file);
            return;
        }
        Intent intent = ImageUtils.getImageCaptureIntent(getUriForFile(tempImage));
        startActivityForResult(intent, CHOOSE_FROM_CAMERA);
    }

    public void applyWritePermission() {
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (isCamera) {
            permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        }
        if (Build.VERSION.SDK_INT >= 23) {
            int check = ContextCompat.checkSelfPermission(this, permissions[0]);
            // 权限是否已经 授权 GRANTED---授权  DINIED---拒绝
            if (check == PackageManager.PERMISSION_GRANTED) {
                //调用相机
                if (isCamera) {
                    CameraActivity.openCertificateCamera(this, CameraActivity.TYPE_COMPANY_LANDSCAPE);
                } else {
                    startImagePick();
                }

            } else {
                requestPermissions(permissions, 1);
            }
        } else {
            if (isCamera) {
                CameraActivity.openCertificateCamera(this, CameraActivity.TYPE_COMPANY_LANDSCAPE);
            } else {
                startImagePick();
            }
        }
    }

    private void startImagePick() {
        Intent intent = ImageUtils.getImagePickerIntent();
        startActivityForResult(intent, CHOOSE_FROM_GALLAY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        String image;
        switch (requestCode) {
            case CHOOSE_FROM_GALLAY:
                image = ImageUtils.getImageFileFromPickerResult(this, data);
                startActionCrop(image);
                break;
            case CHOOSE_FROM_CAMERA:
                //注意小米拍照后data 为null
                image = tempImage.getPath();
                startActionCrop(image);
                break;
            case RESULT_FROM_CROP:
                showNewPhoto();//
                break;
            case CameraActivity.REQUEST_CODE:
                String path = data.getStringExtra("result");
//                startActionCrop(path);
                if (!TextUtils.isEmpty(path)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (bitmap == null) {
                        return;
                    }
                    addPath(path);
                }
                break;
            default:
                break;
        }
    }

    private void showNewPhoto() {
        if (!StringUtils.isEmpty(croppedImage) && croppedImage.exists()) {
            try {
                ImageUtils.compressImage(croppedImage.getPath(), 300, 300, Bitmap.CompressFormat.JPEG);
            } catch (Exception e) {
                L.e(e);
            }
        }
        if (croppedImage != null) {
            addPath(croppedImage.getAbsolutePath());
        }
    }

    public void addPath(String path) {
        list.add(path);
        adapter.setData(list);
        adapter.notifyDataSetChanged();
        againTv.setText(R.string.uploading_again);
    }

    private File tempImage;
    private File croppedImage;

    private void startActionCrop(String image) {
        if (TextUtils.isEmpty(image)) {
            UIHelper.ToastMessage(this, R.string.image_not_exists);
            return;
        }
        File imageFile = new File(image);
        if (!imageFile.exists()) {
            UIHelper.ToastMessage(this, R.string.image_not_exists);
            return;
        }
        croppedImage = ImageUtils.getTempFile(this, "head");
        if (croppedImage == null) {
            UIHelper.ToastMessage(this, R.string.cannot_create_temp_file);
            return;
        }
        Intent intent = ImageUtils.getImageCropIntent(getUriForFile(imageFile), Uri.fromFile(croppedImage));
        startActivityForResult(intent, RESULT_FROM_CROP);
    }

    //解决Android 7.0之后的Uri安全问题
    private Uri getUriForFile(File file) {
        if (file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(this, "com.lanmei.lijia.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }

}
