package com.example.gsyvideoplayer.utils;

/**
 * @PACKAGE com.lchr.video.util
 * @DESCRIPTION: TODO
 * @AUTHOR dongen_wang
 * @DATE 4/13/17 18:07
 * @VERSION V1.0
 */
public class Mp4Util {

    public static int getMvhd(byte[] arr){
        if(arr == null || arr.length ==0)
            return -1;
        int i = 0,len = arr.length;
        while (i < len){
            String str = bytesToHexString(new byte[]{arr[i],arr[i+1],arr[i+2],arr[i+3]});
            if("6D766864".equals(str)){// mvhd
                i+=4;
                break;
            }
            i+=4;
        }
        return i;
    }


    /**
     * 数组转换成十六进制字符串
     * @param  bArray
     * @return HexString
     */
    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    public static String getDuration(long time){
        String hour = "",minute = "",second = "";
        long d = time;
        long h = d / (60*60);
        if(h > 0) {
            if (h > 9) {
                hour = h + ":";
            } else {
                hour = "0"+h+":";
            }
        }
        long m = d % (3600) / 60;
        if(m > 0) {
            if (m > 9) {
                minute = m + ":";
            } else {
                minute = "0"+m+":";
            }
        }
        long s = d % (3600) % 60;
        if(s > 0) {
            if (s > 9) {
                second = s + "";
            } else {
                second = "0"+s;
            }
        }
        return hour+minute+second;
    }
}
