package com.example.zpj_work.myapplication.analysis;

import android.util.Log;

import com.example.zpj_work.myapplication.usb.DevComm;

import java.util.Arrays;

import static com.example.zpj_work.myapplication.usb.DevComm.Byte2Hex;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/8 0008 12:06
 * @描述
 */
public class Analysis {

    private static final String      TAG       = "hik";

    static byte[]  receiveData = null;

    private static String     portName;
    private static byte[][]   data_bit;
    private static String[][] data_str;


    public void setData_bit(byte[][] data_bit) {
        this.data_bit = data_bit;
    }


    public byte[][] getData_bit() {
        return data_bit;
    }

    public void setData_str(String[][] data_str) {
        this.data_str = data_str;
    }


    public String[][] getData_Str() {
        return data_str;
    }

//    public String [] getData() {
//        Log.d(TAG, "Data: "+Arrays.toString(data));
//
//        return data;
//    }

//    public static String[][] byteToString(byte[][] bytes) {
//        String[][] strings = new String[1024][1024];
//        for (int i=0; i<bytes.length; i++) {
//            for (int j=0, k=0; j<bytes[i].length; j++, k++) {
//                if (Byte2Hex(bytes[i][j]) == null) {
//                    continue;
//                }
//                strings[i][k] = Byte2Hex(bytes[i][j]);
//            }
//
//        }
//        return strings;
//    }

}
