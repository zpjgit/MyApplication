package com.example.zpj_work.myapplication.usb;

import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.util.Log;

import com.example.zpj_work.myapplication.analysis.Analysis;
import com.example.zpj_work.myapplication.listviewtest.Fruit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.zpj_work.myapplication.usb.DevComm.Byte2Hex;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 15:27
 * @描述
 */
public class ReceiveMessage {

    private static final String      TAG       = "hik";
    private              List<Fruit> fruitList =new ArrayList<>();

//    byte[] mybuffer, receiveData;
    String [] receiveDatas = new String[1024];
    byte[][] data_bit = new byte[1024][1024];
    String[][] data_str = new String[1024][1024];

    static String [] data = null;
    static String [] data_String = null;



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
                    data_str[i][j] = DevComm.Byte2Hex(receiveData[j]);
                    data_bit[i][j] = receiveData[j];
                }

                receiveDatas[i] = DevComm.bytesToHexString(receiveData);
                Log.d(TAG, "\nreceiveDatas " + ret + ": " + receiveDatas[i]);

//                Fruit data = new Fruit(i+1, receiveDatas[i].toUpperCase());
//                fruitList.add(data);

//                DevComm.sleep(10);

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
//------------------------------------------------------------------------------------------------------------
            Analysis analysis = new Analysis();
            analysis.setData_str(data_str);

            for (int i=0; i<data_str.length; i++) {
                if (data_str[i][0] == null) {
                    continue;
                }
                data_str[i] = DevComm.removeArrayEmptyTextBackNewArray(data_str[i]);//data_str[i][]
                Log.d(TAG, "\n=========================== " + ": ==>" + Arrays.toString(data_str[i]));//getLabel_epc
            }
//------------------------------------------------------------------------------------------------------------
//            Analysis analysis = new Analysis();
//            analysis.setData_str(data_str);
//
//            for (int i=0; i<data_str.length; i++) {
//                if (data_str[i][0] == null) {
//                    continue;
//                }
//                data_str[i] = DevComm.removeArrayEmptyTextBackNewArray(data_str[i]);//data_str[i][]
//                Log.d(TAG, "\n=========================== " + ": ==>" + Arrays.toString(data_str[i]));//getLabel_epc
//            }
//------------------------------------------------------------------------------------------------------------

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
