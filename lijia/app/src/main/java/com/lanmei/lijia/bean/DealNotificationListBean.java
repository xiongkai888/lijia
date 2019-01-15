package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai
 * 交易通知
 */
public class DealNotificationListBean extends AbsListBean {


    public String data;

    public List<DealNotificationBean> dataList;

    @Override
    public List<DealNotificationBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), DealNotificationBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/6/7.
     * 交易通知
     */

    public static class DealNotificationBean {

        /**
         * id : 13
         * addtime : 1528253970
         * uptime : 1528253970
         * oid : 398
         * uid : 141
         * oemid : 5
         * type : 0
         * content :  您有一条新的订单，请尽快确认!
         * see_state : 0
         * is_del : 0
         * title : 您收到了一条 订单推送。
         */

        private String id;
        private String addtime;
        private String uptime;
        private String oid;
        private String uid;
        private String oemid;
        private String type;
        private String content;
        private String see_state;
        private String is_del;
        private String title;
        private String validity;//0|1=>未超时|已经超时

        public void setValidity(String validity) {
            this.validity = validity;
        }

        public String getValidity() {
            return validity;
        }

        private boolean isEdit;

        public boolean isEdit() {
            return isEdit;
        }

        public void setEdit(boolean edit) {
            isEdit = edit;
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

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getOemid() {
            return oemid;
        }

        public void setOemid(String oemid) {
            this.oemid = oemid;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getSee_state() {
            return see_state;
        }

        public void setSee_state(String see_state) {
            this.see_state = see_state;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}