package com.yalo.erunn.parking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by erunn on 2017-06-10.
 */

public class ParkingActivity extends AppCompatActivity {
    private static final String TAG = "ParkingActivity";
    private static final int REQUEST_PARKING = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking);
    }
}
