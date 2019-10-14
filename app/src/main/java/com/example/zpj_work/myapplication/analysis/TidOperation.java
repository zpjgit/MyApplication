package com.example.zpj_work.myapplication.analysis;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/12 0012 11:12
 * @描述
 */
public class TidOperation {

    private static final String TAG = "hik";

    private static int tidFirst;
    private static int tidLen;
    private static String tidPassword;
    private static String tidRegion;


    public void setTidOperation(int tidFirst, int tidLen, String tidPassword) {
        this.tidFirst = tidFirst;
        this.tidLen = tidLen;
        this.tidPassword = tidPassword;
        this.tidRegion = "02";
    }

    public int getTidFirst() {
        return tidFirst;
    }

    public int getTidLen() {
        return tidLen;
    }

    public String getTidPassword() {
        return tidPassword;
    }

    public String getTidRegion() {
        return tidRegion;
    }
}
