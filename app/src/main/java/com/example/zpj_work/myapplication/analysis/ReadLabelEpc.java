package com.example.zpj_work.myapplication.analysis;

import android.util.Log;

import com.example.zpj_work.myapplication.usb.DevComm;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/10 0010 19:09
 * @描述
 */
public class ReadLabelEpc {

    private static final String TAG                   = "hik";

    private boolean status = false;

    public void setTranslate(int m, String[] data_epc) {

        //-------------------------------------------------------------------------------盘点epc
        boolean s1 = data_epc[4].equals("0D");
        Log.d(TAG, "\n=============boo==s1============ " + ": ==>" + s1);
        boolean s2 = data_epc[5].equals("05");
        Log.d(TAG, "\n=============boo==s2============ " + ": ==>" + s2);
        boolean s3 = data_epc[6].equals("00");
        Log.d(TAG, "\n=============boo==s3============ " + ": ==>" + s3);

        String check01 = DevComm.checksum(data_epc, 0, data_epc.length-3).toUpperCase(); //计算校验值
        boolean s4 = data_epc[data_epc.length-3].equals(check01);
        Log.d(TAG, "\n=============boo==s4============ " + ": ==>" + s4);

        //-------------------------------------------------------------------------------控制读epc
        boolean s5 = data_epc[4].equals("0D");
        Log.d(TAG, "\n=============boo==s5============ " + ": ==>" + s5);
        boolean s6 = data_epc[5].equals("06");
        Log.d(TAG, "\n=============boo==s6============ " + ": ==>" + s6);
        boolean s7 = data_epc[6].equals("00");
        Log.d(TAG, "\n=============boo==s7============ " + ": ==>" + s7);

        String check02 = DevComm.checksum(data_epc, 0, data_epc.length-3).toUpperCase(); //计算校验值
        boolean s8 = data_epc[data_epc.length-3].equals(check02);
        Log.d(TAG, "\n=============boo==s8============ " + ": ==>" + s8);
        //-------------------------------------------------------------------------------

        if (m == 1) {
            if (s1 && s2 && s3 && s4) {
                this.status = true;
            }
        } else if (m == 3) {
            if (s5 && s6 && s7 && s8) {
                this.status = true;
            }
        } else {
            this.status = false;
        }

    }


    public boolean getTranslate() {
        return status;
    }
}
