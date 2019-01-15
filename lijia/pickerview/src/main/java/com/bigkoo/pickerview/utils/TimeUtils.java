package com.bigkoo.pickerview.utils;

import com.bigkoo.pickerview.model.IPickerViewData;
import com.bigkoo.pickerview.model.PickerViewData;

import java.util.ArrayList;
import java.util.Calendar;

public class TimeUtils {

    /**
     * 今天 点
     */
    public ArrayList<String> getTodayHourData(){
        int max =currentHour();
        if (max<23/*&&currentMin()>45*/){
            max=max+1;
        }
        ArrayList<String> lists=new ArrayList<>();
        for (int i=max;i<24;i++){
            lists.add(i+"点");
        }
        return lists;
    }

    /**
     * 明天 后天 点
     */
    public ArrayList<String> getHourData(){
        ArrayList<String> lists=new ArrayList<>();
        for (int i=0;i<24;i++){
            lists.add(i+"点");
        }
        return lists;
    }

    /**
     * 明天 后天  分
     */
    private ArrayList<IPickerViewData> getMinData(){
        ArrayList<IPickerViewData> dataArrayList=new ArrayList<>();
        for (int i=0;i<6;i++){
            dataArrayList.add(new PickerViewData((i*10)+"分"));
        }
        return dataArrayList;
    }
    /**
     * 明天 后天
     */
    public ArrayList<ArrayList<IPickerViewData>> getmD(){
        ArrayList<ArrayList<IPickerViewData>> d=new ArrayList<>();
        for (int i=0;i<24;i++){
            d.add(getMinData());
        }
        return d;
    }

    /**
     * 明天 后天  2222
     */
    public ArrayList<ArrayList<IPickerViewData>> getmD2(){
        //14
        int max =currentHour();
//        if (currentMin()>45){
//            max=max+1;
//        }
        int value =24-max;
        ArrayList<ArrayList<IPickerViewData>> d=new ArrayList<>();
        for (int i=0;i<value;i++){
            if (i==0){
                d.add(getTodyMinData());
            }else {
                d.add(getMinData());
            }

        }
        return d;
    }

    /**
     * 明天 后天  分2222
     */
    private ArrayList<IPickerViewData> getTodyMinData(){

        int min = currentMin();
        int current=min/10+1;
//        if (min>35&&min<=45){
//            current =0;
//        }else if (min>45&&min<=55){
//            current=1;
//        } else if (min>55){
//            current=2;
//        }else if (min<=5){
//            current=2;
//        }else if (min>5&&min<=15){
//            current=3;
//        }else if (min>15&&min<=25){
//            current=4;
//        }else if (min>25&&min<=35){
//            current=5;
//        }
//        int max =currentHour();
//        if (max>23&& min>35){
//            current=5;
//        }
        if (current>5)
            current=5;


        ArrayList<IPickerViewData> dataArrayList=new ArrayList<>();
        for (int i=current;i<6;i++){
            dataArrayList.add(new PickerViewData((i*10)+"分"));
        }
        return dataArrayList;
    }

    public int currentMin(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MINUTE);
    }


    public int currentHour(){
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.HOUR_OF_DAY);
    }

    /**
     * @param day 0:现在;
     *            1:今天;
     *            2:明天;
     *            3:后天;
     *
     * @param hour 时
     * @param minute 分
     *
     * @return 时间戳 秒
     * */
    public static long getResultTime(int day,int hour,int minute){
//        TimeZone tz = TimeZone.getDefault();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY,hour);
        cal.set(Calendar.MINUTE,minute);
        cal.set(Calendar.SECOND,0);

        long addTime=0;
        addTime=24*60*60;
        addTime=day*addTime;

        long curtime=cal.getTimeInMillis()/1000+addTime;
//        cal.setTimeInMillis(curtime*1000);

        return curtime;
    }
}
