package com.rock.teachlibrary;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.BounceInterpolator;
import android.widget.ImageView;

import com.rock.teachlibrary.cache.DiskCache;
import com.rock.teachlibrary.cache.RamCache;
import com.rock.teachlibrary.http.HttpUtil;
import com.rock.teachlibrary.utils.ImageUtil;
import com.rock.teachlibrary.utils.SDCardUtil;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Rock on 2017/2/20.
 */

public class ImageLoader {

    private static final int LOAD_SUCCEED = 100;
    private static final int LOAD_FAIL = 101;
    private static final String TAG = ImageLoader.class.getSimpleName();

    private static DiskCache diskCache = new DiskCache();

    private static Context sContext;

    /**
     * 保证框架有统一的初始化入口
     * @param context
     */
    public static void init(Context context){
        sContext = context;
        SDCardUtil.init(context);
    }

    // 获取CPU的核心数
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    // 线程池的核心线程数
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAX_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final long KEEP_ALIVE = 1;
    private static final BlockingQueue<Runnable> BLOCKING_QUEUE = new LinkedBlockingQueue<>(128);
    /**
     * 可配置线程池
     *    ① 核心线程数  不管有没有任务，线程池都会保留这些数量的活跃线程
     *    ② 最大线程数   当任务突然增加，核心线程已不满足任务需求，我们可以继续创建线程完成任务，就是线程池内所允许
     *                      的最多同时工作的线程数
     *    ③ 存活时间    当超过核心数的多余线程（没有任务，并且又不是核心线程），他们会被线程池关闭，这个参数就是存活时间
     *    ④ 存活时间的单位    和第三个参数是成对使用的，用来作为第三个参数的一个单位
     *    ⑤ 阻塞队列     当任务超过最大线程数时，多余的任务要进行排队
     *    ⑥ 线程工厂      线程不够用，去产生新线程的工厂类
     */
    private static Executor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,MAX_POOL_SIZE,KEEP_ALIVE, TimeUnit.SECONDS,BLOCKING_QUEUE);


    public static void display(final ImageView container, final String url){
        /**
         * 通过网络请求，将url对应的图片获取回来
         *  将图片设置到ImageView
         */

        Bitmap bitmap = RamCache.getInstance().get(url);
        if (bitmap != null) {
            container.setImageBitmap(bitmap);
            return;
        }

        // 设置一个占位图
        container.setImageResource(R.mipmap.loading);
        container.setTag(url);

        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                // 执行在主线程，用来处理子线程发送回主线程的消息
                switch (msg.what) {
                    case LOAD_SUCCEED:
                        if (container.getTag().equals(url)) {
                            container.setImageBitmap((Bitmap) msg.obj);
                            // 开启动画
                            Animation animation = AnimationUtils.loadAnimation(sContext, R.anim.item_in);
                            animation.setInterpolator(new BounceInterpolator());
                            container.startAnimation(animation);
                        }
                        break;
                    case LOAD_FAIL:
                        Log.e(TAG, "handleMessage: 加载失败:" + url );
                        container.setImageResource(R.mipmap.fail);
                        break;
                }
            }
        };
        // 在线程池中提交任务
        sExecutor.execute(new Runnable() {
            @Override
            public void run() {
                // 从DiskCache缓存中去加载
                Bitmap diskBitmap = diskCache.get(url);
                if (diskBitmap != null) {
                    // 从缓存中获取到了
                    // 我们应该将图片放到RamCache
                    RamCache.getInstance().put(url,diskBitmap);
                    // 发回主线程
                    Message msg = Message.obtain();
                    msg.what = LOAD_SUCCEED;
                    msg.obj = diskBitmap;
                    handler.sendMessage(msg);
                    return;
                }

                // 在这里我们要进行网络请求，下载图片
                byte[] bytes = HttpUtil.getBytes(url);
                if (bytes != null) {
                    // 解码成图片
                    Bitmap bitmap = ImageUtil.sample2Bitmap(bytes);
                    // 将位图发送会主线程
                    Message msg = Message.obtain();
                    msg.what = LOAD_SUCCEED;
                    msg.obj = bitmap;
                    // 添加到内存缓存中
                    RamCache.getInstance().put(url,bitmap);
                    // 添加到磁盘缓存中
                    diskCache.put(url,bitmap);
                    handler.sendMessage(msg);
                }else{
                    // 发消息回主线程，通知加载失败
                    handler.sendEmptyMessage(LOAD_FAIL);
                }

            }
        });

    }
}
