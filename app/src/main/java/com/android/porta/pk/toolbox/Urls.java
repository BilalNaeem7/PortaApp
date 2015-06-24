package com.android.porta.pk.toolbox;

import android.content.Context;
import android.net.Uri;

import com.android.porta.pk.utils.LogUtils;

import java.io.File;

/**
 * Created by Talha on 6/21/15.
 */
public class Urls {
    public static final String APP_SCHEME = "ports://";
    public static final String FILE_SCHEME = "file://";
    public static final String ANDROID_ASSETS = FILE_SCHEME + "/android_asset";
    public static final String HOST_ASSETS_PATH = "/assets/android";
    public static final String INTERNAL_ASSETS_DIRECTORY = "internal_assets";

    private final static String TAG = LogUtils.makeLogTag(Urls.class);

    private static final String HOST = getHost();

    private static final String API = "/porta_api";

    private static final String url = HOST + API;

    public static String makeUrl(String method) {
        return url + method;
    }

    public static Uri makeUri(String method) {
        return Uri.parse(url + method);
    }

    public static String prependHost(String method) {
        return HOST + method;
    }

    public static Uri prependHostUri(String method) {
        return Uri.parse(HOST + method);
    }

    public static String getHost() {
        // TODO make this gradle setting
        return "http://52.10.55.105/porta";
    }

    public static File getInternalAssetsFile(Context context, String filename) {
        return new File(context.getFilesDir() + File.separator + INTERNAL_ASSETS_DIRECTORY, filename);
    }
}
