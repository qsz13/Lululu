package com.danielqiu.lululu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.danielqiu.lululu.models.Record;
import com.danielqiu.lululu.models.RecordPoint;
import com.danielqiu.lululu.models.RecordType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;

public class LuingActivity extends Activity implements SensorEventListener {


    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private int peekCount = 0;
    private TextView totalTextView;
    private LineChart mChart;
    private LineData mSensorData;
    private float[] lastSensorData = new float[] {0,0,0};
    private Calendar startTime;
    private RecordType mode;

    private ArrayList<String> xVals = new ArrayList<String>();
    ArrayList<RecordPoint> points = new ArrayList<RecordPoint>();

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();
    Runnable doRecord = new Runnable() {

        @Override
        public void run() {
            long millis = System.currentTimeMillis();

            addPoint(millis,lastSensorData[1]);

            timerHandler.postDelayed(this, 30);
        }
    };

    public int getCount(){
        return peekCount / 2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luing);
        totalTextView = (TextView)findViewById(R.id.totalTextView);
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        mChart = (LineChart)findViewById(R.id.sensorChart);

        startTime = Calendar.getInstance();

        mode = (RecordType)getIntent().getExtras().getSerializable("Mode");

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
        timerHandler.post(doRecord);
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(doRecord);
        mSensorManager.unregisterListener(this, mAccelerometer);
    }

    @Override
    protected void onStop() {
        super.onStop();

        Calendar curr = Calendar.getInstance();
        try {
            Record record = new Record(mode,startTime,curr.getTimeInMillis() - startTime.getTimeInMillis() ,count);
            record.getEntries().addAll(points);
            App.getDatabase().newRecord(record);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event){
        lastSensorData = event.values.clone();
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        double module = Math.cbrt(x*x+y*y+z*z);

        if (y * lastSensorData[1] <= 0)
            peekCount++;

        String out = "x:" + x + " y:" + y + " z:" +z + " m:" +module +" count:"+getCount();
        Log.i("sensor",out);

    }

    public void onStopButtonClick(View view)
    {
        onBackPressed();
    }

    private void addPoint(float x,float y)
    {
        points.add(new RecordPoint(lastSensorData[0],lastSensorData[1],lastSensorData[2],x));
        xVals.add(Float.toString(y));
        int xIndex = xVals.size();
        mSensorData.addEntry(new Entry(y,xIndex),0);

        //refresh chart
        mChart.notifyDataSetChanged();
        mChart.invalidate();
    }
}
