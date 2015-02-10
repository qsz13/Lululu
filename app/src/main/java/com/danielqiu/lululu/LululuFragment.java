package com.danielqiu.lululu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.LinearLayout;

import com.danielqiu.lululu.models.DatabaseHelper;
import com.danielqiu.lululu.models.Record;
import com.danielqiu.lululu.models.RecordPoint;
import com.danielqiu.lululu.models.RecordType;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jeffrey on 2015/2/9.
 */
public class LululuFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    private  RecordType recordType;
    private LinearLayout mRecordsLayout;

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Fragment newInstance(int sectionNumber) {

        LululuFragment fragment = new LululuFragment(RecordType.values()[sectionNumber - 1]);
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public LululuFragment(RecordType type) {
        recordType = type;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_lululu, container, false);
        mRecordsLayout = (LinearLayout)rootView.findViewById(R.id.records);

        try {
             List<Record> data =App.getDatabase().getRecords(recordType);
            for (Record item : data) {
                mRecordsLayout.addView(Assemble(item));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    private LineChart Assemble(Record rec){
        LineChart chart = new LineChart(mRecordsLayout.getContext());
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals =new ArrayList<Entry>();
        for (RecordPoint point : rec.getEntries()){
            xVals.add(Float.toString(point.getTime()));
            yVals.add(new Entry(point.getSensorY(),xVals.size()));
        }

        LineData sensorData =  new LineData(xVals, new LineDataSet(yVals,getString(R.string.SensorData)));

        chart.setData(sensorData);
        chart.setMinimumHeight(100);
        return  chart;
    }
}
