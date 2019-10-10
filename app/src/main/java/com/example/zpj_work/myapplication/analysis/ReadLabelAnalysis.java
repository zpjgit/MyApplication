package com.example.zpj_work.myapplication.analysis;

import android.util.Log;

import com.example.zpj_work.myapplication.usb.DevComm;

import java.util.Arrays;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/9 0009 10:05
 * @描述
 */
public class ReadLabelAnalysis {

//    Analysis analysis = new Analysis();
//    String[][] data_all = analysis.getData_str();

    private static final String TAG                   = "hik";
    private static String[] label_epc = null;

    public static String epc = null;
    public static String epcun = null;
    private String[] cache;
    //    public static String[] label_epc = null;

    //    public void setLabel_epc(String[][] data_all) {
    public void setLabel_epc() {


        Analysis analysis = new Analysis();
        String[][] data_all = analysis.getData_str();
        label_epc = new String[data_all.length];

        int k = 0, i=0;
//        String[] label_epc = new String[data_all.length];

        for (i=0,k=0; i<data_all.length; i++) {
            if (data_all[i][0] == null) {
                continue;
            }
            data_all[i] = DevComm.removeArrayEmptyTextBackNewArray(data_all[i]);//data_str[i][]
            Log.d(TAG, "\n=========data_all================== " + ": ==>" + Arrays.toString(data_all[i]));//getLabel_epc

            String getepc = getEpc(data_all[i]);
            if (getepc == null) {
                continue;
            }

            label_epc[k]     = getepc;
            Log.d(TAG, "\n=============label_epc[k] = getepc============== " + ": ==>" + label_epc[k]);
            k++;
        }

        label_epc = DevComm.removeArrayEmptyTextBackNewArray(label_epc);
        Log.d(TAG, "\n=============label_epc============== " + ": ==>" + Arrays.toString(label_epc));

        this.label_epc = label_epc;
    }


    public String[] getLabel_epc() {
        return label_epc;
    }

    //获取单个epc
    public String getEpc(String[] data_epc) {

        boolean s1 = data_epc[4].equals("0D");
        boolean s2 = data_epc[5].equals("05");
        boolean s3 = data_epc[6].equals("00");

        String check = DevComm.checksum(data_epc, 0, data_epc.length-3).toUpperCase(); //计算校验值
        boolean s4 = data_epc[data_epc.length-3].equals(check);
//        Log.d(TAG, "\n=============s4============== check: " + check + " data_epc[data_epc.length-3]: " + data_epc[data_epc.length-3]);



        if (s1 && s2 && s3 && s4) {

            for (int i=0; i<(data_epc.length-2); i++) {
                if (data_epc[i].equals("99")) {
                    if (!(data_epc[i+1].equals("A5")) && !(data_epc[i+1].equals("66")) && !(data_epc[i+1].equals("95"))) {
                        Log.d(TAG, "\n=============if (s1 && s2 && s3 && s4)============== " + ": !!! ------------->" + data_epc[i] + ", " + data_epc[i+1]);
                        return epcun;
                    }
                    switch (data_epc[i+1]) {
                        case "A5":
                            data_epc[i] = "5A";
                            break;
                        case "66":
                            data_epc[i] = "99";
                            break;
                        case "95":
                            data_epc[i] = "6A";
                            break;
                    }
                    Log.d(TAG, "\n=============if (s1 && s2 && s3 && s4)============== " + ": ------------->" + data_epc[i] + ", " + data_epc[i+1]);

                    for (int j=i+1; j<(data_epc.length); j++) {

                        try {
                            String data = data_epc[j + 1];
                            data_epc[j] = data;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    data_epc[data_epc.length-1] = epcun;
                    data_epc = DevComm.removeArrayEmptyTextBackNewArray(data_epc);
                } else {
                    continue;
                }

            }



            Log.d(TAG, "\n=============if (s1 && s2 && s3 && s4)============== " + ": ==>" + Arrays.toString(data_epc));

            String cmdred_HIGH = data_epc[15];
            String cmdred_LOW  = data_epc[16];

            String EpcLeng = cmdred_LOW.concat(cmdred_HIGH);//拼接EPC的长度
//            Log.d(TAG, "\n=============iEpcLeng============== " + EpcLeng);

            int iEpcLeng = (int) DevComm.f(EpcLeng);//将字符串转为十进制
//            Log.d(TAG, "\n=============iEpcLeng============== " + iEpcLeng);

            epc = DevComm.StringArraysJoint(data_epc, 17, iEpcLeng + 17);
            if (epc == null) {
                return epcun;
            }
            Log.d(TAG, "\n=============getEpc============== " + EpcLeng + "," + iEpcLeng + ": ==>" +"EPC:"+ epc);

        }
//        Log.d(TAG, "\n++++++++++++++getEpc++++++++++++++++++ ");

        return epc;
    }


}
