package com.lanmei.lijia.ui.league;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.ChooseWorkListBean;
import com.lanmei.lijia.event.LeagueEvent;
import com.lanmei.lijia.event.SubmitDataEvent;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.CompressPhotoUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 师傅加盟
 */
public class MasterLeagueActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.city_name_tv)
    TextView cityNameTv;//城市名称
    @InjectView(R.id.id_number_tv)
    TextView idNumberTv;//是否已选身份证照片（提示）
    @InjectView(R.id.name_et)
    EditText nameEt;//
    @InjectView(R.id.id_number_et)
    EditText idNumberEt;//
    @InjectView(R.id.other_tv)
    TextView otherTv;//
    @InjectView(R.id.serve_tv)
    TextView serveTv;//
    List<String> successPath1;  // 存储上传阿里云成功后的上传路径(身份证正面)
    List<String> successPath2;  // 存储上传阿里云成功后的上传路径(身份证反面)
    List<String> successPath3;  // 存储上传阿里云成功后的上传路径(其他证书照片)
    boolean isCompress1 = false;//是否在压缩图片
    boolean isCompress2 = false;//是否在压缩图片
    boolean isCompress3 = false;//是否在压缩图片


    @Override
    public int getContentViewId() {
        return R.layout.activity_master_league;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.master_league);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        EventBus.getDefault().register(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_confirm, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_confirm:
                confirm();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void confirm() {
        if (StringUtils.isEmpty(city)) {
            UIHelper.ToastMessage(this, R.string.choose_city);
            return;
        }
        if (StringUtils.isEmpty(idFront) || StringUtils.isEmpty(idReverse)) {
            UIHelper.ToastMessage(this, "请选择身份证照片");
            return;
        }
        String name = CommonUtils.getStringByEditText(nameEt);
        if (StringUtils.isEmpty(name)) {
            UIHelper.ToastMessage(this, R.string.input_name);
            return;
        }
        String idNumber = CommonUtils.getStringByEditText(idNumberEt);
        String id = "";
        try {
            id = StringUtils.IDCardValidate(idNumber);
            if (!StringUtils.isEmpty(id)) {
                UIHelper.ToastMessage(this, id);
                return;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (StringUtils.isEmpty(certificateList)) {
            UIHelper.ToastMessage(this, "请选择其他证书照片");
            return;
        }
        if (StringUtils.isEmpty(chooseWorkList)) {
            UIHelper.ToastMessage(this, "请选择服务工种");
            return;
        }
        addLeague();
    }

    private void addLeague() {
        isCompress1 = true;//在压缩
        isCompress2 = true;//在压缩
        if (!StringUtils.isEmpty(certificateList)) {
            isCompress3 = true;//在压缩
            successPath3 = new ArrayList<>();
            new CompressPhotoUtils().CompressPhoto(this, certificateList, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
                @Override
                public void success(List<String> list) {
                    successPath3 = list;
                    isCompress3 = false;
                    loadLeague();
//                    new UpdateImageViewTask(3, list).execute();
                }
            }, "3");
        }
        List<String> list = new ArrayList<>();//身份证正面
        list.add(idFront);
        successPath1 = new ArrayList<>();
        new CompressPhotoUtils().CompressPhoto(this, list, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
            @Override
            public void success(List<String> list) {
                successPath1 = list;
                isCompress1 = false;
                loadLeague();
//                new UpdateImageViewTask(1, list).execute();
            }
        }, "1");
        List<String> list2 = new ArrayList<>();//身份证反面
        list2.add(idReverse);
        successPath2 = new ArrayList<>();
        new CompressPhotoUtils().CompressPhoto(this, list2, new CompressPhotoUtils.CompressCallBack() {//压缩图片（可多张）
            @Override
            public void success(List<String> list) {
                successPath2 = list;
                isCompress2 = false;
                loadLeague();
//                new UpdateImageViewTask(2, list).execute();
            }
        }, "2");
    }

    private void loadLeague() {
        if (!isCompress1 && !isCompress2 && !isCompress3) {
            LiJiaApi api = new LiJiaApi("app/perfectmember");
            api.addParams("city", city);//城市名称
            api.addParams("cardimg1", StringUtils.isEmpty(successPath1) ? "" : successPath1.get(0));//身份证正面
            api.addParams("cardimg2", StringUtils.isEmpty(successPath2) ? "" : successPath2.get(0));//身份证反面
            api.addParams("card", CommonUtils.getStringByEditText(idNumberEt));//身份证号
            api.addParams("uid", api.getUserId(this));//用户数据ID
            api.addParams("username", CommonUtils.getStringByEditText(nameEt));//用户名称
            api.addParams("craft", workType);//工种信息
            api.addParams("otherphotos", getOtherphotos());//其他证书 图片集（数组）
            HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
                @Override
                public void onResponse(BaseBean response) {
                    if (isFinishing()) {
                        return;
                    }
                    EventBus.getDefault().post(new SubmitDataEvent());
                    UIHelper.ToastMessage(getContext(), response.getMsg());
                    finish();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    UIHelper.ToastMessage(getContext(), error.getMessage());
                }
            });
        }
    }

    private String getOtherphotos() {
        if (StringUtils.isEmpty(successPath3)) {
            return "";
        }
        StringBuffer buffer = new StringBuffer();
        int size = successPath3.size();
        for (int i = 0; i < size; i++) {
            buffer.append(successPath3.get(i) + ",");
        }
        String s = buffer.toString();
        if (!StringUtils.isEmpty(s)) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://上传某张图片失败
                    UIHelper.ToastMessage(getContext(), "上传图片失败：" + msg.obj);
                    break;
                case 2:
                    break;
            }
        }
    };

    private String city;
    private String idFront;//身份证正面
    private String idReverse;//身份证反面
    private ArrayList<String> certificateList;
    private List<ChooseWorkListBean.ChooseWorkBean> chooseWorkList;


    @OnClick({R.id.ll_city, R.id.ll_id_number, R.id.ll_other, R.id.ll_serve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_city://城市
                IntentUtil.startActivity(this, PositionActivity.class);
                break;
            case R.id.ll_id_number://身份证照片
                Bundle bundle = new Bundle();
                if (!StringUtils.isEmpty(idFront)) {
                    bundle.putString("idFront", idFront);
                    bundle.putString("idReverse", idReverse);
                }
                IntentUtil.startActivity(this, UploadingIdActivity.class, bundle);
                break;
            case R.id.ll_other://其他证书照片
                Bundle certificateBundle = new Bundle();
                if (!StringUtils.isEmpty(certificateList)) {
                    certificateBundle.putStringArrayList("list", certificateList);
                }
                IntentUtil.startActivity(this, UploadingCertificateActivity.class, certificateBundle);
                break;
            case R.id.ll_serve://服务工种
                Bundle chooseWorkBundle = new Bundle();
                if (!StringUtils.isEmpty(chooseWorkList)) {
                    chooseWorkBundle.putSerializable("list", (Serializable) chooseWorkList);
                }
                IntentUtil.startActivity(this, ChooseWorkActivity.class, chooseWorkBundle);
                break;
        }
    }

    String workType = "";//工种信息ID集合

    @Subscribe
    public void leagueEvent(LeagueEvent event) {

        switch (event.getType()) {//1 城市  2 身份证照片 3 其他证书照片 4 服务工种
            case 1:
                city = event.getCity();
                cityNameTv.setText(city);
                break;
            case 2:
                idFront = event.getIdFront();
                idReverse = event.getIdReverse();
                showChoose(idNumberTv, StringUtils.isEmpty(idFront) && StringUtils.isEmpty(idReverse));
                break;
            case 3:
                certificateList = event.getCertificateList();
                showChoose(otherTv, StringUtils.isEmpty(certificateList));
                break;
            case 4:
                chooseWorkList = event.getChooseWorkList();
                serveTv.setText(getWorkName(chooseWorkList));
                break;
        }
    }

    private String getWorkName(List<ChooseWorkListBean.ChooseWorkBean> list) {
        if (StringUtils.isEmpty(list)) {
            return "";
        }
        workType = "";
        StringBuffer buffer = new StringBuffer();
        StringBuffer workTypeBuffer = new StringBuffer();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            ChooseWorkListBean.ChooseWorkBean bean = list.get(i);
            buffer.append(bean.getClassname() + "、");
            workTypeBuffer.append(bean.getSetval() + ",");
        }
        String s = buffer.toString();
        if (!StringUtils.isEmpty(s)) {
            s = CommonUtils.getSubString(s);
            workType = CommonUtils.getSubString(workTypeBuffer.toString());
        }
        return s;
    }

    private void showChoose(TextView textView, boolean b) {
        if (b) {
            textView.setText("");
        } else {
            textView.setText(R.string.chosen);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
