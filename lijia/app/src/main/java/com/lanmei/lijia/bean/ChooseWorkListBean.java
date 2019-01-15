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
public class ChooseWorkListBean extends AbsListBean {


    public String data;

    public List<ChooseWorkBean> dataList;

    @Override
    public List<ChooseWorkBean> getDataList() {
        try {
            L.d("BeanRequest","列表解密后："+Des.decode(data));
            dataList =  JSON.parseArray(Des.decode(data), ChooseWorkBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    /**
     * Created by xkai on 2018/4/16.
     * 热门城市
     */

    public static class ChooseWorkBean implements Serializable {

        private boolean isChoose;
        /**
         * setval : 88
         * classname : 杂工
         */

        private String setval;
        private String classname;

        public void setChoose(boolean choose) {
            isChoose = choose;
        }

        public boolean isChoose() {
            return isChoose;
        }

        public String getSetval() {
            return setval;
        }

        public void setSetval(String setval) {
            this.setval = setval;
        }

        public String getClassname() {
            return classname;
        }

        public void setClassname(String classname) {
            this.classname = classname;
        }
    }

}