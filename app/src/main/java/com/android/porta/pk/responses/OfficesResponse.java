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
public class OfficesResponse extends Response implements Parcelable {

    public static final String KEY = "offices.response";
    public List<Office> data;

    protected OfficesResponse(Parcel in) {
        if (in.readByte() == 0x01) {
            data = new ArrayList<Office>();
            in.readList(data, Office.class.getClassLoader());
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
    public static final Parcelable.Creator<OfficesResponse> CREATOR = new Parcelable.Creator<OfficesResponse>() {
        @Override
        public OfficesResponse createFromParcel(Parcel in) {
            return new OfficesResponse(in);
        }

        @Override
        public OfficesResponse[] newArray(int size) {
            return new OfficesResponse[size];
        }
    };

    /**
     * Model to hold office data.
     * Sample response from api is
     * {
     * "id": "1",
     * "office": "Head Office",
     * "type": "Office",
     * "address": "121-Ferozepur Road, Lahore -Pakistan",
     * "telephone": "92-42-3 742 7482, 3 742 7483",
     * "fax": "92-42-3 742 7481",
     * "email": "info@waheedsons.com.pk",
     * "latitude": "31.527006",
     * "longitude": "74.323305"
     * }
     */
    public static class Office implements Parcelable {
        public long id;
        public String office;
        public String type;
        public String address;
        public String telephone;
        public String fax;
        public String email;
        public float latitude;
        public float longitude;

        protected Office(Parcel in) {
            id = in.readLong();
            office = in.readString();
            type = in.readString();
            address = in.readString();
            telephone = in.readString();
            fax = in.readString();
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
            dest.writeLong(id);
            dest.writeString(office);
            dest.writeString(type);
            dest.writeString(address);
            dest.writeString(telephone);
            dest.writeString(fax);
            dest.writeString(email);
            dest.writeFloat(latitude);
            dest.writeFloat(longitude);
        }

        @SuppressWarnings("unused")
        public static final Parcelable.Creator<Office> CREATOR = new Parcelable.Creator<Office>() {
            @Override
            public Office createFromParcel(Parcel in) {
                return new Office(in);
            }

            @Override
            public Office[] newArray(int size) {
                return new Office[size];
            }
        };
    }

    public static OfficesResponse get(Bundle args) {
        return args.getParcelable(KEY);
    }

    public void putSelf(Bundle args) {
        args.putParcelable(KEY, this);
    }

    public static Request<OfficesResponse> getOfficesRequest(Listener<OfficesResponse> listener,
                                                             ErrorListener errListener) {
        String url = Urls.makeUrl("/getoffices");
        LogUtils.LOGD("OfficesResponse", ">>url is: " + url);
        return new GsonRequest<OfficesResponse>(Request.Method.GET, url,
                OfficesResponse.class, null, listener, errListener);
    }
}