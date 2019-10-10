package com.example.zpj_work.myapplication.analysis;

import android.util.Log;

import com.example.zpj_work.myapplication.usb.DevComm;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/10 0010 16:51
 * @描述
 */
public class TranslateAnalysis {
    private static final String      TAG       = "hik";

    private static String[] data;
    public  static String[] uns = null;
    public  static String un = null;


    public void setTranslate(String[] data) {
        for (int i=0; i<(data.length-2); i++) {
            if (data[i].equals("99")) {
                if (!(data[i+1].equals("A5")) && !(data[i+1].equals("66")) && !(data[i+1].equals("95"))) {
                    Log.d(TAG, "\n=============if (s1 && s2 && s3 && s4)============== " + ": !!! ------------->" + data[i] + ", " + data[i+1]);
                    this.data = uns;
                }
                switch (data[i+1]) {
                    case "A5":
                        data[i] = "5A";
                        break;
                    case "66":
                        data[i] = "99";
                        break;
                    case "95":
                        data[i] = "6A";
                        break;
                }
                Log.d(TAG, "\n=============if (s1 && s2 && s3 && s4)============== " + ": ------------->" + data[i] + ", " + data[i+1]);

                for (int j=i+1; j<(data.length); j++) {

                    try {
                        String data_sr = data[j + 1];
                        data[j] = data_sr;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                data[data.length-1] = un;
                data = DevComm.removeArrayEmptyTextBackNewArray(data);
            } else {
                continue;
            }

        }


        this.data = data;
    }


    public String[] getTranslate() {
        return data;
    }

}
