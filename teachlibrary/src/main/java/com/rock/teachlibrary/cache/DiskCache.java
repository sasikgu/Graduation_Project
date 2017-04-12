package com.rock.teachlibrary.cache;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.rock.teachlibrary.utils.ImageUtil;
import com.rock.teachlibrary.utils.SDCardUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 *
 *  logger logcat 工具
 *      打印的时候会打印方法名，会打印线程名
 *      还会自动格式化打印出来的json，xml
 *
 */

public class DiskCache implements ImageCache {

    private static final String TAG = DiskCache.class.getSimpleName();

    @Override
    public void put(String url, Bitmap bitmap) {
        // url 用来作为bitmap存储文件的一个标识
        /**
         *  存储一个文件
         *     文件要存的路径
         *     文件输出流
         *     要存储的数据  bitmap
         */
        String path = SDCardUtil.url2Path(url);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            // 将bitmap存储到文件中
            /**
             * jpeg 不能存储透明像素,如果你存储的图片中包含透明像素，并且你使用这个方法进行了存储，存储为jpeg格式
             *      当你在decode回来的时候，图片中的透明像素（糊了） 都变成了黑色
             *
             *  png 被称为无损图片，它可以支持透明像素，它所占的空间比较大
             *
             *  webp 不是Android 原生的，可以支持透明像素，并且所占空间还很小
             */
            boolean compress = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            if (compress) {
                Log.e(TAG, "put: 缓存成功" );
            }else{
                Log.e(TAG, "put: 缓存失败" );
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.e(TAG, "put: 磁盘缓存异常");
        }

    }

    @Override
    public Bitmap get(String url) {
        /**
         *  将url转换为存储路径
         *  判断指定路径是否存在
         */
        String path = SDCardUtil.url2Path(url);
        File file = new File(path);
        if (file.exists()) {
            Log.e(TAG, "get: 从磁盘缓存加载图片" );
            return ImageUtil.sample2Bitmap(path);
        }
        Log.e(TAG, "get: 还未缓存此图片" );
        return null;
    }
}
