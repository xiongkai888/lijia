package com.lanmei.lijia.ui.home.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.LiJiaApi;
import com.lanmei.lijia.bean.SetOrderListBean;
import com.lanmei.lijia.utils.CommonUtils;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.BaseBean;
import com.xson.common.bean.UserBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;
import com.xson.common.utils.des.Des;
import com.xson.common.widget.CenterTitleToolbar;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

/**
 * 接单设置
 */
public class OrderReceivingSetActivity extends BaseActivity {

    @InjectView(R.id.toolbar)
    CenterTitleToolbar toolbar;
    @InjectView(R.id.money_tv)
    TextView moneyTv;
    @InjectView(R.id.distance_tv)
    TextView distanceTv;
    @InjectView(R.id.switch_iv)
    ImageView switchIv;
    int switchNum = 0;
    NumberPicker pickerMoney;
    NumberPicker pickerDistance;

//    TimePicker timePicker;


    String money;
    String distance;

    @Override
    public int getContentViewId() {
        return R.layout.activity_order_receiving_set;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setTitle(R.string.order_receiving_set);
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.back);

        loadMoney();
        loadDistance();

        UserBean bean = CommonUtils.getUserBean(this);
        if (StringUtils.isEmpty(bean)) {
            return;
        }
        money = bean.getMinimum_the_door();
        distance = bean.getService_range();
        if (!StringUtils.isEmpty(money)){
            moneyTv.setText(String.format(getString(R.string.money), money));
        }
        if (!StringUtils.isEmpty(distance)){
            distanceTv.setText(String.format(getString(R.string.distance), distance));
        }
        String openDay = bean.getOpen_day();
        if (StringUtils.isSame(CommonUtils.isOne, openDay)) {
            switchNum = 1;
            switchIv.setImageResource(R.drawable.switch_on);
        } else {
            switchNum = 0;
            switchIv.setImageResource(R.drawable.switch_off);
        }
    }

    private void loadMoney() {
        LiJiaApi api = new LiJiaApi("app/classtab");
        api.addParams("tablename", "Minimum_the_door");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<SetOrderListBean>() {
            @Override
            public void onResponse(SetOrderListBean response) {
                if (isFinishing()) {
                    return;
                }
                List<SetOrderListBean.OrderBean> list = response.getDataList();
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                initMoneyPicker(list);
                try {
                    L.d("BeanRequest", Des.decode(response.data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadDistance() {
        LiJiaApi api = new LiJiaApi("app/classtab");
        api.addParams("tablename", "Service_range");
        HttpClient.newInstance(this).request(api, new BeanRequest.SuccessListener<SetOrderListBean>() {
            @Override
            public void onResponse(SetOrderListBean response) {
                if (isFinishing()) {
                    return;
                }
                List<SetOrderListBean.OrderBean> list = response.getDataList();
                if (StringUtils.isEmpty(list)) {
                    return;
                }
                initDistancePicker(list);
                try {
                    L.d("BeanRequest", Des.decode(response.data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initMoneyPicker(List<SetOrderListBean.OrderBean> list) {
        List<Number> numberList = getNumberList(list);
        if (StringUtils.isEmpty(numberList)) {
            return;
        }
        pickerMoney = new NumberPicker(this);
        pickerMoney.setOffset(3);//偏移量
        pickerMoney.setItems(numberList);
        pickerMoney.setLabel("元");
        pickerMoney.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                money = item;
                L.d("BeanRequest", "money:" + money);
                moneyTv.setText(String.format(getString(R.string.money), money));
            }
        });
    }

    private List<Number> getNumberList(List<SetOrderListBean.OrderBean> list) {
        List<Number> stringList = new ArrayList<>();
        int size = list.size();
        for (int i = 0; i < size; i++) {
            SetOrderListBean.OrderBean bean = list.get(i);
            if (!StringUtils.isEmpty(bean) && !StringUtils.isEmpty(bean.getSetval())) {
                Number number = new Integer(bean.getSetval());
                stringList.add(number);
            }
        }
        return stringList;
    }

    private void initDistancePicker(List<SetOrderListBean.OrderBean> list) {
        List<Number> numberList = getNumberList(list);
        if (StringUtils.isEmpty(numberList)) {
            return;
        }
        pickerDistance = new NumberPicker(this);
        pickerDistance.setOffset(3);//偏移量
        pickerDistance.setItems(numberList);
        pickerDistance.setLabel("米");
        pickerDistance.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                distance = item;
                L.d("BeanRequest", "distance:" + distance);
                distanceTv.setText(String.format(getString(R.string.distance), distance));
            }
        });
    }


    @OnClick({R.id.ll_money, R.id.ll_distance, R.id.switch_iv, R.id.save_bt})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_money:
                if (pickerMoney != null) {
                    pickerMoney.show();
                }
                break;
            case R.id.ll_distance:
                if (pickerDistance != null) {
                    pickerDistance.show();
                }
                break;
            case R.id.switch_iv:
                if (switchNum == 0) {
                    switchNum = 1;
                    switchIv.setImageResource(R.drawable.switch_on);
                } else {
                    switchNum = 0;
                    switchIv.setImageResource(R.drawable.switch_off);
                }
                break;
            case R.id.save_bt:
                save();
                break;
        }
    }

    private void save() {
        if (StringUtils.isEmpty(money)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_minimum_fee));
            return;
        }
        if (StringUtils.isEmpty(distance)) {
            UIHelper.ToastMessage(this, getString(R.string.choose_minimum_distance));
            return;
        }
        LiJiaApi api = new LiJiaApi("app/order_to_set_save");
        api.addParams("service_range", distance);//服务范围
        api.addParams("minimum_the_door", money);//最低上门费用
        api.addParams("open_day", switchNum);//是否当天处理 1|0
        api.addParams("uid", api.getUserId(this));
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<BaseBean>() {
            @Override
            public void onResponse(BaseBean response) {
                if (isFinishing()) {
                    return;
                }
                UIHelper.ToastMessage(getContext(), response.getMsg());
                CommonUtils.loadUserInfo(getContext(), null);
                finish();
            }
        });
    }
}
