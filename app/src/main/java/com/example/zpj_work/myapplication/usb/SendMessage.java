package com.example.zpj_work.myapplication.usb;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 15:27
 * @描述
 */
public class SendMessage {

    private static final String TAG                   = "hik";


    /*发送数据*/
    public int send_Message(byte[] sendBytes, UsbEndpoint epOut, UsbDeviceConnection myDeviceConnection, int TIMEOUT){
        int ret = -1;
        if(epOut != null){
            ret = myDeviceConnection.bulkTransfer(epOut, sendBytes, sendBytes.length, TIMEOUT);
            Log.d(TAG,"send ok");
        } else {
            Log.d(TAG,"send failed");
        }

        return ret;

    }
}
