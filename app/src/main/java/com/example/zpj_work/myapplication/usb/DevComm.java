package com.example.zpj_work.myapplication.usb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    //去除一维数组的空项
    public static String[] removeArrayEmptyTextBackNewArray(String[] strArray) {
        List<String> strList= Arrays.asList(strArray);
        List<String> strListNew=new ArrayList<>();
        for (int i = 0; i <strList.size(); i++) {
            if (strList.get(i)!=null&&!strList.get(i).equals("")){
                strListNew.add(strList.get(i));
            }
        }
        String[] strNewArray = strListNew.toArray(new String[strListNew.size()]);

        return strNewArray;
    }

    //十六进制字符转十进制int
    public static long f(String s) {
        int n=0;
        long b;
        long sum=0;
        for(int i=s.length()-1;i>=0;i--) {
            char a=s.charAt(i);
            if(a=='A'||a=='B'||a=='C'||a=='D'||a=='E'||a=='F') {
                b=a-'0'-7;
            }else
                b=a-'0';
            //				b=b*16; //错误，若不用for循环，b每次都是全新的数，相当于每次都是*16的1次方
            //				sum=sum+b;
            for(int j=0;j<n;j++) {
                b=b*16;
            }
            sum=sum+b;
            n++;
        }
        //		System.out.println(sum);
        return (long)sum;
    }

    static public String StringArraysJoint(String[] inBytArr, int offset, int len)//String数组拼接为字符串
    {
        StringBuilder sb = new StringBuilder(inBytArr.length*3);
        int offset1 = inBytArr.length;  //从0算起不需要“int offset1 = inBytArr.length-1”

        for( int i = offset; i < len; i++ )
        {
            if (inBytArr == null) {
                return null;
            }
//            sb.append(inBytArr[i]);
            try {
                sb.append(inBytArr[i]).append("");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    //计算校验和
    static String checksum = "aa";
    static public String checksum(String[] cmdredNo0x, int offset, int end) {
        //		String checksums = StringArraysJoint(cmdredNo0x, 0, (cmdredNo0x.length-3));//包含结束帧
        String checksums = StringArraysJoint(cmdredNo0x, offset, end);
        //		System.out.println(checksums);
        checksum = makeCheckSum(checksums); //得到十六进制校验和

        return checksum;
    }

    //计算校验位 ，返回十六进制校验位
    static public String makeCheckSum(String data) {
        int dSum = 0;
        int length = data.length();
        int index = 0;
        // 遍历十六进制，并计算总和
        while (index < length) {
            String s = data.substring(index, index + 2); // 截取2位字符
            dSum += Integer.parseInt(s, 16); // 十六进制转成十进制 , 并计算十进制的总和
            index = index + 2;
        }

        int mod = dSum % 256; // 用256取余，十六进制最大是FF，FF的十进制是255
        String checkSumHex = Integer.toHexString(mod); // 余数转成十六进制
        length = checkSumHex.length();
        if (length < 2) {
            checkSumHex = "0" + checkSumHex;  // 校验位不足两位的，在前面补0
        }
        return checkSumHex;
    }

    public static byte[] intToByte2byte(int val){
        byte[] b = new byte[2];
        b[0] = (byte)(val & 0xFF);
        b[1] = (byte)((val >> 8) & 0xFF);

        return b;
    }
}
