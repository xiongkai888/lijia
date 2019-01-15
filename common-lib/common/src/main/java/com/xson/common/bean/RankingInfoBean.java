package com.xson.common.bean;

/**
 * Created by xkai on 2018/4/8.
 */

public class RankingInfoBean {

    /**
     * id : 44
     * score : 70500
     * nickname : 科
     * pic : http://app-1980.oss-cn-shenzhen.aliyuncs.com/44/1522137624.png
     */

    private String id;
    private String score;
    private String nickname;
    private String pic;
    /**
     * community : 659047
     * count : 1
     */

    private String community;
    private int count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
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

    public String getCommunity() {
        return community;
    }

    public void setCommunity(String community) {
        this.community = community;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
