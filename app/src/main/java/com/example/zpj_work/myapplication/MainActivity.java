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
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zpj_work.myapplication.analysis.Analysis;
import com.example.zpj_work.myapplication.analysis.EpcOperation;
import com.example.zpj_work.myapplication.analysis.ReadLabelAnalysis;
import com.example.zpj_work.myapplication.analysis.RegionOption;
import com.example.zpj_work.myapplication.analysis.TidOperation;
import com.example.zpj_work.myapplication.analysis.UserOperation;
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
    static int epctag = 0, tidtag = 0, usertag = 0;
//    int first_re = 0;
//    int len_re = 3;
//    String password_re = "00000000";
//    String region_re   = "03";

    CheckBox checkBoxEPC  = null;
    CheckBox checkBoxTID  = null;
    CheckBox checkBoxUSER = null;

    boolean epcon = true;
    boolean tidon = true;
    boolean useron = true;

    //epc
    int epcFirst;
    int epcLen;
    String epcPassword;

    //tid
    int tidFirst;
    int tidLen;
    String tidPassword;

    //user
    int userFirst;
    int userLen;
    String userPassword;

//    final EditText editTextFirst = (EditText) findViewById(R.id.editTextFirst);
//    final EditText editTextLen = (EditText) findViewById(R.id.editTextLen);
//    final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkBoxEPC = (CheckBox)findViewById(R.id.checkboxepc);
        checkBoxTID = (CheckBox)findViewById(R.id.checkboxtid);
        checkBoxUSER = (CheckBox)findViewById(R.id.checkboxuser);

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


