package com.example.zpj_work.myapplication;

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
import android.hardware.usb.UsbRequest;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    private static final String TAG                   = "hik";
    private static final int VendorID                 = 0x1ef5;
    private static final int ProductID                = 0x0200;
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private static final int TIMEOUT                  = 3000;
    private UsbManager myUsbManager;
    private TextView info;
    private UsbDevice myUsbDevice;
    private UsbInterface myInterface;
    private UsbDeviceConnection myDeviceConnection;
    private UsbEndpoint epOut;
    private UsbEndpoint epIn;
    byte[] mybuffer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        5A 55 06 00 0D 06 00 C8 6A 69
        mybuffer = new byte[]{(byte)0x5A, (byte)0x55, (byte)0x06, (byte)0x00, (byte)0x0D, (byte)0x06, (byte)0x00, (byte)0xC8, (byte)0x6A, (byte)0x69};

//        cmd = "5A5506000D0700C96A69";
//        cmd01 = "5A5508000D11000000D56A69";
//        buf = HexToByteArr(cmd01);

//        Log.i(TAG, bytesToHexString(mybuffer));
        info = (TextView) findViewById(R.id.info);
        myUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
        enumerateDevice();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //添加一个按钮用来再次枚举设备
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                enumerateDevice();

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //枚举设备
    private void enumerateDevice() {
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
                    Toast.makeText(this, "枚举设备成功", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "枚举设备成功");
                    findInterface();
                } else {
                    Log.d(TAG, "Not Found VID and PID");
                }
            }
        } else {
            new AlertDialog.Builder(this).setTitle("未枚举到设备").setMessage("请连接设备").setCancelable(false)
                    .setNeutralButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).show();
        }
    }

    //找到接口
    private void findInterface() {
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
                openDevice();
            }
//            myInterface = intf;
//            Log.d(TAG, "找到我的设备接口 "+intf.getInterfaceClass()+", "+intf.getInterfaceSubclass()+", "+intf.getInterfaceProtocol());
//            openDevice();

            break;
        }
    }

    //获取权限, 打开设备
    private void openDevice() {
        Log.d(TAG, "openDevice : "+ myUsbDevice.getInterfaceCount());

        if (myInterface != null) {
            Log.d(TAG, "openDevice 不为空 : "+ myUsbDevice.getInterfaceCount());

            UsbDeviceConnection conn = null;
            //在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
            PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);
            if (!myUsbManager.hasPermission(myUsbDevice)) {
                myUsbManager.requestPermission(myUsbDevice, pi);
            }
            if (myUsbManager.hasPermission(myUsbDevice)) {
                conn = myUsbManager.openDevice(myUsbDevice);
            } else {
                Toast.makeText(this, "未获得权限", Toast.LENGTH_SHORT).show();
            }

            if (conn == null) {
                return;
            }

            if (conn.claimInterface(myInterface, true)) {
                myDeviceConnection = conn; //到此你的android设备已经连上HID设备
                Log.d(TAG, "打开设备成功");
                Toast.makeText(this, "打开设备成功", Toast.LENGTH_SHORT).show();
                assignEndpoint();
            } else {
                conn.close();
            }
        }
    }

    //拿到端点，用bulkTransfer进行数据发收
    private void assignEndpoint() {
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

//        if (myInterface.getEndpoint(1) != null) {
//            epOut = myInterface.getEndpoint(1);
//        }
//        if (myInterface.getEndpoint(0) != null) {
//            epIn = myInterface.getEndpoint(0);
//        }

        info.setText(myUsbDevice.getDeviceName() + "\nInterfaceCount: " + myUsbDevice.getInterfaceCount() + "\nEndpointCount: " + myInterface.getEndpointCount());
        Log.d(TAG, "assignEndpoint: \n"+myUsbDevice.getDeviceName() + "\nInterfaceCount: " + myUsbDevice.getInterfaceCount() + "\nEndpointCount: " + myInterface.getEndpointCount());
        Log.d(TAG, "assignEndpoint");

        String cmd = "5A5506000D0700C96A69";
        String cmd01 = "5A5508000D11000000D56A69";
        byte[] buf = HexToByteArr(cmd01);
        int re = send_Message(buf);//mybuffer
        byte[] reByte = new byte[64];
        int re2 = receive_Message(reByte);
//        for (Byte byte1:reByte) {
//            System.err.println(byte1);
//        }

        //-----------------------------------------
//        int outMax = outEndpoint.getMaxPacketSize();
//        int inMax = inEndpoint.getMaxPacketSize();
//        ByteBuffer byteBuffer = ByteBuffer.allocate(inMax);
//        UsbRequest usbRequest = new UsbRequest();
//        usbRequest.initialize(connection, inEndpoint);
//        usbRequest.queue(byteBuffer, inMax);
//        if(connection.requestWait() == usbRequest) {
//            byte[] retData = byteBuffer.array();
//            for (Byte byte1 : retData) {
//                System.err.println(byte1);
//            }
//        }
        //-----------------------------------------

        Log.i(TAG, "re: "+re+", re2: "+re2+"\n"+bytesToHexString(reByte));
        Toast.makeText(this, bytesToHexString(reByte), Toast.LENGTH_LONG).show();
        info.setText(myUsbDevice.getDeviceName() + "\nInterfaceCount: " + myUsbDevice.getInterfaceCount() + "\nEndpointCount: " + myInterface.getEndpointCount()
                +"\nepOut: "+cmd01
                +"\nre"+re2+"\n"+bytesToHexString(reByte)
        );

    }

    /*发送数据*/
    public int send_Message(byte[] sendBytes){
        int ret = -1;
        if(epOut != null){
            ret = myDeviceConnection.bulkTransfer(epOut, sendBytes, sendBytes.length, TIMEOUT);
            Log.d(TAG,"send ok");
        }else {
            Log.d(TAG,"send failed");
        }

        return ret;

    }

    /*接收数据*/
    public  int receive_Message(byte[] receiveBytes){

        int ret = -1;
        if(epIn != null){
            ret = myDeviceConnection.bulkTransfer(epIn, receiveBytes, receiveBytes.length, TIMEOUT);
//            ret = Byte2Hex(myDeviceConnection.bulkTransfer(epIn, receiveBytes, receiveBytes.length, TIMEOUT));
            Log.d(TAG, "receive ok");

        }else {
            Log.d(TAG, "receive failed");
        }

        return ret;
    }

    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    /*byte数组拼接为String*/
    public String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i=0; i<src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    static public int isOdd(int num)
    {
        return num & 0x1;
    }

    static public byte HexToByte(String inHex)
    {
        return (byte) Integer.parseInt(inHex,16);
    }

    static public byte[] HexToByteArr(String inHex)
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen)==1)
        {//奇数
            hexlen++;
            result = new byte[(hexlen/2)];
            inHex="0"+inHex;
        }else {//偶数
            result = new byte[(hexlen/2)];
        }
        int j=0;
        for (int i = 0; i < hexlen; i+=2)
        {
            result[j]=HexToByte(inHex.substring(i,i+2));
            j++;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
