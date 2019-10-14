package com.example.zpj_work.myapplication.analysis;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/12 0012 9:25
 * @描述
 */
public class EpcOperation {

    private static final String TAG = "hik";

    private static int epcFirst;
    private static int epcLen;
    private static String epcPassword;
    private static String epcRegion;


    public void setEpcOperation(int epcFirst, int epcLen, String epcPassword) {
        this.epcFirst = epcFirst;
        this.epcLen = epcLen;
        this.epcPassword = epcPassword;
        this.epcRegion = "01";
    }

    public int getEpcFirst() {
        return epcFirst;
    }

    public int getEpcLen() {
        return epcLen;
    }

    public String getEpcPassword() {
        return epcPassword;
    }

    public String getEpcRegion() {
        return epcRegion;
    }
}
