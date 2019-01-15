package com.oss;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;


/**
 * Created by Administrator on 2016/9/14.
 */

public class ManageOssUpload {

    private OSS oss;

//    private static final String uploadFilePath = "/storage/emulated/0/ckw/img/head1473820263615.jpeg";
//    private static final String uploadObject = "wlyg/img/1473820263615.jpeg";
//    private static final String downloadObject = "sampleObject";

    public ManageOssUpload(Context mContext) {
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(OssUserInfo.accessKeyId, OssUserInfo.accessKeySecret);

        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSLog.enableLog();
        oss = new OSSClient(mContext, OssUserInfo.endpoint, credentialProvider, conf);


    }

    public void uploadFile_img(String uploadFilePath,OssUploadListener ossUploadListener){
        if (TextUtils.isEmpty(uploadFilePath))
            return ;
        String fileName=uploadFilePath.substring(uploadFilePath.lastIndexOf("/")+1);
        fileName= OssUserInfo.uploadPath+fileName;
        new PutObjectSamples(oss, OssUserInfo.testBucket, fileName, uploadFilePath)
                .asyncPutObjectFromLocalFile(ossUploadListener);

    }

    /**
     * 同步阻塞上传
     * @param uploadFilePath
     * @param type  防止上传几组图片时出错（地址一样）
     * @return
     */
    public String uploadFile_img(String uploadFilePath,String type){
        if (TextUtils.isEmpty(uploadFilePath))
            return null;
        String fileName = uploadFilePath.substring(uploadFilePath.lastIndexOf("/")+1);
        fileName= OssUserInfo.uploadPath+type+"/"+fileName;
        return new PutObjectSamples(oss, OssUserInfo.testBucket, fileName, uploadFilePath)
                .putObjectFromLocalFile();

    }

    public boolean uploadFile_del(String imgUrl){
        boolean result=false;

        String name=getUrlKey(imgUrl);
//        L.d("uploadFile_del",""+name);
        if (name.isEmpty())
            return result;
//        name="wlyg/img/"+name;
        ManageObjectSamples manageObjectSamples =   new ManageObjectSamples(oss, OssUserInfo.testBucket,name);
        if (manageObjectSamples.checkIsObjectExist()){
            try {
                result =  manageObjectSamples.syncDeleteObject(name);
            } catch (ClientException e) {
                e.printStackTrace();
            } catch (ServiceException e) {
                e.printStackTrace();
            }
        }
         return result;
    }

    public void delOssPic(String imgUrl,OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult> ossDelCallback){
        String name=getUrlKey(imgUrl);
//        L.d("uploadFile_del",""+name);
        if (name.isEmpty())
            return ;

        // 创建删除请求
        DeleteObjectRequest delete = new DeleteObjectRequest(OssUserInfo.testBucket,name);
        // 异步删除

        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, ossDelCallback);
        deleteTask.waitUntilFinished();

    }

    public String getUrlKey(String picOssUrl){
        boolean result=false;
        if (TextUtils.isEmpty(picOssUrl))
            return "";
        if (!picOssUrl.contains(".com"))
            return "";
        String name=picOssUrl.substring(picOssUrl.lastIndexOf(".com")+5);
//        L.d("uploadFile_del",""+name);
        return name;
    }
}
