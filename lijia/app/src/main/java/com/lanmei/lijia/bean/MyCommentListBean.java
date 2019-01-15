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
public class MyCommentListBean extends AbsListBean {


    public String data;

    public List<MyCommentBean> dataList;

    @Override
    public List<MyCommentBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), MyCommentBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 我的评论
     */

    public static class MyCommentBean implements Serializable {


        /**
         * id : 1
         * addtime : 1526902890
         * uptime : 1526902890
         * oid : 53
         * uid : 154
         * lev : 5
         * member_id : 141
         * content : dskafjklsdfj
         * oemid : 5
         * user : {"id":"154","username":"lm_android_02","nickname":"lm_android_02","pic":"http://images.itlanmei.cn/smartmall/img/user_154/1525335806808.png"}
         */

        private String id;
        private String addtime;
        private String uptime;
        private String oid;
        private String uid;
        private String lev;
        private String member_id;
        private String content;
        private String content2;
        private String oemid;
        private UserBean user;

        public void setContent2(String content2) {
            this.content2 = content2;
        }

        public String getContent2() {
            return content2;
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

        public String getLev() {
            return lev;
        }

        public void setLev(String lev) {
            this.lev = lev;
        }

        public String getMember_id() {
            return member_id;
        }

        public void setMember_id(String member_id) {
            this.member_id = member_id;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getOemid() {
            return oemid;
        }

        public void setOemid(String oemid) {
            this.oemid = oemid;
        }

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public static class UserBean {
            /**
             * id : 154
             * username : lm_android_02
             * nickname : lm_android_02
             * pic : http://images.itlanmei.cn/smartmall/img/user_154/1525335806808.png
             */

            private String id;
            private String username;
            private String nickname;
            private String pic;

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

            public String getPic() {
                return pic;
            }

            public void setPic(String pic) {
                this.pic = pic;
            }
        }
    }

}