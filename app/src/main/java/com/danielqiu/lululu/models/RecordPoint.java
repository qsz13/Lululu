package com.danielqiu.lululu.models;

import com.j256.ormlite.field.*;
import com.j256.ormlite.table.*;
/**
 * Created by Jeffrey on 2015/2/10.
 */

@DatabaseTable(tableName = "RecordPoint")
public class RecordPoint {

    public  static final String  RECORD_FK_COLUMN_NAME = "GroupID";

    @DatabaseField(generatedId = true)
    private int ID;

    @DatabaseField(canBeNull = false,foreign = true,foreignAutoRefresh = true,columnDefinition =RECORD_FK_COLUMN_NAME)
    private Record Group;

    @DatabaseField(canBeNull = false)
    private float SensorX;

    @DatabaseField(canBeNull = false)
    private float SensorY;

    @DatabaseField(canBeNull = false)
    private float SensorZ;

    @DatabaseField(canBeNull = false)
    private float Time;

    protected RecordPoint(){}

    public RecordPoint(float x,float y,float z,float time){
        SensorX = x;
        SensorY = y;
        SensorZ = z;
        Time = time;
    }

    public RecordPoint(float x,float y,float z,float time,Record group){
        SensorX = x;
        SensorY = y;
        SensorZ = z;
        Time = time;
        Group = group;
    }

    public float getSensorX() {
        return SensorX;
    }

    public float getSensorY() {
        return SensorY;
    }

    public float getSensorZ() {
        return SensorZ;
    }

    public float getTime() {
        return Time;
    }
}
