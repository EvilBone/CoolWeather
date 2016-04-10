package com.coolweather.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.media.ImageReader;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by bone on 16/3/29.
 */
public class HttpUtil {
    public static void sendHttpRequest(final String address, final HttpCallbackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(address);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder builder = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }
                    if (listener != null) {
                        listener.onFinish(builder.toString());
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                } finally {
                    if (connection != null) {
                        connection.disconnect();
                    }
                }
            }
        }).start();

    }
    public static void sendHttpRequestForFile(final Context context, final String condCode, final String address, final HttpCallbackListenerFile listener){
       new Thread(new Runnable() {
           @Override
           public void run() {
               Log.d("HttpUtil",address);
               HttpURLConnection connection = null;
               try {
                   URL url = new URL(address);
                   connection = (HttpURLConnection) url.openConnection();
                   connection.setRequestMethod("GET");
                   connection.setConnectTimeout(8000);
                   connection.setReadTimeout(8000);
                   InputStream in = connection.getInputStream();

                   FileOutputStream fileOutputStream = context.openFileOutput(condCode,Context.MODE_PRIVATE);
                   byte[] bytes = new byte[1024];
                   int len =0;
                   while((len = in.read(bytes))!=-1){
                       fileOutputStream.write(bytes);
                   }
                   Bitmap bitmap = BitmapFactory.decodeStream(in);
                   if (listener != null) {
                       listener.onFinish(bitmap);
                   }
               } catch (Exception e) {
                   if (listener != null) {
                       listener.onError(e);
                   }
               } finally {
                   if (connection != null) {
                       connection.disconnect();
                   }
               }

           }
       }).start();
    }
}
