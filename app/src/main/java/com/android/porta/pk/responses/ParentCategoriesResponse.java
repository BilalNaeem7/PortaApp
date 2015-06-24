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
public class ParentCategoriesResponse extends Response implements Serializable {

    public static final String KEY = "parent.categories.response";
    public List<ParentCategory> data;

    /**
     * Model for Parent Category
     */
    public static class ParentCategory implements  Serializable {
        public long id;
        public String category_name;
        public String parent_category; /*": "0",*/ // TODO not sure what's this
        public String slug;

        @Override
        public String toString() {
            return "ParentCategory{" +
                    "id=" + id +
                    ", category_name='" + category_name + '\'' +
                    ", parent_category='" + parent_category + '\'' +
                    ", slug='" + slug + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "ParentCategoriesResponse{" +
                "data=" + data.toString() +
                '}';
    }

    public static ParentCategoriesResponse get(Bundle args) {
        // TODO check if this can be null
        return (ParentCategoriesResponse) args.getSerializable(KEY);
    }

    public void putSelf(Bundle args) {
        args.putSerializable(KEY, this);
    }

    public static Request<ParentCategoriesResponse> getParentCategoriesRequest(Listener<ParentCategoriesResponse> listener,
                                                                               ErrorListener errListener) {
        String url = Urls.makeUrl("/getparentcategories");
        LogUtils.LOGD("ParentCategoriesResponse", ">>Url is: " + url);
        return new GsonRequest<ParentCategoriesResponse>(Request.Method.GET, url, ParentCategoriesResponse.class,
                null, listener, errListener);
    }
}
