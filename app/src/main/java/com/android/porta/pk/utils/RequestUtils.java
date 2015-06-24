package com.android.porta.pk.utils;

import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;

import java.util.HashMap;
import java.util.Map;

public class RequestUtils {
    private static Map<String, String> standardHeader;

    static {
        standardHeader = new HashMap<String, String>();
        standardHeader.put("Accept", "application/json");
        standardHeader.put("Content-Type", "application/x-www-form-urlencoded");
        standardHeader.put("Accept-Encoding", "gzip");
    }

    public static Request<?> preAdd(Context context, Request<?> request) {
        try {
            // if context is null, just return what was passed
            if (context == null) {
                return request;
            }
            Map<String, String> oldHeaders = request.getHeaders();
            if (oldHeaders == null) {// what?
                oldHeaders = new HashMap<String, String>();
            }
            oldHeaders.putAll(standardHeader);
        } catch (AuthFailureError e) {
            e.printStackTrace();
        }
        return request;
    }

}
