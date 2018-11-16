package com.sunland.netmodule.utils;

/**
 * Created by PeitaoYe
 * On 2018/8/31
 **/
public class HtmlUtils {
    public static String replaceHtmlTrans(String html) {
        html = html.replace("&lt;", "<");
        html = html.replace("&gt;", ">");
        html = html.replace("&amp;", "&");
        html = html.replace("&apos;", "'");
        html = html.replace("&quot;", "\"");
        html = html.replace("&#xd;", "");
        return html;
    }
}
