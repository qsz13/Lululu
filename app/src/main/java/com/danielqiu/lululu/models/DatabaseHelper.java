package com.danielqiu.lululu.models;

/**
 * Created by Jeffrey on 2015/2/10.
 */

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.danielqiu.lululu.App;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of your database. This class also usually provides
 * the DAOs used by the other classes.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {


    public interface OnRecordChangedListener{
        void Update(Record record);
        void Insert(Record record);
        void Delete(Record record);
    }

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "lululu.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    private  OnRecordChangedListener recordChangedListener;

    // the DAO object we use to access the SimpleData table
    private Dao<Record, Integer> recordDao = null;
    private Dao<RecordPoint, Integer> recordPointDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public OnRecordChangedListener getRecordChangedListener() {
        return recordChangedListener;
    }

    public void setRecordChangedListener(OnRecordChangedListener recordChangedListener) {
        this.recordChangedListener = recordChangedListener;
    }

    /**
     * This is called when the database is first created. Usually you should call createTable statements here to create
     * the tables that will store your data.
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Record.class);
            TableUtils.createTable(connectionSource, RecordPoint.class);

        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * This is called when your application is upgraded and it has a higher version number. This allows you to adjust
     * the various data to match the new version number.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Record.class, true);
            TableUtils.dropTable(connectionSource, RecordPoint.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our SimpleData class. It will create it or just give the cached
     * value.
     */
    public Dao<Record, Integer> getRecordDao() throws SQLException {
        if (recordDao == null) {
            recordDao = getDao(Record.class);
        }
        return recordDao;
    }

    public Dao<RecordPoint, Integer> getRecordPointsDao() throws SQLException {
        if (recordPointDao == null) {
            recordPointDao = getDao(RecordPoint.class);
        }
        return recordPointDao;
    }

    public List<Record> getRecords(RecordType type) throws SQLException {
        int mode = type.ordinal();
        return getRecordDao().queryForEq("Mode",mode);
    }

    public  void newRecord(Record r) throws SQLException {
        Dao<Record,Integer> dao = getRecordDao();
        Dao<RecordPoint,Integer> pointsDao = getRecordPointsDao();
        dao.create(r);
        //NOTE: the points in Record.Entries may not insert into DB before
        for (RecordPoint p : r.getEntries()) {
            p.setGroup(r);
            pointsDao.create(p);
        }

        if (recordChangedListener != null){
            recordChangedListener.Insert(r);
        }
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        recordDao = null;
        recordPointDao = null;
        recordChangedListener = null;
    }
}