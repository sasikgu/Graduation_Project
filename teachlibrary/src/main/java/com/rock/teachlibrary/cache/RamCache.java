package com.rock.teachlibrary.cache;

import android.graphics.Bitmap;
import android.util.Log;
import android.util.LruCache;

/**
 *  LRU （算法）    （Least Recently Used） 最近最少使用原则
 *      LRUCache
 *
 *     饿汉式单例，只要类一加载就直接占用了我们的内存
 *          好处是线程安全
 *     懒汉式单例，比较节约内存，线程不安全（可以添加同步锁来解决）
 *          添加同步锁之后，会降低性能
 */

public class RamCache implements ImageCache {

    private static final String TAG = RamCache.class.getSimpleName();
    private static RamCache mRamCache;

    private LruCache<String,Bitmap> mLruCache;

    private RamCache(){
        // 通常会分配系统可用内存的 八分之一来做为Ram缓存的大小
        int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 8);
        Log.e(TAG, "RamCache: " + String.valueOf(Runtime.getRuntime().maxMemory() / 8 ));
        Log.e(TAG, "RamCache: " + maxMemory);
        mLruCache = new LruCache<String, Bitmap>(maxMemory){
            // 用来计算一个item大小的方法
            @Override
            protected int sizeOf(String key, Bitmap value) {
                // 一行占用的内存 *  行数（高度）
                return value.getRowBytes() * value.getHeight();
            }
        };

    }

    /**
     *  使用双重判断外加同步锁来优化懒汉式单例
     *    可以解决线程不安全问题
     *    又可以提高直接加同步锁，性能损失的问题
     */
    public static RamCache getInstance(){
        if (mRamCache == null) {
            synchronized (RamCache.class){
                if (mRamCache == null){
                    mRamCache = new RamCache();
                }
            }
        }
        return mRamCache;
    }

    @Override
    public void put(String url, Bitmap bitmap) {
        mLruCache.put(url,bitmap);
    }

    @Override
    public Bitmap get(String url) {
        return mLruCache.get(url);
    }
}
