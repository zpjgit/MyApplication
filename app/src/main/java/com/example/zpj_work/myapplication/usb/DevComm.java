package com.example.zpj_work.myapplication.usb;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/9/30 0030 15:23
 * @描述
 */
public class DevComm {

    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    /*byte数组拼接为String*/
    public static String bytesToHexString(byte[] src) {
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

    public static void sleep(int str) {
        try {
            Thread.currentThread().sleep(str);//毫秒
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
