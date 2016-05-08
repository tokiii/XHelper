package com.lost.cuthair.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.lost.cuthair.utils.AppManager;

/**
 * Created by lost on 2016/5/9.
 */
public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppManager.getAppManager().finishActivity(this);
    }
}
