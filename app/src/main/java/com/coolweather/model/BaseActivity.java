package com.coolweather.model;

import android.app.Activity;
import android.app.ListActivity;
import android.os.Bundle;

import com.coolweather.util.ActivityController;

import java.util.List;

/**
 * Created by bone on 16/4/10.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }
}
