package com.lanmei.lijia.ui.settting.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.bigkoo.pickerview.view.PopPicker;
import com.data.volley.Response;
import com.data.volley.error.VolleyError;
import com.lanmei.lijia.R;
import com.lanmei.lijia.WebSocket.AbsBaseWebSocketService;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.OrderListBean;
import com.lanmei.lijia.bean.StepBean;
import com.lanmei.lijia.event.AddCostBean;
import com.lanmei.lijia.event.OperationOrderEvent;
import com.lanmei.lijia.helper.AddCostHelper;
import com.lanmei.lijia.helper.BGASortableNinePhotoHelper;
import com.lanmei.lijia.ui.settting.activity.details.FixDescribeActivity;
import com.lanmei.lijia.ui.settting.activity.details.FixPicsActivity;
import com.lanmei.lijia.utils.AKDialog;
import com.lanmei.lijia.utils.BaiduLocation;
import com.lanmei.lijia.utils.CommonUtils;
import com.lanmei.lijia.utils.CompressPhotoUtils;
import com.lanmei.lijia.utils.JsonUtil;
import com.lanmei.lijia.view.stepview.HorizontalStepView;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.DataBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.helper.ImageHelper;
import com.xson.common.utils.DoubleUtil;
import com.xson.common.utils.FormatTime;
import com.xson.common.utils.IntentUtil;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.CenterTitleToolbar;
import com.xson.common.widget.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;

/**
 * 订单信息
 * <p>
 * 那么一大推，
 */
