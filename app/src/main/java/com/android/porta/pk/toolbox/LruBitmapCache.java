package com.android.porta.pk.toolbox;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.util.LruCache;

import com.android.volley.toolbox.ImageLoader.ImageCache;
 
@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
public class LruBitmapCache extends LruCache<String, Bitmap> implements ImageCache {
 
	public LruBitmapCache(int maxSize) {
		super(maxSize);
	}
	
	@Override
	protected int sizeOf(String key, Bitmap value) {
		return value.getRowBytes() * value.getHeight();
	}
	
	@Override
	public Bitmap getBitmap(String url) {
		return get(url);
	}
 
	@Override
	public void putBitmap(String url, Bitmap bitmap) {
		put(url, bitmap);
	}
 
}