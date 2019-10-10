package com.example.zpj_work.myapplication.analysis;

import com.example.zpj_work.myapplication.usb.DevComm;

/**
 * @创建者 zhengpengjie
 * @创建日期 2019/10/10 0010 18:16
 * @描述
 */
public class ReadOperation {

    public static String readOperation(int first_re, int len_re, String password_re, String region_re) {

        String cmd = null;

        try {
            //偏移地址
            byte[] first_read_byte = DevComm.intToByte2byte(first_re);      //int转byte
            String first_read_String1 = DevComm.Byte2Hex(first_read_byte[0]);//1  十六进制 低位
            String first_read_String2 = DevComm.Byte2Hex(first_read_byte[1]);//0  十六进制 高位

            //长
            //帧长
            int len = 15+len_re*2;
            byte[] len_read_byte = DevComm.intToByte2byte(len);
            String len_read_String1 = DevComm.Byte2Hex(len_read_byte[0]);
            String len_read_String2 = DevComm.Byte2Hex(len_read_byte[1]);
            //读取长
            byte[] len_re_byte = DevComm.intToByte2byte(len_re);
            //		String len_re_String1 = MyFunc.Byte2Hex(len_re_byte[0]);
            //		String len_re_String2 = MyFunc.Byte2Hex(len_re_byte[1]);

            //不启用FastID功能非连续盘点
            byte[] cmd02 = new byte[]{(byte)0x5A,(byte)0x55,(byte)0x08,(byte)0x00,(byte)0x0D,(byte)0x11,(byte)0x00,(byte)0x00,(byte)0x00,(byte)0xD5,(byte)0x6A,(byte)0x69};
            String CMD02 = "5A5508000D11000000D56A69";


            String[] cmdEpc = new String[21];


            //帧头
            cmdEpc[0] = "5A";
            cmdEpc[1] = "55";
            //帧长
            cmdEpc[2] = "11";
            cmdEpc[3] = "00";
            //端口
            cmdEpc[4] = "0D";
            //指令类型
            cmdEpc[5] = "12";
            cmdEpc[6] = "00";
            //帧载start------------------------------------
            //操作区
            cmdEpc[7] = region_re;//"01";//0x01:EPC区; 0x02:TID区; 0x03:USER区;
            //偏移地址
            cmdEpc[8] = first_read_String1;
            cmdEpc[9] = first_read_String2;
            System.out.println("cmdEpc[8]==>"+cmdEpc[8]);
            System.out.println("cmdEpc[9]==>"+cmdEpc[9]);
            //读取长度
            cmdEpc[10] = DevComm.Byte2Hex(len_re_byte[0]);
            cmdEpc[11] = DevComm.Byte2Hex(len_re_byte[1]);
            System.out.println("cmdEpc[10]==>"+cmdEpc[10]);
            System.out.println("cmdEpc[11]==>"+cmdEpc[11]);
            //访问密码
            String sbpass = password_re;
            for (int i = 0; i < 4; i++) {
                cmdEpc[i+12] = sbpass.substring((i*2), (i*2+2));//每次两个字符
                System.out.println("cmdEpc[i+12] "+cmdEpc[i+12]);
            }

            //天线轮询读取
            cmdEpc[16] = "00";//(byte)0x00; //0x0:不进行轮询切换,使用默认天线进行读取;
            //0x1:进行天线轮询切换,轮询方式由"轮询模式"标记决定;
            //轮询模式
            cmdEpc[17] = "00";//(byte)0x00; //0x00:非连续; 0x01:连续;
            //帧载end---------------------------------------
            //检验和
            cmdEpc[18] = DevComm.makeCheckSum(DevComm.StringArraysJoint(cmdEpc, 0, 18)).toUpperCase();//(DevComm.checksum(cmdEpc, 0, 18)).toUpperCase();
            System.out.println("cmdEpc[18]==>"+cmdEpc[18]);
            //帧尾
            cmdEpc[19] = "6A";
            cmdEpc[20] = "69";

            cmd = DevComm.StringArraysJoint(cmdEpc, 0, 21);
            byte[] buf = DevComm.HexToByteArr(cmd);
            //		byte[] buf = MyFunc.HexToByteArr("5A5508000D11000000D56A69");
//



            //================================================================获取设备的EPC码start
        } catch (Exception e) {
            // TODO: handle exception
        } finally {

            return cmd;
        }


    }
}
