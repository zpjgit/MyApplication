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

    public void setTranslate(String[] data_epc) {

        boolean s1 = data_epc[4].equals("0D");
        Log.d(TAG, "\n=============boo==s1============ " + ": ==>" + s1);
        boolean s2 = data_epc[5].equals("05");
        Log.d(TAG, "\n=============boo==s2============ " + ": ==>" + s2);
        boolean s3 = data_epc[6].equals("00");
        Log.d(TAG, "\n=============boo==s3============ " + ": ==>" + s3);

        String check = DevComm.checksum(data_epc, 0, data_epc.length-3).toUpperCase(); //计算校验值
        boolean s4 = data_epc[data_epc.length-3].equals(check);
        Log.d(TAG, "\n=============boo==s4============ " + ": ==>" + s4);

        if (s1 && s2 && s3 && s4) {
            this.status = true;
        }

    }


    public boolean getTranslate() {
        return status;
    }
}
