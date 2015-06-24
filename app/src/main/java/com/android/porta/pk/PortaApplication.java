package com.android.porta.pk;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.util.LruCache;

import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.toolbox.HurlStack;
import com.android.porta.pk.toolbox.NoSSLv3Compat;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;


/**
 * Created by Talha on 6/20/15.
 */
public class PortaApplication extends Application {

    private static final String TAG = "PortaApplication";
    private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        HurlStack stack = null;
        LogUtils.LOGD(TAG, "SDK version is " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT < 20) {
            stack = new HurlStack(null, new NoSSLv3Compat.NoSSLv3Factory());
            LogUtils.LOGD(TAG, "Initialized stack with NoSSLv3Compat.");
        } else {
            stack = new HurlStack();
            LogUtils.LOGD(TAG, "Initialized stack with default constructor.");
        }

        mRequestQueue = Volley.newRequestQueue(this, stack);
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader(){
        if(mImageLoader==null){
            mImageLoader = new ImageLoader(this.mRequestQueue, new ImageLoader.ImageCache() {
                private final LruCache<String, Bitmap> mCache = new LruCache<String, Bitmap>(10);

                public void putBitmap(String url, Bitmap bitmap) {
                    mCache.put(url, bitmap);
                }

                public Bitmap getBitmap(String url) {
                    return mCache.get(url);
                }
            });
        }
        return mImageLoader;
    }

}
