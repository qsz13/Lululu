package com.danielqiu.lululu;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    private TextView mTextView;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    private float[] lastSensorData = new float[] {0,0,0};
    private int count = 0;
    private Boolean is_up = true;

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
                mTextView = (TextView) stub.findViewById(R.id.text);
            }
        });
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
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double module = Math.cbrt(x*x+y*y+z*z);

        //mTextView.setText(out);


        if ((lastSensorData[1]) * event.values[1] < 0  && module >= 0.3)
            count++;

        String out = "x:" + x + " y:" + y + " z:" +z + " m:" +module +" count:"+count+"\n";
        Log.d("debug",out);
        lastSensorData = event.values.clone();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
