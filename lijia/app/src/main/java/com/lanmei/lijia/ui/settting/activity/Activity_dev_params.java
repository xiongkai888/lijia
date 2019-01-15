package com.lanmei.lijia.ui.settting.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lanmei.lijia.R;
import com.lanmei.lijia.api.DevApi;
import com.lanmei.lijia.bean.BeanDevParams;
import com.xson.common.app.BaseActivity;
import com.xson.common.bean.NoPageListBean;
import com.xson.common.helper.BeanRequest;
import com.xson.common.helper.HttpClient;
import com.xson.common.utils.L;
import com.xson.common.utils.StringUtils;
import com.xson.common.utils.UIHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import butterknife.InjectView;

/**
 * 设备参数
 */
public class Activity_dev_params extends BaseActivity {

    String devNo;
    @InjectView(R.id.img_right_bt_right)
    ImageView imgRight;
    @InjectView(R.id.img_right_bt_left)
    ImageView imgLeft;
    @InjectView(R.id.list)
    ListView listView;

    private int p = 1;

    private AdapterDevParams adapterDevParams;
    private List<String> devTags;
    private Map<String, BeanValue> devParamses;


    @Override
    public int getContentViewId() {
        return R.layout.activity_dev_params;
    }

    @Override
    protected void initAllMembersView(Bundle savedInstanceState) {
        initIntent(getIntent());
        initUi();
    }

    @Override
    public void initIntent(Intent intent) {
        super.initIntent(intent);
        devNo = intent.getStringExtra("value");
    }

    public void initUi() {
        devTags = new ArrayList<>();
        devParamses = new HashMap<>();
        adapterDevParams = new AdapterDevParams(this, devTags);

        listView.setAdapter(adapterDevParams);
        findViewById(R.id.left_imbt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        imgRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p++;
                requestServerData();
            }
        });

        imgLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                p--;
                if (p < 1) {
                    p = 1;
                    UIHelper.ToastMessage(getContext(), "已是首页");
                    return;
                }
                requestServerData();
            }
        });
        requestServerData();
    }


    public void requestServerData() {
        DevApi api = new DevApi("app/get_device_log");
        api.addParams("device_id", devNo);
        api.addParams("p", p);
        HttpClient.newInstance(this).loadingRequest(api, new BeanRequest.SuccessListener<NoPageListBean<BeanDevParams>>() {
            @Override
            public void onResponse(NoPageListBean<BeanDevParams> response) {
                if (isFinishing()) {
                    return;
                }
                List<BeanDevParams> list = response.data;
                if (StringUtils.isEmpty(list) && p == 1) {
                    UIHelper.ToastMessage(getContext(), "请确认输入设备ID是否正确");
                    return;
                } else if (!StringUtils.isEmpty(list)) {
                    parserReview(list);
                }

            }
        });
    }

    private void parserReview(List<BeanDevParams> params) {
        if (params == null)
            return;

        try {

            devTags.clear();
            devParamses.clear();
            devTags.add("时间");
            int count = params.size();
            BeanValue itemValue = new BeanValue();
            switch (count) {
                case 3:
                    itemValue.value3 = params.get(2).getAddtime();
                case 2:
                    itemValue.value2 = params.get(1).getAddtime();
                case 1:
                    itemValue.value1 = params.get(0).getAddtime();

            }
            devParamses.put(devTags.get(0), itemValue);
            JSONObject item;
            switch (count) {
                case 3:
                    item = new JSONObject(params.get(2).getAll_format());
                    paserData(item, 3);
                case 2:
                    item = new JSONObject(params.get(1).getAll_format());
                    paserData(item, 2);
                case 1:
                    item = new JSONObject(params.get(0).getAll_format());
                    paserData(item, 1);

            }
            adapterDevParams.refreshData(devTags);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void paserData(JSONObject all_format, int group) {

        JSONObject all_format_item = null;
        try {
            Iterator<String> iterKey = all_format.keys();
            while (iterKey.hasNext()) {
                String key = iterKey.next();
                all_format_item = all_format.getJSONObject(key);
                Iterator<String> keyiter = all_format_item.keys();
                while (keyiter.hasNext()) {
                    JSONArray itemArr1 = all_format_item.getJSONArray(keyiter.next());
                    if (key.equals("0")) {
                        for (int n = 0; n < itemArr1.length(); n++) {
                            JSONArray itemArr2 = itemArr1.getJSONArray(n);
                            setItemParams(itemArr2, group);
                        }
                    } else {
                        setItemParams(itemArr1, group);
                    }


                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setItemParams(JSONArray jsonArray, int group) {
        try {
            String tag = jsonArray.getString(0);
            String flag = jsonArray.getString(1);
            String value = jsonArray.getString(3);


            if (flag.length() != 0) {
//                Llog.out("参数",tag+":"+value);
                value = value.substring(0, 1);
                L.d("参数", tag + ":" + value);

                if (tag.equals("高压开关")) {
                    value = value.equals("1") ? "断开" : "闭合";
                } else if (tag.equals("低压开关")) {
                    value = value.equals("1") ? "断开" : "闭合";
                } else if (tag.equals("水流开关")) {
                    value = value.equals("1") ? "断开" : "闭合";
                } else {
                    value = value.equals("1") ? "开" : "关";
                }
            }
            BeanValue devinfo = null;
            if (devParamses.containsKey(tag)) {
                devinfo = devParamses.get(tag);
            }
            if (devinfo == null) {
                devTags.add(tag);
                devinfo = new BeanValue();
            }
            switch (group) {
                case 3:
                    devinfo.value3 = value;
                    break;
                case 2:
                    devinfo.value2 = value;
                    break;
                case 1:
                    devinfo.value1 = value;
                    break;

            }
            devParamses.put(tag, devinfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public class AdapterDevParams extends BaseAdapter {

        Context context;
        List<String> list;

        public AdapterDevParams(Context context, List<String> list) {
            this.context = context;
            this.list = list;
        }

        @Override
        public int getCount() {
            return StringUtils.isEmpty(list) ? 0 : list.size();
        }

        @Override
        public String getItem(int position) {
            if (StringUtils.isEmpty(list)) {
                return null;
            }
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(context).inflate(R.layout.item_dev_info, parent, false);
                viewHolder.txtTag = (TextView) convertView.findViewById(R.id.txt_tag);
                viewHolder.txtValue_1 = (TextView) convertView.findViewById(R.id.txt_value_1);
                viewHolder.txtValue_2 = (TextView) convertView.findViewById(R.id.txt_value_2);
                viewHolder.txtValue_3 = (TextView) convertView.findViewById(R.id.txt_value_3);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            String tag = getItem(position);
            viewHolder.txtTag.setText(tag);
            BeanValue item = devParamses.get(tag);
            viewHolder.txtValue_1.setText(item.value1);
            viewHolder.txtValue_2.setText(item.value2);
            viewHolder.txtValue_3.setText(item.value3);
            return convertView;
        }

        protected class ViewHolder {
            TextView txtTag;
            TextView txtValue_1;
            TextView txtValue_2;
            TextView txtValue_3;
        }

        public void refreshData(List<String> list) {
            this.list = list;
            notifyDataSetChanged();
        }

    }

    private class BeanValue {
        public String value1, value2, value3;
    }
}
