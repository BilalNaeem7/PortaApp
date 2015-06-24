package com.android.porta.pk.responses;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.toolbox.GsonRequest;
import com.android.porta.pk.toolbox.Urls;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 6/21/15.
 */
public class CategoryProductsResponse extends Response implements Parcelable {

    public static final String KEY = "Category.Products.Response";

    public List<Product> data;

    protected CategoryProductsResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<Product>();
            in.readList(data, Product.class.getClassLoader());
        } else {
            data = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (data == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(data);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<CategoryProductsResponse> CREATOR = new Parcelable.Creator<CategoryProductsResponse>() {
        @Override
        public CategoryProductsResponse createFromParcel(Parcel in) {
            return new CategoryProductsResponse(in);
        }

        @Override
        public CategoryProductsResponse[] newArray(int size) {
            return new CategoryProductsResponse[size];
        }
    };

    public void putSelf(Bundle args) {
        args.putParcelable(KEY, this);
    }

    public static CategoryProductsResponse get(Bundle args) {
        return args.getParcelable(KEY);
    }

    public static Request<CategoryProductsResponse> getCategoryProductRequest(long catId, Listener<CategoryProductsResponse> listener,
                                                                     ErrorListener errorListener) {
        String url = Urls.makeUri("/getproducts").buildUpon().appendPath(Long.toString(catId)).build().toString();
        LogUtils.LOGD("CategoryProductsResponse", ">>url is: " + url);
        return new GsonRequest<CategoryProductsResponse>(Request.Method.GET, url, CategoryProductsResponse.class,
                null, listener, errorListener);
    }

}
