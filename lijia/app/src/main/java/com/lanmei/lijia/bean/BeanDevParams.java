package com.lanmei.lijia.bean;

public class BeanDevParams {

    /**
     * all : 81 00 a0 ec ee a0 a0 a8 00 00 00 00 00 00 56 0e|81 01 21 48 2b 40 3e ff ff ff ff ff ff 3f e6 d8|81 02 32 2c 20 20 36 46 2e 37 20 21 20 20 8c dc|81 03 00 38 01 38 01 75 43 2a 21 20 20 38 79 fd
     * all_format : {"0":{"1":[["强制除霜",6,7,"01"],["强制开启电加热",4,5,"01"]],"2":[["高压开关",6,7,"11"],["低压开关",4,5,"01"],["水流开关",2,3,"11"]],"3":[["压缩机",6,7,"11"],["四通阀",4,5,"01"],["外风机",2,3,"11"],["循环水泵",0,1,"01"]],"4":[["曲轴加热",6,7,"01"],["电加热",4,5,"01"]],"5":[["除霜",6,7,"01"],["水箱防冻",4,5,"01"]]},"1":{"2":["热水设置温度","","",40],"6":["热水模式","","",223],"12":["路由信号强度","","",31]},"3":{"1":["出水温度","","",-32],"3":["外环境温度","","",-31],"4":["外盘管1温度","","",24],"5":["回气1温度","","",-31],"6":["排气1温度","","",85],"7":["电子膨胀阀开","","",35],"8":["机型代码","","",10],"11":["保护代码","","",0],"12":["水箱温度","","",24]}}
     * addtime : 2018-05-21 11:32:08
     * fault :
     * device_type : 1
     */

    private String all;
    private String all_format;
    private String addtime;
    private String fault;
    private String device_type;

    public String getAll() {
        return all;
    }

    public void setAll(String all) {
        this.all = all;
    }

    public String getAll_format() {
        return all_format;
    }

    public void setAll_format(String all_format) {
        this.all_format = all_format;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getFault() {
        return fault;
    }

    public void setFault(String fault) {
        this.fault = fault;
    }

    public String getDevice_type() {
        return device_type;
    }

    public void setDevice_type(String device_type) {
        this.device_type = device_type;
    }


}
