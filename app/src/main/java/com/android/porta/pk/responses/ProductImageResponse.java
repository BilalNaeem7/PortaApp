package com.android.porta.pk.responses;

import com.android.volley.Request;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.toolbox.GsonRequest;
import com.android.porta.pk.toolbox.Urls;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import java.util.List;

/**
 * Created by Talha on 6/24/15.
 */
public class ProductImageResponse extends Response {

    public List<ProductImage> data;

    public static class ProductImage {
        public long id;
        public long product_id;
        public String image;
        boolean is_default;
    }

    public static Request<ProductImageResponse> getImageRequest(long id, Listener<ProductImageResponse> listener, ErrorListener errListener) {
        String url = Urls.makeUri("/getproductimages")
                .buildUpon()
                .appendPath(Long.toString(id))
                .build()
                .toString();
        LogUtils.LOGD("ProductImageResponse", ">>url " + url);
        return new GsonRequest<ProductImageResponse>(Request.Method.GET, url, ProductImageResponse.class, null, listener, errListener);
    }
}
