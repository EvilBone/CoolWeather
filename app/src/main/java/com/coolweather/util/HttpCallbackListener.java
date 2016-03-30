package com.coolweather.util;

/**
 * Created by bone on 16/3/29.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
