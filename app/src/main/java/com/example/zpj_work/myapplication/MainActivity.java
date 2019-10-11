package com.example.zpj_work.myapplication;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zpj_work.myapplication.analysis.Analysis;
import com.example.zpj_work.myapplication.analysis.ReadLabelAnalysis;
import com.example.zpj_work.myapplication.listviewtest.Fruit;
import com.example.zpj_work.myapplication.listviewtest.FruitAdapter;
import com.example.zpj_work.myapplication.usb.DevComm;
import com.example.zpj_work.myapplication.usb.EnumerateDevice;
import com.example.zpj_work.myapplication.usb.ReceiveMessage;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private static final String TAG                   = "hik";
    private static final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";

    private TextView info;
    private UsbManager myUsbManager;


    private List<Fruit> fruitList =new ArrayList<>();

    int m = 3;
    int first_re = 0;
    int len_re = 3;
    String password_re = "00000000";
    String region_re   = "03";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        info = (TextView) findViewById(R.id.info);
        myUsbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
//        enumerateDevice(m);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //在open前判断是否有连接权限；对于连接权限可以静态分配，也可以动态分配权限
        final PendingIntent pi = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_USB_PERMISSION), 0);

        //添加一个按钮用来再次枚举设备
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click


                EnumerateDevice en = new EnumerateDevice();
//                en.enumerateDevice(1, first_re, len_re, password_re, region_re, myUsbManager, pi);
                en.enumerateDevice(m, first_re, len_re, password_re, region_re, myUsbManager, pi);

                initFruitsData();//初始化数据
                FruitAdapter adapter=new FruitAdapter(MainActivity.this,R.layout.fruit_item,fruitList);
                ListView listView=(ListView)findViewById(R.id.list_view);
                listView.setAdapter(adapter);
            }
        });

        //清屏
        final Button clear_re = (Button) findViewById(R.id.clear_re);
        clear_re.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                FruitAdapter adapter=new FruitAdapter(MainActivity.this,R.layout.fruit_item,fruitList);
                ListView listView=(ListView)findViewById(R.id.list_view);
                listView.setAdapter(adapter);
                clear(listView);
            }
        });

        //测试
        final Button test_q = (Button) findViewById(R.id.text_q);
        test_q.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                //------------------------------------------------------测试analysis.getData_str()中的数据
//                Analysis analysis = new Analysis();
//                String[][] text_q = analysis.getData_str();
//
//                for (int i=0; i<text_q.length; i++) {
//                    if (text_q[i][0] == null) {
//                        continue;
//                    }
//                    text_q[i] = DevComm.removeArrayEmptyTextBackNewArray(text_q[i]);//data_str[i][]
//                    Log.d(TAG, "\n=============text_q============== " + ": ==>" + Arrays.toString(text_q[i]));
//                }
                //------------------------------------------------------
                ReadLabelAnalysis ReadLabel = new ReadLabelAnalysis();
                ReadLabel.setLabel_epc(m, first_re, len_re);
                String[] text_ReadLabel = ReadLabel.getLabel_epc();//setLabel_epc

                Log.d(TAG, "\n=============text_ReadLabel============== " + ": ==>" + Arrays.toString(text_ReadLabel));
                //------------------------------------------------------

            }
        });

        //添加一个按钮用来停止设备
        final Button buttonST = (Button) findViewById(R.id.buttonST);
        buttonST.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                EnumerateDevice en = new EnumerateDevice();
                en.enumerateDevice(0, first_re, len_re, password_re, region_re, myUsbManager, pi);

            }
        });

//        initFruits();//初始化数据
//        FruitAdapter adapter=new FruitAdapter(MainActivity.this,R.layout.fruit_item,fruitList);
//        ListView listView=(ListView)findViewById(R.id.list_view);
//        listView.setAdapter(adapter);


    }

    //初始化滚动数据
//    private void initFruits() { //
//        ReceiveMessage re = new ReceiveMessage();
//        String[] receiveDatas = re.getData();
//        for (int i = 1; i < 10; i++) {
//            Fruit data = new Fruit(i, "5A 55 08 00 0D 11 00 00 01 D6 6A 69");
////            Fruit data = new Fruit(i, receiveDatas[i].toUpperCase());
//
//            fruitList.add(data);
//        }
//    }

    private void initFruitsData() { //ok
        ReceiveMessage re = new ReceiveMessage();
        String[] receiveDatas = re.getData();

        ReadLabelAnalysis reepc = new ReadLabelAnalysis();
        String[] ReadLabelEpc = reepc.getLabel_epc();

        //------------------------------------------------------
        ReadLabelAnalysis ReadLabel = new ReadLabelAnalysis();
        ReadLabel.setLabel_epc(m, first_re, len_re);
        String[] text_ReadLabel = ReadLabel.getLabel_epc();//setLabel_epc

//        Log.d(TAG, "\n=============text_ReadLabel============== " + ": ==>" + Arrays.toString(text_ReadLabel));
        //------------------------------------------------------

//        for (int i = 1; i <= 1024; i++) {
        for (int i = 1; i <= text_ReadLabel.length; i++) {
//            Fruit data = new Fruit(i, receiveDatas[i-1].toUpperCase());//显示原始的冲缓存中取到的数据,没有对转译字符进行处理
//            Fruit data = new Fruit(i, ReadLabelEpc[i-1].toUpperCase());
            Fruit data = new Fruit(i, text_ReadLabel[i-1].toUpperCase());
            fruitList.add(data);
//            if (text_ReadLabel[i] == null) {
//                break;
//            }
        }
    }

    public void clear(View view) {
        fruitList.removeAll(fruitList);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }











//
//    /*断开已连接的USB设备*/
//    public void disconnect_USB(Context context){
//
//        BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
//            public void onReceive(Context context, Intent intent) {
//                String action = intent.getAction();
//
//                if (UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action)) {
//                    UsbDevice device = (UsbDevice)intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
//                    if (device != null) {
//                        myDeviceConnection.releaseInterface(myInterface);
//                        myDeviceConnection.close();
//                    }
//                }
//            }
//        };
//
//
//    }



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