public class OrderInfoActivity extends BaseActivity implements PopPicker.PickerSelectListener, BGASortableNinePhotoLayout.Delegate, SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar mToolbar;
    @InjectView(R.id.stepview)
    HorizontalStepView stepView;
    @InjectView(R.id.ll_parent)
    LinearLayout llParent;
    @InjectView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    @InjectView(R.id.ll_add_cost)
    LinearLayout llAddCost;//增加的上门费列表
    @InjectView(R.id.f_add_cost)
    FrameLayout f_addCost;//增加的上门费列表按钮
    @InjectView(R.id.add_cost_iv)
    ImageView addCostIv;//
    AddCostHelper costHelper;//上门费用
    OrderListBean.OrderBean bean;

    @InjectView(R.id.head_iv)
    CircleImageView headIv;
    @InjectView(R.id.user_name_tv)
    TextView userNameTv;
    @InjectView(R.id.fix_content_tv)
    TextView fixContentTv;
    @InjectView(R.id.subscribe_time_tv)
    TextView subscribeTimeTv;
    @InjectView(R.id.address_tv)
    TextView addressTv;
    @InjectView(R.id.order_no_tv)
    TextView orderNoTv;
    @InjectView(R.id.order_time_tv)
    TextView orderTimeTv;
    @InjectView(R.id.device_id_tv)
    TextView deviceIdTv;//设备id
    @InjectView(R.id.ll_id)
    LinearLayout llTv;//设备id  layout
    @InjectView(R.id.ll_pics)
    LinearLayout llPics;//报修相片
    @InjectView(R.id.ll_describe)
    LinearLayout llDescribe;//报修描述
    @InjectView(R.id.ll_fix_pics)
    LinearLayout llFixPics;//维修照片
    @InjectView(R.id.ll_door_fee)
    LinearLayout llDoorFee;//上门费
    @InjectView(R.id.door_fee_tv)
    TextView doorFeeTv;//上门费
    @InjectView(R.id.phone_tv)
    TextView phoneTv;
    @InjectView(R.id.price_tv)
    TextView fixPriceTv;
    @InjectView(R.id.go_price_tv)
    TextView goPriceTv;
    @InjectView(R.id.total_price_tv)
    TextView totalPriceTv;//总价
    @InjectView(R.id.go_tv)
    TextView goTv;//去上门等状态
    @InjectView(R.id.order_cancel_tv)
    TextView orderCancelTv;//取消订单
    @InjectView(R.id.hint_tv)
    TextView hintTv;//提示语
    @InjectView(R.id.cost_num_tv)
    TextView costNumTv;//
    String toTime;
    @InjectView(R.id.snpl_moment_add_photos)
    BGASortableNinePhotoLayout mPhotosSnpl;//拖拽排序九宫格控件
    BGASortableNinePhotoHelper mPhotoHelper;
    private String id;//订单id

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_info;
    }

    private void showAddCost(boolean isShow) {
        if (isShow) {
            llAddCost.setVisibility(View.VISIBLE);
            f_addCost.setVisibility(View.VISIBLE);
            initAddCost();
        } else {
            llAddCost.setVisibility(View.GONE);
            f_addCost.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        llParent.setVisibility(View.GONE);
        initPermission();
        setSupportActionBar(mToolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayShowTitleEnabled(true);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setTitle(R.string.order_info);
        actionbar.setHomeAsUpIndicator(R.drawable.back);
        EventBus.getDefault().register(this);
        id = getIntent().getStringExtra("value");
        if (StringUtils.isEmpty(id)) {
            UIHelper.ToastMessage(this, "id为空");
            finish();
            return;
        }
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.color00D3C4, R.color.colorCC000A, R.color.red, R.color.color998462, R.color.color494d5c, R.color.black);
        refresh();
    }


    private void loadOrderDetails() {
        LiJiaApi api = new LiJiaApi("app/repair_order_details");
        api.addParams("uid", api.getUserId(this));
        api.addParams("id", id);
        HttpClient.newInstance(this).request(false, api, new BeanRequest.SuccessListener<DataBean<String>>() {
            @Override
            public void onResponse(DataBean<String> response) {
                if (isFinishing()) {
                    return;
                }
                swipeRefreshLayout.setRefreshing(false);
                try {
                    L.d("BeanRequest", "订单详情：" + Des.decode(response.data));
                    OrderListBean.OrderBean orderBean = JsonUtil.jsonToBean(Des.decode(response.data), OrderListBean.OrderBean.class);
                    if (orderBean != null) {
                        bean = orderBean;
                        initData();
                        llParent.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    UIHelper.ToastMessage(getContext(), "数据解析出错");
                    L.d("BeanRequest", "解析错误：" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (isFinishing()) {
                    return;
                }
                swipeRefreshLayout.setRefreshing(false);
                UIHelper.ToastMessage(getContext(), error.getMessage());
            }
        });
    }

    private void initData() {
        initInfo();
        initStepView();
    }

    private void initAddCost() {
        costHelper = new AddCostHelper(this, llAddCost);
        costHelper.setPriceChangeListener(new AddCostHelper.PriceChangeListener() {
            @Override
            public void priceChange(double price) {
                double showPrice = price - Double.valueOf(bean.getTotal_price());
                goPriceTv.setText(String.format(getString(R.string.yuan), DoubleUtil.formatFloatNumber(price)));
                if (showPrice > 0.0) {
                    totalPriceTv.setVisibility(View.VISIBLE);
                    totalPriceTv.setText(String.format(getString(R.string.total_price), DoubleUtil.formatFloatNumber(showPrice)));
                } else {
                    totalPriceTv.setVisibility(View.GONE);
                }
                if (StringUtils.isEmpty(costHelper.getBeanList())) {
                    goTv.setText(R.string.arrive);
                } else {
                    goTv.setText(R.string.add_items);
                }
            }

            @Override
            public void getNum(int num) {
                costNumTv.setText(String.format(getString(R.string.items_cost_num), Integer.toString(num)));
            }
        });
        List<String> list1 = bean.getMaintenance_projects();
        List<String> list2 = bean.getTotal_prices();
        if (StringUtils.isEmpty(list1) && StringUtils.isEmpty(list2)) {
            return;
        }
        int size1 = list1.size();
        int size2 = list1.size();
        if (size1 != size2) {//个数不一样时候
            return;
        }
        costHelper.setData(list1, list2);
        costNumTv.setText(String.format(getString(R.string.items_cost_num), Integer.toString(costHelper.getNum())));
        String doorFee = bean.getTotal_price();
        double tailPrice = costHelper.getTotalPrice() - DoubleUtil.formatFloatNumber(doorFee);
        String tailPriceStr = DoubleUtil.formatFloatNumber(tailPrice);
        goPriceTv.setText(String.format(getString(R.string.yuan), tailPriceStr));
        totalPriceTv.setText(String.format(getString(R.string.total_price), tailPriceStr));
        doorFeeTv.setText(String.format(getString(R.string.yuan_burden), doorFee));
        totalPriceTv.setVisibility(View.VISIBLE);
        llDoorFee.setVisibility(View.VISIBLE);//显示上门费
        addCostIv.setVisibility(View.GONE);
    }

    private void initPhotoHelper() {
        mPhotosSnpl.setVisibility(View.VISIBLE);
        mPhotoHelper = new BGASortableNinePhotoHelper(this, mPhotosSnpl);
        // 设置拖拽排序控件的代理
        mPhotoHelper.setDelegate(this);
    }

    private void initInfo() {
        FormatTime time = new FormatTime();
        OrderListBean.OrderBean.UserBean userBean = bean.getUser();
        if (!StringUtils.isEmpty(userBean)) {
            ImageHelper.load(this, userBean.getPic(), headIv, null, true, R.drawable.default_pic, R.drawable.default_pic);
            userNameTv.setText(userBean.getNickname());
        }
        phoneTv.setText(bean.getPhone());
        fixContentTv.setText(bean.getClasstypename());//维修内容
        fixPriceTv.setText(String.format(getString(R.string.yuan), bean.getTotal_price()));//维修上门费
        fixPriceTv.setTextColor(getResources().getColor(R.color.red));
        if (StringUtils.isEmpty(bean.getMake_an_appointment()) || StringUtils.isSame(bean.getMake_an_appointment(), CommonUtils.isZero)) {//
            subscribeTimeTv.setText(R.string.arrange_self);
        } else {
            time.setTime(bean.getMake_an_appointment());
            subscribeTimeTv.setText(time.getTime());//预约上门时间
        }
        addressTv.setText(bean.getAddress());//地址
        String dustbin_id = "";
        String id = bean.getDustbin_id();
        if (StringUtils.isSame(bean.getPlatform_product(), CommonUtils.isOne)) {//平台产品
            if (StringUtils.isSame(bean.getOverdue(), CommonUtils.isOne)) {
//                dustbin_id = id + "(已过期)";
                dustbin_id = id;
            } else {
                dustbin_id = id + "(保修期)";
            }
        } else {//不是平台产品
            dustbin_id = id;
        }
        if (StringUtils.isEmpty(dustbin_id)) {//设备id为空的时候不显示设备ID
            llTv.setVisibility(View.GONE);
        } else {
            llTv.setVisibility(View.VISIBLE);
            deviceIdTv.setText(dustbin_id);
        }
        if (StringUtils.isEmpty(bean.getImgs())) {
            llPics.setVisibility(View.GONE);//报修照片为空的时候不显示
        } else {
            llPics.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isEmpty(bean.getContent())) {
            llDescribe.setVisibility(View.GONE);//报修描述为空的时候不显示
        } else {
            llDescribe.setVisibility(View.VISIBLE);
        }
        if (StringUtils.isEmpty(bean.getImgs1())) {
            llFixPics.setVisibility(View.GONE);//维修照片为空的时候不显示
        } else {
            llFixPics.setVisibility(View.VISIBLE);
        }
        orderNoTv.setText("订单编号：" + bean.getPay_no());//订单编号

        time.setTime(bean.getAddtime());
        orderTimeTv.setText("下单时间：" + time.formatterTimeNoSeconds());//下单时间
        totalPriceTv.setVisibility(View.GONE);
        llDoorFee.setVisibility(View.GONE);
    }

    PopPicker popPicker;

    private void initDateTimePicker() {
        popPicker = new PopPicker(this);
    }

    private void initStepView() {
        //-----------------------------this data is example and you can also get data from server-----------------------------
        List<StepBean> stepsList = new ArrayList<>();
        StepBean stepBean0 = new StepBean("等待接单", 0);
        StepBean stepBean1 = new StepBean("等待服务", -1);
        StepBean stepBean2 = new StepBean("正在服务", -1);
        StepBean stepBean3 = new StepBean("服务完成", -1);
        stepsList.add(stepBean0);
        stepsList.add(stepBean1);
        stepsList.add(stepBean2);
        stepsList.add(stepBean3);

        setStepsBeanList(stepsList);
        //-----------------------------this data is example and you can also get data from server-----------------------------
        stepView.setStepViewTexts(stepsList)
                .setTextSize(12)//set textSize
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.color33cea6))//设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(getBaseContext(), R.color.color999))//设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.color33cea6))//设置StepsView text字体的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(getBaseContext(), R.color.color999))//设置StepsView text未完字体的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.pay_on))//设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.pay_off))//设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(getBaseContext(), R.drawable.pay_on));//设置StepsViewIndicator AttentionIcon
    }


    private void setStepsBeanList(List<StepBean> stepsList) {
        hintTv.setVisibility(View.GONE);
        orderCancelTv.setVisibility(View.GONE);
        showAddCost(!StringUtils.isEmpty(bean.getMaintenance_projects()));//是否显示增加项目费用
        int position = 0;
        switch (bean.getState()) {
            case "2":
                goTv.setText(R.string.go);
                position = 1;
                if (!StringUtils.isEmpty(bean.getMake_an_appointment())) {//
                    FormatTime formatTime = new FormatTime();
                    hintTv.setVisibility(View.VISIBLE);
                    long v = Long.parseLong(bean.getMake_an_appointment());
                    v = v - 3 * 60 * 60;
                    formatTime.setTime(v + "");
                    String hint = "在接单后，请打电话给客户，约定好上门时间，在" + formatTime.getTime() + "后才可以点击出发哦，如果在出发前取消订单，会影响后面派单机会，感谢您的配合！";
                    hintTv.setText(hint);
                }
                orderCancelTv.setVisibility(View.VISIBLE);

                if (StringUtils.isSame(bean.getTotime(), CommonUtils.isZero)) {//要确认订单
                    initDateTimePicker();
                    goTv.setText(R.string.affirm_order);
                    position = 0;
                    subscribeTimeTv.setText(subscribeTimeTv.getText().toString() + "(点击修改)");//预约上门时间
                }
                break;
            case "3":
                position = 1;
                goTv.setText(R.string.have_left);
                orderCancelTv.setVisibility(View.VISIBLE);
                break;
            case "4":
                showAddCost(true);//是否显示增加项目费用
                position = 1;
                if (!StringUtils.isEmpty(bean.getMaintenance_projects())) {
                    goTv.setText(R.string.client_affirm);
                } else {
                    goTv.setText(R.string.arrive);
                }
                hintTv.setVisibility(View.VISIBLE);
                hintTv.setText("您到达客户现场后，如果需要产生配件或者工时费用，得要同客户沟通谈好，您可以线下收客户增加费用，或者叫客户走线上付款，严格按我们收费标准去收费，如果有客户投诉，平台会有严格惩罚制度，感谢您的配合。");
                orderCancelTv.setVisibility(View.VISIBLE);
                break;
            case "5":
                position = 2;
                goTv.setText(R.string.serving);
                hintTv.setVisibility(View.VISIBLE);
                hintTv.setText("服务完成后，请上传现场拍照照片，如果需要付余款客户，提醒用户付余款，给好评哦，感谢您的配合。");
                initPhotoHelper();
                break;
            case "6":
                position = 1;
                goTv.setText(R.string.agree_serve);
                break;
            case "7":
                position = 3;
                goTv.setText(R.string.serve_done);
                mPhotosSnpl.setVisibility(View.GONE);
                break;
            case "8":
                position = 3;
                goTv.setText(R.string.pay_succeed);
                break;
            case "9":
                position = 3;
                goTv.setText(R.string.order_done);
                orderCancelTv.setVisibility(View.VISIBLE);
                orderCancelTv.setText(R.string.delete_order);
                break;
            case "10":
            case "11":
                position = 3;
                goTv.setText(R.string.order_cancel);
                StepBean bean = stepsList.get(3);
                bean.setName(getString(R.string.order_cancel));
                orderCancelTv.setVisibility(View.VISIBLE);
                orderCancelTv.setText(R.string.delete_order);
                break;
        }
        int size = stepsList.size();
        for (int i = 0; i < size; i++) {
            StepBean bean = stepsList.get(i);
            if (position > i) {
                bean.setState(1);
            } else if (position == i) {
                bean.setState(0);
            } else {
                bean.setState(-1);
            }
        }
        stepView.setPosition(position);
    }

    String state;

    @OnClick({R.id.go_tv, R.id.ll_pics, R.id.ll_describe, R.id.ll_fix_pics, R.id.order_cancel_tv, R.id.ll_add_cost_button, R.id.contact_tv, R.id.see_map_tv, R.id.subscribe_time_tv})
    public void onViewClicked(View view) {
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        switch (view.getId()) {
            case R.id.go_tv://去上门
                String stateString = "";
                switch (bean.getState()) {
                    case "2":
                        if (StringUtils.isSame(bean.getTotime(), CommonUtils.isZero)) {//修改上门时间
                            changeGoTime();
                            return;
                        }
                        if (!CommonUtils.isWithinThreeHours(this, bean.getMake_an_appointment())) {
                            return;
                        }
                        stateString = "确定要上门？";
                        state = "3";
                        break;
                    case "3":
                        if (getPermission()) {//获取定位权限
                            stateString = "";
                            break;
                        }
                        stateString = "确定已到达？";
                        state = "4";
                        break;
                    case "4":
                        if (!StringUtils.isEmpty(bean.getMaintenance_projects())) {
                            return;
                        }
                        List<AddCostBean> list = costHelper.getBeanList();
                        if (!StringUtils.isEmpty(list)) {
                            if (isCost(list)) {//要输入完整的项目名和价格才能往下走
                                return;
                            }
                            double total = costHelper.getTotalPrice();
                            double price = Double.valueOf(bean.getTotal_price());
                            if (total < price) {
                                UIHelper.ToastMessage(this, "你输入增加总费用，低于上门费用，请重新输入");
                                return;
                            }
                            stateString = "你已添加项目，确定要开始服务？确定后等待客户确认";
                        } else {
                            stateString = "你未添加项目，确定要开始服务？";
                        }
                        state = "1010";
                        break;
                    case "5":
                        if (StringUtils.isEmpty(mPhotoHelper)) {
                            return;
                        }
                        if (StringUtils.isEmpty(mPhotoHelper.getData())) {
                            UIHelper.ToastMessage(this, "请上传现场拍照照片");
                            return;
                        }
                        stateString = "确认上传现场拍照照片？";
                        state = "7";
                        break;
                    case "6":
                        stateString = "确定要开始服务？";
                        state = "5";
                        break;
                    case "7":
                    case "8":
                    case "9":
                    case "10":
                    case "11":
                        stateString = "";
                        state = "";
                        break;
                }
                if (StringUtils.isEmpty(stateString)) {
                    return;
                }
                AKDialog.getAlertDialog(this, stateString, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        orderOperation(state);
                    }
                });
                break;
            case R.id.order_cancel_tv://取消订单
                switch (bean.getState()) {
                    case "9":
                    case "10":
                    case "11":
                        AKDialog.getAlertDialog(this, "确定要删除该订单？", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteOrder();
                            }
                        });
                        return;
                }
                IntentUtil.startActivity(this, OrderCancelActivity.class, id);
                break;
            case R.id.ll_add_cost_button://增加上门费
                if (StringUtils.isEmpty(bean.getMaintenance_projects())) {//没提交过项目费的可以点击增加
                    costHelper.addCostItem();
                    costNumTv.setText(String.format(getString(R.string.items_cost_num), Integer.toString(costHelper.getNum())));
                    if (StringUtils.isEmpty(costHelper.getBeanList())) {
                        goTv.setText(R.string.arrive);
                    } else {
                        goTv.setText(R.string.add_items);
                    }
                }
                break;
            case R.id.contact_tv://联系客户
                UIHelper.callPhone(this, bean.getPhone());
                break;
            case R.id.see_map_tv://查看地图
                CommonUtils.location(this, bean.getLat(), bean.getLon());
                break;
            case R.id.subscribe_time_tv://修改预约时间
                if (StringUtils.isSame(bean.getTotime(), CommonUtils.isZero)) {
                    showPopPicker();
                }
                break;
            case R.id.ll_pics://报修照片
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                IntentUtil.startActivity(this, FixPicsActivity.class, bundle);
                break;
            case R.id.ll_describe://报修描述
                IntentUtil.startActivity(this, FixDescribeActivity.class, bean.getContent());
                break;
            case R.id.ll_fix_pics://维修照片
                Bundle BundleCompat = new Bundle();
                BundleCompat.putSerializable("bean", bean);
                BundleCompat.putBoolean("fix_pics", true);
                IntentUtil.startActivity(this, FixPicsActivity.class, BundleCompat);
                break;
        }
    }

    private void orderOperation(final String state) {
        if (StringUtils.isSame("4", state)) {//socket连接状态时直接用用户信息里面的定位
            if (AbsBaseWebSocketService.connectStatus == 2) {
                UserBean bean = CommonUtils.getUserBean(this);
                if (StringUtils.isEmpty(bean)) {
                    return;
                }
                lon1 = bean.getLon();
                lat1 = bean.getLat();
                loadState(state);
            } else {
                initBaiDu(state);
            }
        } else if (StringUtils.isSame("7", state)) {//上传现场相片
            new CompressPhotoUtils().CompressPhoto(OrderInfoActivity.this, mPhotoHelper.getData(), new CompressPhotoUtils.CompressCallBack() {
                @Override
                public void success(List<String> list) {
                    if (isFinishing()) {
                        return;
                    }
                    uploadingImgs(list);
                }
            }, "5");
        } else {
            loadState(state);
        }
    }

    private void deleteOrder() {
        LiJiaApi api = new LiJiaApi("app/del_repair_order");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);//
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), getString(R.string.delete_succeed));
                OperationOrderEvent event = new OperationOrderEvent();
                event.setDelete(true);
                EventBus.getDefault().post(event);
                finish();
            }
        });
    }

    //提交上门时间
    private void changeGoTime() {
        String appointment = bean.getMake_an_appointment();
        boolean isMake = StringUtils.isEmpty(appointment) || StringUtils.isSame(appointment, CommonUtils.isZero);
        boolean isTotime = StringUtils.isEmpty(toTime);
        if (isMake && isTotime) {
            showPopPicker();
            UIHelper.ToastMessage(this, getString(R.string.modification_time));
        } else if (!isMake && CommonUtils.isPastTime(bean.getMake_an_appointment())) {//订单上门时间小于现在的时间
            showPopPicker();
            UIHelper.ToastMessage(this, getString(R.string.past_time));
        } else if (!isMake && isTotime) {
            affirmChangeTime(getString(R.string.do_not_modification_time), false);
        } else if (!isTotime) {
            affirmChangeTime(getString(R.string.done_modification_time), true);
        }
    }

    private void showPopPicker() {
        if (popPicker != null) {
            popPicker.show(this);
        }
    }

    /**
     * @param content
     * @param isChange 是否修改了时间  true 修改 false 未修改
     */
    private void affirmChangeTime(String content, final boolean isChange) {
        AKDialog.getAlertDialog(this, content, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (!isChange) {
                    toTime = bean.getMake_an_appointment();
                }
                order();//确认接单
            }
        });
    }

    private void uploadingImgs(List<String> successPath) {
        LiJiaApi api = new LiJiaApi("app/service_order_edit");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("imgs1", JsonUtil.getJSONArrayByList(successPath));
//        api.addParams("toto", 1);
        api.addParams("state", 7);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), "上传成功");
                EventBus.getDefault().post(new OperationOrderEvent());
            }
        });
    }


    private void order() {
        if (StringUtils.isEmpty(id)) {
            id = bean.getId();
        }
        LiJiaApi api = new LiJiaApi("app/service_order_yes");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("totime", toTime);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), getString(R.string.operate_success));
                EventBus.getDefault().post(new OperationOrderEvent());
            }
        });
    }

    //订单操作的时候调用
    @Subscribe
    public void operationOrderEvent(OperationOrderEvent event) {
        if (event.isDelete()) {
            return;
        }
        refresh();
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
            }
        }
    }

    private boolean getPermission() {//是否需要位置权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED);
        }
        return false;
    }


    public void loadState(final String state) {
        if (StringUtils.isEmpty(id)) {
            id = bean.getId();
        }
        LiJiaApi api = new LiJiaApi("app/service_order_edit");
        api.addParams("uid", api.getUserId(this));
        api.addParams("oid", id);
        api.addParams("state", state);
        if (StringUtils.isSame("4", state)) {
            api.addParams("lat1", lat1);// 师傅到达时 纬度
            api.addParams("lon1", lon1);//师傅到达时 经度
        } else if (StringUtils.isSame("1010", state)) {//
            List<AddCostBean> list = costHelper.getBeanList();
            if (!StringUtils.isEmpty(list)) {
                if (isCost(list)) {
                    return;
                }
                api.addParams("state", "");
                api.addParams("maintenance_projects", getMaintenance_projects_array(list));// 附加 维修项目集合(array)
                api.addParams("total_prices", getTotal_prices_array(list));// 附加 维修项目金额(array)
//                api.addParams("toto", 1);//用于
            } else {
                api.addParams("state", "5");//没有曾经额外的项目就直接改状态
            }

        }
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), getString(R.string.operate_success));
                EventBus.getDefault().post(new OperationOrderEvent());
            }
        });
    }


    private boolean isCost(List<AddCostBean> list) {
        int size = list.size();
        for (int i = 0; i < size; i++) {
            AddCostBean bean = list.get(i);
            if (StringUtils.isEmpty(bean.getCostName())) {
                UIHelper.ToastMessage(this, "请输入维修项目" + (i + 1) + "名称");
                return true;
            }
            if (StringUtils.isEmpty(bean.getCost())) {
                UIHelper.ToastMessage(this, "请输入维修项目" + (i + 1) + "价格");
                return true;
            }
        }
        return false;
    }

    private JSONArray getMaintenance_projects_array(List<AddCostBean> list) {
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isEmpty(list)) {
            return jsonArray;//nerver return null
        }
        for (AddCostBean object : list) {
            jsonArray.add(object.getCostName());
        }
        return jsonArray;
    }

    private JSONArray getTotal_prices_array(List<AddCostBean> list) {
        JSONArray jsonArray = new JSONArray();
        if (StringUtils.isEmpty(list)) {
            return jsonArray;//nerver return null
        }
        for (AddCostBean object : list) {
            jsonArray.add(object.getCost());
        }
        return jsonArray;
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (StringUtils.isEmpty(grantResults)) {
            return;
        }
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        } else {
            switch (requestCode) {
                case 100:
                    Toast.makeText(this, "访问被拒绝,无法获取定位信息！", Toast.LENGTH_SHORT).show();//1526985120
                    break;
            }
        }
    }

    String lat1, lon1;

    private void initBaiDu(final String state) {
        new BaiduLocation(this, new BaiduLocation.WHbdLocationListener() {
            @Override
            public void bdLocationListener(LocationClient locationClient, BDLocation location) {
                if (location != null) {
                    locationClient.stop();
                    lat1 = location.getLatitude() + "";
                    lon1 = location.getLongitude() + "";
                    loadState(state);
                } else {//定位失败就 获取用户信息的定位（重新加载）
                    CommonUtils.loadUserInfo(getContext(), new CommonUtils.UserInfoListener() {
                        @Override
                        public void userInfo(UserBean bean) {
                            if (StringUtils.isEmpty(bean)) {
                                return;
                            }
                            lon1 = bean.getLon();
                            lat1 = bean.getLat();
                            loadState(state);
                        }

                        @Override
                        public void error(String error) {
                            initBaiDu(state);
                        }
                    });
                }
            }
        });
    }


    @Override
    public void onPickerSelectListener(String time, long timeLong) {
        goTv.setText("修改上门时间");
        subscribeTimeTv.setText("师傅预约时间：" + time);
        toTime = timeLong + "";
        bean.setMake_an_appointment(toTime);
    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        if (mPhotoHelper != null) {
            mPhotoHelper.onClickAddNinePhotoItem(sortableNinePhotoLayout, view, position, models);
        }
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (mPhotoHelper != null) {
            mPhotoHelper.onClickDeleteNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
        }
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        if (mPhotoHelper != null) {
            mPhotoHelper.onClickNinePhotoItem(sortableNinePhotoLayout, view, position, model, models);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mPhotoHelper != null) {
            mPhotoHelper.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        loadOrderDetails();
    }

    public void refresh() {
        swipeRefreshLayout.setRefreshing(true);
        onRefresh();
    }
}
