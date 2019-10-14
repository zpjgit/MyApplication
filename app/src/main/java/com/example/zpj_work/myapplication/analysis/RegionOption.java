package com.example.zpj_work.myapplication.analysis;

import android.util.Log;

import com.example.zpj_work.myapplication.usb.DevComm;

import java.util.Arrays;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/12 0012 14:29
 * @描述
 */
public class RegionOption {

    private static final String TAG = "hik";

    private static String[] text_ReadLabelEpc  = null;
    private static String[] text_ReadLabelTid  = null;
    private static String[] text_ReadLabelUser = null;
//    private static String[] text_ReadLabelNull = new String[] {" "};
    private static int epcNum = 0, tidNum = 0, userNum = 0;

    public void setRegionOption(int m, int tag, int first, int len) {

        ReadLabelAnalysis ReadLabel = null;
        try {
            ReadLabel = new ReadLabelAnalysis();
            ReadLabel.setLabel_epc(m, first, len);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (tag == 1) {
            text_ReadLabelEpc = ReadLabel.getLabel_epc();
            text_ReadLabelEpc = DevComm.removeArrayEmptyTextBackNewArray(text_ReadLabelEpc);
            Log.d(TAG, "\n==============tag=============1 " + ": ==>" + tag + "Epc: " + Arrays.toString(text_ReadLabelEpc) + "读到的项数: " + text_ReadLabelEpc.length);
            this.epcNum = text_ReadLabelEpc.length;
            this.text_ReadLabelEpc = text_ReadLabelEpc;

        } else if (tag == 2) {
            text_ReadLabelTid = ReadLabel.getLabel_epc();
            text_ReadLabelTid = DevComm.removeArrayEmptyTextBackNewArray(text_ReadLabelTid);
            Log.d(TAG, "\n==============tag=============2 " + ": ==>" + tag + "Tid: " + Arrays.toString(text_ReadLabelTid) + "读到的项数: " + text_ReadLabelTid.length);
            this.tidNum = text_ReadLabelTid.length;
            this.text_ReadLabelTid = text_ReadLabelTid;

        } else if (tag == 3) {
            text_ReadLabelUser = ReadLabel.getLabel_epc();
            text_ReadLabelUser = DevComm.removeArrayEmptyTextBackNewArray(text_ReadLabelUser);
            Log.d(TAG, "\n==============tag=============3 " + ": ==>" + tag + "User: " + Arrays.toString(text_ReadLabelUser) + "读到的项数: " + text_ReadLabelUser.length);
            this.userNum = text_ReadLabelUser.length;
            this.text_ReadLabelUser = text_ReadLabelUser;

        } else {
            Log.d(TAG, "\n==============tag=============0 " + ": ==>" + tag);
//            this.text_ReadLabelEpc  = null;
//            this.text_ReadLabelTid  = null;
//            this.text_ReadLabelUser = null;
        }

    }

    public String[] getReadLabelEpc() {
        return text_ReadLabelEpc;
    }

    public String[] getReadLabelTid() {
        return text_ReadLabelTid;
    }

    public String[] getReadLabelUser() {
        return text_ReadLabelUser;
    }

    public int getEpcNum() {
        return epcNum;
    }

    public int getTidNum() {
        return tidNum;
    }

    public int getUserNum() {
        return userNum;
    }

}
