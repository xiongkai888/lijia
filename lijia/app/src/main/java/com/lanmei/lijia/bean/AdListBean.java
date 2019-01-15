package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai
 * 帮助中心轮播图
 */
public class AdListBean extends AbsListBean {


    public String data;

    public List<AdBean> dataList;

    @Override
    public List<AdBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), AdBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 我的评论
     */

    public static class AdBean {


        /**
         * id : 18
         * sort : 0
         * classid : 7
         * category_id : 0
         * pic : http://image-znsc.img-cn-shenzhen.aliyuncs.com/Uploads/imgs/20180530/15276467427596.png
         * smallpic :
         * bgcolor :
         * link : #
         * userid : 5
         * uptime : 1527646745
         * addtime : 1527646745
         * state : 1
         */

        private String id;
        private String sort;
        private String classid;
        private String category_id;
        private String pic;
        private String smallpic;
        private String bgcolor;
        private String link;
        private String userid;
        private String uptime;
        private String addtime;
        private String state;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSort() {
            return sort;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getSmallpic() {
            return smallpic;
        }

        public void setSmallpic(String smallpic) {
            this.smallpic = smallpic;
        }

        public String getBgcolor() {
            return bgcolor;
        }

        public void setBgcolor(String bgcolor) {
            this.bgcolor = bgcolor;
        }

        public String getLink() {
            return link;
        }

        public void setLink(String link) {
            this.link = link;
        }

        public String getUserid() {
            return userid;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public String getUptime() {
            return uptime;
        }

        public void setUptime(String uptime) {
            this.uptime = uptime;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

}