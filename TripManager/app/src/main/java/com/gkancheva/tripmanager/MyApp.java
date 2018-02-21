package com.gkancheva.tripmanager;

import android.app.Application;
import android.content.ContextWrapper;

import com.gkancheva.tripmanager.model.expense.Accommodation;
import com.gkancheva.tripmanager.model.expense.Expense;
import com.gkancheva.tripmanager.model.expense.RentACar;
import com.gkancheva.tripmanager.model.expense.Transportation;
import com.gkancheva.tripmanager.model.trip.Trip;
import com.orm.SugarContext;

import java.io.File;

public class MyApp extends Application {
    @Override
    public void onCreate() {
    super.onCreate();
//        if(dataBaseExists(this, "trip_manager.db")) {
//            SugarDb db = new SugarDb(getApplicationContext());
//            new File(db.getDB().getPath()).delete();
//        }
        SugarContext.init(getApplicationContext());
        if(!dataBaseExists(this, "trip_manager.db")) {
            Trip.findById(Trip.class, (long)1);
            Expense.findById(Expense.class, (long)1);
            Transportation.findById(Transportation.class, (long)1);
            Accommodation.findById(Accommodation.class, (long)1);
            RentACar.findById(RentACar.class, (long)1);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    private boolean dataBaseExists(ContextWrapper ctx, String dbName) {
        File dbFile = ctx.getDatabasePath(dbName);
        return dbFile.exists();
    }
}
