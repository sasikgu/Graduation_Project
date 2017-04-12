package com.rock.teachlibrary.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 *
 *  主要就是用来处理图片，压缩图片的
 *
 */

public class ImageUtil {

    public static Bitmap sample2Bitmap(byte[] bytes,int expectWidth,int expectHeight){
        // 解码图片时的配置选项
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置解码器仅读取边缘，这样设置之后BitmapFactory在解码的时候，不会将图片真正加载到内存中
        // 它仅会将图片的宽高存储到options中 outHeight和outWidth
        options.inJustDecodeBounds = true;

        // 使用配置选项进行图片解码
        BitmapFactory.decodeByteArray(bytes,0,bytes.length, options);

        // 获取图片的宽高
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        // 假定 理想图片的大小 宽高在400以内
        int sample = Math.max(outHeight / expectHeight, outWidth / expectWidth);
        /**
         * inSample在小于等于1的时候，会被当作1来使用
         * 在大与1的时候，它的真实取值是2的n次幂，向下取整
         *  你设置的值为5 会当作4来对待
         */
        options.inSampleSize = sample;
        // 关闭仅解码边缘
        options.inJustDecodeBounds = false;
        // 使用配置选项去加载图片
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public static Bitmap sample2Bitmap(byte[] bytes){
        return sample2Bitmap(bytes,300,300);
    }

    public static Bitmap sample2Bitmap(String path,int expectWidth,int expectHeight){

        // 解码图片时的配置选项
        BitmapFactory.Options options = new BitmapFactory.Options();
        // 设置解码器仅读取边缘，这样设置之后BitmapFactory在解码的时候，不会将图片真正加载到内存中
        // 它仅会将图片的宽高存储到options中 outHeight和outWidth
        options.inJustDecodeBounds = true;

        // 使用配置选项进行图片解码
        BitmapFactory.decodeFile(path ,options);

        // 获取图片的宽高
        int outHeight = options.outHeight;
        int outWidth = options.outWidth;

        // 假定 理想图片的大小 宽高在400以内
        int sample = Math.max(outHeight / expectHeight, outWidth / expectWidth);
        /**
         * inSample在小于等于1的时候，会被当作1来使用
         * 在大与1的时候，它的真实取值是2的n次幂，向下取整
         *  你设置的值为5 会当作4来对待
         */
        options.inSampleSize = sample;
        // 关闭仅解码边缘
        options.inJustDecodeBounds = false;
        // 使用配置选项去加载图片
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap sample2Bitmap(String path){
        return sample2Bitmap(path,300,300);
    }

}
