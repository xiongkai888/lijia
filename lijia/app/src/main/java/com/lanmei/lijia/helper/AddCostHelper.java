package com.lanmei.lijia.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.event.AddCostBean;
import com.lanmei.lijia.utils.SimpleTextWatcher;
import com.xson.common.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by xkai on 2018/5/10.
 * 增加上门费
 */

public class AddCostHelper {

    private Context context;
    private LinearLayout root;
    private List<AddCostBean> beanList;
    boolean isCanAdd = true;

    public AddCostHelper(Context context, LinearLayout root) {
        this.context = context;
        this.root = root;
        this.root.removeAllViews();
        beanList = new ArrayList<>();
//        addCostItem();
    }

    public void addCostItem() {
        beanList.add(new AddCostBean());
        addView(beanList.size() - 1);
    }

    public void setData(List<String> maintenance_projects, List<String> total_prices) {
        isCanAdd = false;
//        this.maintenance_projects = maintenance_projects;
//        this.total_prices = total_prices;
        beanList = null;
        beanList = new ArrayList<>();
        int size = maintenance_projects.size();
        for (int i = 0; i < size; i++) {
            AddCostBean bean = new AddCostBean();
            bean.setCostName(maintenance_projects.get(i));
            bean.setCost(total_prices.get(i));
            beanList.add(bean);
        }
        refreshAddCostData();
    }

    private void addView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_cost, null);
        root.addView(view);
        new ViewHolder(view, position);
    }

    public class ViewHolder {

        @InjectView(R.id.position_tv)
        TextView positionTv;
        @InjectView(R.id.cost_name_et)
        EditText costNameEt;
        @InjectView(R.id.cost_et)
        EditText costEt;
        @InjectView(R.id.delete_iv)
        ImageView deleteIv;
        AddCostBean bean;

        public ViewHolder(View view, final int position) {
            ButterKnife.inject(this, view);
            bean = beanList.get(position);
            positionTv.setText((position + 1) + "");
            costNameEt.setText(bean.getCostName());
            costEt.setText(bean.getCost());
            costNameEt.setFocusable(isCanAdd);
            costEt.setFocusable(isCanAdd);
            costNameEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setCostName(s + "");
                }
            });
            costEt.addTextChangedListener(new SimpleTextWatcher() {
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    bean.setCost(s + "");
                    if (l != null) {
                        l.priceChange(getTotalPrice());
                    }
                }
            });
            if (isCanAdd) {
                deleteIv.setVisibility(View.VISIBLE);
                deleteIv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isCanAdd) {
                            return;
                        }
                        beanList.remove(position);
                        refreshAddCostData();
                        if (l != null) {
                            l.priceChange(getTotalPrice());
                            l.getNum(getNum());
                        }
                    }
                });
            }else {
                deleteIv.setVisibility(View.GONE);
            }
        }
    }

    private void refreshAddCostData() {
        root.removeAllViews();
        if (StringUtils.isEmpty(beanList)) {
            return;
        }
        int size = beanList.size();
        for (int i = 0; i < size; i++) {
            addView(i);
        }
    }

    public List<AddCostBean> getBeanList() {
        return beanList;
    }

    public double getTotalPrice() {
        if (StringUtils.isEmpty(beanList)) {
            return 0;
        }
        double price = 0;
        int size = beanList.size();
        for (int i = 0; i < size; i++) {
            AddCostBean bean = beanList.get(i);
            if (!StringUtils.isEmpty(bean.getCost())) {
                price += Double.valueOf(bean.getCost());
            }
        }
        return price;
    }

    public int getNum(){
        return StringUtils.isEmpty(beanList)?0:beanList.size();
    }

    PriceChangeListener l;

    public void setPriceChangeListener(PriceChangeListener l) {
        this.l = l;
    }

    public interface PriceChangeListener {
        void priceChange(double price);
        void getNum(int num);
    }

}
