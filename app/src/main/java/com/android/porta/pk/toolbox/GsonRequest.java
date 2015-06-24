package com.android.porta.pk.toolbox;

import com.android.porta.pk.utils.LogUtils;
import com.android.porta.pk.utils.Maps;
import com.android.porta.pk.utils.ResponseUtils;
import com.android.porta.pk.utils.GsonUtils;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Volley adapter for JSON requests that will be parsed into Java objects by
 * Gson.
 */
public class GsonRequest<T> extends Request<T> {
	private final Gson gson = GsonUtils.getGson();
	private final Class<T> clazz;
	private Map<String, String> headers;
	private final Listener<T> listener;

	/**
	 * Make a GET request and return a parsed object from JSON.
	 * 
	 * @param url
	 *            URL of the request to make
	 * @param clazz
	 *            Relevant class object, for Gson's reflection
	 * @param headers
	 *            Map of request headers
	 */
	// public GsonRequest(String url, Class<T> clazz, Map<String, String>
	// headers,
	// Listener<T> listener, ErrorListener errorListener) {
	// this(url, clazz, headers, listener, errorListener, Urls
	// .getDigestedKey());
	// }
	//
	// public GsonRequest(String url, Class<T> clazz, Map<String, String>
	// headers,
	// Listener<T> listener, ErrorListener errorListener, String key) {
	// super(Method.GET, key != null ? Urls.addKey(url, key) : url,
	// errorListener);
	// // socket, read and connection timeout changed
	// super.setRetryPolicy(getDefaultRetryPolicy());
	// this.clazz = clazz;
	// this.headers = headers;
	// this.listener = listener;
	//
	// }

	public GsonRequest(int method, String url, Class<T> clazz,
			Map<String, String> headers, Listener<T> listener,
			ErrorListener errorListener) {
		super(method, url, errorListener);
		// socket, read and connection timeout changed
		// super.setRetryPolicy(getDefaultRetryPolicy());
		this.clazz = clazz;
		this.headers = headers;
		this.listener = listener;
        setRetryPolicy(new DefaultRetryPolicy(
                 DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 3,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
	}

	// public DefaultRetryPolicy getDefaultRetryPolicy() {
	// DefaultRetryPolicy defaultRetry = new DefaultRetryPolicy(
	// 4 * DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
	// DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
	// DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
	// return defaultRetry;
	// }

	@Override
	public Map<String, String> getHeaders() throws AuthFailureError {
		// dont't return super because it will return immutable map
		return headers != null ? headers : (headers = Maps
				.<String, String> newHashMap());
	}

	@Override
	protected void deliverResponse(T response) {
		listener.onResponse(response);
	}

	@Override
	protected Response<T> parseNetworkResponse(NetworkResponse response) {
		try {
			String json = ResponseUtils.getUnzippedResponse(response);
			LogUtils.LOGD(">> parseNetworkResponse", json + "<<");
			return Response.success(gson.fromJson(json, clazz),
					HttpHeaderParser.parseCacheHeaders(response));

		} catch (UnsupportedEncodingException e) {
			return Response.error(new ParseError(e));
		} catch (JsonSyntaxException e) {
			return Response.error(new ParseError(e));
		} catch (IOException e) {
			return Response.error(new VolleyError(e));
		}
	}

	@Override
	public String toString() {
		return "GsonRequest [ headers=" + headers + ", toString()="
				+ super.toString() + "]";
	}

}