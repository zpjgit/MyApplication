package com.example.zpj_work.myapplication.analysis;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/12 0012 11:14
 * @描述
 */
public class UserOperation {

    private static final String TAG = "hik";

    private static int userFirst;
    private static int userLen;
    private static String userPassword;
    private static String userRegion;


    public void setUserOperation(int userFirst, int userLen, String userPassword) {
        this.userFirst = userFirst;
        this.userLen = userLen;
        this.userPassword = userPassword;
        this.userRegion = "03";
    }

    public int getUserFirst() {
        return userFirst;
    }

    public int getUserLen() {
        return userLen;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserRegion() {
        return userRegion;
    }
}
