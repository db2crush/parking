package com.yalo.erunn.parking;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextClock;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by erunn on 2017-06-10.
 */

public class LoadingActivity extends Activity {
    private final int LOADING_LENGTH = 4000;

    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(LoadingActivity.this, MapActivity.class));
                finish();
            }
        }, LOADING_LENGTH);


    }
}
