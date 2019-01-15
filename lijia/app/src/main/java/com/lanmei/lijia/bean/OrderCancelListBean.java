package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.io.Serializable;
import java.util.List;

/**
 * @author xkai
 */
public class OrderCancelListBean extends AbsListBean {


    public String data;

    public List<OrderCancelBean> dataList;

    @Override
    public List<OrderCancelBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), OrderCancelBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 热门城市
     */

    public static class OrderCancelBean implements Serializable {

        private boolean isChoose;
        /**
         * id : 3
         * addtime : 1526547624
         * uptime : 1526547624
         * state : 1
         * title : 来得慢
         * oemid : 5
         * judge : 0
         * order_by : 0
         * io : 1
         */

        private String id;
        private String addtime;
        private String uptime;
        private String state;
        private String title;
        private String oemid;
        private String judge;
        private String order_by;
        private String io;

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getOemid() {
            return oemid;
        }

        public void setOemid(String oemid) {
            this.oemid = oemid;
        }

        public String getJudge() {
            return judge;
        }

        public void setJudge(String judge) {
            this.judge = judge;
        }

        public String getOrder_by() {
            return order_by;
        }

        public void setOrder_by(String order_by) {
            this.order_by = order_by;
        }

        public String getIo() {
            return io;
        }

        public void setIo(String io) {
            this.io = io;
        }
    }

}