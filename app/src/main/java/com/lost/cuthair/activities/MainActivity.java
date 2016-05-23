package com.lost.cuthair.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;

import com.lost.cuthair.R;
import com.lost.cuthair.utils.SharePreferenceUtils;

public class MainActivity extends BaseActivity {

    private ImageView btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn = (ImageView) findViewById(R.id.btn);
        if ((boolean)SharePreferenceUtils.get(MainActivity.this, "isFirst", false)) {
            btn.setVisibility(View.GONE);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                    startActivity(intent);
                    finish();
                }
            }, 3000);
        }


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharePreferenceUtils.put(MainActivity.this, "isFirst", true);
                Intent intent = new Intent(MainActivity.this, ContactActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
