package com.rock.teachlibrary.utils;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Rock on 2017/2/21.
 */

public class SDCardUtil {

    private static final String TAG = SDCardUtil.class.getSimpleName();
    private static Context sContext;

    public static void init(Context context){
        sContext = context;
    }

    public static String url2Path(String url){
        /**
         *  要优先获取缓存目录
         *  将url转换为文件名
         *  cacheDir + "/" + fileName
         */
        return getCacheDir() + File.separator + url2FileName(url);
    }

    public static String getCacheDir(){
        /**
         *  文件缓存
         *      内部存储
         *      外部存储
         *
         *   Context.getCacheDir
         */
        if (sContext == null) {
            throw new NullPointerException("ImageLoader还未进行初始化");
        }
        File cacheDir = sContext.getCacheDir();
        return cacheDir.getAbsolutePath();
    }

    public static String url2FileName(String url){
//        String path = url.substring(url.lastIndexOf("/") + 1);
        String path = md5(url);
        return path;
    }

    /**
     *  MD5
     *      摘要算法，不属于加密范畴，但是很多公司会把它放到模块中使用，通常用来验证
     *      MD5会生成一个128位的2进制数组
     *      Java中内置生成的是16进制的数组 32
     */
    public static String md5(String url){
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(url.getBytes());
            // 获取摘要后的数据
            byte[] digest = md5.digest();
            String result = hex2String(digest);
            Log.e(TAG, "md5: " + result );
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "error_name";
    }

    public static String hex2String(byte[] bytes){
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            // 将16进制转换为String
            sb.append(String.format("%2x",b));
        }
        return sb.toString();
    }





}
