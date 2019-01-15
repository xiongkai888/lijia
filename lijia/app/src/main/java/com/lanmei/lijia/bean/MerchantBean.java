package com.lanmei.lijia.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by xkai on 2018/5/23.
 */

public class MerchantBean implements Serializable{

    /**
     * id : 6
     * uid : 141
     * addtime : 1527067105
     * uptime : 1527067105
     * oemid : 5
     * mname : 阿里旺旺有效公司
     * address : 天河路
     * phone : 15914369999
     * contact : 15914369999
     * content : 我们不一样！
     * imgs : [""]
     */

    private String id;
    private String uid;
    private String addtime;
    private String uptime;
    private String oemid;
    private String mname;
    private String address;
    private String phone;
    private String contact;
    private String content;
    private List<String> imgs;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getOemid() {
        return oemid;
    }

    public void setOemid(String oemid) {
        this.oemid = oemid;
    }

    public String getMname() {
        return mname;
    }

    public void setMname(String mname) {
        this.mname = mname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImgs() {
        return imgs;
    }

    public void setImgs(List<String> imgs) {
        this.imgs = imgs;
    }
}
