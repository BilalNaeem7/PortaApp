package com.android.porta.pk.responses;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import com.android.porta.pk.toolbox.GsonRequest;
import com.android.porta.pk.toolbox.Urls;
import com.android.porta.pk.utils.LogUtils;
import com.android.volley.Request;
import com.android.volley.Response.Listener;
import com.android.volley.Response.ErrorListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bilal on 6/29/2015.
 */
public class DealersResponse extends Response implements Parcelable {

    public static final String KEY = "dealers.response";
    public List<Dealer> data;

    public DealersResponse(Parcel in) {
        if(in.readByte() == 0x01) {
            data = new ArrayList<>();
            in.readList(data, Dealer.class.getClassLoader());
        }
        else {
            data = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if(data == null) {
            dest.writeByte((byte) 0x01);
        }
        else {
            dest.writeByte((byte) 0x01);
            dest.writeList(data);
        }
    }

    public static final Parcelable.Creator<DealersResponse> CREATOR = new Parcelable.Creator<DealersResponse>() {

        @Override
        public DealersResponse createFromParcel(Parcel source) {
            return new DealersResponse(source);
        }

        @Override
        public DealersResponse[] newArray(int size) {
            return new DealersResponse[size];
        }
    };

    public static class Dealer implements Parcelable {

        public String dealer;
        public String contact_person;
        public String address;
        public String telephone;
        public String mobile;
        public String email;
        public float latitude;
        public float longitude;

        protected Dealer(Parcel in) {
            dealer = in.readString();
            contact_person = in.readString();
            address = in.readString();
            telephone = in.readString();
            mobile = in.readString();
            email = in.readString();
            latitude = in.readFloat();
            longitude = in.readFloat();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(dealer);
            dest.writeString(contact_person);
            dest.writeString(address);
            dest.writeString(telephone);
            dest.writeString(mobile);
            dest.writeString(email);
            dest.writeFloat(latitude);
            dest.writeFloat(longitude);
        }

        public static final Parcelable.Creator<Dealer> CREATOR = new Parcelable.Creator<Dealer>() {

            @Override
            public Dealer createFromParcel(Parcel source) {
                return new Dealer(source);
            }

            @Override
            public Dealer[] newArray(int size) {
                return new Dealer[size];
            }
        };
    }

    public static DealersResponse get(Bundle args) {
        return args.getParcelable(KEY);
    }

    public void putSelf(Bundle args) {
        args.putParcelable(KEY, this);
    }

    public static Request<DealersResponse> getDealersRequest(Listener<DealersResponse> listener,
                                                             ErrorListener errorListener) {
        String url = Urls.makeUrl("/getalldealers");
        LogUtils.LOGD("DealersResponse", ">>url is:" + url);

        return new GsonRequest<DealersResponse>(Request.Method.GET, url, DealersResponse.class,
                null, listener, errorListener);
    }
}
