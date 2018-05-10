package com.example.a.varnidoorbell;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by a on 17-Feb-2016.
 */
public class CustomService extends Service {

    public CustomService(){}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.e("service started", "asdf");
        Thread cThread = new Thread(new GetData());
        cThread.interrupt();
        cThread.start();
    }

//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        return START_STICKY;
//    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Intent restartService = new Intent(getApplicationContext(),
                this.getClass());
        restartService.setPackage(getPackageName());
        PendingIntent restartServicePI = PendingIntent.getService(
                getApplicationContext(), 1, restartService,
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.setRepeating(AlarmManager.RTC_WAKEUP, 5000, 5000, restartServicePI);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
