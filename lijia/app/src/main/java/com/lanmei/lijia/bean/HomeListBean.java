package com.lanmei.lijia.bean;

import com.lanmei.lijia.utils.JsonUtil;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.io.Serializable;
import java.util.List;

/**
 * @author xkai
 */
public class HomeListBean extends AbsListBean {


    public String data;

    public List<HomeBean.RepairBean> dataList;

    @Override
    public List<HomeBean.RepairBean> getDataList() {
        return null;
    }

    public HomeBean getHomeBean(){
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            HomeBean bean =  JsonUtil.jsonToBean(Des.decode(data), HomeBean.class);
            return bean;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 热门城市
     */

    public static class HomeBean implements Serializable {


        /**
         * id : 141
         * username : 我的天啊
         * nickname : u_18778277461
         * realname : null
         * user_type : 5
         * user_type_fix : 0
         * pic : http://image-znsc.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180516/15264655646693.jpg
         * email :
         * password : e10adc3949ba59abbe56e057f20f883e
         * hash :
         * proxy : 0
         * pid : 0
         * rebate : 0.00
         * phone : 18778277461
         * area : null
         * city_id : 1
         * sex : 0
         * birth : null
         * address : null
         * qq : null
         * reg_ip :
         * reg_time : 0
         * ip :
         * time : 0
         * login_count : 0
         * point : null
         * status : 1
         * prop : null
         * custom : null
         * open_type : 0
         * open_id :
         * family_size : 0
         * household_income : 0
         * jpush_regid : null
         * expenditure : 0.00
         * join_status : 0
         * join_time : 0
         * distributor_time : 0
         * maintain_free : 0
         * post : 0
         * favour : 0
         * fans : 0
         * follow : 0
         * month_expenditure : 0.00
         * year_expenditure : 0.00
         * today_earnings : 0.00
         * type : 1
         * addtime : 1524030790
         * uptime : 1526614080
         * audit_time : 1526465567
         * state : 1
         * appid : 007915602
         * userid : 5
         * city : 广州市
         * cardimg1 : http://image-znsc.oss-cn-shenzhen.aliyuncs.com/lanmei/lijia/img1/2018-05-18 11:27:53-0.jpg
         * cardimg2 : http://image-znsc.oss-cn-shenzhen.aliyuncs.com/lanmei/lijia/img2/2018-05-18 11:27:53-0.jpg
         * card : 450221198810242953
         * otherphotos : null
         * craft : null
         * service_range : 20000
         * minimum_the_door : 20.00
         * open_day : 0
         * balance : 93895.00
         * lon : 113.3516
         * lat : 23.145808
         * score : 85
         * checkscore : 1
         * complete : null
         * endorder : 1
         * repair : [{"id":"542","addtime":"1526575475","uptime":"1526575493","endtime":"1526575475","state":"2","platform_product":"0","dustbin_id":"000","imgs":null,"content":"在一起时代","lon":"113.35220356233","lat":"23.148856025133","address":"广州市天河区五山路265号","type":"1","make_an_appointment":"1526659200","total_price":"8.00","pay_type":"6","userid":"5","member_id":"141","pay_status":"1","uid":"162","phone":"13560251768","overdue":"0","classtype":"11","ordertime":"1526575475","members":null,"pay_no":"20180518MDZZ2111","pick_up_time":"1526575493","lon1":null,"lat1":null,"ontime":null,"imgs1":null,"maintenance_projects":null,"total_prices":null,"classtypename":"油烟机维修","appid":"426341361","user":{"id":"162","username":"u_000162","nickname":"蓝美科技","realname":null,"user_type":"1","user_type_fix":"0","pic":"http://images.itlanmei.cn/162/1526571082.png","email":"","password":"e10adc3949ba59abbe56e057f20f883e","hash":"","proxy":"0","pid":"0","rebate":"0.00","phone":"13560251768","area":null,"city_id":"1","sex":"0","birth":null,"address":null,"qq":null,"reg_ip":"","reg_time":"0","ip":"","time":"0","login_count":"0","point":null,"status":"1","prop":null,"custom":null,"open_type":"0","open_id":"","family_size":"0","household_income":"0","jpush_regid":null,"expenditure":"0.00","join_status":"0","join_time":"0","distributor_time":"0","maintain_free":"0","post":"8","favour":"0","fans":"0","follow":"0","month_expenditure":"0.00","year_expenditure":"0.00","today_earnings":"0.00","type":"1","addtime":"1526261713","uptime":"1526571175","audit_time":null,"state":"0","appid":"426341361","userid":"5","city":null,"cardimg1":null,"cardimg2":null,"card":null,"otherphotos":null,"craft":null,"service_range":null,"minimum_the_door":null,"open_day":"0","balance":"811.99","lon":null,"lat":null,"score":"100","checkscore":"0","complete":null,"endorder":"1"}},{"id":"465","addtime":"1526520972","uptime":"1526520982","endtime":"1526520972","state":"2","platform_product":"0","dustbin_id":"000","imgs":null,"content":"测试","lon":"113.35223096065","lat":"23.14771200971","address":"广州市天河区天河北路725号","type":"2","make_an_appointment":null,"total_price":"19.00","pay_type":"6","userid":"5","member_id":"141","pay_status":"1","uid":"148","phone":"13416152445","overdue":"0","classtype":null,"ordertime":"1526520972","members":null,"pay_no":"2018051 05-18 11:29:26.055 7387-7387/com.lanmei.lijia D/BeanRequest: 7MDZZ2613","pick_up_time":"1526520982","lon1":null,"lat1":null,"ontime":null,"imgs1":null,"maintenance_projects":null,"total_prices":null,"classtypename":null,"appid":null,"user":{"id":"148","username":"u_000148","nickname":"u_13416152","realname":null,"user_type":"5","user_type_fix":"0","pic":"http://images.itlanmei.cn/148/1525427614.png","email":"","password":"e10adc3949ba59abbe56e057f20f883e","hash":"","proxy":"0","pid":"0","rebate":"0.00","phone":"13416152445","area":null,"city_id":"1","sex":"0","birth":null,"address":null,"qq":null,"reg_ip":"","reg_time":"0","ip":"","time":"0","login_count":"0","point":null,"status":"1","prop":null,"custom":null,"open_type":"0","open_id":"","family_size":"0","household_income":"0","jpush_regid":null,"expenditure":"0.00","join_status":"0","join_time":"0","distributor_time":"0","maintain_free":"0","post":"27","favour":"0","fans":"0","follow":"1","month_expenditure":"0.00","year_expenditure":"0.00","today_earnings":"0.00","type":"1","addtime":"1524637696","uptime":"1526611258","audit_time":"1524708737","state":"1","appid":"426341361","userid":"5","city":null,"cardimg1":null,"cardimg2":null,"card":null,"otherphotos":null,"craft":null,"service_range":null,"minimum_the_door":null,"open_day":"0","balance":"9102.99","lon":"113.35037832446","lat":"23.147062398456","score":"71","checkscore":"1","complete":null,"endorder":"1"}}]
         * count : 1
         * ordermoney : 0
         */

        private String id;
        private String username;
        private String nickname;
        private String realname;
        private String user_type;
        private String user_type_fix;
        private String pic;
        private String email;
        private String password;
        private String hash;
        private String proxy;
        private String pid;
        private String rebate;
        private String phone;
        private String area;
        private String city_id;
        private String sex;
        private String birth;
        private String address;
        private String qq;
        private String reg_ip;
        private String reg_time;
        private String ip;
        private String time;
        private String login_count;
        private String point;
        private String status;
        private String prop;
        private String custom;
        private String open_type;
        private String open_id;
        private String family_size;
        private String household_income;
        private String jpush_regid;
        private String expenditure;
        private String join_status;
        private String join_time;
        private String distributor_time;
        private String maintain_free;
        private String post;
        private String favour;
        private String fans;
        private String follow;
        private String month_expenditure;
        private String year_expenditure;
        private String today_earnings;
        private String type;
        private String addtime;
        private String uptime;
        private String audit_time;
        private String state;
        private String appid;
        private String userid;
        private String city;
        private String cardimg1;
        private String cardimg2;
        private String card;
        private String otherphotos;
        private String craft;
        private String service_range;
        private String minimum_the_door;
        private String open_day;
        private String balance;
        private String lon;
        private String lat;
        private String score;
        private String checkscore;
        private String complete;
        private String endorder;
        private String count;
        private int ordermoney;
        private List<RepairBean> repair;

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

        public String getUser_type() {
            return user_type;
        }

        public void setUser_type(String user_type) {
            this.user_type = user_type;
        }

        public String getUser_type_fix() {
            return user_type_fix;
        }

        public void setUser_type_fix(String user_type_fix) {
            this.user_type_fix = user_type_fix;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getRebate() {
            return rebate;
        }

        public void setRebate(String rebate) {
            this.rebate = rebate;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getQq() {
            return qq;
        }

        public void setQq(String qq) {
            this.qq = qq;
        }

        public String getReg_ip() {
            return reg_ip;
        }

        public void setReg_ip(String reg_ip) {
            this.reg_ip = reg_ip;
        }

        public String getReg_time() {
            return reg_time;
        }

        public void setReg_time(String reg_time) {
            this.reg_time = reg_time;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getLogin_count() {
            return login_count;
        }

        public void setLogin_count(String login_count) {
            this.login_count = login_count;
        }

        public String getPoint() {
            return point;
        }

        public void setPoint(String point) {
            this.point = point;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProp() {
            return prop;
        }

        public void setProp(String prop) {
            this.prop = prop;
        }

        public String getCustom() {
            return custom;
        }

        public void setCustom(String custom) {
            this.custom = custom;
        }

        public String getOpen_type() {
            return open_type;
        }

        public void setOpen_type(String open_type) {
            this.open_type = open_type;
        }

        public String getOpen_id() {
            return open_id;
        }

        public void setOpen_id(String open_id) {
            this.open_id = open_id;
        }

        public String getFamily_size() {
            return family_size;
        }

        public void setFamily_size(String family_size) {
            this.family_size = family_size;
        }

        public String getHousehold_income() {
            return household_income;
        }

        public void setHousehold_income(String household_income) {
            this.household_income = household_income;
        }

        public String getJpush_regid() {
            return jpush_regid;
        }

        public void setJpush_regid(String jpush_regid) {
            this.jpush_regid = jpush_regid;
        }

        public String getExpenditure() {
            return expenditure;
        }

        public void setExpenditure(String expenditure) {
            this.expenditure = expenditure;
        }

        public String getJoin_status() {
            return join_status;
        }

        public void setJoin_status(String join_status) {
            this.join_status = join_status;
        }

        public String getJoin_time() {
            return join_time;
        }

        public void setJoin_time(String join_time) {
            this.join_time = join_time;
        }

        public String getDistributor_time() {
            return distributor_time;
        }

        public void setDistributor_time(String distributor_time) {
            this.distributor_time = distributor_time;
        }

        public String getMaintain_free() {
            return maintain_free;
        }

        public void setMaintain_free(String maintain_free) {
            this.maintain_free = maintain_free;
        }

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getFavour() {
            return favour;
        }

        public void setFavour(String favour) {
            this.favour = favour;
        }

        public String getFans() {
            return fans;
        }

        public void setFans(String fans) {
            this.fans = fans;
        }

        public String getFollow() {
            return follow;
        }

        public void setFollow(String follow) {
            this.follow = follow;
        }

        public String getMonth_expenditure() {
            return month_expenditure;
        }

        public void setMonth_expenditure(String month_expenditure) {
            this.month_expenditure = month_expenditure;
        }

        public String getYear_expenditure() {
            return year_expenditure;
        }

        public void setYear_expenditure(String year_expenditure) {
            this.year_expenditure = year_expenditure;
        }

        public String getToday_earnings() {
            return today_earnings;
        }

        public void setToday_earnings(String today_earnings) {
            this.today_earnings = today_earnings;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getAudit_time() {
            return audit_time;
        }

        public void setAudit_time(String audit_time) {
            this.audit_time = audit_time;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCardimg1() {
            return cardimg1;
        }

        public void setCardimg1(String cardimg1) {
            this.cardimg1 = cardimg1;
        }

        public String getCardimg2() {
            return cardimg2;
        }

        public void setCardimg2(String cardimg2) {
            this.cardimg2 = cardimg2;
        }

        public String getCard() {
            return card;
        }

        public void setCard(String card) {
            this.card = card;
        }

        public String getOtherphotos() {
            return otherphotos;
        }

        public void setOtherphotos(String otherphotos) {
            this.otherphotos = otherphotos;
        }

        public String getCraft() {
            return craft;
        }

        public void setCraft(String craft) {
            this.craft = craft;
        }

        public String getService_range() {
            return service_range;
        }

        public void setService_range(String service_range) {
            this.service_range = service_range;
        }

        public String getMinimum_the_door() {
            return minimum_the_door;
        }

        public void setMinimum_the_door(String minimum_the_door) {
            this.minimum_the_door = minimum_the_door;
        }

        public String getOpen_day() {
            return open_day;
        }

        public void setOpen_day(String open_day) {
            this.open_day = open_day;
        }

        public String getBalance() {
            return balance;
        }

        public void setBalance(String balance) {
            this.balance = balance;
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

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public String getCheckscore() {
            return checkscore;
        }

        public void setCheckscore(String checkscore) {
            this.checkscore = checkscore;
        }

        public String getComplete() {
            return complete;
        }

        public void setComplete(String complete) {
            this.complete = complete;
        }

        public String getEndorder() {
            return endorder;
        }

        public void setEndorder(String endorder) {
            this.endorder = endorder;
        }

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public int getOrdermoney() {
            return ordermoney;
        }

        public void setOrdermoney(int ordermoney) {
            this.ordermoney = ordermoney;
        }

        public List<RepairBean> getRepair() {
            return repair;
        }

        public void setRepair(List<RepairBean> repair) {
            this.repair = repair;
        }

        public static class RepairBean {
            /**
             * id : 542
             * addtime : 1526575475
             * uptime : 1526575493
             * endtime : 1526575475
             * state : 2
             * platform_product : 0
             * dustbin_id : 000
             * imgs : null
             * content : 在一起时代
             * lon : 113.35220356233
             * lat : 23.148856025133
             * address : 广州市天河区五山路265号
             * type : 1
             * make_an_appointment : 1526659200
             * total_price : 8.00
             * pay_type : 6
             * userid : 5
             * member_id : 141
             * pay_status : 1
             * uid : 162
             * phone : 13560251768
             * overdue : 0
             * classtype : 11
             * ordertime : 1526575475
             * members : null
             * pay_no : 20180518MDZZ2111
             * pick_up_time : 1526575493
             * lon1 : null
             * lat1 : null
             * ontime : null
             * imgs1 : null
             * maintenance_projects : null
             * total_prices : null
             * classtypename : 油烟机维修
             * appid : 426341361
             * user : {"id":"162","username":"u_000162","nickname":"蓝美科技","realname":null,"user_type":"1","user_type_fix":"0","pic":"http://images.itlanmei.cn/162/1526571082.png","email":"","password":"e10adc3949ba59abbe56e057f20f883e","hash":"","proxy":"0","pid":"0","rebate":"0.00","phone":"13560251768","area":null,"city_id":"1","sex":"0","birth":null,"address":null,"qq":null,"reg_ip":"","reg_time":"0","ip":"","time":"0","login_count":"0","point":null,"status":"1","prop":null,"custom":null,"open_type":"0","open_id":"","family_size":"0","household_income":"0","jpush_regid":null,"expenditure":"0.00","join_status":"0","join_time":"0","distributor_time":"0","maintain_free":"0","post":"8","favour":"0","fans":"0","follow":"0","month_expenditure":"0.00","year_expenditure":"0.00","today_earnings":"0.00","type":"1","addtime":"1526261713","uptime":"1526571175","audit_time":null,"state":"0","appid":"426341361","userid":"5","city":null,"cardimg1":null,"cardimg2":null,"card":null,"otherphotos":null,"craft":null,"service_range":null,"minimum_the_door":null,"open_day":"0","balance":"811.99","lon":null,"lat":null,"score":"100","checkscore":"0","complete":null,"endorder":"1"}
             */

            private String id;
            private String addtime;
            private String uptime;
            private String endtime;
            private String state;
            private String platform_product;
            private String dustbin_id;
            private List<String> imgs;
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
            private List<String> imgs1;
            private List<String> maintenance_projects;
            private List<String> total_prices;
            private String classtypename;
            private String appid;
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

            public List<String> getImgs() {
                return imgs;
            }

            public void setImgs(List<String> imgs) {
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

            public List<String> getImgs1() {
                return imgs1;
            }

            public void setImgs1(List<String> imgs1) {
                this.imgs1 = imgs1;
            }

            public List<String> getMaintenance_projects() {
                return maintenance_projects;
            }

            public void setMaintenance_projects(List<String> maintenance_projects) {
                this.maintenance_projects = maintenance_projects;
            }

            public List<String> getTotal_prices() {
                return total_prices;
            }

            public void setTotal_prices(List<String> total_prices) {
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
                 * user_type : 1
                 * user_type_fix : 0
                 * pic : http://images.itlanmei.cn/162/1526571082.png
                 * email :
                 * password : e10adc3949ba59abbe56e057f20f883e
                 * hash :
                 * proxy : 0
                 * pid : 0
                 * rebate : 0.00
                 * phone : 13560251768
                 * area : null
                 * city_id : 1
                 * sex : 0
                 * birth : null
                 * address : null
                 * qq : null
                 * reg_ip :
                 * reg_time : 0
                 * ip :
                 * time : 0
                 * login_count : 0
                 * point : null
                 * status : 1
                 * prop : null
                 * custom : null
                 * open_type : 0
                 * open_id :
                 * family_size : 0
                 * household_income : 0
                 * jpush_regid : null
                 * expenditure : 0.00
                 * join_status : 0
                 * join_time : 0
                 * distributor_time : 0
                 * maintain_free : 0
                 * post : 8
                 * favour : 0
                 * fans : 0
                 * follow : 0
                 * month_expenditure : 0.00
                 * year_expenditure : 0.00
                 * today_earnings : 0.00
                 * type : 1
                 * addtime : 1526261713
                 * uptime : 1526571175
                 * audit_time : null
                 * state : 0
                 * appid : 426341361
                 * userid : 5
                 * city : null
                 * cardimg1 : null
                 * cardimg2 : null
                 * card : null
                 * otherphotos : null
                 * craft : null
                 * service_range : null
                 * minimum_the_door : null
                 * open_day : 0
                 * balance : 811.99
                 * lon : null
                 * lat : null
                 * score : 100
                 * checkscore : 0
                 * complete : null
                 * endorder : 1
                 */

                private String id;
                private String username;
                private String nickname;
                private String realname;
                private String user_type;
                private String user_type_fix;
                private String pic;
                private String email;
                private String password;
                private String hash;
                private String proxy;
                private String pid;
                private String rebate;
                private String phone;
                private String area;
                private String city_id;
                private String sex;
                private String birth;
                private String address;
                private String qq;
                private String reg_ip;
                private String reg_time;
                private String ip;
                private String time;
                private String login_count;
                private String point;
                private String status;
                private String prop;
                private String custom;
                private String open_type;
                private String open_id;
                private String family_size;
                private String household_income;
                private String jpush_regid;
                private String expenditure;
                private String join_status;
                private String join_time;
                private String distributor_time;
                private String maintain_free;
                private String post;
                private String favour;
                private String fans;
                private String follow;
                private String month_expenditure;
                private String year_expenditure;
                private String today_earnings;
                private String type;
                private String addtime;
                private String uptime;
                private String audit_time;
                private String state;
                private String appid;
                private String userid;
                private String city;
                private String cardimg1;
                private String cardimg2;
                private String card;
                private String otherphotos;
                private String craft;
                private String service_range;
                private String minimum_the_door;
                private String open_day;
                private String balance;
                private String lon;
                private String lat;
                private String score;
                private String checkscore;
                private String complete;
                private String endorder;

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

                public String getUser_type() {
                    return user_type;
                }

                public void setUser_type(String user_type) {
                    this.user_type = user_type;
                }

                public String getUser_type_fix() {
                    return user_type_fix;
                }

                public void setUser_type_fix(String user_type_fix) {
                    this.user_type_fix = user_type_fix;
                }

                public String getPic() {
                    return pic;
                }

                public void setPic(String pic) {
                    this.pic = pic;
                }

                public String getEmail() {
                    return email;
                }

                public void setEmail(String email) {
                    this.email = email;
                }

                public String getPassword() {
                    return password;
                }

                public void setPassword(String password) {
                    this.password = password;
                }

                public String getHash() {
                    return hash;
                }

                public void setHash(String hash) {
                    this.hash = hash;
                }

                public String getProxy() {
                    return proxy;
                }

                public void setProxy(String proxy) {
                    this.proxy = proxy;
                }

                public String getPid() {
                    return pid;
                }

                public void setPid(String pid) {
                    this.pid = pid;
                }

                public String getRebate() {
                    return rebate;
                }

                public void setRebate(String rebate) {
                    this.rebate = rebate;
                }

                public String getPhone() {
                    return phone;
                }

                public void setPhone(String phone) {
                    this.phone = phone;
                }

                public String getArea() {
                    return area;
                }

                public void setArea(String area) {
                    this.area = area;
                }

                public String getCity_id() {
                    return city_id;
                }

                public void setCity_id(String city_id) {
                    this.city_id = city_id;
                }

                public String getSex() {
                    return sex;
                }

                public void setSex(String sex) {
                    this.sex = sex;
                }

                public String getBirth() {
                    return birth;
                }

                public void setBirth(String birth) {
                    this.birth = birth;
                }

                public String getAddress() {
                    return address;
                }

                public void setAddress(String address) {
                    this.address = address;
                }

                public String getQq() {
                    return qq;
                }

                public void setQq(String qq) {
                    this.qq = qq;
                }

                public String getReg_ip() {
                    return reg_ip;
                }

                public void setReg_ip(String reg_ip) {
                    this.reg_ip = reg_ip;
                }

                public String getReg_time() {
                    return reg_time;
                }

                public void setReg_time(String reg_time) {
                    this.reg_time = reg_time;
                }

                public String getIp() {
                    return ip;
                }

                public void setIp(String ip) {
                    this.ip = ip;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getLogin_count() {
                    return login_count;
                }

                public void setLogin_count(String login_count) {
                    this.login_count = login_count;
                }

                public String getPoint() {
                    return point;
                }

                public void setPoint(String point) {
                    this.point = point;
                }

                public String getStatus() {
                    return status;
                }

                public void setStatus(String status) {
                    this.status = status;
                }

                public String getProp() {
                    return prop;
                }

                public void setProp(String prop) {
                    this.prop = prop;
                }

                public String getCustom() {
                    return custom;
                }

                public void setCustom(String custom) {
                    this.custom = custom;
                }

                public String getOpen_type() {
                    return open_type;
                }

                public void setOpen_type(String open_type) {
                    this.open_type = open_type;
                }

                public String getOpen_id() {
                    return open_id;
                }

                public void setOpen_id(String open_id) {
                    this.open_id = open_id;
                }

                public String getFamily_size() {
                    return family_size;
                }

                public void setFamily_size(String family_size) {
                    this.family_size = family_size;
                }

                public String getHousehold_income() {
                    return household_income;
                }

                public void setHousehold_income(String household_income) {
                    this.household_income = household_income;
                }

                public String getJpush_regid() {
                    return jpush_regid;
                }

                public void setJpush_regid(String jpush_regid) {
                    this.jpush_regid = jpush_regid;
                }

                public String getExpenditure() {
                    return expenditure;
                }

                public void setExpenditure(String expenditure) {
                    this.expenditure = expenditure;
                }

                public String getJoin_status() {
                    return join_status;
                }

                public void setJoin_status(String join_status) {
                    this.join_status = join_status;
                }

                public String getJoin_time() {
                    return join_time;
                }

                public void setJoin_time(String join_time) {
                    this.join_time = join_time;
                }

                public String getDistributor_time() {
                    return distributor_time;
                }

                public void setDistributor_time(String distributor_time) {
                    this.distributor_time = distributor_time;
                }

                public String getMaintain_free() {
                    return maintain_free;
                }

                public void setMaintain_free(String maintain_free) {
                    this.maintain_free = maintain_free;
                }

                public String getPost() {
                    return post;
                }

                public void setPost(String post) {
                    this.post = post;
                }

                public String getFavour() {
                    return favour;
                }

                public void setFavour(String favour) {
                    this.favour = favour;
                }

                public String getFans() {
                    return fans;
                }

                public void setFans(String fans) {
                    this.fans = fans;
                }

                public String getFollow() {
                    return follow;
                }

                public void setFollow(String follow) {
                    this.follow = follow;
                }

                public String getMonth_expenditure() {
                    return month_expenditure;
                }

                public void setMonth_expenditure(String month_expenditure) {
                    this.month_expenditure = month_expenditure;
                }

                public String getYear_expenditure() {
                    return year_expenditure;
                }

                public void setYear_expenditure(String year_expenditure) {
                    this.year_expenditure = year_expenditure;
                }

                public String getToday_earnings() {
                    return today_earnings;
                }

                public void setToday_earnings(String today_earnings) {
                    this.today_earnings = today_earnings;
                }

                public String getType() {
                    return type;
                }

                public void setType(String type) {
                    this.type = type;
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

                public String getAudit_time() {
                    return audit_time;
                }

                public void setAudit_time(String audit_time) {
                    this.audit_time = audit_time;
                }

                public String getState() {
                    return state;
                }

                public void setState(String state) {
                    this.state = state;
                }

                public String getAppid() {
                    return appid;
                }

                public void setAppid(String appid) {
                    this.appid = appid;
                }

                public String getUserid() {
                    return userid;
                }

                public void setUserid(String userid) {
                    this.userid = userid;
                }

                public String getCity() {
                    return city;
                }

                public void setCity(String city) {
                    this.city = city;
                }

                public String getCardimg1() {
                    return cardimg1;
                }

                public void setCardimg1(String cardimg1) {
                    this.cardimg1 = cardimg1;
                }

                public String getCardimg2() {
                    return cardimg2;
                }

                public void setCardimg2(String cardimg2) {
                    this.cardimg2 = cardimg2;
                }

                public String getCard() {
                    return card;
                }

                public void setCard(String card) {
                    this.card = card;
                }

                public String getOtherphotos() {
                    return otherphotos;
                }

                public void setOtherphotos(String otherphotos) {
                    this.otherphotos = otherphotos;
                }

                public String getCraft() {
                    return craft;
                }

                public void setCraft(String craft) {
                    this.craft = craft;
                }

                public String getService_range() {
                    return service_range;
                }

                public void setService_range(String service_range) {
                    this.service_range = service_range;
                }

                public String getMinimum_the_door() {
                    return minimum_the_door;
                }

                public void setMinimum_the_door(String minimum_the_door) {
                    this.minimum_the_door = minimum_the_door;
                }

                public String getOpen_day() {
                    return open_day;
                }

                public void setOpen_day(String open_day) {
                    this.open_day = open_day;
                }

                public String getBalance() {
                    return balance;
                }

                public void setBalance(String balance) {
                    this.balance = balance;
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

                public String getScore() {
                    return score;
                }

                public void setScore(String score) {
                    this.score = score;
                }

                public String getCheckscore() {
                    return checkscore;
                }

                public void setCheckscore(String checkscore) {
                    this.checkscore = checkscore;
                }

                public String getComplete() {
                    return complete;
                }

                public void setComplete(String complete) {
                    this.complete = complete;
                }

                public String getEndorder() {
                    return endorder;
                }

                public void setEndorder(String endorder) {
                    this.endorder = endorder;
                }
            }
        }
    }

}