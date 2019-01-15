package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai
 * 反馈类型
 */
public class QuestionFeedbackListBean extends AbsListBean {


    public String data;

    public List<QuestionFeedbackBean> dataList;

    @Override
    public List<QuestionFeedbackBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), QuestionFeedbackBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 热门问题
     */

    public static class QuestionFeedbackBean {


        private boolean isChoose;

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public boolean isChoose() {
            return isChoose;
        }

        /**
         * id : 1
         * name : 服务发布
         * annotation : 发布、审核总是失败
         */

        private String id;
        private String name;
        private String annotation;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getAnnotation() {
            return annotation;
        }

        public void setAnnotation(String annotation) {
            this.annotation = annotation;
        }
    }

}