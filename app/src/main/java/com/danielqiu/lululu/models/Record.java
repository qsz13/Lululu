package com.danielqiu.lululu.models;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Jeffrey on 2015/2/10.
 */
@DatabaseTable(tableName = "Record")
public class Record {

    @DatabaseField(generatedId = true)
    private int ID;

    @DatabaseField(canBeNull = false)
    private int Count;

    @DatabaseField(canBeNull = false)
    private float Duration;

    @DatabaseField(canBeNull = false)
    private float StartDateTime;

    @DatabaseField(canBeNull = false)
    private int Mode;

    @ForeignCollectionField(eager = true,columnName = RecordPoint.RECORD_FK_COLUMN_NAME)
     ForeignCollection<RecordPoint> Entries;

    protected Record()
    {}

    public Record(RecordType type,Calendar startTime,float duration,int count){
        Mode = type.ordinal();
        StartDateTime = startTime.getTimeInMillis();
        Duration = duration;
        Count = count;
    }

    public RecordType getMode()
    {
        return RecordType.values()[Mode];
    }

    public int getCount() {
        return Count;
    }

    public float getDuration() {
        return Duration;
    }

    public float getStartDateTime() {
        return StartDateTime;
    }

    public ForeignCollection<RecordPoint> getEntries() {
        return Entries;
    }
}
