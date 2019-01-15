package com.lanmei.lijia.event;

import com.lanmei.lijia.bean.ChooseWorkListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xkai on 2018/4/20.
 *
 */

public class LeagueEvent {

    private int type;//1 城市  2 身份证照片 3 其他证书照片 4 服务工种
    private String city;
    private String idFront;//身份证正面
    private String idReverse;//身份证反面
    private ArrayList<String> certificateList;
    private List<ChooseWorkListBean.ChooseWorkBean> chooseWorkList;

    public void setIdFront(String idFront) {
        this.idFront = idFront;
    }

    public void setIdReverse(String idReverse) {
        this.idReverse = idReverse;
    }



    public String getIdFront() {
        return idFront;
    }

    public String getIdReverse() {
        return idReverse;
    }

    public LeagueEvent(int type){
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public void setCertificateList(ArrayList<String> certificateList) {
        this.certificateList = certificateList;
    }

    public void setChooseWorkList(List<ChooseWorkListBean.ChooseWorkBean> chooseWorkList) {
        this.chooseWorkList = chooseWorkList;
    }

    public String getCity() {
        return city;
    }

    public ArrayList<String> getCertificateList() {
        return certificateList;
    }

    public List<ChooseWorkListBean.ChooseWorkBean> getChooseWorkList() {
        return chooseWorkList;
    }

}
