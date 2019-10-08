package com.example.zpj_work.myapplication.usb;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

import java.util.Arrays;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 15:27
 * @描述
 */
public class ReceiveMessage {

    private static final String TAG                   = "hik";

//    byte[] mybuffer, receiveData;
    String [] receiveDatas = new String[1024];
    static String [] data = null;

    /*接收数据*/
    public  int receive_Message(byte[] receiveBytes, UsbEndpoint epIn, UsbDeviceConnection myDeviceConnection, int TIMEOUT){
        int ret = 1;
        if(epIn != null){
            for (int i = 0; i < 1024; i++) {
                ret = myDeviceConnection.bulkTransfer(epIn, receiveBytes, receiveBytes.length, TIMEOUT);
//                if (receiveBytes.length != ret || ret == 0 || DevComm.bytesToHexString(receiveBytes) == null) {
                if (ret <= 0 || DevComm.bytesToHexString(receiveBytes) == null) {

                    Log.d(TAG, "\n读到的数据为空或者出错 " + ret + ", " + receiveBytes.length);

                    break;
                }

                byte[] receiveData = new byte[ret];
                for (int j = 0; j < ret; j++) {
                    receiveData[j] = receiveBytes[j];
                }

                receiveDatas[i] = DevComm.bytesToHexString(receiveData);
                Log.d(TAG, "\nreceiveDatas " + ret + ": " + receiveDatas[i]);


                DevComm.sleep(300);

            }
//            info.setText(
//                    receiveDatas[0] + "\n"
//                            + receiveDatas[1] + "\n"
//                            + receiveDatas[2] + "\n"
//                            + receiveDatas[3] + "\n"
//                            + receiveDatas[4] + "\n"
//                            + receiveDatas[5] + "\n"
//                            + receiveDatas[6] + "\n"
//                            + receiveDatas[7] + "\n"
//                            + receiveDatas[8] + "\n"
//                            + receiveDatas[9]
//            );


//            DevComm.sleep(300);
            data = receiveDatas;
            Log.d(TAG, "DataRe: "+Arrays.toString(receiveDatas));

        } else {
            Log.d(TAG, "receive failed");
        }

        return ret;
    }

    public String [] getData() {
        Log.d(TAG, "Data: "+Arrays.toString(data));

        return data;
    }


}
