package com.lanmei.lijia.bean;

import com.alibaba.fastjson.JSON;
import com.xson.common.bean.AbsListBean;
import com.xson.common.utils.L;
import com.xson.common.utils.des.Des;

import java.util.List;

/**
 * @author xkai
 *
 */
public class QuestionHotListBean extends AbsListBean {


    public String data;

    public List<QuestionHotBean> dataList;

    @Override
    public List<QuestionHotBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), QuestionHotBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 热门问题
     */

    public static class QuestionHotBean {

        /**
         * id : 1
         * title : 关于我们
         */

        private String id;
        private String title;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }

}