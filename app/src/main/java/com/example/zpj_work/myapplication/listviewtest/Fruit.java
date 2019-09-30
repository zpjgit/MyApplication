package com.example.zpj_work.myapplication.listviewtest;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 14:19
 * @描述
 */
public class Fruit {
    private int number;//编号
    private String data; //数据
    public Fruit(int number,String data){
        this.number=number;
        this.data=data;
    }

    public int getNumber() {
        return number;
    }

    public String getData() {
        return data;
    }
}
