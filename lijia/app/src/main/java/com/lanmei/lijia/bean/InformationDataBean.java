package com.lanmei.lijia.bean;

import com.lanmei.lijia.utils.JsonUtil;
import com.xson.common.bean.DataBean;
import com.xson.common.utils.des.Des;

/**
 * Created by xkai on 2018/6/7.
 */

public class InformationDataBean<T> extends DataBean<T>{

    private String seecount;

    public void setSeecount(String seecount) {
        this.seecount = seecount;
    }

    public String getSeecount() {
        return seecount;
    }

    public InformationBean getData(){
        try {
            InformationBean  bean = JsonUtil.jsonToBean(Des.decode((String) data), InformationBean.class);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static class InformationBean{

        /**
         * id : 11
         * addtime : 1528253761
         * uptime : 1528353938
         * oid : 397
         * uid : 141
         * oemid : 5
         * type : 0
         * content :  您有一条新的订单，请尽快确认!
         * see_state : 0
         * is_del : 1
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
    }
}
