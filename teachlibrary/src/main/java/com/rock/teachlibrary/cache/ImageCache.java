package com.rock.teachlibrary.cache;

import android.graphics.Bitmap;

/**
 * Created by Rock on 2017/2/20.
 */

public interface ImageCache {

    public void put(String url, Bitmap bitmap);

    public Bitmap get(String url);

}
