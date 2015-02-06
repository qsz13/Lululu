package com.danielqiu.lululu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int count = 0;
    private Boolean is_up = true;
    private TextView textView;
    private TextView totalTextView;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView)findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        button = (Button)findViewById(R.id.button);
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

    }


    @Override
    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    @Override
    public void onSensorChanged(SensorEvent event){
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double module = Math.cbrt(x*x+y*y+z*z);
        if(y>1&& module>3 && !is_up) {
            is_up = true;
            count++;
            String out = "x:" + x + " y:" + y + " z:" +z + " m:" +module +" count:"+count+"\n";
            textView.append(out);
            totalTextView.setText("Total: "+count);
            Log.i("count","count:"+count);
        }
        else if (-1>y && module>3 && is_up) {
            is_up = false;
//            Log.i("sensor", "x:" + x);
//            Log.i("sensor", "y:" + y);
//            Log.i("sensor", "z:" + z);
//            Log.i("sensor", "m:" + module);
            String out = "x:" + x + " y:" + y + " z:" +z + " m:" +module + "\n";
            textView.append(out);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void clearButtonClicked(View view)
    {
        count = 0;
        textView.setText("");
        Log.i("lululu", "clear");
    }
}
