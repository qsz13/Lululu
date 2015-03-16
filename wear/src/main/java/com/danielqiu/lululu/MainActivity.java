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
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        float currentSensorY = event.values[1];

        if ((currentSensorY * lastSensorY)<= 0)
            peekCount++;

        String out = event.values[0] + "," + event.values[1] + "," + event.values[2] + ","+getCount();
        Log.d("debug",out);

        viewUpdater.post(new Runnable() {
            @Override
            public void run() {
                mTextView.setText(Integer.toString(getCount()));
            }
        });

        lastSensorY = currentSensorY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
