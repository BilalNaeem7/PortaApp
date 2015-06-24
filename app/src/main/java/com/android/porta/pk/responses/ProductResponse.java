package com.android.porta.pk.responses;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.porta.pk.toolbox.GsonRequest;
import com.android.porta.pk.toolbox.Urls;
import com.android.porta.pk.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Talha on 6/21/15.
 */
public class ProductResponse extends Response implements Parcelable {

    public static final String KEY = "product.response";

    public List<Product> data;

    protected ProductResponse(Parcel in) {
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
    public static final Parcelable.Creator<ProductResponse> CREATOR = new Parcelable.Creator<ProductResponse>() {
        @Override
        public ProductResponse createFromParcel(Parcel in) {
            return new ProductResponse(in);
        }

        @Override
        public ProductResponse[] newArray(int size) {
            return new ProductResponse[size];
        }
    };

    public static Request<ProductResponse> getProductRequest(long productId,
                                         Listener<ProductResponse> listener,
                                         ErrorListener errorListener) {
        String url = Urls.makeUri("/getproduct").buildUpon().appendPath(Long.toString(productId)).build().toString();
        LogUtils.LOGD("ProductResponse", ">>url is: " + url);
        return new GsonRequest<>(Request.Method.GET, url, ProductResponse.class,
                null, listener, errorListener);
    }

}
