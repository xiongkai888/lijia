package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai
 */
public class HotCityListBean extends AbsListBean {


    public String data;

    public List<HotCityBean> dataList;

    @Override
    public List<HotCityBean> getDataList() {
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode(data));
            dataList = JSON.parseArray(Des.decode(data), HotCityBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 热门城市
     */

    public static class HotCityBean {

        /**
         * id : 440100
         * pid : 440000
         * name : 广州市
         * sort : 99
         * sale : 1
         * price : 0.00
         * addtime : null
         * uptime : 1524021135
         * lon : 113.271431
         * lat : 23.135361
         * state : 1
         * hot : 1
         */

        private String id;
        private String pid;
        private String name;
        private String sort;
        private String sale;
        private String price;
        private String addtime;
        private String uptime;
        private String lon;
        private String lat;
        private String state;
        private String hot;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getSale() {
            return sale;
        }

        public void setSale(String sale) {
            this.sale = sale;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
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

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getHot() {
            return hot;
        }

        public void setHot(String hot) {
            this.hot = hot;
        }
    }
}