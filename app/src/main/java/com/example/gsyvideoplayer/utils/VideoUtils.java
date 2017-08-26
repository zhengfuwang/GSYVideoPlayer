package com.example.gsyvideoplayer.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLDecoder;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by xiyangyuge on 2017/8/8.
 */

public class VideoUtils {

    public static Map<String, String> getUrlParams(String url) {
        Map<String, String> params = new HashMap<String, String>();
        try {
            String urlPart = url.substring(url.indexOf("?")+1);
            if (urlPart != null) {
                String query = urlPart;
                String[] splitArr = query.split("_DYR_AND_");
                if(splitArr.length >= 1) {
                    for (String param : splitArr) {
                        String[] pair = param.split("_DYR_EQUAL_");
                        String key = URLDecoder.decode(pair[0], "UTF-8");
                        String value = "";
                        if (pair.length > 1) {
                            value = URLDecoder.decode(pair[1], "UTF-8");
                            params.put(key, value);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return params;
    }

    private String readFileHeader(HttpURLConnection conn){
        byte[] stream = new byte[512];
        InputStream inputStream = null;
        try {

            inputStream = conn.getInputStream();
            inputStream.read(stream);
            int index = Mp4Util.getMvhd(stream);
            if(stream.length > index) {
                index = index + 12;
                byte[] scaleTime = new byte[]{stream[index],stream[index+1],stream[index+2],stream[index+3]};
                String scalTime = Mp4Util.bytesToHexString(scaleTime);
                byte[] duration = new byte[]{stream[index+4],stream[index+5],stream[index+6],stream[index+7]};
                String durationStr = Mp4Util.bytesToHexString(duration);
                int scalInt = Integer.parseInt(scalTime,16);
                int durationInt = Integer.parseInt(durationStr,16);
//                duration/scaleTime
                return Mp4Util.getDuration(durationInt/scalInt);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public static String FormetFileSize(long fileS) { //转换文件大小
        DecimalFormat df = new DecimalFormat("0.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) +"G";
        }
        return fileSizeString;
    }

    public static String readTextFromInputStream(InputStream is) throws Exception {
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer buffer = new StringBuffer("");
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            buffer.append(str);
            buffer.append("\n");
        }
        return buffer.toString();
    }
}
