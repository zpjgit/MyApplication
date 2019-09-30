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

    byte[] mybuffer, receiveData;
    String [] receiveDatas = new String[10];
    static String [] data = null;

    /*接收数据*/
    public  int receive_Message(byte[] receiveBytes, UsbEndpoint epIn, UsbDeviceConnection myDeviceConnection, int TIMEOUT){
        int ret = 1;
        if(epIn != null){
            for (int i = 0; i < 10; i++) {
                ret = myDeviceConnection.bulkTransfer(epIn, receiveBytes, receiveBytes.length, TIMEOUT);
                //            ret = Byte2Hex(myDeviceConnection.bulkTransfer(epIn, receiveBytes, receiveBytes.length, TIMEOUT));
                //                Log.d(TAG, "receive ok"+ret);
                receiveData = new byte[ret];
                for (int j = 0; j < ret; j++) {
                    receiveData[j] = receiveBytes[j];
                }

                receiveDatas[i] = DevComm.bytesToHexString(receiveData);
                Log.d(TAG, "\nreceiveDatas " + ret + ": " + receiveDatas[i]);
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

            //            initFruits(receiveDatas);//初始化数据
            //            FruitAdapter adapter=new FruitAdapter(MainActivity.this,R.layout.fruit_item,fruitList);
            //            ListView listView=(ListView)findViewById(R.id.list_view);
            //            listView.setAdapter(adapter);

            //            //初始化滚动数据
            //            private void initFruits() {
            //                for (int i = 1; i < 100; i++) {
            //                    Fruit data = new Fruit(i, "5A 55 08 00 0D 11 00 00 01 D6 6A 69");
            //                    fruitList.add(data);
            //                }
            //            }

            DevComm.sleep(300);
            data = receiveDatas;
            Log.d(TAG, "DataRe: "+Arrays.toString(receiveDatas));

            //            Log.d(TAG, "receiveDatas: \n"+receiveDatas[1]+"\n"+receiveDatas[2]+"\n"+receiveDatas[3]);
            //            info.setText("\n"+receiveDatas[1]+"\n"+receiveDatas[2]+"\n"+receiveDatas[3]);
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
