package com.danielqiu.lululu;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private Handler viewUpdater;

    // flag that should be set true if handler should stop
    boolean mStopHandler = false;

    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (mTextView != null) {
                mTextView.setText(Integer.toString(getCount()));
            }

            if (!mStopHandler) {
                viewUpdater.postDelayed(this, 300);
            }
        }
    };

    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float lastSensorY = 0f;

    private int peekCount = 0;

    public int getCount() {
        return peekCount /2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.countTextView);
            }
        });
        viewUpdater = new Handler();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        mStopHandler = false;
        viewUpdater.post(runnable);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStopHandler = true;
        viewUpdater.removeCallbacks(runnable);
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentSensorY = event.values[1];

        if ((currentSensorY * lastSensorY)<= 0)
            peekCount++;

        lastSensorY = currentSensorY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
