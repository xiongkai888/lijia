package com.bigkoo.pickerview.view;

import android.content.Context;
import android.util.Log;

import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.model.IPickerViewData;
import com.bigkoo.pickerview.model.TimeBean;
import com.bigkoo.pickerview.utils.TimeUtils;

import java.util.ArrayList;

public class PopPicker {
    private Context mContext;
    OptionsPickerView pvOptions;
    private ArrayList<TimeBean> options1Items;
    private ArrayList<ArrayList<String>> options2Items;
    private ArrayList<ArrayList<ArrayList<IPickerViewData>>> options3Items;

    public PopPicker(Context mContext) {
        this.mContext = mContext;
        options1Items = new ArrayList<>();
        options2Items = new ArrayList<>();
        options3Items = new ArrayList<>();
        initOptionsPicker();
    }

    private void initOptionsPicker() {
        //选项选择器
        pvOptions = new OptionsPickerView(mContext);
        TimeUtils timeUtils = new TimeUtils();
        //选项1
//        options1Items.add(new TimeBean("现在"));
        final boolean hasToday = timeUtils.currentHour() < 23;
        if (hasToday)
            options1Items.add(new TimeBean("今天"));
        options1Items.add(new TimeBean("明天"));
        options1Items.add(new TimeBean("后天"));


//        //选项 1 2
//        ArrayList<String> options2Items_01=new ArrayList<>();
//        options2Items_01.add("--");
        //22

        ArrayList<String> options2Items_02 = timeUtils.getTodayHourData();
        //32
        ArrayList<String> options2Items_03 = timeUtils.getHourData();
        //32
        ArrayList<String> options2Items_04 = timeUtils.getHourData();

//        options2Items.add(options2Items_01);
        if (hasToday)
            options2Items.add(options2Items_02);
        options2Items.add(options2Items_03);
        options2Items.add(options2Items_04);


        //选项3
//        ArrayList<ArrayList<IPickerViewData>> options3Items_01 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_02 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_03 = new ArrayList<>();
        ArrayList<ArrayList<IPickerViewData>> options3Items_04 = new ArrayList<>();


//        ArrayList<IPickerViewData> options3Items_01_01=new ArrayList<>();
//        options3Items_01_01.add(new PickerViewData("--"));
//        options3Items_01.add(options3Items_01_01);
        options3Items_02 = timeUtils.getmD2();
        options3Items_03 = timeUtils.getmD();
        options3Items_04 = timeUtils.getmD();


//        options3Items.add(options3Items_01);
        if (hasToday)
            options3Items.add(options3Items_02);
        options3Items.add(options3Items_03);
        options3Items.add(options3Items_04);


        //三级联动效果
        pvOptions.setPicker(options1Items, options2Items, options3Items, true);
        //设置选择的三级单位
//        pwOptions.setLabels("省", "市", "区");
        pvOptions.setTitle(" ");
        pvOptions.setCyclic(false, false, false);
        //设置默认选中的三级项目
        //监听确定选择按钮
        pvOptions.setSelectOptions(0, 0, 0);
        pvOptions.setOnoptionsSelectListener(new OptionsPickerView.OnOptionsSelectListener() {

            @Override
            public void onOptionsSelect(int options1, int option2, int options3) {
                //返回的分别是三个级别的选中位置
                String hourStr = options2Items.get(options1).get(option2);
                String minuteStr = options3Items.get(options1).get(option2).get(options3).getPickerViewText();
                String tx = options1Items.get(options1).getPickerViewText()
                        + hourStr
                        + minuteStr;
//                txtRepairTime.setText(tx);
                int day, hour = 0, minute = 0;
                day = options1;
                if (!hasToday)
                    day += 1;
                hour = Integer.parseInt(hourStr.substring(0, hourStr.length() - 1));
                minute = Integer.parseInt(minuteStr.substring(0, minuteStr.length() - 1));
                long repairTime = TimeUtils.getResultTime(day, hour, minute);
                Log.i("预约时间：", tx + "：" + repairTime);
                if (pickerSelectListener != null) {
                    pickerSelectListener.onPickerSelectListener(tx, repairTime);
                }
            }
        });

    }

    private PickerSelectListener pickerSelectListener;

    public void show(PickerSelectListener pickerSelectListener) {
        this.pickerSelectListener = pickerSelectListener;
        pvOptions.show();
    }

    public interface PickerSelectListener {
        public void onPickerSelectListener(String time, long timeLong);
    }
}
