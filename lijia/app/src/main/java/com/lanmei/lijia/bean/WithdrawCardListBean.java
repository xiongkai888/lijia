package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * Created by xkai on 2018/5/3.
 * 申请提现列表
 */

public class WithdrawCardListBean extends AbsListBean {


    public String data;

    public List<CardBean> dataList;

    @Override
    public List<CardBean> getDataList() {
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode(data));
            dataList = JSON.parseArray(Des.decode(data), CardBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    public static class CardBean {

        /**
         * id : 6
         * uid : 92
         * banks_id : 0
         * banks_name : 中国工商银行
         * realname : 熊猫
         * banks_no : 1587556546954565545
         * provinceid : 0
         * cityid : 0
         * status : 1
         * addtime : 2017-07-25 17:18:02
         */

        private String id;
        private String uid;
        private String banks_id;
        private String banks_name;
        private String realname;
        private String banks_no;
        private String provinceid;
        private String cityid;
        private String status;
        private String addtime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getBanks_id() {
            return banks_id;
        }

        public void setBanks_id(String banks_id) {
            this.banks_id = banks_id;
        }

        public String getBanks_name() {
            return banks_name;
        }

        public void setBanks_name(String banks_name) {
            this.banks_name = banks_name;
        }

        public String getRealname() {
            return realname;
        }

        public void setRealname(String realname) {
            this.realname = realname;
        }

        public String getBanks_no() {
            return banks_no;
        }

        public void setBanks_no(String banks_no) {
            this.banks_no = banks_no;
        }

        public String getProvinceid() {
            return provinceid;
        }

        public void setProvinceid(String provinceid) {
            this.provinceid = provinceid;
        }

        public String getCityid() {
            return cityid;
        }

        public void setCityid(String cityid) {
            this.cityid = cityid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }
    }

}
