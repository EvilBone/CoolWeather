package com.coolweather.util;

import android.util.Log;

/**
 * Created by bone on 16/4/10.
 */
public class LogUtil {
    public static void d(String tag,String msg){
        Log.d(tag,msg);
    }
    public static void e(String tag,String error){
        Log.e(tag,error);

    }
    public static void i(String tag,String msg){
        Log.i(tag,msg);
    }
    public static void v(String tag,String msg){
        Log.v(tag,msg);
    }

    public static void w(String tag,String msg){
        Log.w(tag, msg);
    }



}
