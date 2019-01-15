package com.lanmei.lijia.ui.league;

import android.Manifest;
import android.app.Activity;
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
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.certificate.CameraActivity;
import com.lanmei.lijia.event.LeagueEvent;
import com.lanmei.lijia.utils.AKDialog;
import com.xson.common.app.BaseActivity;
import com.xson.common.utils.ImageUtils;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 上传身份证
 */
public class UploadingIdActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.front_iv)
    ImageView frontIv;
    @InjectView(R.id.reverse_iv)
    ImageView reverseIv;
    int type;

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        Bundle bundle = intent.getBundleExtra("bundle");
        if (bundle != null){
            frontPathStr = bundle.getString("idFront");
            reversePathStr = bundle.getString("idReverse");
        }
    }

    @Override
    public int getContentViewId() {
        return R.layout.activity_uploading_id;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.uploading_id);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        if (!StringUtils.isEmpty(frontPathStr) && !StringUtils.isEmpty(reversePathStr)){
            Bitmap bitmap = BitmapFactory.decodeFile(frontPathStr);
            frontIv.setImageBitmap(bitmap);
            bitmap = BitmapFactory.decodeFile(reversePathStr);
            reverseIv.setImageBitmap(bitmap);
        }
    }

    boolean isCamera = false;

    @OnClick({R.id.front_iv, R.id.reverse_iv, R.id.cancel_tv, R.id.uploading_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.front_iv://身份证正面
                this.type = CameraActivity.TYPE_IDCARD_FRONT;
                dialog();
                break;
            case R.id.reverse_iv://身份证反面
                this.type = CameraActivity.TYPE_IDCARD_BACK;
                dialog();
                break;
            case R.id.cancel_tv://取消
                finish();
                break;
            case R.id.uploading_bt://上传
                uploading();
                break;
        }
    }

    private void uploading() {
        if (StringUtils.isEmpty(frontPathStr)){
            UIHelper.ToastMessage(this,getString(R.string.choose_id_front));
            return;
        }
        if (StringUtils.isEmpty(reversePathStr)){
            UIHelper.ToastMessage(this,getString(R.string.choose_id_reverse));
            return;
        }
        LeagueEvent event = new LeagueEvent(2);
        event.setIdFront(frontPathStr);
        event.setIdReverse(reversePathStr);
        EventBus.getDefault().post(event);
        finish();
    }

    private void dialog(){
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
                    CameraActivity.openCertificateCamera(this, type);
                } else {
                    startImagePick();
                }

            } else {
                requestPermissions(permissions, 1);
            }
        } else {
            if (isCamera) {
                CameraActivity.openCertificateCamera(this, type);
            } else {
                startImagePick();
            }
        }
    }

    private void startImagePick() {
        Intent intent = ImageUtils.getImagePickerIntent();
        startActivityForResult(intent, CHOOSE_FROM_GALLAY);
    }

    private static final int CHOOSE_FROM_GALLAY = 1;
    private static final int CHOOSE_FROM_CAMERA = 2;
    private static final int RESULT_FROM_CROP = 3;

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

    private File tempImage;
    private File croppedImage;

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
                L.d("cameraPreview",image);
                startActionCrop(image);
                break;
            case RESULT_FROM_CROP:
                showNewPhoto();//
                break;
            case CameraActivity.REQUEST_CODE:
                String path = data.getStringExtra("result");
                if (!TextUtils.isEmpty(path)) {
                    Bitmap bitmap = BitmapFactory.decodeFile(path);
                    if (bitmap == null){
                        return;
                    }
                    if (type == CameraActivity.TYPE_IDCARD_FRONT){//正面
                        frontPathStr = path;
                        frontIv.setImageBitmap(bitmap);
                    }else {//反面
                        reverseIv.setImageBitmap(bitmap);
                        reversePathStr = path;
                    }
                }
                break;
            default:
                break;
        }
    }
    String frontPathStr;//选择正面剪切后的路径()
    String reversePathStr;//选择反面剪切后的路径()

    private void showNewPhoto() {
        if (!StringUtils.isEmpty(croppedImage) && croppedImage.exists()) {
            try {
                ImageUtils.compressImage(croppedImage.getPath(), 300, 300, Bitmap.CompressFormat.JPEG);
            } catch (Exception e) {
                L.e(e);
            }
        }
        if (croppedImage != null) {
            if (type == CameraActivity.TYPE_IDCARD_FRONT){
                frontPathStr = croppedImage.getAbsolutePath();
                Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(frontPathStr, frontIv.getWidth(), frontIv.getHeight());
                frontIv.setImageBitmap(bitmap);
                return;
            }
            reversePathStr = croppedImage.getAbsolutePath();
//            pic = heaPathStr;
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(reversePathStr, reverseIv.getWidth(), reverseIv.getHeight());
            reverseIv.setImageBitmap(bitmap);
        }
    }

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

}
