package com.danielqiu.lululu;

import android.app.Application;
import android.content.Context;

import com.danielqiu.lululu.models.DatabaseHelper;
import com.j256.ormlite.android.apptools.OpenHelperManager;

/**
 * Created by Jeffrey on 2015/2/10.
 */
public class App  extends Application {

    private static Context context;
    private static DatabaseHelper databaseHelper;

    public void onCreate(){
        super.onCreate();
        App.context = getApplicationContext();
        databaseHelper = OpenHelperManager.getHelper(context, DatabaseHelper.class);
    }

    public static Context getAppContext() {
        return App.context;
    }
    public static DatabaseHelper getDatabase() {
        return App.databaseHelper;
    }
}