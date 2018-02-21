package com.gkancheva.tripmanager.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gkancheva.tripmanager.MainActivity;
import com.gkancheva.tripmanager.R;

public class AlarmNotificationService extends Service{
    private NotificationManager mNotifyManager;
    private static final String MESSAGE = "Message";
    private static final String BODY = "Body";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
    }

//    @SuppressWarnings("static-access")
//    @Override
//    public void onStart(Intent intent, int startId)
//    {
//        super.onStart(intent, startId);
//        Bundle bundle = intent.getExtras();
//        String message = bundle.getString(MESSAGE);
//        String body = bundle.getString(BODY);
//        displayNotification(message, body);
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Bundle bundle = intent.getExtras();
        String message = bundle.getString(MESSAGE);
        String body = bundle.getString(BODY);
        displayNotification(message, body);
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        Log.i("MY SERVICE", "My service on destroy");
        stopSelf();
        super.onDestroy();
    }

    public void displayNotification(String title, String body) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setContentText(body)
                        .setAutoCancel(true);

        Intent resultIntent = new Intent(this.getApplicationContext(), MainActivity.class);
        PendingIntent resultPendIntent =
                PendingIntent.getActivity(
                this.getApplicationContext(), 0, resultIntent,
                        PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(resultPendIntent);
        int mNotificationId = 001;
        mNotifyManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        mNotifyManager.notify(mNotificationId, builder.build());
    }
}
