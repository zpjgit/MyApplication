package com.example.zpj_work.myapplication.usb;

import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import com.example.zpj_work.myapplication.analysis.ReadOperation;

import java.util.HashMap;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 14:58
 * @描述
 */
public class EnumerateDevice {

    private static final String TAG                   = "hik";
    private UsbManager myUsbManager;
    private static final int VendorID                 = 0x1ef5;
    private static final int ProductID                = 0x0200;
    private UsbDevice myUsbDevice;
    private UsbInterface myInterface;
//    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private UsbDeviceConnection myDeviceConnection;
    private UsbEndpoint epOut;
    private UsbEndpoint epIn;
    private static final int TIMEOUT                  = 3000;

    //枚举设备
    public void enumerateDevice(int m, int first_re, int len_re, String password_re, String region_re, UsbManager myUsbManager, PendingIntent pi) {
        if (myUsbManager == null) {
            return;
        }

        HashMap<String, UsbDevice> deviceList = myUsbManager.getDeviceList();
        if (!deviceList.isEmpty()) { // deviceList不为空
            StringBuffer sb = new StringBuffer();
            for (UsbDevice device : deviceList.values()) {
                sb.append(device).toString();
                sb.append("\n");
                //                info.setText(sb);
                //输出设备信息
                Log.d(TAG, "DeviceInfo: "+device.getDeviceId() + ", "+device.getVendorId()+", "+device.getProductId());

                //枚举到设备
                if (device.getVendorId() == VendorID && device.getProductId() == ProductID) {
                    myUsbDevice = device;
//                    Toast.makeText(this, "枚举设备成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "枚举设备成功");
                    findInterface(m, first_re, len_re, password_re, region_re, myUsbManager, pi);
                } else {
                    Log.d(TAG, "Not Found VID and PID");
                }
            }
        } else {
//            new AlertDialog.Builder(this).setTitle("未枚举到设备").setMessage("请连接设备").setCancelable(false)
//                    .setNeutralButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    }).show();
        }
    }

    //找到接口
    private void findInterface(int m, int first_re, int len_re, String password_re, String region_re, UsbManager myUsbManager, PendingIntent pi) {
        if (myUsbDevice == null) {
            return;
        }

        Log.d(TAG, "interfaceCounts : "+ myUsbDevice.getInterfaceCount());
        for (int i=0; i<myUsbDevice.getInterfaceCount(); i++) {
            Log.d(TAG, "interfaceCounts too : "+ myUsbDevice.getInterfaceCount());

            UsbInterface intf = myUsbDevice.getInterface(i);

            //根据手上的设备做一些判断，其实这些信息都可以在枚举到设备时打印出来
            if (intf.getInterfaceClass() == 255 && intf.getInterfaceSubclass() == 0 && intf.getInterfaceProtocol() == 255) {
                myInterface = intf;
                Log.d(TAG, "找到我的设备接口. 接口的id号: "+intf.getId());
                openDevice(m, first_re, len_re, password_re, region_re, myUsbManager, pi);
            }

            break;
        }
    }

    //获取权限, 打开设备
    private void openDevice(int m, int first_re, int len_re, String password_re, String region_re, UsbManager myUsbManager, PendingIntent pi) {
        Log.d(TAG, "openDevice : "+ myUsbDevice.getInterfaceCount());

        if (myInterface != null) {
            Log.d(TAG, "openDevice 不为空 : "+ myUsbDevice.getInterfaceCount());

            UsbDeviceConnection conn = null;
            //在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
//            PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            if (!myUsbManager.hasPermission(myUsbDevice)) {
                myUsbManager.requestPermission(myUsbDevice, pi);
            }
            if (myUsbManager.hasPermission(myUsbDevice)) {
                conn = myUsbManager.openDevice(myUsbDevice);
            } else {
//                Toast.makeText(this, "未获得权限", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "openDevice 未获得权限 : ");

            }

            if (conn == null) {
                return;
            }

            if (conn.claimInterface(myInterface, true)) {
                myDeviceConnection = conn; //到此你的android设备已经连上HID设备
                Log.d(TAG, "打开设备成功");
//                Toast.makeText(this, "打开设备成功", Toast.LENGTH_SHORT).show();
                assignEndpoint(m, first_re, len_re, password_re, region_re);
            } else {
                conn.close();
            }
        }
    }

    //拿到端点，用bulkTransfer进行数据发收
    private void assignEndpoint(int m, int first_re, int len_re, String password_re, String region_re) {
        for (int i=0; i<myInterface.getEndpointCount(); i++ ) {
            UsbEndpoint ep = myInterface.getEndpoint(i);
            if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                    epOut = ep;
                } else {
                    epIn = ep;
                }
            }
        }

        Log.d(TAG, "assignEndpoint: \n"+myUsbDevice.getDeviceName() + "\nInterfaceCount: " + myUsbDevice.getInterfaceCount() + "\nEndpointCount: " + myInterface.getEndpointCount());
        Log.d(TAG, "assignEndpoint");

        byte[] buf = null;
        String cmd = "5A5506000D0700C96A69";
        String cmd01 = "5A5508000D11000000D56A69"; //非连续盘点不启用FastID功能
        String cmd02 = "5A5508000D11000001D66A69"; //连续盘点不启用FastID功能    5A 55 08 00 0D 11 00 00 01 D6 6A 69
        String abortc = "5A5506000D0300C56A69"; //停止
        if (m == 0) {
            buf = DevComm.HexToByteArr(abortc);
            SendMessage send = new SendMessage();
            int re = send.send_Message(buf, epOut, myDeviceConnection, TIMEOUT);
//            myDeviceConnection.releaseInterface(myInterface);

            //            myDeviceConnection.close();

        }
        if (m == 1) {
            buf = DevComm.HexToByteArr(cmd01);
            SendMessage send = new SendMessage();
            int re = send.send_Message(buf, epOut, myDeviceConnection, TIMEOUT);
            myDeviceConnection.releaseInterface(myInterface);

            //            myDeviceConnection.close();//关闭usb口

        }

        if (m == 2) {
            buf = DevComm.HexToByteArr(cmd02);
            SendMessage send = new SendMessage();
            int re = send.send_Message(buf, epOut, myDeviceConnection, TIMEOUT);
            myDeviceConnection.releaseInterface(myInterface);

            //            myDeviceConnection.close();//关闭usb口

        }

        if (m == 3) {
            ReadOperation readOp = new ReadOperation();

            String rO =readOp.readOperation(first_re, len_re, password_re, region_re);

            buf = DevComm.HexToByteArr(rO);
            SendMessage send = new SendMessage();
            int re = send.send_Message(buf, epOut, myDeviceConnection, TIMEOUT);
            myDeviceConnection.releaseInterface(myInterface);

            //            myDeviceConnection.close();//关闭usb口

        }
        ReceiveMessage receive = new ReceiveMessage();
        byte[] reByte = new byte[1024];

        int re2 = receive.receive_Message(reByte, epIn, myDeviceConnection, TIMEOUT);


    }



}
