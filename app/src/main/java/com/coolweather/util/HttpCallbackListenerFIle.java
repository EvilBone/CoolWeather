package com.coolweather.util;

import android.graphics.Bitmap;

/**
 * Created by bone on 16/4/9.
 */
public interface HttpCallbackListenerFile {
    void onFinish(Bitmap bitmap);

    void onError(Exception e);
}
