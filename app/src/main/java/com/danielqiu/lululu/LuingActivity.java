package com.danielqiu.lululu;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class LuingActivity extends ActionBarActivity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int count = 0;
    private Boolean is_up = true;
    private TextView totalTextView;
    private LineChart mChart;
    private LineData mSensorData;
    private ArrayList<String> xVals = new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luing);
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mChart = (LineChart)findViewById(R.id.sensorChart);

        // fake data
        LineDataSet set = new LineDataSet(new ArrayList<Entry>(),getString(R.string.SensorData));
        mSensorData =  new LineData(xVals,set);
        mChart.setData(mSensorData);

        //styles
        mChart.setDrawXLabels(false);
        mChart.setDrawYValues(false);

        totalTextView.setText("0!");
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

        if (module > 3)
        {
            addPoint(event.timestamp,y);
        }

        if(y>1&& module>3 && !is_up) {
            is_up = true;
            count++;
            totalTextView.setText(count + "!!");
        }
        else if (-1>y && module>3 && is_up) {
            is_up = false;
        }

        String out = "x:" + x + " y:" + y + " z:" +z + " m:" +module +" count:"+count+"\n";
       // Log.i("sensor",out);

    }

    private void addPoint(float x,float y)
    {
        xVals.add(Float.toString(y));
        int xIndex = xVals.size();
        mSensorData.addEntry(new Entry(y,xIndex),0);

        //refresh chart
        mChart.setData(mSensorData);
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    public void onStopButtonClick(View view)
    {
        onBackPressed();;
    }
}
