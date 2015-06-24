package com.android.porta.pk.utils;

import com.android.volley.NetworkResponse;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

public class ResponseUtils {
	public static String getUnzippedResponse(NetworkResponse response)
			throws IOException {
		String encoding = getContentEncoding(response);
		if (encoding == null || !encoding.contains("gzip")) {
			return new String(response.data,
					HttpHeaderParser.parseCharset(response.headers));
		} else {
			StringBuilder output = new StringBuilder();
			GZIPInputStream gStream = new GZIPInputStream(
					new ByteArrayInputStream(response.data));
			InputStreamReader reader = new InputStreamReader(gStream);
			BufferedReader in = new BufferedReader(reader);
			String read;
			while ((read = in.readLine()) != null) {
				output.append(read);
			}
			reader.close();
			in.close();
			gStream.close();
			return output.toString();
		}
	}

	private static String getContentEncoding(NetworkResponse response) {
		return response.headers == null ? null : response.headers
				.get("Content-Encoding");
	}
}
