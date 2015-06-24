package com.android.porta.pk.responses;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.porta.pk.toolbox.GsonRequest;
import com.android.porta.pk.toolbox.Urls;
import com.android.porta.pk.utils.LogUtils;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Talha on 6/21/15.
 */
public class SubCategoryResponse extends Response implements Serializable {

    private static final String TAG = "SubCategoryResponse";
    public static final String KEY = "Category.Children.Response";
    public List<CategoryChild> data;

    public static class CategoryChild implements Serializable {
        public long id;
        public String category_name;
        public String slug;
        public int last_child;
    }

    public void putSelf(Bundle args) {
        args.putSerializable(KEY, this);
    }

    public static SubCategoryResponse get(Bundle args) {
        return (SubCategoryResponse) args.getSerializable(KEY);
    }

    public static Request<SubCategoryResponse> getSubCategoryRequest(long parentCatId,
                Listener<SubCategoryResponse> listener, ErrorListener errListener) {
        String url = Urls.makeUri("/getCategoryChildren")
                .buildUpon()
                .appendPath(Long.toString(parentCatId))
                .build()
                .toString();
        LogUtils.LOGD(TAG, ">>url is: " + url);
        return new GsonRequest<SubCategoryResponse>(Request.Method.GET, url, SubCategoryResponse.class,
                null, listener, errListener);
    }

}
