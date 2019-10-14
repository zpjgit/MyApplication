package com.example.zpj_work.myapplication.listviewtest;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 14:19
 * @描述
 */
public class Fruit {
    private int number;//序号
    private  int antennas;  //天线
    private String dataepc; //数据
    private String datatid; //数据
    private String datauser; //数据



    public Fruit(int number, int antennas, String dataepc, String datatid, String datauser){
        this.number=number;
        this.antennas = 0;
        this.dataepc=dataepc;
        this.datatid=datatid;
        this.datauser=datauser;

    }

    public int getNumber() {
        return number;
    }

    public int getAntennas() {
        return antennas;
    }

    public String getDataepc() {
        return dataepc;
    }

    public String getDatatid() {
        return datatid;
    }

    public String getDatauser() {
        return datauser;
    }
}