//        EditText editTextRegion = (EditText) findViewById(R.id.editTextRegion);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click

                if (epctag == 1) {
                    final EditText editTextFirst = (EditText) findViewById(R.id.editTextFirst);
                    final EditText editTextLen = (EditText) findViewById(R.id.editTextLen);
                    final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

                    epcFirst = Integer.parseInt(editTextFirst.getText().toString());
                    epcLen = Integer.parseInt(editTextLen.getText().toString());
                    epcPassword = editTextPassword.getText().toString();

                    EpcOperation epcOperation = new EpcOperation();

                    epcOperation.setEpcOperation(epcFirst, epcLen, epcPassword);

                    int first_epc = epcOperation.getEpcFirst();
                    int len_epc = epcOperation.getEpcLen();
                    String password_epc = epcOperation.getEpcPassword();
                    String region_epc = epcOperation.getEpcRegion();


                    EnumerateDevice en = new EnumerateDevice();
                    //                en.enumerateDevice(1, first_re, len_re, password_re, region_re, myUsbManager, pi);
                    en.enumerateDevice(m, 0, first_epc, len_epc, password_epc, region_epc, myUsbManager, pi);

                    try {
                        RegionOption regionOption = new RegionOption();
                        regionOption.setRegionOption(m, epctag, first_epc, len_epc);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    DevComm.sleep(300);

                }

                if (tidtag == 2) {
                    final EditText editTextFirst = (EditText) findViewById(R.id.editTextFirst);
                    final EditText editTextLen = (EditText) findViewById(R.id.editTextLen);
                    final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

                    tidFirst = Integer.parseInt(editTextFirst.getText().toString());
                    tidLen = Integer.parseInt(editTextLen.getText().toString());
                    tidPassword = editTextPassword.getText().toString();

                    TidOperation tidOperation = new TidOperation();

                    tidOperation.setTidOperation(tidFirst, tidLen, tidPassword);

                    int first_tid = tidOperation.getTidFirst();
                    int len_tid = tidOperation.getTidLen();
                    String password_tid = tidOperation.getTidPassword();
                    String region_tid = tidOperation.getTidRegion();


                    EnumerateDevice en = new EnumerateDevice();
                    //                en.enumerateDevice(1, first_re, len_re, password_re, region_re, myUsbManager, pi);
                    en.enumerateDevice(m, 0, first_tid, len_tid, password_tid, region_tid, myUsbManager, pi);

                    try {
                        RegionOption regionOption = new RegionOption();
                        regionOption.setRegionOption(m, tidtag, first_tid, len_tid);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DevComm.sleep(300);
                }

                if (usertag == 3) {
                    final EditText editTextFirst = (EditText) findViewById(R.id.editTextFirst);
                    final EditText editTextLen = (EditText) findViewById(R.id.editTextLen);
                    final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

                    userFirst = Integer.parseInt(editTextFirst.getText().toString());
                    userLen = Integer.parseInt(editTextLen.getText().toString());
                    userPassword = editTextPassword.getText().toString();

                    UserOperation userOperation = new UserOperation();

                    userOperation.setUserOperation(userFirst, userLen, userPassword);

                    int first_user = userOperation.getUserFirst();
                    int len_user = userOperation.getUserLen();
                    String password_user = userOperation.getUserPassword();
                    String region_user = userOperation.getUserRegion();


                    EnumerateDevice en = new EnumerateDevice();
                    //                en.enumerateDevice(1, first_re, len_re, password_re, region_re, myUsbManager, pi);
                    en.enumerateDevice(m, 0, first_user, len_user, password_user, region_user, myUsbManager, pi);


                    try {
                        RegionOption regionOption = new RegionOption();
                        regionOption.setRegionOption(m, usertag, first_user, len_user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    DevComm.sleep(300);

                }

                if (epctag == 0 && tidtag == 0 && usertag == 0) {
                    Log.d(TAG, "\n=========================== " + ": ==>" + "请选择需要读取的内容");
                    Toast.makeText(MainActivity.this, "请选择需要读取的内容", Toast.LENGTH_SHORT).show();
                } else {
                    initFruitsData(epctag, tidtag, usertag);//初始化数据
                    FruitAdapter adapter = new FruitAdapter(MainActivity.this, R.layout.fruit_item, fruitList);
                    ListView listView = (ListView) findViewById(R.id.list_view);
                    listView.setAdapter(adapter);
                }
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

                EnumerateDevice en = new EnumerateDevice();
                //                en.enumerateDevice(1, first_re, len_re, password_re, region_re, myUsbManager, pi);
                en.enumerateDevice(m, 104, 0, 0, "00000000", "01", myUsbManager, pi);
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
                EpcOperation epcOperation = new EpcOperation();

                int first_re = epcOperation.getEpcFirst();
                int len_re   = epcOperation.getEpcLen();
                String password_re = epcOperation.getEpcPassword();
                String region_re   = "01";

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
                EpcOperation epcOperation = new EpcOperation();
                int first_re = epcOperation.getEpcFirst();
                int len_re   = epcOperation.getEpcLen();
                String password_re = epcOperation.getEpcPassword();
                String region_re   = "01";

                EnumerateDevice en = new EnumerateDevice();
                en.enumerateDevice(0, 0, first_re, len_re, password_re, region_re, myUsbManager, pi);

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

    private void initFruitsData(int epctag, int tidtag, int usertag) { //ok
//        ReceiveMessage re = new ReceiveMessage();
//        String[] receiveDatas = re.getData();
//
//        ReadLabelAnalysis reepc = new ReadLabelAnalysis();
//        String[] ReadLabelEpc = reepc.getLabel_epc();

        //------------------------------------------------------
//        ReadLabelAnalysis ReadLabel = new ReadLabelAnalysis();

//        EpcOperation epcOperation = new EpcOperation();
//        int first_re = epcOperation.getEpcFirst();
//        int len_re   = epcOperation.getEpcLen();
//        String password_re = epcOperation.getEpcPassword();
//        String region_re   = epcOperation.getEpcRegion();

//        ReadLabel.setLabel_epc(m, first_re, len_re);
//        String[] text_ReadLabelEpc = ReadLabel.getLabel_epc();
//        String[] text_ReadLabelTid = ReadLabel.getLabel_epc();
//        String[] text_ReadLabelUser = ReadLabel.getLabel_epc();

//        Log.d(TAG, "\n=============text_ReadLabel============== " + ": ==>" + Arrays.toString(text_ReadLabel));
        //------------------------------------------------------
        RegionOption regionOption = new RegionOption();

        try {
            String[] text_ReadLabelEpc = regionOption.getReadLabelEpc();
            String[] text_ReadLabelTid = regionOption.getReadLabelTid();
            String[] text_ReadLabelUser = regionOption.getReadLabelUser();

            int readEpcNum = regionOption.getEpcNum();
            int readTidNum = regionOption.getTidNum();
            int readUserNum = regionOption.getUserNum();

            Log.d(TAG, "\n=========================== readEpcNum: " + readEpcNum + " readTidNum: " + readTidNum + " readUserNum: " + readUserNum);
            //        for (int i = 1; i <= 1024; i++) {
            for (int i = 0, k = 1; i < (readEpcNum/1) || i < (readTidNum/1) || i < (readUserNum/1); i++, k++) {
    //            Fruit data = null;
    //            Fruit data = new Fruit(i, receiveDatas[i-1].toUpperCase());//显示原始的冲缓存中取到的数据,没有对转译字符进行处理
    //            Fruit data = new Fruit(i, ReadLabelEpc[i-1].toUpperCase());
                String nepc = null;
                String ntid = null;
                String nuser = null;

                if (epctag == 1){
                    try {
                        nepc = text_ReadLabelEpc[i].toUpperCase();
                    } catch (Exception e) {
                        e.printStackTrace();
                        nepc = null;
                    }
                } else {
                    nepc = null;
                }

                if (tidtag == 2)  {
                    try {
                        ntid = text_ReadLabelTid[i].toUpperCase();
                    } catch (Exception e) {
                        e.printStackTrace();
                        ntid = null;
                    }
                } else {
                    ntid = null;
                }

                if (usertag == 3) {
                    try {
                        nuser = text_ReadLabelUser[i].toUpperCase();
                    } catch (Exception e) {
//                        e.printStackTrace();
                        nuser = null;
                    }
                } else {
                    nuser = null;
                }

//                if (epctag == 1) {
//                    if (text_ReadLabelEpc[i] == null) {
//                        continue;
//                    } else {
//                        Log.d(TAG, "\n=============f (epctag == 1)============== " + text_ReadLabelEpc[i]);
//                        nepc = text_ReadLabelEpc[i].toUpperCase();
//                    }
//                }
//
//                if (tidtag == 2) {
//                    if (text_ReadLabelTid[i] == null) {
//                        Log.d(TAG, "\n=============f (tidtag == 2))============== "+text_ReadLabelTid[i]);
//
//                        break;
//                    } else {
//                        ntid = text_ReadLabelTid[i].toUpperCase();
//                    }
//                }
//
//                if (usertag == 3) {
//                    if (text_ReadLabelUser[i] == null) {
//                        continue;
//                    } else {
//                        nuser = text_ReadLabelUser[i].toUpperCase();
//                    }
//                }


                //                Fruit data = new Fruit(i, 0, text_ReadLabelEpc[i - 1].toUpperCase(), text_ReadLabelTid[i - 1].toUpperCase(), text_ReadLabelUser[i - 1].toUpperCase());
                Fruit data = new Fruit(k, 0, nepc, ntid, nuser);
                fruitList.add(data);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {

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

    public void onCheckboxClicked(View view) {
        final EditText editTextFirst = (EditText) findViewById(R.id.editTextFirst);
        final EditText editTextLen = (EditText) findViewById(R.id.editTextLen);
        final EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);

        boolean checked = ((Checkable) view).isChecked();
        try {
            switch (view.getId()) {

                case R.id.checkboxepc:
                    if (checked) {
                        epcFirst = Integer.parseInt(editTextFirst.getText().toString());
                        epcLen = Integer.parseInt(editTextLen.getText().toString());
                        epcPassword = editTextPassword.getText().toString();

                        if (epcPassword.equals("")) {
                            Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        }

                        epctag = 1;

                        Log.d(TAG, "\n=========================== " + ": ==>" + "epc on" + epcFirst + ", " + epcLen + ", " + epcPassword);
                        Toast.makeText(MainActivity.this, "开始读 EPC", Toast.LENGTH_SHORT).show();
                    } else {
                        epctag = 0;
                        Log.d(TAG, "\n=========================== " + ": ==>" + "epc off");
                        Toast.makeText(MainActivity.this, "epc off", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.checkboxtid:
                    if (checked) {
                        tidFirst = Integer.parseInt(editTextFirst.getText().toString());
                        tidLen = Integer.parseInt(editTextLen.getText().toString());
                        tidPassword = editTextPassword.getText().toString();

                        if (tidPassword.equals("")) {
                            Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        }

                        tidtag = 2;

                        Log.d(TAG, "\n=========================== " + ": ==>" + "tid on" + tidFirst + ", " + tidLen + ", " + tidPassword);
                        Toast.makeText(MainActivity.this, "开始读 TID", Toast.LENGTH_SHORT).show();
                    } else {
                        tidtag = 0;
                        Log.d(TAG, "\n=========================== " + ": ==>" + "tid off");
                        Toast.makeText(MainActivity.this, "tid off", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.checkboxuser:
                    if (checked) {
                        userFirst = Integer.parseInt(editTextFirst.getText().toString());
                        userLen = Integer.parseInt(editTextLen.getText().toString());
                        userPassword = editTextPassword.getText().toString();

                        if (userPassword.equals("")) {
                            Toast.makeText(MainActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                        }

                        usertag = 3;

                        Log.d(TAG, "\n=========================== " + ": ==>" + "user on" + userFirst + ", " + userLen + ", " + userPassword);
                        Toast.makeText(MainActivity.this, "开始读 USER", Toast.LENGTH_SHORT).show();
                    } else {
                        usertag = 0;
                        Log.d(TAG, "\n=========================== " + ": ==>" + " user off ==> " + usertag);
                        Toast.makeText(MainActivity.this, "user off", Toast.LENGTH_SHORT).show();
                    }
                    break;

            }
        } catch (NumberFormatException e) {
            Log.d(TAG, "\n=========================== " + ": ==>" + "请输入 偏移地址 和 读取长度");
            Toast.makeText(MainActivity.this, "请输入 偏移地址 和 读取长度", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}
