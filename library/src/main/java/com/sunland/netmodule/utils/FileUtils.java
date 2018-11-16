package com.sunland.netmodule.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 文件操作工具类
 */
public class FileUtils {
    /**
     * 判断指定的文件（或目录）是否存在（完整路径文件名）
     */
    public static boolean exists(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    /**
     * 判断指定的文件是否存在（完整路径文件名）
     */
    public static boolean existsFile(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File f = new File(path);
        return f.exists() && !f.isDirectory();
    }

    /**
     * 判断指定的目录是否存在（完整路径目录名）
     */
    public static boolean existsDirectory(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File f = new File(path);
        return f.exists() && f.isDirectory();
    }

    /**
     * 判断指定的文件（或目录）是否存在
     *
     * @param path     文件所在目录
     * @param filename 文件名
     */
    public static boolean exists(String path, String filename) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(filename))
            return false;
        String fn = filename;
        while (!TextUtils.isEmpty(fn) && fn.startsWith("/"))
            fn = fn.substring(1);

        if (path.endsWith("/"))
            return exists(path + filename);
        else
            return exists(path + "/" + filename);
    }

    /**
     * 判断指定的文件是否存在
     *
     * @param path     文件所在目录
     * @param filename 文件名
     */
    public static boolean existsFile(String path, String filename) {
        if (TextUtils.isEmpty(path) || TextUtils.isEmpty(filename))
            return false;
        String fn = filename;
        while (!TextUtils.isEmpty(fn) && fn.startsWith("/"))
            fn = fn.substring(1);

        if (path.endsWith("/"))
            return existsFile(path + filename);
        else
            return existsFile(path + "/" + filename);
    }

    /**
     * 创建指定的目录
     *
     * @param path    需要创建的目录
     * @param delfile 当存在同名文件（非目录）时，是否将文件删除
     * @return 目录是否创建成功
     */
    public static boolean mkdir(String path, boolean delfile) {
        if (TextUtils.isEmpty(path))
            return false;
        File f = new File(path);
        if (!f.exists())
            return f.mkdirs();
        else if (f.isDirectory())
            return true;
        else if (delfile && f.delete())
            return f.mkdir();
        return false;
    }

    /**
     * 删除指定的文件
     */
    public static boolean delete(String filename) {
        if (TextUtils.isEmpty(filename))
            return true;
        File f = new File(filename);
        return !f.exists() || f.delete();
    }

    /**
     * 重新命名文件名
     *
     * @param srcFilename  原文件名
     * @param destFilename 新文件名
     */
    public static boolean rename(String srcFilename, String destFilename) {
        if (!exists(srcFilename) || exists(destFilename))
            return false;
        File f = new File(srcFilename);
        return f.renameTo(new File(destFilename));
    }

    /**
     * 获取指定文件的长度（文件大小、字节数）
     */
    public static long getLength(String filename) {
        if (TextUtils.isEmpty(filename))
            return 0;
        File f = new File(filename);
        if (f.exists() && !f.isDirectory())
            return f.length();
        return 0;
    }
}
