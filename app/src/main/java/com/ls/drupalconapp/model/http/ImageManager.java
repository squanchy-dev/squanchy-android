package com.ls.drupalconapp.model.http;


import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Yakiv M. on 22.09.2014.
 */
@Deprecated //Use Drupal image view instead
public class ImageManager {

    private static ImageManager mInstance;
    private CacheImageLoader mImageLoader;


    private ImageManager(Context context) {
        Context applicationContext = context.getApplicationContext();
        mImageLoader = new CacheImageLoader(
                Volley.newRequestQueue(applicationContext),
                new MemoryCache(50 * 1024 * 1024));
    }

    public static synchronized void initializeWith(Context context) {
        if (mInstance == null) {
            mInstance = new ImageManager(context);
        }
    }

    public static synchronized ImageLoader loader() {
        if (mInstance == null) {
            throw new IllegalStateException(ImageManager.class.getSimpleName() +
                    " is not initialized, call initializeWith(..) method first.");
        }
        return mInstance.getImageLoader();
    }

    public CacheImageLoader getImageLoader() {
        return mImageLoader;
    }

    public static class MemoryCache
            extends LruCache<String, Bitmap>
            implements ImageLoader.ImageCache {

        public MemoryCache() {
            this(getDefaultLruCacheSize());
        }

        public MemoryCache(int sizeInKiloBytes) {
            super(sizeInKiloBytes);
        }

        @Override
        protected int sizeOf(String key, Bitmap value) {
            return value.getRowBytes() * value.getHeight() / 1024;
        }

        @Override
        public Bitmap getBitmap(String url) {
            return get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            put(url, bitmap);
        }

        public static int getDefaultLruCacheSize() {
            final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
            final int cacheSize = maxMemory / 8;
            return cacheSize;
        }
    }

    public class CacheImageLoader extends ImageLoader {

        private final LruCache mMemoryCache;
        private final Cache mDiskCache;

        public CacheImageLoader(RequestQueue queue, MemoryCache memoryCache) {
            super(queue, memoryCache);
            mMemoryCache = memoryCache;
            mDiskCache = queue.getCache();
        }

        public LruCache getMemoryCache() {
            return mMemoryCache;
        }

        public Cache getDiskCache() {
            return mDiskCache;
        }
    }
}
