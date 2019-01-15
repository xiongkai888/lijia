package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.io.Serializable;
import java.util.List;

/**
 * @author xkai 设置接单
 */
public class BusinessOpportuntyListBean extends AbsListBean {


    public String data;


    public List<BusinessOpportuntyBean> dataList;

    public void setCount(String count) {
        this.count = count;
    }

    public String getCount() {
        return count;
    }

    /**
     * count : 7
     */

    private String count;

    @Override
    public List<BusinessOpportuntyBean> getDataList() {
        try {
            L.d("BeanRequest", "列表解密后：" + Des.decode(data));
            dataList = JSON.parseArray(Des.decode(data), BusinessOpportuntyBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }


    /**
     * Created by xkai on 2018/4/25.
     * 最低上门费用
     */

    public static class BusinessOpportuntyBean implements Serializable{

        /**
         * id : 103
         * addtime : 1530870985
         * uptime : 1530870985
         * endtime : 1530870985
         * state : 1
         * platform_product : 0
         * dustbin_id : 
         * imgs : 
         * content : 这么大
         * lon : 113.35077193284
         * lat : 23.147591053033
         * address : 广东省广州市天河区天河北路703号
         * type : 1
         * make_an_appointment : 1530871200
         * total_price : 1.00
         * pay_type : 6
         * userid : 5
         * member_id : null
         * pay_status : 1
         * uid : 162
         * phone : 13560251768
         * overdue : 1
         * classtype : 11
         * ordertime : 1530871141
         * members : null
         * pay_no : 20180706MDZZ6493
         * pick_up_time : null
         * lon1 : null
         * lat1 : null
         * ontime : null
         * imgs1 : null
         * maintenance_projects : null
         * total_prices : null
         * classtypename : 油烟机维修
         * appid : 426341361
         * depart_for_time : null
         * starttime : null
         * endordertime : null
         * is_del : 0
         * is_evaluation : 0
         * totime : 0
         * pay_state : 0
         * two_pay_no : 0
         * endtotal_price : 0.00
         * two_endtime : 0
         * send_members : 148,171,199,,,
         * send_num : 3
         * city : 广州市
         * refund : 0.00
         * refund_state : 0
         * refund_time : 0
         * user : {"id":"162","username":"u_000162","nickname":"蓝美科技","realname":null,"pic":"http://images.itlanmei.cn/162/1526571082.png","user_type":"1","status":"1"}
         */

        private String id;
        private String addtime;
        private String uptime;
        private String endtime;
        private String state;
        private String platform_product;
        private String dustbin_id;
        private String imgs;
        private String content;
        private String lon;
        private String lat;
        private String address;
        private String type;
        private String make_an_appointment;
        private String total_price;
        private String pay_type;
        private String userid;
        private String member_id;
        private String pay_status;
        private String uid;
        private String phone;
        private String overdue;
        private String classtype;
        private String ordertime;
        private String members;
        private String pay_no;
        private String pick_up_time;
        private String lon1;
        private String lat1;
        private String ontime;
        private String imgs1;
        private String maintenance_projects;
        private String total_prices;
        private String classtypename;
        private String appid;
        private String depart_for_time;
        private String starttime;
        private String endordertime;
        private String is_del;
        private String is_evaluation;
        private String totime;
        private String pay_state;
        private String two_pay_no;
        private String endtotal_price;
        private String two_endtime;
        private String send_members;
        private String send_num;
        private String city;
        private String refund;
        private String refund_state;
        private String refund_time;
        private UserBean user;

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

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPlatform_product() {
            return platform_product;
        }

        public void setPlatform_product(String platform_product) {
            this.platform_product = platform_product;
        }

        public String getDustbin_id() {
            return dustbin_id;
        }

        public void setDustbin_id(String dustbin_id) {
            this.dustbin_id = dustbin_id;
        }

        public String getImgs() {
            return imgs;
        }

        public void setImgs(String imgs) {
            this.imgs = imgs;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
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

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getMake_an_appointment() {
            return make_an_appointment;
        }

        public void setMake_an_appointment(String make_an_appointment) {
            this.make_an_appointment = make_an_appointment;
        }

        public String getTotal_price() {
            return total_price;
        }

        public void setTotal_price(String total_price) {
            this.total_price = total_price;
        }

        public String getPay_type() {
            return pay_type;
        }

        public void setPay_type(String pay_type) {
            this.pay_type = pay_type;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getPay_status() {
            return pay_status;
        }

        public void setPay_status(String pay_status) {
            this.pay_status = pay_status;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getOverdue() {
            return overdue;
        }

        public void setOverdue(String overdue) {
            this.overdue = overdue;
        }

        public String getClasstype() {
            return classtype;
        }

        public void setClasstype(String classtype) {
            this.classtype = classtype;
        }

        public String getOrdertime() {
            return ordertime;
        }

        public void setOrdertime(String ordertime) {
            this.ordertime = ordertime;
        }

        public String getMembers() {
            return members;
        }

        public void setMembers(String members) {
            this.members = members;
        }

        public String getPay_no() {
            return pay_no;
        }

        public void setPay_no(String pay_no) {
            this.pay_no = pay_no;
        }

        public String getPick_up_time() {
            return pick_up_time;
        }

        public void setPick_up_time(String pick_up_time) {
            this.pick_up_time = pick_up_time;
        }

        public String getLon1() {
            return lon1;
        }

        public void setLon1(String lon1) {
            this.lon1 = lon1;
        }

        public String getLat1() {
            return lat1;
        }

        public void setLat1(String lat1) {
            this.lat1 = lat1;
        }

        public String getOntime() {
            return ontime;
        }

        public void setOntime(String ontime) {
            this.ontime = ontime;
        }

        public String getImgs1() {
            return imgs1;
        }

        public void setImgs1(String imgs1) {
            this.imgs1 = imgs1;
        }

        public String getMaintenance_projects() {
            return maintenance_projects;
        }

        public void setMaintenance_projects(String maintenance_projects) {
            this.maintenance_projects = maintenance_projects;
        }

        public String getTotal_prices() {
            return total_prices;
        }

        public void setTotal_prices(String total_prices) {
            this.total_prices = total_prices;
        }

        public String getClasstypename() {
            return classtypename;
        }

        public void setClasstypename(String classtypename) {
            this.classtypename = classtypename;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getDepart_for_time() {
            return depart_for_time;
        }

        public void setDepart_for_time(String depart_for_time) {
            this.depart_for_time = depart_for_time;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndordertime() {
            return endordertime;
        }

        public void setEndordertime(String endordertime) {
            this.endordertime = endordertime;
        }

        public String getIs_del() {
            return is_del;
        }

        public void setIs_del(String is_del) {
            this.is_del = is_del;
        }

        public String getIs_evaluation() {
            return is_evaluation;
        }

        public void setIs_evaluation(String is_evaluation) {
            this.is_evaluation = is_evaluation;
        }

        public String getTotime() {
            return totime;
        }

        public void setTotime(String totime) {
            this.totime = totime;
        }

        public String getPay_state() {
            return pay_state;
        }

        public void setPay_state(String pay_state) {
            this.pay_state = pay_state;
        }

        public String getTwo_pay_no() {
            return two_pay_no;
        }

        public void setTwo_pay_no(String two_pay_no) {
            this.two_pay_no = two_pay_no;
        }

        public String getEndtotal_price() {
            return endtotal_price;
        }

        public void setEndtotal_price(String endtotal_price) {
            this.endtotal_price = endtotal_price;
        }

        public String getTwo_endtime() {
            return two_endtime;
        }

        public void setTwo_endtime(String two_endtime) {
            this.two_endtime = two_endtime;
        }

        public String getSend_members() {
            return send_members;
        }

        public void setSend_members(String send_members) {
            this.send_members = send_members;
        }

        public String getSend_num() {
            return send_num;
        }

        public void setSend_num(String send_num) {
            this.send_num = send_num;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRefund() {
            return refund;
        }

        public void setRefund(String refund) {
            this.refund = refund;
        }

        public String getRefund_state() {
            return refund_state;
        }

        public void setRefund_state(String refund_state) {
            this.refund_state = refund_state;
        }

        public String getRefund_time() {
            return refund_time;
        }

        public void setRefund_time(String refund_time) {
            this.refund_time = refund_time;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 162
             * username : u_000162
             * nickname : 蓝美科技
             * realname : null
             * pic : http://images.itlanmei.cn/162/1526571082.png
             * user_type : 1
             * status : 1
             */

            private String id;
            private String username;
            private String nickname;
            private String realname;
            private String pic;
            private String user_type;
            private String status;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUsername() {
                return username;
            }

            public void setUsername(String username) {
                this.username = username;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getRealname() {
                return realname;
            }

            public void setRealname(String realname) {
                this.realname = realname;
            }

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }

            public String getUser_type() {
                return user_type;
            }

            public void setUser_type(String user_type) {
                this.user_type = user_type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }
    }
}