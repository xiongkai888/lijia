package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai 设置接单
 */
public class SetOrderListBean extends AbsListBean {


    public String data;

    public List<OrderBean> dataList;

    @Override
    public List<OrderBean> getDataList() {
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode(data));
            dataList = JSON.parseArray(Des.decode(data), OrderBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/25.
     * 最低上门费用
     */

    public static class OrderBean {

        /**
         * id : 103
         * addtime : 1524646346
         * uptime : 1524646346
         * tablename : Minimum_the_door
         * classname : 最低上门费用
         * state : 1
         * setval : 20
         * userid : 5
         */

        private String id;
        private String addtime;
        private String uptime;
        private String tablename;
        private String classname;
        private String state;
        private String setval;
        private String userid;

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

        public String getTablename() {
            return tablename;
        }

        public void setTablename(String tablename) {
            this.tablename = tablename;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getSetval() {
            return setval;
        }

        public void setSetval(String setval) {
            this.setval = setval;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }
    }
}