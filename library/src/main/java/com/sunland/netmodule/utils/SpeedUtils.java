package com.sunland.netmodule.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * Created by LSJ on 2017/10/23.
 */

public class SpeedUtils {

    /**
     * 获取手机下载某一地址的平均速度
     *
     * @param url
     * @throws RuntimeException
     */
    public static String getDownloadAvgSpeed(String url) throws RuntimeException {
        String speed = "";

        HttpURLConnection conn = null;
        InputStream inputStream = null;
        long fileSize = 0;//文件总长度
        long downloadFileSize = 0; //已下载文件长度

        try {
            URL myURL = new URL(url);
            conn = (HttpURLConnection) myURL.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(15 * 1000);
            Map<String, List<String>> map = conn.getHeaderFields();

            if (fileSize <= 0) {
                fileSize = conn.getContentLength();
                if (fileSize <= 0) {
                    List<String> tmp = map.get("Content-Length");
                    if (tmp != null && tmp.size() > 0)
                        fileSize = Long.parseLong(tmp.get(0));
                }
            }

            if (fileSize <= 0)
                throw new RuntimeException("无法获取文件总长度");

            if (conn.getResponseCode() != 200) //断点下载为206，否则正常的是200
                throw new RuntimeException("服务器连接失败");
            inputStream = conn.getInputStream();

            if (inputStream == null)
                throw new RuntimeException("无法获取下载文件流");
            byte buf[] = new byte[1024 * 10];
            long startTime = System.currentTimeMillis();
            do {
                // 循环读取
                int numread = inputStream.read(buf);
                if (numread == -1 || (System.currentTimeMillis() - startTime) > 10 * 1000) {
                    break;
                }
                downloadFileSize += numread;
            } while (true);

            long intervalTime = System.currentTimeMillis() - startTime;
            // speed = formatSpeed(downloadFileSize*1000.0 / intervalTime);
            speed = String.format("%.2f", downloadFileSize * 1000.0 / intervalTime);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        } finally {
            try {
                if (inputStream != null)
                    inputStream.close();
                if (conn != null) conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e.getMessage());
            }
            return speed;
        }
    }

    /**
     * 转换日常速度显示
     *
     * @param speed 读取总字节除以耗时
     * @return
     */
    public static String formatSpeed(double speed) {
        if (speed >= 1024 * 1024)
            return String.format("%.2f", speed / 1024 / 1024) + "MB/s";
        else
            return String.format("%.2f", speed / 1024) + "KB/s";
    }
}
