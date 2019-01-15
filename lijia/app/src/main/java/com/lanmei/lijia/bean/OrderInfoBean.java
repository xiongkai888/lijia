package com.lanmei.lijia.bean;

/**
 * Created by xkai on 2018/5/11.
 * 师傅收到的订单信息
 */

public class OrderInfoBean {

    /**
     * ID : SERVER
     * TO : 141
     * TYPE : 2
     * MSG : {"oid":292,"classtype":"5","make_an_appointment":"1526015095","platform_product":"普通热水器","total_price":"17","address":"广州市天河区华师西门","content":"咖啡"}
     */

    private String ID;
    private String TO;
    private int TYPE;
    private MSGBean MSG;

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getTO() {
        return TO;
    }

    public void setTO(String TO) {
        this.TO = TO;
    }

    public int getTYPE() {
        return TYPE;
    }

    public void setTYPE(int TYPE) {
        this.TYPE = TYPE;
    }

    public MSGBean getMSG() {
        return MSG;
    }

    public void setMSG(MSGBean MSG) {
        this.MSG = MSG;
    }

    public static class MSGBean {
        /**
         * oid : 292
         * classtype : 5
         * make_an_appointment : 1526015095
         * platform_product : 普通热水器
         * total_price : 17
         * address : 广州市天河区华师西门
         * content : 咖啡
         */

        private String oid;
        private String classtype;
        private String make_an_appointment;
        private String platform_product;
        private String total_price;
        private String address;
        private String content;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getClasstype() {
            return classtype;
        }

        public void setClasstype(String classtype) {
            this.classtype = classtype;
        }

        public String getMake_an_appointment() {
            return make_an_appointment;
        }

        public void setMake_an_appointment(String make_an_appointment) {
            this.make_an_appointment = make_an_appointment;
        }

        public String getPlatform_product() {
            return platform_product;
        }

        public void setPlatform_product(String platform_product) {
            this.platform_product = platform_product;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
