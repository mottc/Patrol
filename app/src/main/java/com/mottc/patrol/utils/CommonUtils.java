package com.mottc.patrol.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created with Android Studio
 * User: mottc
 * Date: 2017/4/19
 * Time: 16:24
 */
public class CommonUtils {

    public static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context, "mottc.provider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }
}
