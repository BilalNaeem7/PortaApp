package com.android.porta.pk.toolbox;

import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.utils.Maps;
import com.android.porta.pk.utils.ResponseUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class StringRequest extends com.android.volley.toolbox.StringRequest {
	private Map<String, String> headers;

	public StringRequest(String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(url, listener, errorListener);
        init();
	}

	public StringRequest(int method, String url, Listener<String> listener,
			ErrorListener errorListener) {
		super(method, url, listener, errorListener);
        init();
	}

    private void init(){
        setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }
	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// dont't return super because it will return immutable map
		return headers != null ? headers : (headers = Maps
				.<String, String> newHashMap());
	}

	@Override
	protected Response<String> parseNetworkResponse(NetworkResponse response) {
		String parsed;
		try {
			parsed = ResponseUtils.getUnzippedResponse(response);
			LogUtils.LOGE(">> String parseNetworkResponse", parsed + "");
		} catch (UnsupportedEncodingException e) {
			parsed = new String(response.data);
		} catch (IOException e) {
			return Response.error(new VolleyError(e));
		}
		return Response.success(parsed,
				HttpHeaderParser.parseCacheHeaders(response));
	}

	@Override
	public String toString() {
		String headers = null;
		try {
			headers = getHeaders().toString();
		} catch (AuthFailureError e) {
			e.printStackTrace();
		}
		return "GsonRequest [ headers=" + headers + ", toString()="
				+ super.toString() + "]";
	}
}
