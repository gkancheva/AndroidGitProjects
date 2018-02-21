package com.gkancheva.tripmanager.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmBroadcastReceiver extends BroadcastReceiver {

    public static final int REQUEST_CODE = 123;
    private static final String MESSAGE = "Message";
    private static final String BODY = "Body";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, AlarmNotificationService.class);
        service.putExtra(MESSAGE, intent.getExtras().getString(MESSAGE));
        service.putExtra(BODY, intent.getExtras().getString(BODY));
        context.startService(service);
    }
}
