package com.lanmei.lijia.ui.settting.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.MerchantBean;
import com.lanmei.lijia.event.MerchantUpdataAdEvent;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.JsonUtil;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.CenterTitleToolbar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 商家资料
 */
public class MerchantDataActivity extends BaseActivity implements Toolbar.OnMenuItemClickListener {

    @InjectView(R.id.banner)
    ConvenientBanner banner;

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.name_et)
    EditText mNameEt;//场地名称
    @InjectView(R.id.address_et)
    EditText mAddressEt;//场地地址
    @InjectView(R.id.contact_et)
    EditText mContactEt;//联系人
    @InjectView(R.id.phone_et)
    EditText mPhoneEt;//电话
    @InjectView(R.id.place_introduction_et)
    EditText mPlaceIntroductionEt;//商家介绍
    EditText[] etID;
    boolean isTurning = true;

    @Override
    public int getContentViewId() {
        return R.layout.activity_merchant_data;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle("商家资料");
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        //toolbar的menu点击事件的监听
        mToolbar.setOnMenuItemClickListener(this);
        etID = new EditText[]{mNameEt, mAddressEt,mContactEt, mPhoneEt, mPlaceIntroductionEt};
        for (EditText et : etID) {
            et.setFocusableInTouchMode(false);//设置不可编辑
        }
        loadData();
        EventBus.getDefault().register(this);
    }

    private void loadData() {
        LiJiaApi api = new LiJiaApi("app/merchants");
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<DataBean<String> >() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                int code = response.getCode();
                if (code == 404){
                    return;
                }
                try {
                    L.d("BeanRequest", "用户信息：" + Des.decode(response.data));
                    MerchantBean merchantBean = JsonUtil.jsonToBean(Des.decode(response.data), MerchantBean.class);
                    if (merchantBean != null) {
                        bean = merchantBean;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                setMerchant(bean, isTurning);
            }
        });
    }

    private void setMerchant(MerchantBean bean, boolean isTurning) {
        if (bean == null) {
            return;
        }
        //广告轮播
        CommonUtils.setBanner(banner, bean.getImgs(), isTurning);
        mNameEt.setText(bean.getMname());
        mAddressEt.setText(bean.getAddress());
        mContactEt.setText(bean.getContact());
        mPhoneEt.setText(bean.getPhone());
        mPlaceIntroductionEt.setText(bean.getContact());

    }

    @Subscribe
    public void merchantUpdataAdEvent(MerchantUpdataAdEvent event) {
        isTurning = false;
        loadData();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {

        if (item.getItemId() == R.id.action_edit) {
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_save);
            for (EditText et : etID) {
                et.setFocusableInTouchMode(true);//设置可编辑
            }
        } else if (item.getItemId() == R.id.action_save) {//先退出输入法
            String name = CommonUtils.getStringByEditText(mNameEt);
            if (StringUtils.isEmpty(name)) {
                UIHelper.ToastMessage(this, "请输入商家名称");
                return false;
            }
            String address = CommonUtils.getStringByEditText(mAddressEt);
            if (StringUtils.isEmpty(address)) {
                UIHelper.ToastMessage(this, "请输入商家地址");
                return false;
            }
            String contact = CommonUtils.getStringByEditText(mContactEt);
            if (StringUtils.isEmpty(contact)) {
                UIHelper.ToastMessage(this, "请输入联系人姓名");
                return false;
            }
            String phone = mPhoneEt.getText().toString().trim();
            if (StringUtils.isEmpty(phone)) {
                UIHelper.ToastMessage(this, "请输入联系电话");
                return false;
            }
            String placeIntroduction = mPlaceIntroductionEt.getText().toString().trim();
            if (StringUtils.isEmpty(placeIntroduction)) {
                UIHelper.ToastMessage(this, "请输入商家介绍");
                return false;
            }
            updateMerchantData(name,address,phone,placeIntroduction,contact);
            View view = this.getCurrentFocus();
            if (view != null) {
                ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).
                        hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
            mToolbar.getMenu().clear();
            mToolbar.inflateMenu(R.menu.menu_edit);
            for (EditText et : etID) {
                et.setFocusable(false);
                et.setFocusableInTouchMode(false);//设置不可编辑
            }

        }
        return true;
    }
    MerchantBean bean;

    //更新商家资料
    private void updateMerchantData(String name, String address, String phone, String placeIntroduction,String contact) {
        HttpClient httpClient = HttpClient.newInstance(this);
        LiJiaApi api = new LiJiaApi("app/edit_merchants");
        api.addParams("uid",api.getUserId(this));
        api.addParams("mname",name);//商家名称
        api.addParams("address",address);//地址
        api.addParams("phone",phone);
        api.addParams("contact",contact);//联系人名称
        api.addParams("content",placeIntroduction);//详细信息
//        api.addParams("imgs",placeIntroduction);//轮播图 （array）
        httpClient.loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(MerchantDataActivity.this, response.getMsg());
                isTurning = false;
            }
        });
    }

    @OnClick(R.id.uploading_tv)
    public void showUploadingIv() {//点击上传图片
//        UIHelper.ToastMessage(this,R.string.developing);
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", bean);
        IntentUtil.startActivity(this, MerchantAlbumActivity.class, bundle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
