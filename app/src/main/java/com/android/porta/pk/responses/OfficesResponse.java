package com.android.porta.pk.responses;

import com.android.volley.Request;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.toolbox.GsonRequest;
import com.android.porta.pk.toolbox.Urls;

import java.util.List;

/**
 * Created by Talha on 6/21/15.
 */
public class OfficesResponse extends Response {

    public List<Office> data;

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
    public static class Office {
        public long id;
        public String office;
        public String type;
        public String address;
        public String telephone;
        public String fax;
        public String email;
        public float latitude;
        public float longitude;
    }

    public static Request<OfficesResponse> getOfficesRequest(Listener<OfficesResponse> listener,
                                                             ErrorListener errListener) {
        String url = Urls.makeUrl("/getoffices");
        LogUtils.LOGD("OfficesResponse", ">>url is: " + url);
        return new GsonRequest<OfficesResponse>(Request.Method.GET, url,
                OfficesResponse.class, null, listener, errListener);
    }
}