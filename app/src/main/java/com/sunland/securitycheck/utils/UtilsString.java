package com.sunland.securitycheck.utils;

import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilsString {
    public static String subString(String src, String startTag, String endTag) {
        int start = src.indexOf(startTag);
        if (start == -1)
            return "";

        String tmp = src.substring(start + startTag.length());
        int end = tmp.indexOf(endTag);

        if (end == -1)
            return "";

        return tmp.substring(0, end);
    }

    public static boolean isIPString(String src) {
        // from android 2.2
        // Pattern ps = Patterns.IP_ADDRESS;
        // Matcher ms = ps.matcher(IP);
        // return ms.matches();
        Pattern IP_ADDRESS = Pattern
                .compile("((25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9])\\.(25[0-5]|2[0-4]"
                        + "[0-9]|[0-1][0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1]"
                        + "[0-9]{2}|[1-9][0-9]|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|[0-1][0-9]{2}"
                        + "|[1-9][0-9]|[0-9]))");
        Matcher matcher = IP_ADDRESS.matcher(src);
        return matcher.matches();
    }

    public static boolean isPortString(String src) {
        if (src.equals(""))
            return false;

        if (!isNumberString(src))
            return false;

        int port = Integer.valueOf(src);
        return (port >= 1 && port <= 65535);
    }

    public static boolean isAlphaNumber(String src) {
        String ps = "[a-zA-Z_0-9]*";
        Pattern pattern = Pattern.compile(ps);
        Matcher matcher = pattern.matcher(src);
        return matcher.matches();
    }

    public static boolean isChineseString(String src) {
        String ps = "[\u4E00-\u9FA5]*";
        Pattern pattern = Pattern.compile(ps);
        Matcher matcher = pattern.matcher(src);
        return matcher.matches();
    }

    public static boolean isNumberString(String src) {
        String ps = "[0-9]*";
        Pattern pattern = Pattern.compile(ps);
        Matcher matcher = pattern.matcher(src);
        return matcher.matches();
    }

    public static boolean checkDate(String src, String format) {
        // format maybe like "yyyyMMdd", "yyyyMM", "HHmm", "yyyyMMddHHmm",
        // "yyyyMMddHHmmss", "yyyy-MM-dd" "yyyy-MM-dd HH:mm", etc

        if (src.length() != format.length())
            return false;

        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        dateFormat.setLenient(false);

        try {
            dateFormat.parse(src);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String checkId(String id) {
        if (id == null)
            return "";

        if (id.length() != 15 && id.length() != 18)
            return "";

        String tmp = id.toUpperCase();
        if (id.length() == 15)
            tmp = id.substring(0, 6) + "19" + id.substring(6) + "0";

        String birth = tmp.substring(6, 14);

        if (!checkDate(birth, "yyyyMMdd"))
            return "";

        if (!isNumberString(tmp.substring(0, 17)))
            return "";

        byte ba[] = tmp.getBytes();
        int[] iw = new int[]{7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8,
                4, 2};
        byte[] iv = new byte[]{'1', '0', 'X', '9', '8', '7', '6', '5', '4',
                '3', '2'};

        int sum = 0;
        for (int i = 0; i < 17; i++)
            sum += (ba[i] - '0') * iw[i];

        if (id.length() == 15) {
            ba[17] = iv[sum % 11];
            return new String(ba);
        } else {
            if (ba[17] != iv[sum % 11])
                return "";
            else
                return id;
        }
    }

    public static boolean isContainSpecialChar(String src) {
        // @&{}
        if (src.indexOf("@") != -1)
            return true;

        if (src.indexOf("&") != -1)
            return true;

        if (src.indexOf("{") != -1)
            return true;

        if (src.indexOf("}") != -1)
            return true;

        return false;
    }

    public static String convertStringNull(String param) {
        if (param == null) {
            return "";
        } else {
            return param;
        }
    }

    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 替换html转义字符
     *
     * @param html
     * @return
     */
    public static String replaceHtmlTrans(String html) {
        html = html.replace("&lt;", "<");
        html = html.replace("&gt;", ">");
        html = html.replace("&amp;", "&");
        html = html.replace("&apos;", "'");
        html = html.replace("&quot;", "\"");
        html = html.replace("&#xd;", "");
        return html;
    }

    /**
     * 去掉字符串的前导零
     *
     * @param num
     * @return
     */
    public static String trimLeftZero(String num) {
        if (num.equals("0") || num.startsWith("0."))
            return num;
        else
            return num.replaceFirst("^0+", "");
    }

    public static boolean isEmpty(String str) {
        if (str == null || str.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
